package wiki.zimo.scorecrawler.service;

import wiki.zimo.scorecrawler.domain.Student;

import java.io.IOException;

public interface CrawlerService {
    Student getStudentWithScore(Student user) throws IOException;
}
