# yibinu-score-crawler
宜宾学院教务系统成绩爬虫

# 前言
> 三教大厅有一个智能终端，上面可以利用身份证打印自己的成绩（有次数限制）；但是学校的智慧校园网站里面并没有可以打印排好版的成绩单的接口（坑就坑在这里）。而据博主了解，要想打印成绩单只有两个途径，1是到三教大厅，2是找二级学院教学管理科的老师，请ta帮忙下载pdf文档。博主由于要准备研究生复试，需要成绩单，而目前处于疫情期间，又不能返校，于是通过各种关系找到了我们学院那位教学管理科的老师，可能是博主跟ta不熟的原因，总感觉ta态度很冷淡，于是本着求人不如求己的想法，做了这个爬虫，并免费开放给大家使用

# 功能
输入学号和密码，系统将会返回排好版的成绩单（与三教大厅打印的成绩单几乎相同）

# 如何使用
1. [点击这里访问系统](http://www.zimo.wiki:8080/yibinu-score-crawler/)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200303155344196.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM2NzM3OTM0,size_16,color_FFFFFF,t_70)
2. 输入学号和密码
3. 点击下载成绩单按钮
4. 效果图![点击这里输入图片描述](https://img-blog.csdnimg.cn/20200303163257622.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM2NzM3OTM0,size_16,color_FFFFFF,t_70)

### 致计算机相关专业的同学或者对爬虫源码感兴趣的同学
1. 本系统开源，[源码地址](https://github.com/ZimoLoveShuang/yibinu-score-crawler.git)
2. 系统实现语言是java，但可以轻松的按照设计思路用python或者其他编程语言实现，此处不再赘述
3. 系统分为两个模块，爬虫模块和渲染成绩单模块
4. 爬虫模块通过分析智慧校园源码和接口完成，主要原理为模拟浏览器行为登陆教务（智慧校园）系统，获取接口返回的数据，包装为bean，以供下面渲染成绩单使用
5. 渲染成绩单模块按照教学管理科老师帮忙下载的pdf文档作为模板，将上面请求到的信息poi-tl渲染完成
6. 系统技术找是springboot + thymeleaf + jsoup + poi-tl

# 如果觉得博主写得不错，或者想支持博主，请帮忙在GitHub点个Star或者扫描下面二维码捐赠博主
<table>
    <tr>
        <td ><center><img src="https://img-blog.csdnimg.cn/20200303161837163.jpg" ></center></td>
        <td ><center><img src="https://img-blog.csdnimg.cn/20200303162019613.png"  ></center></td>
    </tr>
</table>