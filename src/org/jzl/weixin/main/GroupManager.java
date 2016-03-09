package org.jzl.weixin.main;

import java.util.HashMap;
import java.util.Map;

import org.jzl.weixin.pojo.AccessToken;
import org.jzl.weixin.pojo.Group;
import org.jzl.weixin.util.WeixinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 分组管理器类
 * @author Administrator
 * @date 2014-04-14
 */
public class GroupManager {
	private static Logger log = LoggerFactory.getLogger(GroupManager.class);  
	  
    public static void main(String[] args) {  
        // 第三方用户唯一凭证  
        String appId = "wx84b61f062f6806ef";  
        // 第三方用户唯一凭证密钥  
        String appSecret = "cc9c53b812205bc6c7fbf6f369192482";  
  
        // 调用接口获取access_token  
        AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);  
  
        if (at != null) {  
            // 调用接口创建分组 
            Group g = WeixinUtil.createGroup(at.getToken(),getGroup());                
        }          
    }

	private static Group getGroup() {
		Map<Integer, String> group = new HashMap<Integer,String>();
		group.put(0,"采购人用户");
		group.put(1,"监管部门用户");
		group.put(2,"供应商用户");
		group.put(3,"专家用户");
		return (Group) group;
	}  
}
