package wiki.zimo.scorecrawler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wiki.zimo.scorecrawler.domain.Student;
import wiki.zimo.scorecrawler.service.CrawlerService;
import wiki.zimo.scorecrawler.service.TemplateService;


@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private CrawlerService crawlerService;
    @Autowired
    private TemplateService templateService;

    @RequestMapping("/login")
    public Student login(@RequestParam("xh") String xh, @RequestParam("pwd") String pwd) {
        try{
            Student student = crawlerService.score(new Student(xh, pwd));
            templateService.rendering(student);
            return student;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
