package kafka.serialization;

import com.alibaba.fastjson.JSON;
import kafka.model.User;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 自定义序列化<br/>
 *
 * Created by zhengyong on 17/3/2.
 */
public class UserModelDeSerializer implements Deserializer<User> {

    private String encoding = "UTF8";

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        String propertyName = isKey ? "key.deserializer.encoding" : "value.deserializer.encoding";
        Object encodingValue = configs.get(propertyName);
        if (encodingValue == null) {
            encodingValue = configs.get("deserializer.encoding");
        }

        if (encodingValue != null && encodingValue instanceof String) {
            this.encoding = (String) encodingValue;
        }
    }

    @Override
    public User deserialize(String topic, byte[] data) {
        try {
            String userStr = data == null ? null : new String(data, this.encoding);
            return JSON.parseObject(userStr, User.class);
        } catch (UnsupportedEncodingException var4) {
            throw new SerializationException("Error when deserializing byte[] to string due to unsupported encoding "
                                             + this.encoding);
        }
    }

    @Override
    public void close() {

    }
}
