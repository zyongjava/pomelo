package crawl.webmagic.dygang;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/**
 * http://www.dygang.com/ 下载控制
 */
public class DyGangProcessor implements PageProcessor {

    public static Logger  logger      = LoggerFactory.getLogger(DyGangProcessor.class);

    private static File   file        = null;

    private static String fileDirPath = "/Users/zhengyong/pomelo/dygang/";

    private Site          site        = Site.me().setSleepTime(0).setCycleRetryTimes(3).setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36");

    private void createFile() {
        File fileDir = new File(fileDirPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        file = new File(fileDir, "move.csv");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error("create file error.", e);
            }
        }
    }

    @Override
    public void process(Page page) {

        createFile();

        List pagenation = page.getHtml().links().regex("http://www.dygang.com/.*").all();
        page.addTargetRequests(pagenation);

        String moveName = page.getHtml().xpath("//div[@align=center]/a/text()").toString();
        String date = page.getHtml().regex("上映日期.*?<").get();
        if (StringUtils.isNotBlank(date)) {
            date = date.substring(5, date.length() - 1);
        } else {
            date = "-";
        }

        String source = page.getUrl().toString();
        List<String> urls = new Html(page.getRawText()).xpath("//table[@bgcolor=#0099cc]/tbody/tr").all();
        if (CollectionUtils.isEmpty(urls) || StringUtils.isBlank(moveName)) {
            return;
        }

        for (String urlHref : urls) {
            String url = new Html(urlHref).xpath("//a/@href").toString();
            String name = new Html(urlHref).xpath("//a/text()").toString();
            if (StringUtils.isNotBlank(url)) {
                try {
                    String context = String.format("%s , %s , %s , %s, %s", moveName, url, name, source, date);
                    logger.info(context);

                    BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
                    writer.newLine();
                    writer.write(context);
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    logger.error("write file content error", e);
                }
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

}
