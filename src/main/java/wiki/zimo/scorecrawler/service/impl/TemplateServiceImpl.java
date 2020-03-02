package wiki.zimo.scorecrawler.service.impl;

import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import wiki.zimo.scorecrawler.domain.Score;
import wiki.zimo.scorecrawler.domain.Student;
import wiki.zimo.scorecrawler.service.TemplateService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TemplateServiceImpl implements TemplateService {
    @Override
    public void rendering(Student student) throws IOException {
        File templateFile = ResourceUtils.getFile("classpath:template.docx");
        System.out.println(templateFile.getAbsolutePath());

        // 准备塞入模板中的文本信息
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("xh", student.getXh());
        textMap.put("xm", student.getXm());
        textMap.put("bj", student.getBj());
        textMap.put("nj", student.getNj());
        textMap.put("yxmc", student.getYxmc());
        textMap.put("zymc", student.getZymc());
        textMap.put("xz", student.getXz());
        textMap.put("qdxf", student.getQdxf());
        textMap.put("pjjd", student.getPjjd());

        Calendar now = Calendar.getInstance();
        textMap.put("year", now.get(Calendar.YEAR));
        textMap.put("month", now.get(Calendar.MONTH) + 1);
        textMap.put("day", now.get(Calendar.DAY_OF_MONTH));

        // 读取文档模板
        XWPFDocument doc = new XWPFDocument(new FileInputStream(templateFile));

        // 获取段落并匹配修改变量
        List<XWPFParagraph> paragraphs = doc.getParagraphs();
        for (XWPFParagraph p : paragraphs) {
            List<XWPFRun> runs = p.getRuns();
            for (XWPFRun r : runs) {
                String s = r.toString();
                if (s != null && s.startsWith("{{") && s.endsWith("}}")) {
                    String key = s.substring(2, s.length() - 2);
                    r.setText(textMap.get(key).toString(), 0);
                }
            }
        }

        // 准备动态填入表格的成绩信息
        List<Score> scores = student.getScores();

        // 获取表格并动态修改内容，模板中表格固定是35行，21列，分为三个区域，每7列一个区域
        XWPFTable table = doc.getTables().get(0);
        List<XWPFTableRow> rows = table.getRows();

        int n = scores.size() / (rows.size() - 1) + 1;// 需要循环写几次
        int pos = 0;
        l:
        for (int index = 0; index < n; index++) {
//            System.out.println(index);
            // 0行是表头，内容应该从1开始
            for (int i = 1; i < rows.size(); i++) {
                XWPFTableRow row = rows.get(i);
                List<XWPFTableCell> cells = row.getTableCells();
                // 每次数据只写7列
                for (int j = index * 7; j < index * 7 + 7; j++) {
                    XWPFTableCell cell = cells.get(j);
                    List<XWPFParagraph> ps = cell.getParagraphs();
                    for (XWPFParagraph p : ps) {
                        List<XWPFRun> runs = p.getRuns();
                        for (XWPFRun r : runs) {
                            switch (j - index * 7) {
                                case 0:
                                    r.setText(String.valueOf(scores.get(pos).getXqSort()), 0);
                                    break;
                                case 1:
                                    r.setText(scores.get(pos).getKcmc(), 0);
                                    break;
                                case 2:
                                    r.setText(scores.get(pos).getKclb(), 0);
                                    break;
                                case 3:
                                    r.setText(scores.get(pos).getXdzk(), 0);
                                    break;
                                case 4:
                                    r.setText(String.valueOf(scores.get(pos).getXf()), 0);
                                    break;
                                case 5:
                                    r.setText(String.valueOf(scores.get(pos).getCj()), 0);
                                    break;
                                case 6:
                                    r.setText(String.valueOf(scores.get(pos).getJd()), 0);
                                    break;
                            }

                            pos++;

                            // 写完了，退出
                            if (pos >= scores.size()) {
                                break l;
                            }
                        }
                    }
                }
            }
        }

        System.out.println("pos=" + pos);

        FileOutputStream out = new FileOutputStream(new File("C:\\Users\\zimo\\Desktop\\out.docx"));
        doc.write(out);

        out.close();
    }
}
