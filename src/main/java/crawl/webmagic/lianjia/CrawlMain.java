package crawl.webmagic.lianjia;

import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;

import java.io.File;

/**
 * 爬取链家
 *
 * Created by pomelo on 16/10/19.
 */
public class CrawlMain {

    public static Logger logger     = LoggerFactory.getLogger(CrawlMain.class);

    public static String store_path = "/Users/zhengyong/pomelo";

    public static void main(String[] args) {

        String log4j = CrawlMain.class.getResource("/log4j.xml").getPath();
        DOMConfigurator.configure(log4j);
        createFilePath();

        OOSpider.create(Site.me().setTimeOut(100000),new ConsolePageModelPipeline(), House.class)
                // .addPipeline(new JsonFilePipeline("/Users/pomelo/json"))
                //设置Scheduler，
                .addPipeline(new ConsolePipeline())
                .setScheduler(new FileCacheQueueScheduler(store_path + "/queue"))
                .addUrl("http://hz.fang.lianjia.com/loupan/").thread(5).run();

        logger.info("start spider success.");
    }

    private static void createFilePath() {
        File fileDir = new File(store_path);
        if (!fileDir.exists()) {
            boolean success = fileDir.mkdirs();
            logger.info(String.format("make path %s", success));
        }
    }

}
