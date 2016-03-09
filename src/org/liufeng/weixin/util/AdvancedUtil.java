package org.liufeng.weixin.util;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.liufeng.weixin.pojo.WeixinQRCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kin.weixin.Weixin;

public class AdvancedUtil {
	private static Logger log = LoggerFactory.getLogger(Weixin.class);
	/**
     * 创建永久带参二维码
     * 
     */
    public static String createPermanentQRCode(String accessToken,int sceneId){
    	String ticket = null;
    	String requestUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
    	requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
    	String jsonMsg = "{\"action_name\" :\"QR_LIMIT_SCENE\",\"action_info\":{\"scene\":{\"scene_id\":\"123\"}}}";
    	try {
			JSONObject jsonObject = AdvancedUtil.post2(requestUrl, jsonMsg);
			ticket = jsonObject.getString("ticket");
			log.info("创建永久二维码成功！",ticket);
			System.out.println(ticket);
		} catch (Exception e) {
			// TODO: handle exception
//				int errorCode = jsonObject.getInt("errcode");
//	    		String errorMsg = jsonObject.getString("errmsg");
				log.error("创建永久二维码失败！",e);
//				System.out.println("创建永久二维码失败！");
		}
        return ticket; 
    	
    }
    /**
     * 创建临时带参数二维码
     * @param args
     * @throws Exception
     */
    public static WeixinQRCode createTemporaryQRCode(String accessToken,int expireSeconds,int sceneId){
    	WeixinQRCode weixinQRCode = null;
    	String requestUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
    	requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
    	
    	String jsonMsg = "{\"expire_seconds\": 1800, \"action_name\": \"QR_SCENE\"," +
    			"\"action_info\": {\"scene\": {\"scene_id\": 123}}}";
    	System.out.println(jsonMsg);
    	try {
			JSONObject jsonObject =   AdvancedUtil.post2(requestUrl, jsonMsg);
			System.out.println(jsonObject);
			weixinQRCode = new WeixinQRCode();
			weixinQRCode.setTicket(jsonObject.getString("ticket"));
			weixinQRCode.setExpireSeconds(jsonObject.getString("expire_seconds"));			
			log.info("创建临时二维码成功！",weixinQRCode);	//写死的二维码			
		} catch (Exception e) {
			weixinQRCode = null;
			// TODO: handle exception
//				int errorCode = jsonObject.getInt("errcode");
//	    		String errorMsg = jsonObject.getString("errmsg");
				log.error("创建临时二维码失败！",e);
//				System.out.println("创建永久二维码失败！");
		}
		return weixinQRCode;    	
    }
    public static  JSONObject post2(String url, String json) {
    	JSONObject jsonObject = null;
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        try{
            StringEntity s = new StringEntity(json);
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");
            post.setEntity(s);
 
            HttpResponse res = client.execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                HttpEntity entity = res.getEntity();
                jsonObject = JSON.parseObject(EntityUtils.toString(entity, "utf-8"));
//                System.out.println(EntityUtils.toString(entity, "utf-8"));
                System.out.println(jsonObject);//json对象
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
		return jsonObject;
    }
    public static void main(String[] args) throws Exception {
		String accessToken = Weixin.getAccessToken("wx84b61f062f6806ef", "cc9c53b812205bc6c7fbf6f369192482");	  
//		String ticket = createPermanentQRCode(accessToken, 617);
//		log.info(ticket);		
		WeixinQRCode weixinQRCode = createTemporaryQRCode(accessToken, 900, 111111);
		System.out.println(weixinQRCode.getTicket());
		System.out.println(weixinQRCode.getExpireSeconds());

    }
}
