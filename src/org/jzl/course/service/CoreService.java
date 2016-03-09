package org.jzl.course.service;  
  
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jzl.course.message.resp.TextMessage;
import org.jzl.course.util.MessageUtil;
  
  
/** 
 * 核心服务类 
 *  
 * @author liufeng 
 * @date 2013-05-20 
 */  
public class CoreService {  
    /** 
     * 处理微信发来的请求 
     *  
     * @param request 
     * @return 
     */  
    public static String processRequest(HttpServletRequest request) {  
        String respMessage = null;  
        try {  
            // 默认返回的文本消息内容  
            String respContent = "";  
            
            // xml请求解析  
            Map<String, String> requestMap = MessageUtil.parseXml(request);  
  
            // 发送方帐号（open_id）  
            String fromUserName = requestMap.get("FromUserName");  
            // 公众帐号  
            String toUserName = requestMap.get("ToUserName");  
            // 消息类型  
            String msgType = requestMap.get("MsgType");  
  
            // 回复文本消息  
            TextMessage textMessage = new TextMessage();  
            textMessage.setToUserName(fromUserName);  
            textMessage.setFromUserName(toUserName);  
            textMessage.setCreateTime(new Date().getTime());  
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);  
            textMessage.setFuncFlag(0);  
  
            // 文本消息  
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {  
                respContent = "您发送的是文本消息！";  
//                String content = requestMap.get("Content").trim();
//                if(content.equalsIgnoreCase("help")){
//                	respContent = GameService.getGameRule();
//                }
//                //查看游戏战绩
//                else if(content.equalsIgnoreCase("score")){
//                	respContent = GameService.getUserScore(request, fromUserName);                	               	
//                }
//                //如果是4位数并且无重复
//                else if(GameUtil.verifyNumber(content) && !GameUtil.verifyRepeat(content)){
//                	respContent = GameService.process(request, fromUserName, content);
//                }else{
//                	respContent = "请输入4个不重复的数字，例如：0269";
//                }
            	 
            	
            }  
            // 图片消息  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {  
                respContent = "您发送的是图片消息！";  
            }  
            // 地理位置消息  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)){
            	respContent = "您发送的是地理位置消息！";            	
            }
            
            // 链接消息  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {  
                respContent = "您发送的是链接消息！";  
            }  
            // 音频消息  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {  
                respContent = "您发送的是音频消息！";  
            }  
            else if (msgType.equals(MessageUtil.HELP)) {  
                respContent = "请输入您需要查询的条件";  
            } 
            // 事件推送  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {  
                // 事件类型  
                String eventType = requestMap.get("Event");  
                // 订阅  
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {                  	
                    respContent = "谢谢您的关注！请选择您的分组：http://1.jzl1991.sinaapp.com/"; 
                    
                } 
//                else if(eventType.equals(MessageUtil.SCAN)){
//                	//当二维码被扫面或者搜索关注时，先让用户选择分组
//                	respContent = "谢谢您的关注！请选择您的分组：http://1.jzl1991.sinaapp.com/"; 
//                }
                // 取消订阅  
                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {  
                    //  取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息  
                }  
                // 自定义菜单点击事件  
                else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {  
                    // 事件KEY值，与创建自定义菜单时指定的KEY值对应  
                    String eventKey = requestMap.get("EventKey");  
                      
                    if (eventKey.equals("11")) {  
                        respContent = "采购公告查询被点击！http://www.shpd-procurement.gov.cn/cgzx/wzgl/news_list.jsp?news_type_id=102&opstatus=query&popedom=3";  
                    } else if (eventKey.equals("12")) {  
                        respContent = "合同下载通知被点击！http://www.shpd-procurement.gov.cn/cgzx/wzgl/zl_list.jsp?opstatus=query&popedom=3";  
                    } else if (eventKey.equals("13")) {  
                        respContent = "保证金退还通知被点击！";  
                    } else if (eventKey.equals("14")) {  
                        respContent = "其他通知被点击！";  
                    } else if (eventKey.equals("21")) {  
                        respContent = "中标公告查询被点击！http://www.shpd-procurement.gov.cn/cgzx/wzgl/news_list.jsp?news_type_id=103&opstatus=query&popedom=3";  
                    } else if (eventKey.equals("22")) {  
                        respContent = "评标会议通知被点击！";  
                    } else if (eventKey.equals("23")) {  
                        respContent = "开标会议通知被点击！";  
                    } else if (eventKey.equals("24")) {  
                        respContent = "中标结果通知被点击！";  
                    }  else if (eventKey.equals("31")) {  
                        respContent = "在线反馈被点击！";  
                    } else if (eventKey.equals("32")) {  
                        respContent = "在线调查被点击！";  
                    } else if (eventKey.equals("33")) {  
                        respContent = "在线咨询被点击！";  
                    } else if (eventKey.equals("34")) {  
                        respContent = "政策法规查询被点击！http://www.shpd-procurement.gov.cn/cgzx/wzgl/news_list.jsp?news_type_id=106&opstatus=query&popedom=3";  
                    } 
//                    if(eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)){
//                    	respContent = "";
//                    }
                }  
            }  
  
            textMessage.setContent(respContent);  
            respMessage = MessageUtil.textMessageToXml(textMessage);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return respMessage;  
    }

	
}  