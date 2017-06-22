package com.wx.seeworld.NetAddress;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class GenerateUrl {

	public static final String DEF_CHATSET = "UTF-8";
	public static final String APPKEY = "fab3d097f1eba215c4963e984bb1b905";

	public static String getHotSpotUrl(String requestFormat,
			String requestMethod) {

		String result = null;
		String url = "http://op.juhe.cn/onebox/news/words"; // 请求接口地址
		Map<String, Object> params = new HashMap<String, Object>(); // 请求参数
		params.put("key", APPKEY); // 应用APPKEY(应用详细页查询)
		params.put("dtype", requestFormat); // 返回数据的格式,xml或json，默认json
		result = AssemblyUrl(url, params, requestMethod);

		return result;
	}

	// 新闻检索
	public static String getRetrievalUrl(String requestFormat,
			String requestMethod,String newsContent) {

		String result = null;
		String url = "http://op.juhe.cn/onebox/news/query";// 请求接口地址
		Map<String, Object> params = new HashMap<String, Object>();// 请求参数
		params.put("q", newsContent);// 需要检索的关键字,请UTF8 URLENCODE
		params.put("key", APPKEY);// 应用APPKEY(应用详细页查询)
		params.put("dtype", requestFormat);// 返回数据的格式,xml或json，默认json
		result = AssemblyUrl(url, params, requestMethod);

		return result;
	}

	// 生成网址
	public static String AssemblyUrl(String strUrl, Map<String, Object> params,String method) {
		if (method == null || method.equals("GET")) {
			strUrl = strUrl + "?" + urlEncode(params);
			return strUrl;
		} else {
			return strUrl;
		}
	}

	// 将map型转为请求参数型
	private static String urlEncode(Map<String, Object> data) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> map : data.entrySet()) {
			try {
				sb.append(map.getKey())
						.append("=")
						.append(URLEncoder.encode(map.getValue() + "", "UTF-8"))
						.append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
