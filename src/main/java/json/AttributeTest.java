package json;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import annotation.AnnotationConfig;
import annotation.UserDTO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

/**
 * annotation 测试类<br/>
 * <p>
 * 解析同一个注解的值到同一个属性上
 * </p>
 * Created by zhengyong on 16/12/7.
 */
public class AttributeTest {

    public static void main(String[] args) {

        String json = "{\"accountEmail\":\"235564565@163.com\",\"accountMobile\":[\"18969975142\"],\"cardNumber\":[\"345678943356432347\"],\"myMobile\":{\"accountMobile\":\"15983283921\"},\"myCard\":{\"cardNumber\":\"345678943356432322\"},\"myIdNumber\":{\"idNumber\":[\"15983283421\",\"15983283221\"]},\"myQQ\":{\"qqNumber\":[\"234234234\",\"34353443\"]},\"idNumber\":\"511028199010011111\",\"qqNumber\":[\"3445323210\",\"344532328\",\"344532329\"]}";
        JSONObject jsonObject = JSON.parseObject(json);
        Attribute attribute = new Attribute();
        reflect(jsonObject, attribute);
        System.out.println(JSON.toJSONString(attribute));

    }

    public static void reflect(JSONObject jsonObject, Attribute attribute) {
        if (jsonObject == null) {
            return;
        }
        Field[] fields = attribute.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Class<?> clazz = field.getType();
            AttributeConfig annotation = field.getAnnotation(AttributeConfig.class);
            if (annotation == null) {
                return;
            }
            String[] includes = annotation.includes();
            if (includes == null) {
                return;
            }

            if (clazz.isAssignableFrom(String.class)) {
                for (int k = 0; k < includes.length; k++) {
                    String[] item = includes[k].split("/");
                    Object tempJSON1 = getObject(jsonObject, item);
                    field.setAccessible(true);
                    try {
                        field.set(attribute, tempJSON1);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (clazz.isAssignableFrom(Set.class)) {
                Set value = Sets.newHashSet();
                for (int k = 0; k < includes.length; k++) {
                    String[] item = includes[k].split("/");
                    Object tempJSON1 = getObject(jsonObject, item);
                    if (tempJSON1 != null) {
                        if (tempJSON1 instanceof List) {
                            value.addAll((List) tempJSON1);
                        } else {
                            value.add(tempJSON1);
                        }
                    }
                }

                field.setAccessible(true);
                try {
                    field.set(attribute, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * @param jsonObject jsonObject
     * @param item keyItem
     * @return Object
     */
    private static Object getObject(JSONObject jsonObject, String[] item) {
        JSONObject tempJSON = (JSONObject) jsonObject.clone();
        Object tempJSON1 = null;
        for (int c = 0; c < item.length; c++) {
            tempJSON1 = getJSONValue(tempJSON, item[c]);
            if (c != item.length - 1) {
                if (tempJSON1 instanceof JSONObject) {
                    tempJSON = (JSONObject) tempJSON1;
                } else if (tempJSON1 instanceof String) {
                    tempJSON = JSON.parseObject((String) tempJSON1);
                }
            }
        }
        return tempJSON1;
    }

    public static Object getJSONValue(JSONObject jsonObject, String key) {
        if (jsonObject == null || StringUtils.isBlank(key)) {
            return null;
        }
        return jsonObject.get(key);
    }

}
