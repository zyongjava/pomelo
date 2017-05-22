package file;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * Created by zhengyong on 17/4/18.
 */
public class IpListImpl implements IpList {

    private static final Logger logger = LoggerFactory.getLogger(IpListImpl.class);

    /**
     * 根据数据量预算空间初始化Set大小
     */
    private static Set<Long>    set    = Collections.synchronizedSet(new HashSet<Long>(100000));

    /**
     * 构造函数,初始化加载指定路径ip地址
     * 
     * @param filePath ip地址文件路径
     */
    public IpListImpl(String filePath){
        initIpToCache(filePath);
    }

    /**
     * 初始化加载IP地址到内存(假设一行文件一个ip地址)
     * 
     * @param filePath 文件路径
     */
    private void initIpToCache(String filePath) {
        FileReader fr = null;
        BufferedReader buf = null;
        try {
            fr = new FileReader(filePath);
            buf = new BufferedReader(fr);
            String line;
            while ((line = buf.readLine()) != null) {
                if (StringUtils.isNotBlank(line)) {
                    Long ipLong = transferIpToLong(line);
                    if (ipLong != null) {
                        set.add(ipLong);
                    }
                }
            }

        } catch (Exception e) {
            logger.error("init load ip error", e);
        } finally {

            try {
                if (fr != null) {
                    buf.close();
                }
                if (buf != null) {
                    buf.close();
                }
            } catch (IOException e) {
                logger.error("close buf error", e);
            }
        }

    }

    @Override
    public boolean isInList(String ip) {
        long ipLong = transferIpToLong(ip);
        return set.contains(ipLong);
    }

    /**
     * 把ip地址字符串转为long型,节约空间
     * 
     * @param ipString ip地址字符串
     * @return long型ip地址值
     */
    public Long transferIpToLong(String ipString) {
        if (StringUtils.isBlank(ipString)) {
            return null;
        }
        ipString = ipString.trim();
        String[] ipGroup = ipString.split("\\.");
        if (ipGroup.length != 4) {
            return null;
        }
        Long result = 0L;
        result += Long.parseLong(ipGroup[0]) << 24;
        result += Long.parseLong(ipGroup[1]) << 16;
        result += Long.parseLong(ipGroup[2]) << 8;
        result += Long.parseLong(ipGroup[3]);
        return result;
    }

    public static void main(String[] args) {
        String ip = "172.168.43.112";
        IpListImpl ipList = new IpListImpl("/Users/zhengyong/ip.txt");
        boolean result = ipList.isInList(ip);
        logger.info("the ip address={}, result={}", ip, result);

    }
}
