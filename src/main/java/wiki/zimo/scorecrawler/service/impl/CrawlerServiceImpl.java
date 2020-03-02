package wiki.zimo.scorecrawler.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wiki.zimo.scorecrawler.domain.Score;
import wiki.zimo.scorecrawler.domain.Student;
import wiki.zimo.scorecrawler.service.CrawlerService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CrawlerServiceImpl implements CrawlerService {

    @Value("${LOGIN_API}")
    private String LOGIN_API;// 登陆接口
    @Value("${SELECT_ROLE_API}")
    private String SELECT_ROLE_API;// 成绩查询之前的选择角色接口
    @Value("${SCORE_API}")
    private String SCORE_API;// 成绩查询接口
    @Value("${XSJBXX_URL}")
    private String XSJBXX_URL;// 学生基本信息查询页
    @Value("${XSJBXX_API}")
    private String XSJBXX_API;// 学生基本信息查询接口

    public static String USER_AGENT = "User-Agent";
    public static String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.116 Safari/537.36";

    @Override
    public Student score(Student user) throws IOException {

        // 获取登陆页
        Connection con = Jsoup.connect(LOGIN_API).followRedirects(true);
        con.header(USER_AGENT, USER_AGENT_VALUE);
        Connection.Response res = con.execute();

        // 解析登陆页
        Document doc = res.parse();

        // 全局cookie
        Map<String, String> cookies = res.cookies();

        // 获取登陆表单
        Element form = doc.getElementById("casLoginForm");
        // 获取登陆表单里的输入
        Elements inputs = form.getElementsByTag("input");

        // 获取post请求参数
        Map<String, String> params = new HashMap<>();
        for (Element e : inputs) {
            if (e.attr("name").equals("username")) {
                e.attr("value", user.getXh());
            }

            if (e.attr("name").equals("password")) {
                e.attr("value", user.getPwd());
            }

            // 排除空值表单属性
            if (e.attr("name").length() > 0) {
                // 排除记住我
                if (e.attr("name").equals("rememberMe")) {
                    continue;
                }
                params.put(e.attr("name"), e.attr("value"));
            }
        }

//        System.out.println("登陆参数：");
//        System.out.println(params);

        // 模拟登陆
        Connection.Response login = con.ignoreContentType(true).followRedirects(true).method(Connection.Method.POST).data(params).cookies(cookies).execute();
        // 保存登陆后的cookie
        cookies.putAll(login.cookies());

        // 请求选择角色接口，获取成绩查询url
        Document select = Jsoup.connect(SELECT_ROLE_API.replace("{}", String.valueOf(System.currentTimeMillis()))).ignoreContentType(true).cookies(cookies).get();
        // 解析接口返回的json
        JSONObject json = JSON.parseObject(select.body().text());
        // 拿到成绩查询页的url
        String url = json.getJSONObject("data").getJSONArray("groupList").getJSONObject(0).getString("targetUrl");
        // 请求成绩查询页
        res = Jsoup.connect(url).cookies(cookies).execute();
        // 保存成绩查询cookie
        cookies.putAll(res.cookies());

        // 请求成绩查询接口，请求这个接口之前必须先登陆和请求成绩查询url
        doc = Jsoup.connect(SCORE_API).cookies(cookies).ignoreContentType(true).get();

        // 获取成绩json对象
        JSONObject scoreJson = JSON.parseObject(doc.body().text());

//        System.out.println("成绩查询接口：");
//        System.out.println(scoreJson);

        // 解析成绩
        JSONArray scoreJsonArray = scoreJson.getJSONObject("datas").getJSONObject("xscjcx").getJSONArray("rows");
        List<Score> scores = new ArrayList<>();
        for (int i = 0; i < scoreJsonArray.size(); i++) {
            JSONObject obj = scoreJsonArray.getJSONObject(i);
            Score score = new Score();
            score.setXq(obj.getString("XNXQDM"));
            score.setKcmc(obj.getString("KCM"));
            score.setKclb(obj.getString("KCLBDM_DISPLAY"));
            score.setXdzk(obj.getString("CXCKDM_DISPLAY"));
            score.setXf(obj.getDouble("XF"));
            score.setCj(obj.getInteger("ZCJ"));
            score.setJd(obj.getDouble("XFJD"));
            scores.add(score);
        }

//        System.out.println("解析后的成绩：");
//        System.out.println(scores);

        // 请求学生基本信息页
        res = Jsoup.connect(XSJBXX_URL).cookies(cookies).execute();
        // 更新cookie
        cookies.putAll(res.cookies());

        // 请求学生基本信息接口，请求这接口之前必须先请求学生基本信息页以获取cookie
        doc = Jsoup.connect(XSJBXX_API).cookies(cookies).ignoreContentType(true).get();

//        System.out.println("学生基本信息接口：");
//        System.out.println(doc.body().text());

        // 解析学生基本信息
        JSONObject studentJson = JSON.parseObject(doc.body().text()).getJSONObject("datas").getJSONObject("cxxsjbxxlb").getJSONArray("rows").getJSONObject(0);
        user.setXm(studentJson.getString("XM"));
        user.setBj(studentJson.getString("BJMC"));
        user.setNj(studentJson.getString("XZNJ"));
        user.setYxmc(studentJson.getString("YXDM_DISPLAY"));
        user.setZymc(studentJson.getString("NDZYMC"));
        user.setXz(studentJson.getString("XZ"));
        user.setScores(scores);

        System.out.println(user);

        return user;
    }
}
