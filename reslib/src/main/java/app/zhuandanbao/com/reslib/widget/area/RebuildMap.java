package app.zhuandanbao.com.reslib.widget.area;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 重建map
 */
@SuppressLint("NewApi")
public class RebuildMap {
	private static String TAG_LOG = "RebuildMap";
	/**
	 * 交换key和value
	 * @param params
	 *            需要交换的键值对
	 * @return 返回map
	 */
	@SuppressLint("NewApi")
	public static HashMap<String, Object> changeMap(
			HashMap<String, Object> params) {
		Log.d(TAG_LOG, "changeMap()");
		Map<String, Object> treeMap = new TreeMap<String, Object>(params);
		HashMap<String, Object> map = new HashMap<String, Object>();
		for (Map.Entry entry : treeMap.entrySet()) {
			String k = entry.getKey().toString();
			Log.d(TAG_LOG, "k=" + k);
			String v = String.valueOf(entry.getValue());
			Log.d(TAG_LOG, "v=" + v);
			if (v != null && !v.isEmpty()) {
				map.put(v, k);
			}
		}
		Log.d(TAG_LOG, "param:" + map);
		return map;
	}
	/**
	 * 拆分map
	 * @param params
	 *            需要交换的键值对
	 * @return 返回list
	 */
	public static List<String> tearOpenMap(HashMap<String, Object> params) {
		Map<String, Object> treeMap = new TreeMap<>(params);
		List<String> list = new ArrayList<>();
		for (Map.Entry entry : treeMap.entrySet()) {
			String k = entry.getKey().toString();
			if (k != null && !k.isEmpty()) {
				list.add(k);
			}
		}
		return list;
	}
	
	
//	/**
//	 * 拆分map
//	 * @param params
//	 *            需要交换的键值对
//	 * @return 返回list
//	 */
//	public static List<Map<String, Object>> tearOpenMapList(HashMap<String, Object> params) {
//		Map<String, Object> treeMap = new HashMap<String, Object>(params);
//		List<Map<String, Object>> map = new ArrayList<Map<String,Object>>();
//		for (Map.Entry entry : treeMap.entrySet()) {
//			String k = entry.getKey().toString();
//			Log.e("", msg)
//			if (k != null && !k.isEmpty()) {
//				
//				list.add(k);
//			}
//		}
//		return map;
//	}
	
	/**
	 * string的数组转换成map
	 * @param content
	 *            需要转化的参数
	 * @return
	 */
	public static HashMap<String, Object> perJson(String content) {
		HashMap<String, Object> map = JSON.parseObject(content, new TypeReference<HashMap<String, Object>>() {
		});
		return map;
	}
	
	
	/**
	 * @param params
	 * @return
	 */
	public static  HashMap<String, Object> gainMap(HashMap<String, Object> params) {
		Map<String, Object> treeMap = new TreeMap<String, Object>(params);
		HashMap<String, Object> map = new HashMap<String, Object>();
		for (Map.Entry entry : treeMap.entrySet()) {
			com.alibaba.fastjson.JSONObject v = (com.alibaba.fastjson.JSONObject) entry
					.getValue();
			if (v != null) {
				String val = v.getString("val");
				if (val != null) {
					map.put(v.getString("name"), v.getString("val"));
				}
			}
		}
		return map;
	}
	// 判断是否为空
	public static boolean isNull(HashMap<String, Object> map, Context context) {
		Map<String, Object> treeMap = new TreeMap<String, Object>(map);
		for (Map.Entry entry : treeMap.entrySet()) {
			String k = entry.getKey().toString();
			String v = entry.getValue().toString();
			if (TextUtils.isEmpty(v)) {
				Toast.makeText(context, "亲！" + k + "不能不填写", Toast.LENGTH_LONG)
						.show();
				return false;
			}
		}
		return true;
	}
	
	// 加载图片
	public static  void setImage(HashMap<String, ImageView> map) {
		Map<String, ImageView> treeMap = new TreeMap<String, ImageView>(map);
		for (Map.Entry entry : treeMap.entrySet()) {
			String k = entry.getKey().toString();
			ImageView v = (ImageView) entry.getValue();
			if (v != null && TextUtils.isEmpty(k)) {
				// ImageLoader.getInstance().displayImage(k, v,
				// MyImage.deploy(),
				// new AnimateFirstDisplayListener());
			}
		}
	}
}
