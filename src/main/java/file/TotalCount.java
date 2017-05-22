package file;

import minheap.HeapSort;
import minheap.MapHeapSort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengyong on 17/4/18.
 */
public class TotalCount {

    private static final int                  maxCount = 100;

    private static final String               filePath = "/Users/zhengyong/url.txt";

    private static List<Map<String, Integer>> urlMap   = new ArrayList<>(maxCount);

    public static void main(String[] args) throws Exception {

        FileReader fr = new FileReader(filePath);
        BufferedReader buf = new BufferedReader(fr);
        String line;
        while ((line = buf.readLine()) != null) {
            int mod = Math.abs(line.hashCode()) % 100;
            System.out.println(mod);
            writeFile(line, mod);
        }
        buf.close();
    }

    public static boolean writeFile(String content, int index) {
        String filename = "/Users/zhengyong/url/" + index + "_url.txt";
        try {
            File f = new File(filename);
            if (!f.exists()) {
                f.createNewFile();
            }
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f,true));
            BufferedWriter writer = new BufferedWriter(write);
            writer.write(content);
            writer.flush();
            write.close();
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
