package crawl;

import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;

public class GithubRepoPageProcessor implements PageProcessor {

    public static Logger logger = LoggerFactory.getLogger(GithubRepoPageProcessor.class);

    private Site site = Site.me().setSleepTime(0).setCycleRetryTimes(3);

    @Override
    public void process(Page page) {
        page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/\\w+/\\w+)").all());
        page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
        page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());
        if (page.getResultItems().get("name")==null){
            //skip this page
            page.setSkip(false);
        }
        page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        DOMConfigurator.configure("/Users/pomelo/work_dev/http-client/src/main/java/crawl/log4j.xml");//加载.xml文件

      //  Spider.create(new GithubRepoPageProcessor()).addUrl("https://github.com/code4craft").thread(5).run();
        Spider.create(new GithubRepoPageProcessor())
                //从https://github.com/code4craft开始抓
                .addUrl("https://github.com/code4craft")
                //设置Scheduler，使用Redis来管理URL队列
                //.setScheduler(new FileCacheQueueScheduler("/Users/pomelo/euler_deploy/queue"))
                //设置Pipeline，将结果以json方式保存到文件
                //.addPipeline(new JsonFilePipeline("/Users/pomelo/euler_deploy/json"))
                //开启5个线程同时执行
                .thread(5)
                //启动爬虫
                .run();
    }
}