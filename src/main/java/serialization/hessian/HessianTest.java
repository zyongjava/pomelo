package serialization.hessian;

import serialization.object.Person;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * <p>
 * hessian2 序列化和反序列化
 * </p>
 * Created by zhengyong on 17/5/27.
 */
public class HessianTest {

    private static final String filePath = "/Users/zhengyong/work_dev/hessian.txt";

    public static void main(String[] args) throws Exception {

        Person person = new Person();
        person.setId(1222);
        person.setName("张三");
        person.setEmail("524806855@qq.com");

        // encoder data
        FileOutputStream output = new FileOutputStream(filePath);
        try {
            byte[] encoderData = HessianUtil.encoder(person);
            output.write(encoderData);
            System.out.println("encoder finish. the person = " + person);
        } finally {
            if (output != null) {
                output.close();
            }
        }

        // decoder data
        FileInputStream input = new FileInputStream(filePath);
        try {
            byte[] decoderData = new byte[input.available()];
            input.read(decoderData); // 读入流,保存在byte数组
            Object object = HessianUtil.decoder(decoderData);
            System.out.println("decoder finish. the person = " + object.toString());
        } finally {
            if (input != null) {
                input.close();
            }
        }

    }

}
