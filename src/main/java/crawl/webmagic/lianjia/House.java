package crawl.webmagic.lianjia;

import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * Created by pomelo on 16/10/19.
 */

@TargetUrl("http://hz.fang.lianjia.com/loupan/*")
@HelpUrl("http://hz.fang.lianjia.com/loupan")
@ExtractBy(value = "//ul[@id=\"house-lst\"]/li" ,multi = true)
public class House {

    @ExtractBy(value = "//div[@class=info-panel]/div[@class=col-1]/h2/a/text()", notNull = true)
    private String name;

    @ExtractBy(value = "//div[@class=info-panel]/div[@class=col-1]/div[@class=where]/span[@class=region]/text()")
    private String address;

    @ExtractBy(value = "//div[@class=info-panel]/div[@class=col-1]/div[@class=area]/text()")
    private String area;

    @ExtractBy(value = "//div[@class=info-panel]/div[@class=col-1]/div[@class=area]/span/text()")
    private String size;

    @ExtractBy(value = "//div[@class=info-panel]/div[@class=col-1]/div[@class=type]/span[@class=live]/text()")
    private String type;

    @ExtractBy(value = "//div[@class=info-panel]/div[@class=col-1]/div[@class=other]")
    private String label;

    @ExtractBy(value = "//div[@class=info-panel]/div[@class=col-2]/div[@class=price]/div[@class=average]/text()")
    private String price;

    @ExtractByUrl("http://hz\\.fang\\.lianjia\\.com/loupan/(.*)")
    private String page;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
        this.label = label;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

}
