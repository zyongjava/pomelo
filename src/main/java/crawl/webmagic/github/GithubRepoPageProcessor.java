package crawl.webmagic.github;

import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 爬取github https://github.com/zyongjava
 */
public class GithubRepoPageProcessor implements PageProcessor {

    public static Logger logger = LoggerFactory.getLogger(GithubRepoPageProcessor.class);

    private Site         site   = Site.me().setSleepTime(0).setCycleRetryTimes(3);

    @Override
    public void process(Page page) {
        page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/\\w+/\\w+)").all());
        page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
        page.putField("name", page.getHtml().xpath("//a[@data-pjax='#js-repo-pjax-container']/text()").toString());
        if (page.getResultItems().get("name") == null) {
            // skip this page
            page.setSkip(true);
        }
        page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        String log4j = GithubRepoPageProcessor.class.getResource("/log4j.xml").getPath();
        DOMConfigurator.configure(log4j);

        Spider.create(new GithubRepoPageProcessor())
              // 从https://github.com/zyongjava/pomelo开始抓
              .addUrl("https://github.com/zyongjava/pomelo").addPipeline(new ConsolePipeline())
              // 开启5个线程同时执行
              .thread(5)
              // 启动爬虫
              .run();

        logger.info("start spider success.");
    }
}
