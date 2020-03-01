package wiki.zimo.scorecrawler.domain;

import java.util.ArrayList;
import java.util.List;

public class Student {
    String xh;// 学号
    String xm;// 姓名
    String bj;// 班级
    String nj;// 年级
    String yxmc;// 院系名称
    String zymc;// 专业名称
    String xz;// 学制
    double qdxf;// 取得学分
    double pjjd;// 平均绩点
    List<Score> scores;// 成绩集
    String pwd;// 密码

    @Override
    public String toString() {
        return "Student{" +
                "学号='" + xh + '\'' +
                ", 姓名='" + xm + '\'' +
                ", 班级='" + bj + '\'' +
                ", 年级='" + nj + '\'' +
                ", 院系名称='" + yxmc + '\'' +
                ", 专业名称='" + zymc + '\'' +
                ", 学制='" + xz + '\'' +
                ", 取得学分=" + qdxf +
                ", 平均绩点=" + pjjd +
                ", 成绩集=" + scores +
                ", 密码='" + pwd + '\'' +
                '}';
    }

    public Student(String xh, String pwd) {
        this.xh = xh;
        this.pwd = pwd;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getPwd() {
        return pwd;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getBj() {
        return bj;
    }

    public void setBj(String bj) {
        this.bj = bj;
    }

    public String getNj() {
        return nj;
    }

    public void setNj(String nj) {
        this.nj = nj;
    }

    public String getYxmc() {
        return yxmc;
    }

    public void setYxmc(String yxmc) {
        this.yxmc = yxmc;
    }

    public String getZymc() {
        return zymc;
    }

    public void setZymc(String zymc) {
        this.zymc = zymc;
    }

    public String getXz() {
        return xz;
    }

    public void setXz(String xz) {
        this.xz = xz;
    }

    public double getQdxf() {
        return qdxf;
    }

    public void setQdxf(double qdxf) {
        this.qdxf = qdxf;
    }

    public double getPjjd() {
        return pjjd;
    }

    public void setPjjd(double pjjd) {
        this.pjjd = pjjd;
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        for (Score s :scores){
            this.qdxf += s.getXf();
            this.pjjd += s.getJd();
        }
        this.pjjd /= scores.size();
        this.scores = scores;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }


}
