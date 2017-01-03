package crawl.webmagic.music163;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/**
 * 网易云音乐 抓取
 */
public class MusicProcessor implements PageProcessor {

    public static Logger logger = LoggerFactory.getLogger(MusicProcessor.class);

    private Site         site   = Site.me().setSleepTime(0).setDomain("music.163.com").setCycleRetryTimes(3).setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.98 Safari/537.36");

    @Override
    public void process(Page page) {
        List<String> musics = page.getHtml().xpath("//div[@id=song-list-pre-cache]/ul/li").all();

        if (CollectionUtils.isEmpty(musics)) {
            logger.warn("no fond music");
            return;
        }

        for (String music : musics) {
            Html html = new Html(music);
            String url = html.xpath("//li/a/@href").toString();
            String title = html.xpath("//li/a/text()").toString();
            logger.info("music={}, url={}", title, url);
        }

    }

    @Override
    public Site getSite() {
        return site;
    }

}
