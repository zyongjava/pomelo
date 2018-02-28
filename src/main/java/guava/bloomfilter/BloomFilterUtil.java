package guava.bloomfilter;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;
import org.apache.commons.lang3.StringUtils;

/**
 * <pre>
 *     guava bloom filter
 *     布隆过滤器存在误判（false positive），存在不一定真的存在（一定概率误判），但不存在真的不存在。
 * </pre>
 */
public class BloomFilterUtil {

    private static BloomFilter<String> bloomFilter;

    static {
        createBloomFilter();
    }

    /**
     * 初始化 bloom filter
     */
    private static void createBloomFilter() {
        bloomFilter = BloomFilter.create(new Funnel<String>() {
            @Override
            public void funnel(String from, PrimitiveSink into) {
                into.putString(from, Charsets.UTF_8);
            }
        }, 10000L, 0.0001);

    }

    /**
     * 判断是否存在值
     *
     * @param value 值
     * @return true / false
     */
    private static boolean isExistValue(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        boolean exists = bloomFilter.mightContain(value);
        if (!exists) {
            bloomFilter.put(value);
        }
        return exists;
    }

    // test
    public static void main(String[] args) {
        System.out.println(isExistValue(""));
        System.out.println(isExistValue("111"));
        System.out.println(isExistValue("111"));
        System.out.println(isExistValue("11"));
    }
}
