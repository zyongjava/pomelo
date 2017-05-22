package file;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by zhengyong on 17/4/17.
 */
public class ScannerTest {

    private static final String filePath = "/Users/zhengyong/log.txt";

    public static void main(String[] args) throws Exception {
        scannerFile();
        System.out.println("_______________________");
        // readFile();
        // System.out.println("_______________________");
        bufferRead("test");
    }

    /**
     * 输出指定字符串所在行
     * @param patten
     * @throws Exception
     */
    public static void bufferRead(String patten) throws Exception {
        FileReader fr = new FileReader(filePath);
        BufferedReader buf = new BufferedReader(fr);
        String line;
        int lineCount = 0;
        while ((line = buf.readLine()) != null) {
            String temp = line;
            lineCount++;
            int index = 0;
            while (index != -1) {
                index = line.indexOf(patten);
                line = line.substring(index + 1);
                if (index != -1) {
                    System.out.println("line:" + lineCount + ", index:" + index + "; line:" + temp);
                }
            }
        }
        buf.close();

    }

    public static void scannerFile() throws IOException {
        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            inputStream = new FileInputStream(filePath);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                System.out.println(line);
            }
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }
    }

    public static void readFile() throws IOException {
        File file = new File(filePath);
        LineIterator it = FileUtils.lineIterator(file, "UTF-8");
        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                System.out.println(line);
            }
        } finally {
            LineIterator.closeQuietly(it);
        }
    }
}
