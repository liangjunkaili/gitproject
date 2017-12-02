package gitweb.com.message;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;

import gitweb.com.httpUtil.HttpClientUtil;
import net.sf.json.JSONObject;


public class MessageUtil {

	public static final String MESSAGE_TEXT = "text";
	public static final String MESSAGE_NEWS = "news";
	public static final String MESSAGE_IMAGE = "image";
	public static final String MESSAGE_VOICE = "voice";
	public static final String MESSAGE_VIDEO = "video";
	public static final String MESSAGE_LINK = "link";
	public static final String MESSAGE_LOCATION = "location";
	public static final String MESSAGE_EVENT = "event";
	public static final String MESSAGE_SUBSCRIBE = "subscribe";
	public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
	public static final String MESSAGE_CLICK = "CLICK";
	public static final String MESSAGE_VIEW = "VIEW";
	public static final String MESSAGE_MUSIC = "music";
	public static final String MESSAGE_SCANCODE_PUSH = "scancode_push";
	public static final String MESSAGE_LOCATION_SELECT = "location_select";
	public static Map<String,String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException{
		
		Map<String,String> map = new HashMap<String,String>();
		SAXReader reader = new SAXReader();
		
		InputStream ins = request.getInputStream();
		Document doc = reader.read(ins);
		Element root = doc.getRootElement();
		
		List<Element> list = root.elements();
		for(Element e: list){
			map.put(e.getName(), e.getText());
		}
		ins.close();
		return map;
	}
	
	public static String textMessageToXml(TextMessage message){
		XStream xsStream = new XStream();
		xsStream.alias("xml", message.getClass());
		return xsStream.toXML(message);
	}
	
	public static String menuText(){
		StringBuffer sb = new StringBuffer();
		sb.append("欢迎您的关注，请按照菜单提示进行操作:\n\n");
		sb.append("1、一句话\n");
		sb.append("2、一个图文\n\n");
		sb.append("3、一个图片\n\n");
		sb.append("4、一首歌\n\n");
		sb.append("回复?调出此菜单。");
		return sb.toString();
	}
	
	public static String initText(String ToUserName,String FromUserName,String Content){
		TextMessage text = new TextMessage();
		text.setFromUserName(ToUserName);
		text.setToUserName(FromUserName);
		text.setMsgType(MESSAGE_TEXT);
		text.setCreateTime(new Date().toLocaleString());
		text.setContent(Content);
		return textMessageToXml(text);
	}
	
	public static String initTextByTuLing(String ToUserName,String FromUserName,String Content){
		String url = "http://www.tuling123.com/openapi/api?key=50feff4df7d54515b88edb100a484d42&info="+Content;
		JSONObject jsonObject = HttpClientUtil.doGetStr(url);
		String res = jsonObject.getString("text");
		TextMessage text = new TextMessage();
		text.setFromUserName(ToUserName);
		text.setToUserName(FromUserName);
		text.setMsgType(MESSAGE_TEXT);
		text.setCreateTime(new Date().toLocaleString());
		text.setContent(res);
		return textMessageToXml(text);
	}
	public static String firstMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("I am liangjun!");
		return sb.toString();
	}
	
	public static String secondMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("I am liangwei!");
		return sb.toString();
	}
	
	public static String newsMessageToXml(NewsMessage message){
		XStream xsStream = new XStream();
		xsStream.alias("xml", message.getClass());
		xsStream.alias("item", new News().getClass());
		return xsStream.toXML(message);
	}
	
	public static String initNewsMessage(String ToUserName,String FromUserName){
		String message = null;
		List<News> newsList = new ArrayList<News>();
		NewsMessage newsMessage = new NewsMessage();
		News news = new News();
		news.setDescription("我是老师，我想加入名师课堂");
		news.setTitle("我是老师，我想加入名师课堂");
		news.setPicUrl("http://qinmi-10040507.cos.myqcloud.com/book/%E6%89%8B%E6%9C%BA%E7%BB%98%E6%9C%AC%E5%B0%81%E9%9D%A21.png");
		news.setUrl("www.ordos-edu.com");
		News news1 = new News();
		news1.setDescription("我是老师，我想加入名师课堂");
		news1.setTitle("我是老师，我想加入名师课堂");
		news1.setPicUrl("http://qinmi-10040507.cos.myqcloud.com/book/%E6%89%8B%E6%9C%BA%E7%BB%98%E6%9C%AC%E5%B0%81%E9%9D%A21.png");
		news1.setUrl("www.ordos-edu.com");
		News news2 = new News();
		news2.setDescription("我是老师，我想加入名师课堂");
		news2.setTitle("我是老师，我想加入名师课堂");
		news2.setPicUrl("http://qinmi-10040507.cos.myqcloud.com/book/%E6%89%8B%E6%9C%BA%E7%BB%98%E6%9C%AC%E5%B0%81%E9%9D%A21.png");
		news2.setUrl("www.ordos-edu.com");
		
		newsList.add(news);
		newsList.add(news1);
		newsList.add(news2);
		newsMessage.setToUserName(FromUserName);
		newsMessage.setFromUserName(ToUserName);
		newsMessage.setCreateTime(new Date().toLocaleString());
		newsMessage.setMsgType(MESSAGE_NEWS);
		newsMessage.setArticleCount(newsList.size());
		newsMessage.setArticles(newsList);
		message = newsMessageToXml(newsMessage);
		return message;
	}
	
	public static String initImageMessage(String ToUserName,String FromUserName){
		String message = null;
		ImageMessage imageMessage = new ImageMessage();
		imageMessage.setFromUserName(ToUserName);
		imageMessage.setToUserName(FromUserName);
		imageMessage.setMsgType(MESSAGE_IMAGE);
		imageMessage.setCreateTime(new Date().toLocaleString());
		imageMessage.setMediaId("oPedUFdIP7fLn057W41QOJbpX7BDxDGw6R-ZZtKXPYRJdASbXLcZx2E0LO6J18jU");;
		message = imageMessageToXml(imageMessage);
		return message;
	}
	
	public static String imageMessageToXml(ImageMessage message){
		XStream xsStream = new XStream();
		xsStream.alias("xml", message.getClass());
		return xsStream.toXML(message);
	}
	
	public static String initMusicMessage(String ToUserName,String FromUserName){
		String message = null;
		
		MusicMessage musicMessage = new MusicMessage();
		musicMessage.setFromUserName(ToUserName);
		musicMessage.setToUserName(FromUserName);
		musicMessage.setMsgType(MESSAGE_MUSIC);
		musicMessage.setCreateTime(new Date().toLocaleString());
		musicMessage.setThumbMediaId("A1QUwaT8vJ3pWFpGRMfx5jFIztzoKghw04WJ5f0HN2j80oBArBj7Hz2GcuxVyYAu");
		musicMessage.setTitle("see you again");
		musicMessage.setDescription("this is a test music");
		musicMessage.setHQMusicUrl("http://qinmi-10040507.cos.myqcloud.com/%28BB-pad%29_002_bgm.m4a");
		musicMessage.setMusicUrl("http://qinmi-10040507.cos.myqcloud.com/%28BB-pad%29_002_bgm.m4a");
		message = musicMessageToXml(musicMessage);
		return message;
	}
	
	public static String musicMessageToXml(MusicMessage message){
		XStream xsStream = new XStream();
		xsStream.alias("xml", message.getClass());
		return xsStream.toXML(message);
	}
}
