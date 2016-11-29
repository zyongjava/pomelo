package crawl.webmagic.csdn;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * csdn 下载控制
 */
public class CsdnProcessor implements PageProcessor {

    public static Logger       logger      = LoggerFactory.getLogger(CsdnProcessor.class);

    private Site               site        = Site.me().setSleepTime(0).setCycleRetryTimes(3);

    /**
     * 保存下载csdn页面路径
     */
    private static String      fileDirPath = "/Users/zhengyong/pomelo/csdn/";

    private static File        fileDir     = null;

    /**
     * csdn uri 后缀
     */
    public static final String CSDN_URI    = "zhengyong15984285623";

    private void createFilePath() {
        fileDir = new File(fileDirPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
    }

    @Override
    public void process(Page page) {
        createFilePath();

        List pagenation = page.getHtml().links().regex("/" + CSDN_URI + "/article/list/\\d*").all();
        page.addTargetRequests(pagenation);
        // 里面页面只捕捉url加入爬取队列
        if (CollectionUtils.isNotEmpty(pagenation)) {
            List<String> titleList = page.getHtml().xpath("//div[@id='article_list']/div[@class=list_item]").all();
            for (String titleHtml : titleList) {
                page.addTargetRequests(new Html(titleHtml).links().regex("/" + CSDN_URI
                                                                         + "/article/details/\\d*").all());
            }
            page.setSkip(true);
        } else { // csdn具体文章页面
            String title = page.getHtml().xpath("//div[@class=article_title]/h1/span/a/text()").toString();
            String createTime = page.getHtml().xpath("//div[@class=article_r]/span[@class=link_postdate]/text()").toString();
            page.putField("title", title);
            page.putField("createTime", createTime);
            File file = new File(fileDir, title.trim() + ".html");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    logger.error("create file error.", e);
                }
            }
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    FileUtils.write(file, page.getHtml().toString());
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }

        }
    }

    @Override
    public Site getSite() {
        return site;
    }

}
