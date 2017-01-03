package crawl.webmagic.music163;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

/**
 * 网易云音乐 抓取
 */
public class MusicTest {

    public static void main(String[] args) {
        Spider.create(new MusicProcessor())
              // 从url开始抓
              .addUrl("http://music.163.com/m/playlist?id=455563168&userid=325465165#?thirdfrom=qq")
              // 设置Scheduler，使用File来管理URL队列
              // .setScheduler(new FileCacheQueueScheduler("/app/queue"))
              // 设置Pipeline，将结果以json方式保存到文件
              .addPipeline(new ConsolePipeline())
              // 开启5个线程同时执行
              .thread(5)
              // 启动爬虫
              .run();
    }
}
