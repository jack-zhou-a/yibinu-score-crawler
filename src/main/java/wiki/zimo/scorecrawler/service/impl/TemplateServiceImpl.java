package wiki.zimo.scorecrawler.service.impl;

import com.deepoove.poi.XWPFTemplate;
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
//        System.out.println(templateFile.getAbsolutePath());

        // 模板填充docx对象
        XWPFTemplate template = XWPFTemplate.compile(templateFile);

        // 准备塞入模板中的文本信息
        Map<String, Object> model = new HashMap<>();
        model.put("xh", student.getXh());
        model.put("xm", student.getXm());
        model.put("bj", student.getBj());
        model.put("nj", student.getNj());
        model.put("yxmc", student.getYxmc());
        model.put("zymc", student.getZymc());
        model.put("xz", student.getXz());
        model.put("qdxf", student.getQdxf());
        model.put("pjjd", student.getPjjd());

        Calendar now = Calendar.getInstance();
        model.put("year", now.get(Calendar.YEAR));
        model.put("month", now.get(Calendar.MONTH) + 1);
        model.put("day", now.get(Calendar.DAY_OF_MONTH));

        // 构造塞入模板的成绩信息，模板中表格固定是36行，21列，分为三个区域，每7列一个区域
        List<Score> scores = student.getScores();
        final int rows = 36;
        final int area = 7;
        int n = scores.size() / (rows - 1) + 1;// 需要循环写几次
        int pos = 0;// 标记当前正在填入的score数据集下标
        int time = 1;// 标记第几次填区域
        label:
        while (n-- > 0) {
            int startCol = 0;
            switch (time) {
                case 1:
                    startCol = 0;
                    break;
                case 2:
                    startCol = 7;
                    break;
                case 3:
                    startCol = 14;
                    break;
            }

//            System.out.println("time=" + time + ",startCol=" + startCol);
            for (int i = 1; i < rows; i++) {
                for (int j = startCol; j < area + startCol; j++) {
                    String key = "r" + i + "c" + j;
                    String val = null;
                    switch (j - startCol) {
                        case 0:
                            val = String.valueOf(scores.get(pos).getXqSort());
                            break;
                        case 1:
                            val = scores.get(pos).getKcmc();
                            break;
                        case 2:
                            val = scores.get(pos).getKclb();
                            break;
                        case 3:
                            val = scores.get(pos).getXdzk();
                            break;
                        case 4:
                            val = String.valueOf(scores.get(pos).getXf());
                            break;
                        case 5:
                            val = String.valueOf(scores.get(pos).getCj());
                            break;
                        case 6:
                            val = String.valueOf(scores.get(pos).getJd());
                            break;
                    }
                    model.put(key, val);
//                    System.out.println(key + "," + val);
                }

                pos++;
                // 所有信息已经填完结束了
                if (pos >= scores.size()) {
                    break label;
                }
            }
            time++;
        }
//        System.out.println(n);

        // 塞入模板文档
        template.render(model);
//        System.out.println(model);

//        System.out.println("pos=" + pos);

        FileOutputStream out = new FileOutputStream(new File("C:\\Users\\zimo\\Desktop\\out.docx"));
        template.write(out);

        out.close();
    }
}
