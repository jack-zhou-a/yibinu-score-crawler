package wiki.zimo.scorecrawler.controller;

import com.deepoove.poi.XWPFTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wiki.zimo.scorecrawler.domain.Student;
import wiki.zimo.scorecrawler.service.CrawlerService;
import wiki.zimo.scorecrawler.service.TemplateService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;


@Controller
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private CrawlerService crawlerService;
    @Autowired
    private TemplateService templateService;

    @RequestMapping("/getScoreReport")
    public void getScoreReport(@RequestParam("xh") String xh, @RequestParam("pwd") String pwd, HttpServletResponse response) {
        try {
            Student student = crawlerService.getStudentWithScore(new Student(xh, pwd));
            XWPFTemplate template = templateService.renderWordTemplate(student);
            String filePath = System.getProperty("user.dir") + File.separator + System.currentTimeMillis() + ".docx";
            template.writeToFile(filePath);
            File tempFile = new File(filePath);
//            System.out.println(tempFile.getAbsolutePath());

            // 设置文件下载头
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(student.getXm() + "成绩单", "utf-8") + ".docx");
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Content-Length", "" + tempFile.length());

            // 写到网络下载流
            ServletOutputStream out = response.getOutputStream();
            byte[] buffer = new byte[1024];
            InputStream in = new FileInputStream(tempFile);
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }

            in.close();
            out.close();

//            System.out.println(student);

            // 删掉临时文件
            tempFile.delete();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
