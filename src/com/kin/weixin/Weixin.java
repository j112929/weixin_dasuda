package com.kin.weixin;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
public class Weixin {
    private final static Log log = LogFactory.getLog(Weixin.class);
    public final static String HOST = "http://mp.weixin.qq.com";
    public final static String LOGIN_URL = "http://mp.weixin.qq.com/cgi-bin/login?lang=zh_CN";
    public final static String INDEX_URL = "http://mp.weixin.qq.com/cgi-bin/indexpage?t=wxm-index&lang=zh_CN";
    public final static String SENDMSG_URL = "https://mp.weixin.qq.com/cgi-bin/singlesend";
    public final static String FANS_URL = "http://mp.weixin.qq.com/cgi-bin/contactmanagepage?t=wxm-friend&lang=zh_CN&pagesize=10&pageidx=0&type=0&groupid=0";
    public final static String LOGOUT_URL = "http://mp.weixin.qq.com/cgi-bin/logout?t=wxm-logout&lang=zh_CN";
    public final static String DOWNLOAD_URL = "http://mp.weixin.qq.com/cgi-bin/downloadfile?";
    public final static String VERIFY_CODE = "http://mp.weixin.qq.com/cgi-bin/verifycode?";
    public final static String POST_MSG = "https://mp.weixin.qq.com/cgi-bin/masssend?t=ajax-response";
    public final static String VIEW_HEAD_IMG = "http://mp.weixin.qq.com/cgi-bin/viewheadimg";
    public final static String GET_IMG_DATA = "http://mp.weixin.qq.com/cgi-bin/getimgdata";
    public final static String GET_REGIONS = "http://mp.weixin.qq.com/cgi-bin/getregions";
    public final static String GET_MESSAGE = "http://mp.weixin.qq.com/cgi-bin/getmessage";
    public final static String OPER_ADVANCED_FUNC = "http://mp.weixin.qq.com/cgi-bin/operadvancedfunc";
    public final static String MASSSEND_PAGE = "http://mp.weixin.qq.com/cgi-bin/masssendpage";
    public final static String FILE_MANAGE_PAGE = "http://mp.weixin.qq.com/cgi-bin/filemanagepage";
    public final static String OPERATE_APPMSG = "https://mp.weixin.qq.com/cgi-bin/operate_appmsg?token=mysys&lang=zh_CN&sub=edit&t=wxm-appmsgs-edit-new&type=10&subtype=3&ismul=1";
    public final static String FMS_TRANSPORT = "http://mp.weixin.qq.com/cgi-bin/fmstransport";
    
    public final static String CONTACT_MANAGE_PAGE = "http://mp.weixin.qq.com/cgi-bin/contactmanage";
    public final static String OPER_SELF_MENU = "http://mp.weixin.qq.com/cgi-bin/operselfmenu";
    public final static String REPLY_RULE_PAGE = "http://mp.weixin.qq.com/cgi-bin/replyrulepage";
    public final static String SINGLE_MSG_PAGE = "http://mp.weixin.qq.com/cgi-bin/singlemsgpage";
    public final static String USER_INFO_PAGE = "http://mp.weixin.qq.com/cgi-bin/userinfopage";
    public final static String DEV_APPLY = "http://mp.weixin.qq.com/cgi-bin/devapply";
    public final static String UPLOAD_MATERIAL = "https://mp.weixin.qq.com/cgi-bin/uploadmaterial?cgi=uploadmaterial&type=2&token=416919388&t=iframe-uploadfile&lang=zh_CN&formId=1";

    public final static String USER_AGENT_H = "User-Agent";
    public final static String REFERER_H = "Referer";
    public final static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.172 Safari/537.22";
    public final static String UTF_8 = "UTF-8";
    private static Map<String, String> cookiemap = new LinkedHashMap<String, String>();

    private static HttpClient client = new HttpClient();

    private static Cookie[] cookies;
    private static String cookiestr;

    private static String token;
    private static int loginErrCode;
    private static String loginErrMsg;
    private static int msgSendCode;
    private static String msgSendMsg;
//    private List<Fan> fans;

    private static String loginUser;
    private static String loginPwd;
    public static boolean isLogin = false;

    public Weixin(String user, String pwd) {
        Weixin.loginUser = user;
        Weixin.loginPwd = pwd;
    }

    public Cookie[] getCookies() {
        return cookies;
    }

    public void setCookies(Cookie[] cookies) {
        Weixin.cookies = cookies;
    }

    public String getCookiestr() {
        return cookiestr;
    }

    public void setCookiestr(String cookiestr) {
        Weixin.cookiestr = cookiestr;
    }

    public static String getToken() {
        return token;
    }

    public void setToken(String token) {
        Weixin.token = token;
    }

    public int getLoginErrCode() {
        return loginErrCode;
    }

    public void setLoginErrCode(int loginErrCode) {
        Weixin.loginErrCode = loginErrCode;
    }

    public String getLoginErrMsg() {
        return loginErrMsg;
    }

    public void setLoginErrMsg(String loginErrMsg) {
        Weixin.loginErrMsg = loginErrMsg;
    }

    public int getMsgSendCode() {
        return msgSendCode;
    }

    public void setMsgSendCode(int msgSendCode) {
        Weixin.msgSendCode = msgSendCode;
    }

    public String getMsgSendMsg() {
        return msgSendMsg;
    }

    public void setMsgSendMsg(String msgSendMsg) {
        Weixin.msgSendMsg = msgSendMsg;
    }

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        Weixin.loginUser = loginUser;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        Weixin.loginPwd = loginPwd;
    }

    public static boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean isLogin) {
        Weixin.isLogin = isLogin;
    }

    /**
     * 登录,登录失败会重复请求登录
     */
    public void login() {
        boolean bool = _login();
        while (!bool) {
            String info = "【登录失败】【错误代码：" + Weixin.loginErrMsg + "】账号【" + Weixin.loginUser + "】正在尝试重新登�?...";
            log.debug(info);
//            System.out.println(info);
            bool = _login();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                bool = _login();
            }

        }
//        System.out.println("登陆成功");
    }

    /**
     * 发布登录信息,记录cookie，登录状态，token等信息
     * 
     * @return
     */
    public static boolean _login() {
        try {
            PostMethod post = new PostMethod(LOGIN_URL);
            post.setRequestHeader("Referer", "https://mp.weixin.qq.com/");
            post.setRequestHeader(USER_AGENT_H, USER_AGENT);
            NameValuePair[] params = new NameValuePair[] { new NameValuePair("username", Weixin.loginUser),
                    new NameValuePair("pwd", DigestUtils.md5Hex(Weixin.loginPwd.getBytes())), new NameValuePair("f", "json"),
                    new NameValuePair("imagecode", "") };
            post.setQueryString(params);
            int status = client.executeMethod(post);
            if (status == HttpStatus.SC_OK) {
                String ret = post.getResponseBodyAsString();
                LoginJson retcode = JSON.parseObject(ret, LoginJson.class);

//                 System.out.println(retcode.getRet());

                if ((retcode.getBase_resp().getRet() == 302 || retcode.getBase_resp().getRet() == 0)) {
                	Weixin.cookies = client.getState().getCookies();
                    StringBuffer cookie = new StringBuffer();
                    for (Cookie c : client.getState().getCookies()) {
                        cookie.append(c.getName()).append("=").append(c.getValue()).append(";");
                        cookiemap.put(c.getName(), c.getValue());
                    }
                    Weixin.cookiestr = cookie.toString();
                    Weixin.isLogin = true;
                    Weixin.token = getToken(retcode.getRedirect_url());
                    return true;
                }
                int errCode = 0;
                Weixin.loginErrCode = errCode;
                switch (errCode) {

                case -1:
                    Weixin.loginErrMsg = "系统错误";
                    return false;
                case -2:
                    Weixin.loginErrMsg = "帐号或密码错";
                    return false;
                case -23:
                    Weixin.loginErrMsg = "密码错误";
                    return false;
                case -21:
                    Weixin.loginErrMsg = "不存在该帐户";
                    return false;
                case -5:
                    Weixin.loginErrMsg = "访问受限";
                    return false;
                case -6:
                    Weixin.loginErrMsg = "请输入验证";
                    return false;
                case -7:
                    Weixin.loginErrMsg = "此帐号已绑定私人微信号，不可用于公众平台登录";
                    return false;
                case -8:
                    Weixin.loginErrMsg = "邮箱已存";
                    return false;
                case -32:
                    Weixin.loginErrMsg = "验证码输入错误";
                    return false;
                case -200:
                    Weixin.loginErrMsg = "因频繁提交虚假资料，该帐号被拒绝登录";
                    return false;
                case -94:
                    Weixin.loginErrMsg = "请使用邮箱登录";
                    return false;
                case 10:
                    Weixin.loginErrMsg = "该公众会议号已经过期，无法再登录使用";
                    return false;
                case 65201:
                case 65202:
                    Weixin.loginErrMsg = "成功登陆，正在跳�?..";
                    return true;
                case 0:
                    Weixin.loginErrMsg = "成功登陆，正在跳�?..";
                    return true;
                default:
                    Weixin.loginErrMsg = "未知的返回值";
                    return false;
                }
            }
        } catch (Exception e) {
            String info = "【登录失败】【发生异常：" + e.getMessage() + "】";
//            System.err.println(info);
            log.debug(info);
            log.info(info);
            return false;
        }
        return false;
    }

    /**
     * 从登录成功的信息中分离出token信息
     * 
     * @param s
     * @return
     */
    private static String getToken(String s) {
        try {
            if (StringUtils.isBlank(s))
                return null;
            String[] ss = StringUtils.split(s, "?");
            String[] params = null;
            if (ss.length == 2) {
                if (!StringUtils.isBlank(ss[1]))
                    params = StringUtils.split(ss[1], "&");
            } else if (ss.length == 1) {
                if (!StringUtils.isBlank(ss[0]) && ss[0].indexOf("&") != -1)
                    params = StringUtils.split(ss[0], "&");
            } else {
                return null;
            }
            for (String param : params) {
                if (StringUtils.isBlank(param))
                    continue;
                String[] p = StringUtils.split(param, "=");
                if (null != p && p.length == 2 && StringUtils.equalsIgnoreCase(p[0], "token"))
                    return p[1];

            }
        } catch (Exception e) {
            String info = "【解析Token失败】发生异常" + e.getMessage();
//            System.err.println(info);
            log.debug(info);
            log.info(info);
            return null;
        }
        return null;
    }


    /**
     * 发送客服消息
     * @throws Exception
     */
    public void msgpost() throws Exception { 
        String at = Weixin.getAccessToken("wx84b61f062f6806ef", "cc9c53b812205bc6c7fbf6f369192482");
        if (at  != null) {                   
	        // String open_id = "ofIXZt3DJOt99o9O1Ey-Zvj3zuuM";
//	        String strJson = "{\"touser\" :\"ofIXZtyID8n9XisDnuZpTPelGkbM\",";
//	        strJson += "\"msgtype\":\"text\",";
//	        strJson += "\"text\":{";
//	        strJson += "\"content\":\"Hello World\"";
//	        strJson += "}}";
        	String strJson = "{\"touser\" :\"ofIXZtyID8n9XisDnuZpTPelGkbM\",";
	        strJson += "\"msgtype\":\"image\",";
	        strJson += "\"image\":{";
	        strJson += "\"media_id\":\"Hello World\"";
	        strJson += "}}";
	        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?&body=0&access_token="+ at;
	        Weixin.post(url, strJson);
        }
    }
    /**
     * 根据分组进行群发
     * https://mp.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=ACCESS_TOKEN
     */
    public void msgpostbygroup() throws Exception{
    	String at = Weixin.getAccessToken("wx84b61f062f6806ef", "cc9c53b812205bc6c7fbf6f369192482");
    	if (at  != null) {   
    		String strJson = "{\"filter\" :{\"group_id\":\"102\"},";
            strJson += "\"text\":{\"content\":\"吉卓林\"},";
//            strJson += "\"text\":{";
            strJson += "\"msgtype\":\"text\"";
            strJson += "}";
            String url = "https://mp.weixin.qq.com/cgi-bin/message/mass/sendall?access_token="+ at;
            Weixin.post(url, strJson);    		
    	}
    }
    /**
     * 根据openid列表进行群发
     * https://mp.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=ACCESS_TOKEN
     */
    public void msgpostbyopenid() throws Exception{
    	String at = Weixin.getAccessToken("wx84b61f062f6806ef", "cc9c53b812205bc6c7fbf6f369192482");
    	if (at  != null) {   
    		String strJson = "{\"touser\" :[\"ofIXZt3DJOt99o9O1Ey-Zvj3zuuM\",\"ofIXZtyID8n9XisDnuZpTPelGkbM\"],";
            strJson += "\"msgtype\":\"text\",";
            strJson += "\"text\":{\"content\":\"hello from boxer.\"},";
            strJson += "}";
            String url = "https://mp.weixin.qq.com/cgi-bin/message/mass/sendall?access_token="+ at;
            Weixin.post(url, strJson);    		
    	}
    }
    
    public static String post1(String url, String json) {
    	String jsonObject = null;
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
                jsonObject = EntityUtils.toString(entity, "utf-8");
                System.out.println(EntityUtils.toString(entity, "utf-8"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
		return jsonObject;
    }
    public static void post(String url, String json)
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
    
   
 // 获取access_token的接口地址（GET） 限200（次/天）  
    public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx84b61f062f6806ef&secret=cc9c53b812205bc6c7fbf6f369192482";  
      
    /** 
     * 获取access_token 
     *  
     * @param appid 凭证 
     * @param appsecret 密钥 
     * @return 
     * @throws IOException 
     * @throws HttpException 
     */  
    public static String getAccessToken(String appid, String appsecret) throws HttpException, IOException {  
        String a = null;  
//        PostMethod get = new PostMethod(access_token_url);
        GetMethod get = new GetMethod(access_token_url);
//        post.setRequestHeader("Referer", access_token_url);
//        post.setRequestHeader(USER_AGENT_H, USER_AGENT);        
        int status = client.executeMethod(get);
//        System.out.println(status);//200=SC_OK
        if (status == HttpStatus.SC_OK) {
            String msg = get.getResponseBodyAsString();
	    	JSONObject jsonObject = JSONObject.fromObject(msg);                
	    	a=jsonObject.getString("access_token");
	    	System.out.println(a);	//token	    	
        }        
        return a;  
    }
  public static String getTicket() throws Exception{
	  String t = null;
	  String at = Weixin.getAccessToken("wx84b61f062f6806ef", "cc9c53b812205bc6c7fbf6f369192482");	  
	  if (at  != null) {   
  		  String strJson = "{\"action_name\" :\"QR_LIMIT_SCENE\",";
          strJson += "\"action_info\":{\"scene\":{";
          strJson += "\"scene_id\":\"123\"}}}";
          String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+ at;         
          DefaultHttpClient client = new DefaultHttpClient();
          HttpPost post = new HttpPost(url);
          StringEntity s = new StringEntity(strJson);
          s.setContentEncoding("UTF-8");
          s.setContentType("application/json");
          post.setEntity(s);   
          HttpResponse res = client.execute(post);
          if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
              HttpEntity entity = res.getEntity();
              String t1 = EntityUtils.toString(entity, "utf-8");
              JSONObject jsonObject = JSONObject.fromObject(t1);     
              System.out.println(t1);//t1是个json
  	    	  t=jsonObject.getString("ticket");                  
          }  
	  }
	  System.out.println(t);//ticket
	  return t;
  }
  
    /**
     * 说明:<br>
     * new Weixin()对象，先登录再取粉丝数和者发消息。<br>
     * 发消息需要设置post参数中的content<br>
     * 内容中的超链接可以直接发送不用使用<a>标签 经过我（trprebel）修改之后，此份代码可在2013年11月之后使用
     * 我只做了获取粉丝列表和发送消息，其他部分未做 理论上可以获取到粉丝的地址，签名等一切你登陆可以得到的信息
     * 另外可能需要你在本机先登陆过至少一次微信公众平台获取SSL证书 此份代码作者较多，函数前面都有作者名，我只修改了登陆，获取粉丝列表和发送消息
     * 其他代码可能已经不能用了但我并没有删除，方便大家扩展，我做的也比较粗糙，没整理
     * 原文地址：http://50vip.com/blog.php?i=268 使用到的库： commons-codec-1.3.jar
     * commons-httpclient-3.1.jar commons-lang.jar commons-logging-1.0.4.jar
     * fastjson-1.1.15.jar gson-2.2.4.jar httpclient-4.1.3.jar
     * httpcore-4.1.4.jar jsoup-1.5.2.jar 环境：JDK1.6
     * 
     * @param args
     */
    public static void main(String[] args) throws Exception{

//        String LOGIN_USER = "knight.ding@gmail.com"; // 此为上一任作者的用户名和密码，截止到我最后用发现还能用
//        String LOGIN_PWD = "AAbb1122";
    	String LOGIN_USER = "j1129290218@qq.com";
    	String LOGIN_PWD = "1991j12";
        Weixin wx = new Weixin(LOGIN_USER, LOGIN_PWD);
//        wx.login();
//        wx.getCookiestr();        
        wx.msgpost();
//        wx.msgpostbygroup();
//        wx.msgpostbyopenid();
//        Weixin.getTicket();
    }
}
