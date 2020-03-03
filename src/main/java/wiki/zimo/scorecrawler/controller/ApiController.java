package wiki.zimo.scorecrawler.controller;

import com.alibaba.fastjson.JSONException;
import com.deepoove.poi.XWPFTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wiki.zimo.scorecrawler.domain.Student;
import wiki.zimo.scorecrawler.service.CrawlerService;
import wiki.zimo.scorecrawler.service.TemplateService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
    public void getScoreReport(@RequestParam("xh") String xh, @RequestParam("pwd") String pwd, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (xh.equals(session.getAttribute("xh"))) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("<script>alert('已下载成绩单，请检查文件目录');window.location.href='" + request.getContextPath() + "';</script>");
            return;
        }
        try {
            session.setAttribute("xh", xh);
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
            session.removeAttribute("xh");
            response.setContentType("text/html;charset=utf-8");
            if (e instanceof JSONException) {
                response.getWriter().write("<script>alert('用户名或密码错误');window.location.href='" + request.getContextPath() + "';</script>");
                return;
            }
            response.getWriter().write("<script>alert('系统异常，请联系站长（461009747）');window.location.href='" + request.getContextPath() + "';</script>");
        }
    }
}
