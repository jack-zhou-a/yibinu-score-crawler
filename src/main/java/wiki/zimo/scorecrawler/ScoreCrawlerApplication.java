package wiki.zimo.scorecrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ScoreCrawlerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ScoreCrawlerApplication.class, args);
    }

    /**
     * @auther: 子墨
     * @date: 2018/11/24 19:24
     * @describe: Tomcat启动Springboot项目
     * @param: [builder]
     * @return: org.springframework.boot.builder.SpringApplicationBuilder
     * @version v1.0
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }
}
