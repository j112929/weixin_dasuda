package org.jzl.weixin.util;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.jzl.weixin.pojo.AccessToken;
import org.jzl.weixin.pojo.Group;

import com.kin.weixin.Constants;
 

 
public class TestUnit
{
 
    @Test
    public void Test1()
    {
 
    	// 第三方用户唯一凭证  
        String appId = "wx84b61f062f6806ef";  
        // 第三方用户唯一凭证密钥  
        String appSecret = "cc9c53b812205bc6c7fbf6f369192482";  
  
        // 调用接口获取access_token  
        AccessToken token = WeixinUtil.getAccessToken(appId, appSecret);  
  
        if (token != null) {                   
        // String open_id = "ofIXZt3DJOt99o9O1Ey-Zvj3zuuM";
        JSONObject obj = JSONObject.fromObject(token);
        String token1 = obj.getString("token");
        String strJson = "{\"touser\" :\"ofIXZt3DJOt99o9O1Ey-Zvj3zuuM\",";
        strJson += "\"msgtype\":\"text\",";
        strJson += "\"text\":{";
        strJson += "\"content\":\"Hello World\"";
        strJson += "}}";
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?&body=0&access_token=" + token1;
 
        System.out.println(url);
        this.post(url, strJson);
        }
    }
 
    public void post(String url, String json)
    {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        try
        {
            StringEntity s = new StringEntity(json);
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");
            post.setEntity(s);
 
            HttpResponse res = client.execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                HttpEntity entity = res.getEntity();
                System.out.println(EntityUtils.toString(entity, "utf-8"));
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
 
}