package org.liufeng.course.util;

import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.liufeng.course.message.req.ImageMessage;
import org.liufeng.course.message.req.VoiceMessage;
import org.liufeng.course.message.resp.Article;
import org.liufeng.course.message.resp.MusicMessage;
import org.liufeng.course.message.resp.NewsMessage;
import org.liufeng.course.message.resp.TextMessage;
import org.liufeng.weixin.util.WeixinUtil;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
/** 
 * 消息工具类 
 *  
 * @author liufeng 
 * @date 2013-05-19 
 */
public class MessageUtil {
	private final static Log log = LogFactory.getLog(WeixinUtil.class);
	public static final String RESP_MESSAGE_TYPE_TEXT = "text";
	public static final String RESP_MESSAGE_TYPE_MUSIC = "music";
	public static final String RESP_MESSAGE_TYPE_NEWS = "news";
	public static final String REQ_MESSAGE_TYPE_TEXT = "text";
	public static final String REQ_MESSAGE_TYPE_IMAGE = "image";
	public static final String REQ_MESSAGE_TYPE_LOCATION = "location";
	public static final String REQ_MESSAGE_TYPE_LINK = "link";
	public static final String REQ_MESSAGE_TYPE_VOICE = "voice";
	public static final String REQ_MESSAGE_TYPE_EVENT = "event";
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";
	public static final String EVENT_TYPE_CLICK = "click";
	public static final String SCAN = "scan";
	public static final String SCENE_VALUE = "scene_value";
	public static final String TICKET = "ticket";
	public static final String HELP = "help";
	public static final String SCORE = "score";
	

	/** 
     * 解析微信发来的请求（XML） 
     *  
     * @param request 
     * @return 
     * @throws Exception 
     */
	
    public static Map<String,String> parseXml(HttpServletRequest request) throws Exception{
 	   // 将解析结果存储在HashMap中
 	   Map<String,String> map = new HashMap<String,String>();

        // 从request中取得输入流   
        InputStream inputStream = request.getInputStream();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素   
        Element root = document.getRootElement();
        // 得到根元素的所有子节点   
        List<Element> elementList = root.elements();
    
        // 遍历所有子节点
        for(Element e : elementList){
     	   map.put(e.getName(),e.getText());
        }
        // 释放资源
        inputStream.close();
        inputStream = null;
    
        return map;
    }

    /** 
     * 文本消息对象转换成xml 
     *  
     * @param textMessage 文本消息对象 
     * @return xml 
     */
    public static String textMessageToXml(TextMessage textMessage){
 	   xstream.alias("xml",textMessage.getClass());
        return xstream.toXML(textMessage);
    }

    /** 
     * 音乐消息对象转换成xml 
     *  
     * @param musicMessage 音乐消息对象 
     * @return xml 
     */
    public static String musicMessageToXml(MusicMessage musicMessage){
 	   xstream.alias("xml", musicMessage.getClass());
        return xstream.toXML(musicMessage);
    }

    /** 
     * 图文消息对象转换成xml 
     *  
     * @param newsMessage 图文消息对象 
     * @return xml 
     */
    public static String newsMessageToXml(NewsMessage newsMessage){
        xstream.alias("xml",newsMessage.getClass());
        //Article a = new Article();
    	xstream.alias("item",new Article().getClass());
        return xstream.toXML(newsMessage);
    }
	
	/**
	* 扩展xstream，使其支持CDATA块
	*
	* @date 2013-05-19
	*/
   private static XStream xstream = new XStream(new XppDriver() {
       public HierarchicalStreamWriter createWriter(Writer out) {
    	   return new PrettyPrintWriter(out){

           // 对所有xml节点的转换都增加CDATA标记

    		   boolean cdata = true;
    		   
    		   public void startNode(String name,Class clazz){
    			   super.startNode(name,clazz);
    	       }

        	   protected void writeText(QuickWriter writer,String text){
    	   			if(cdata){
    	   				writer.write("<![CDATA[");
    	   				writer.write(text);
    	   				writer.write("]]>");
    	   			}else{
       				 writer.write(text);
    	   			}
           	   }
           };
   	  }
   });
   /**
    * 文本消息转换成xml
    * @param textMessage
    * @return xml
    */
   public static String messageToXml(TextMessage textMessage){
	   xstream.alias("xml", textMessage.getClass());
	   return xstream.toXML(textMessage);
   }
   /**
    * 图片消息转换成xml
    * @param imageMessage
    * @return xml
    */
   public static String messageToXml(ImageMessage imageMessage){
	   xstream.alias("xml", imageMessage.getClass());
	   return xstream.toXML(imageMessage);
   }
   /**
    * 语音消息转换成xml
    * @param voiceMessage
    * @return xml
    */
   public static String messageToXml(VoiceMessage voiceMessage){
	   xstream.alias("xml", voiceMessage.getClass());
	   return xstream.toXML(voiceMessage);
   }
   /**
    * 音乐消息转换成xml
    * @param musicMessage
    * @return xml
    */
   public static String messageToXml(MusicMessage musicMessage){
	   xstream.alias("xml", musicMessage.getClass());
	   return xstream.toXML(musicMessage);
   }
   /**
    * 图文消息转换成xml
    * @param newsMessage
    * @return xml
    */
   public static String messageToXml(NewsMessage newsMessage){
	   xstream.alias("xml", newsMessage.getClass());
	   xstream.alias("item",new Article().getClass());
	   return xstream.toXML(newsMessage);
   }
	/** 
	 * 判断是否是QQ表情 
	 *  
	 * @param content 
	 * @return 
	 */  
	public static boolean isQqFace(String content) {  
	    boolean result = false;  
	  
	    // 判断QQ表情的正则表达式  
	    String qqfaceRegex = "/::\\)|/::~|/::B|/::\\||/:8-\\)|/::<|/::$|/::X|/::Z|/::'\\(|/::-\\||/::@|/::P|/::D|/::O|/::\\(|/::\\+|/:--b|/::Q|/::T|/:,@P|/:,@-D|/::d|/:,@o|/::g|/:\\|-\\)|/::!|/::L|/::>|/::,@|/:,@f|/::-S|/:\\?|/:,@x|/:,@@|/::8|/:,@!|/:!!!|/:xx|/:bye|/:wipe|/:dig|/:handclap|/:&-\\(|/:B-\\)|/:<@|/:@>|/::-O|/:>-\\||/:P-\\(|/::'\\||/:X-\\)|/::\\*|/:@x|/:8\\*|/:pd|/:<W>|/:beer|/:basketb|/:oo|/:coffee|/:eat|/:pig|/:rose|/:fade|/:showlove|/:heart|/:break|/:cake|/:li|/:bome|/:kn|/:footb|/:ladybug|/:shit|/:moon|/:sun|/:gift|/:hug|/:strong|/:weak|/:share|/:v|/:@\\)|/:jj|/:@@|/:bad|/:lvu|/:no|/:ok|/:love|/:<L>|/:jump|/:shake|/:<O>|/:circle|/:kotow|/:turn|/:skip|/:oY|/:#-0|/:hiphot|/:kiss|/:<&|/:&>";  
	    Pattern p = Pattern.compile(qqfaceRegex);  
	    Matcher m = p.matcher(content);  
	    if (m.matches()) {  
	        result = true;  
	    }  
	    return result;  
	} 
	
}
