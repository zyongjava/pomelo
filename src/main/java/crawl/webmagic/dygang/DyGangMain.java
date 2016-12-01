package crawl.webmagic.dygang;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;

import java.io.File;

/**
 * 基于webmagic爬取http://www.dygang.com/
 */
public class DyGangMain {

    public static Logger logger     = LoggerFactory.getLogger(DyGangMain.class);

    public static String store_path = "/Users/zhengyong/pomelo";

    public static void main(String[] args) {

        String log4j = DyGangMain.class.getResource("/log4j.xml").getPath();
        DOMConfigurator.configure(log4j);// 加载.xml文件
        createFilePath();

        Spider.create(new DyGangProcessor())
              // 从url开始抓
              .addUrl("http://www.dygang.com")
              // 设置Scheduler，使用File来管理URL队列
              .setScheduler(new FileCacheQueueScheduler(store_path + "/queue"))
              // 设置Pipeline，将结果以json方式保存到文件
              .addPipeline(new ConsolePipeline())
              // 开启5个线程同时执行
              .thread(5)
              // 启动爬虫
              .run();

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
