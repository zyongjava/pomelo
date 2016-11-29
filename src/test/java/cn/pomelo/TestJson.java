package cn.pomelo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TestJson {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		String version = "v1";
		String json = generateJSON();
		parseJSON(json, version);
		System.out.println(json);
	}

	private static void parseJSON(String json, String version) {
		JSONObject object = JSON.parseObject(json);
		// 获取密码列表
		JSONArray array = object.getJSONArray("password");
		// 获取原密码
		System.out.println("原密码:" + getVersionPassword(array, version));
		//  获取当前密码
		System.out.println("当前密码:" + getCurrentlyPassword(array));
	}
	
	// 获取当前密码
	private static String getVersionPassword(JSONArray array, String version) {
		for (int i = 0; i < array.size(); i++) {
			Map<String, String> map = (Map) array.get(i);
			if (map.get(version) != null) {
				return map.get(version);
			}
		}
		return null;
	}

	// 获取当前密码
	private static String getCurrentlyPassword(JSONArray array) {
		Map<String, String> map = (Map) array.get(array.size() - 1);
		Set<String> key = map.keySet();
		for (String v : key) {
			return map.get(v);
		}
		return null;
	}

	private static String generateJSON() {

		Map<String, Object> map = Maps.newHashMap();
		List<Map<String, String>> list = Lists.newArrayList();

		Map<String, String> version1 = Maps.newHashMap();
		Map<String, String> version2 = Maps.newHashMap();
		Map<String, String> version3 = Maps.newHashMap();
		version1.put("v1", "123456");
		version2.put("v2", "test");
		version3.put("v3", "admin");
		list.add(version1);
		list.add(version3);
		list.add(version2);
		map.put("password", list);

		String result = JSON.toJSONString(map);

		return result;
	}
}
