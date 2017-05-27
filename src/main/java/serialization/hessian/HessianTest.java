package serialization.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * <p>
 * hessian2 序列化和反序列化
 * </p>
 * Created by zhengyong on 17/5/27.
 */
public class HessianTest {

    private static final String filePath = "/Users/zhengyong/111/work_dev/hessian1.txt";

    public static void main(String[] args) throws Exception {

        String url = "http://hessian.caucho.com";

        // encoder data
        FileOutputStream output = new FileOutputStream(filePath);
        try {
            byte[] encoderData = encoder(url);
            output.write(encoderData);
            System.out.println("encoder finish. the data=" + url);
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
            Object object = decoder(decoderData);
            System.out.println("decoder finish. the data=" + object.toString());
        } finally {
            if (input != null) {
                input.close();
            }
        }

    }

    /**
     * 序列化数据
     * 
     * @param message
     * @throws Exception
     */
    private static byte[] encoder(Object message) throws Exception {

        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        try {
            // hession解析二进制
            Hessian2Output hessian2Output = new Hessian2Output(byteOutputStream);
            /**
             * 设置serializerFactory能将hessian序列化的效率提高几倍，如果不设置会导致最初的几次序列化效率低，出现阻塞的情况。
             * 主要原因是如果hessian2Output中的serializerFactory为空的话，writeObject的时候创建这个对象的时候会出现阻塞，导致最初几次调用耗时过长
             */
            SerializerFactory factory = new SerializerFactory();
            hessian2Output.setSerializerFactory(factory);
            // 写入序列化信息
            // hessian2Output.startMessage();
            hessian2Output.writeObject(message);
            // hessian2Output.completeMessage();

            hessian2Output.flush(); // 将序列化信息发送出去

            byte[] data = byteOutputStream.toByteArray();

            return data;
        } finally {
            if (byteOutputStream != null) {
                byteOutputStream.close();
            }
        }
    }

    /**
     * 反序列化二进制数据
     * 
     * @param data
     * @return Object
     * @throws Exception
     */
    private static Object decoder(byte[] data) throws Exception {

        ByteArrayInputStream is = new ByteArrayInputStream(data);
        // hessian解析二进制
        Hessian2Input hessian2Input = new Hessian2Input(is);
        /**
         * 设置serializerFactory能将hessian序列化的效率提高几倍，如果不设置会导致最初的几次序列化效率低，出现阻塞的情况
         */
        SerializerFactory factory = new SerializerFactory();
        hessian2Input.setSerializerFactory(factory);
        // hessian反序列化对象
        // hessian2Input.startMessage();
        Object ret = hessian2Input.readObject();
        // hessian2Input.completeMessage();

        return ret;
    }

}
