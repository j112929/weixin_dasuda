package org.jzl.weixin.util;  
  
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.jzl.weixin.pojo.AccessToken;
import org.jzl.weixin.pojo.Group;
import org.jzl.weixin.pojo.Menu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
  
/** 
 * 公众平台通用接口工具类 
 *  
 * @author liuyq 
 * @date 2013-08-09 
 */  
public class WeixinUtil {  
    private static Logger log = LoggerFactory.getLogger(WeixinUtil.class);  
  
    /** 
     * 发起https请求并获取结果 
     *  
     * @param requestUrl 请求地址 
     * @param requestMethod 请求方式（GET、POST） 
     * @param outputStr 提交的数据 
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值) 
     */  
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {  
        JSONObject jsonObject = null;  
        
        try {  
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化  
            TrustManager[] tm = { new MyX509TrustManager() };  
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");  
            sslContext.init(null, tm, new java.security.SecureRandom());  
            // 从上述SSLContext对象中得到SSLSocketFactory对象  
            SSLSocketFactory ssf = sslContext.getSocketFactory();  
  
            URL url = new URL(requestUrl);  
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();  
            httpUrlConn.setSSLSocketFactory(ssf);  
  
            httpUrlConn.setDoOutput(true);  
            httpUrlConn.setDoInput(true);  
            httpUrlConn.setUseCaches(false);  
            // 设置请求方式（GET/POST）  
            httpUrlConn.setRequestMethod(requestMethod);  
  
            if ("GET".equalsIgnoreCase(requestMethod))  
                httpUrlConn.connect();  
  
            // 当有数据需要提交时  
            if (null != outputStr) {  
                OutputStream outputStream = httpUrlConn.getOutputStream();  
                // 注意编码格式，防止中文乱码  
                outputStream.write(outputStr.getBytes("UTF-8"));  
                outputStream.close();  
            }  
  
            // 将返回的输入流转换成字符串  
            InputStream inputStream = httpUrlConn.getInputStream();  
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
  
            String str = null;  
            StringBuffer buffer = new StringBuffer();  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }  
            bufferedReader.close();  
            inputStreamReader.close();  
            // 释放资源  
            inputStream.close();  
            inputStream = null;  
            httpUrlConn.disconnect();  
            jsonObject = JSONObject.fromObject(buffer.toString());  
        } catch (ConnectException ce) {  
            log.error("Constants server connection timed out.");  
        } catch (Exception e) {  
            log.error("https request error:{}", e);  
        }  
        return jsonObject;  
    }  
 // 获取access_token的接口地址（GET） 限200（次/天）  
    public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx84b61f062f6806ef&appsecret=cc9c53b812205bc6c7fbf6f369192482";  
      
    /** 
     * 获取access_token 
     *  
     * @param appid 凭证 
     * @param appsecret 密钥 
     * @return 
     */  
    public static AccessToken getAccessToken(String appid, String appsecret) {  
        AccessToken accessToken = null;  
      
        String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);  
        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);  
        // 如果请求成功  
        if (null != jsonObject) {  
            try {  
                accessToken = new AccessToken();  
                accessToken.setToken(jsonObject.getString("access_token"));  
                accessToken.setExpiresIn(jsonObject.getInt("expires_in"));  
            } catch (JSONException e) {  
                accessToken = null;  
                // 获取token失败  
                log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));  
            }  
        }  
        return accessToken;  
    }

 // 菜单创建（POST） 限100（次/天）  
    public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";  
      
    /** 
     * 创建菜单 
     *  
     * @param menu 菜单实例 
     * @param accessToken 有效的access_token 
     * @return 0表示成功，其他值表示失败 
     */  
    public static int createMenu(Menu menu, String accessToken) {  
        int result = 0;  
      
        // 拼装创建菜单的url  
        String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);  
        // 将菜单对象转换成json字符串  
        String jsonMenu = JSONObject.fromObject(menu).toString();  
        // 调用接口创建菜单  
        JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);  
      
        if (null != jsonObject) {  
            if (0 != jsonObject.getInt("errcode")) {  
                result = jsonObject.getInt("errcode");  
                log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));  
            }  
        }  
      
        return result;  
    }    
    //分组创建:一个公众账号，最多支持创建500个分组。
    public static String group_create_url = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token=ACCESS_TOKEN";  
    /**
     * 创建分组
     * @param accessToken 接口访问凭证
     * @param groupName 分组名称
     * @return
     */
    public static Group createGroup(String accessToken,Group groupName) {
    	
    	Group g = null;
    	// 拼装创建分组的url  
        String url = group_create_url.replace("ACCESS_TOKEN", accessToken); 
        String jsonData = "{\"group\":{\"name\":\"%s\"}}";
        // 将菜单对象转换成json字符串  
        String jsonGroup = String.format(jsonData,groupName);  
        // 调用接口创建菜单  
        JSONObject jsonObject = httpRequest(url, "POST", jsonGroup);  
      
        if (null != jsonObject) { 
        	try{
        		g = new Group();
        		g.setId(jsonObject.getJSONObject("group").getInt("id"));
        		g.setName(jsonObject.getJSONObject("group").getString("name"));
        	}catch(JSONException e){
        		g = null;
        		int errorCode = jsonObject.getInt("errcode");
        		String errorMsg = jsonObject.getString("errmsg");
        		log.error("创建分组失败 errcode:{} errmsg:{}",errorCode,errorMsg);
        	}
//            if (0 != jsonObject.getInt("errcode")) {  
//                result = jsonObject.getInt("errcode");  
//                log.error("创建分组失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));  
//            }  
        }  
      
        return g;    	
    }
    
    public static String group_remove_url = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=ACCESS_TOKEN";  
    /**
     * 修改用户分组
     * @param accessToken 接口访问凭证
     * @param groupId 分组 id
     * @param groupName 修改后的分组名
     * @return true | false
     */
    public static boolean updateGroup(String accessToken,int groupId,String groupName) {
    	boolean result = false;
    	// 拼装创建分组的url  
        String url = group_remove_url.replace("ACCESS_TOKEN", accessToken);  
        String jsonData = "{\"group\":{\"id\":%d,\"name\":\"%s\"}}";
        // 将菜单对象转换成json字符串  
        String jsonGroup = String.format(jsonData,groupId,groupName);  
        // 调用接口创建菜单  
        JSONObject jsonObject = httpRequest(url, "POST", jsonGroup);  
      
        if (null != jsonObject) {  
        	int errorCode = jsonObject.getInt("errcode");
    		String errorMsg = jsonObject.getString("errmsg");
            if (0 == errorCode) {  
            	result = true;
                
                log.error("修改分组成功 errcode:{} errmsg:{}", errorCode, errorMsg);  
            }  else {
            	log.error("修改分组失败 errcode:{} errmsg:{}", errorCode, errorMsg); 
            }
        }  
      
        return result;
	}
    
    public static String group_move_url = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=ACCESS_TOKEN";  
    /**
     * 移动用户分组
     * @param accessToken 接口访问凭证
     * @param groupId 分组 id
     * @param openId 用户标识
     * @return true | false
     */
    public static boolean moveGroup(String accessToken,String openId,int groupId) {
    	boolean result = false;
    	// 拼装创建分组的url  
        String url = group_move_url.replace("ACCESS_TOKEN", accessToken);  
        String jsonData = "{\"openid\":\"%s\",\"to_groupid\":%d}";
        // 将菜单对象转换成json字符串  
        String jsonGroup = String.format(jsonData,openId,groupId) ;  
        // 调用接口创建菜单  
        JSONObject jsonObject = httpRequest(url, "POST", jsonGroup);  
      
        if (null != jsonObject) {  
        	int errorCode = jsonObject.getInt("errcode");
    		String errorMsg = jsonObject.getString("errmsg");
            if (0 == errorCode) {  
            	result = true;
                
                log.error("移动分组成功 errcode:{} errmsg:{}", errorCode, errorMsg);  
            }  else {
            	log.error("移动分组失败 errcode:{} errmsg:{}", errorCode, errorMsg); 
            }
        }  
      
        return result;
	}
	public static String urlEncodeUTF8(String source) {
		String result =  source;
		try {
			result = java.net.URLEncoder.encode(source,"utf-8");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
	/**
     * 换取二维码
     */
    public static String getQRCode(String ticket,String savePath){
    	String filePath = null;
    	String requestUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
    	requestUrl = requestUrl.replace("TICKET", WeixinUtil.urlEncodeUTF8(ticket));
    	try {    		
			URL  url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setRequestMethod("GET");
//			if(!savePath.endsWith("/")){
//				savePath +="/";				
//			}
			filePath = savePath+"  "+ticket+".jpg";
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
			FileOutputStream fos = new FileOutputStream(new File(filePath));
			byte[] buf = new byte[8096];
			int size = 0;
			while((size = bis.read(buf)) != -1)
				fos.write(buf,0,size);
			fos.close();
			bis.close();
			conn.disconnect();
			log.info("根据ticket换取二维码成功，filePath="+filePath);
		} catch (Exception e) {
			// TODO: handle exception
			filePath = null;
			log.error("二维码换取失败！",e);
		}
		return filePath;
    }
    public static void main(String[] args) {
//		String ticket = "gQGv7zoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL0drd2g0SHJsMWw2dnVvaDBqMkM0AAIENE5GUwMEAAAAAA==";//永久ticket
    	String ticket = "gQFS8DoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL1VFeXZDSnZsU0Y0eF84Q01BV0s0AAIEY2GaUwMECAcAAA==";//临时ticket
		String savePath = System.getProperty("WebRoot");//获取项目的相对路径
		getQRCode(ticket, savePath);
	}
}  