package cn.pomelo;

/**
 * Created by zhengyong on 17/4/17.
 */
public class WordsTest {

    public static void main(String[] args) {
        String word = "我d爱a里巴巴";
        int index = total(word, 7);
        System.out.println(word.substring(0, index+1));
    }

    public static int total(String word, int charIndex) {
        int size = 0;
        for (int i = 0; i < word.length(); i++) {
            size++;
            if (isChinese(word.charAt(i))) {
                size++;
            }
            if (size >= charIndex) {
                return i;
            }
        }
        return size;
    }

    public static boolean print(char c) {
        if ((c >= 0x4e00) && (c <= 0x9fbb)) {
            return true;
        }
        return false;
    }

    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
            || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
            || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
            || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
            || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
}
