package serialization.hessian;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;

/**
 * <p>
 * hessian2 序列化和反序列化
 * </p>
 * Created by zhengyong on 17/5/27.
 */
public class HessianUtil {

    /**
     * 序列化数据
     * 
     * @param message
     * @throws Exception
     */
    public static byte[] encoder(Object message) throws Exception {

        ByteArrayOutputStream byteOutputStream = null;
        // hessian解析二进制
        Hessian2Output hessian2Output = null;
        try {
            byteOutputStream = new ByteArrayOutputStream();
            hessian2Output = new Hessian2Output(byteOutputStream);
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
            if (hessian2Output != null) {
                hessian2Output.close();
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
    public static Object decoder(byte[] data) throws Exception {

        ByteArrayInputStream is = null;
        // hessian解析二进制
        Hessian2Input hessian2Input = null;
        try {
            is = new ByteArrayInputStream(data);
            hessian2Input = new Hessian2Input(is);
            /**
             * 设置serializerFactory能将hessian序列化的效率提高几倍，如果不设置会导致最初的几次序列化效率低，出现阻塞的情况
             */
            System.out.println(new String(data));
            SerializerFactory factory = new SerializerFactory();
            hessian2Input.setSerializerFactory(factory);
            // hessian反序列化对象
            //hessian2Input.startMessage();
            Object ret = hessian2Input.readObject();
            //hessian2Input.completeMessage();
            return ret;
        } finally {
            if (is != null) {
                is.close();
            }
            if (hessian2Input != null) {
                hessian2Input.close();
            }
        }

    }

}
