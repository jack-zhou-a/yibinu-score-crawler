package wiki.zimo.scorecrawler.service;

import wiki.zimo.scorecrawler.domain.Student;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface TemplateService {
    void rendering(Student student) throws IOException;
}
