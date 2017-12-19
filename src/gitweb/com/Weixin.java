package gitweb.com;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import gitweb.com.auth.AuthUtil;
import gitweb.com.card.CardUtil;
import gitweb.com.card.Code;
import gitweb.com.httpUtil.HttpClientUtil;
import gitweb.com.menu.Button;
import gitweb.com.menu.ClickButton;
import gitweb.com.menu.Menu;
import gitweb.com.menu.ViewButton;
import gitweb.com.message.MessageUtil;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/")
public class Weixin {

	@RequestMapping("ceshi")
	@ResponseBody
	public JSONObject ceshi(){
		JSONObject obj = new JSONObject();
		try {
			System.out.println("jinglaile");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return obj;
	}
	@RequestMapping(value="WXServlet",method=RequestMethod.GET)
	public void WXServlet2(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		
		PrintWriter out = response.getWriter();
		if(CheckUtil.checkSign(signature, timestamp, nonce)){
			out.print(echostr);
		}
	}
	@RequestMapping(value="WXServlet",method=RequestMethod.POST)
	public void WXServlet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			Map<String,String> map = MessageUtil.xmlToMap(request);
			String ToUserName = map.get("ToUserName");
			String FromUserName = map.get("FromUserName");
//			String CreateTime = map.get("CreateTime");
			String MsgType = map.get("MsgType");
			String Content = map.get("Content");
//			String MsgId = map.get("MsgId");
			
			String message = null;
			if(MessageUtil.MESSAGE_TEXT.equals(MsgType)){
				if("1".equals(Content)){
					message = MessageUtil.initText(ToUserName, FromUserName, MessageUtil.firstMenu());
				}else if("2".equals(Content)){
					message = MessageUtil.initNewsMessage(ToUserName, FromUserName);
				}else if("3".equals(Content)){
					message = MessageUtil.initImageMessage(ToUserName, FromUserName);
				}else if("4".equals(Content)){
					message = MessageUtil.initMusicMessage(ToUserName, FromUserName);
				}else if("?".equals(Content)||"？".equals(Content)){
					message = MessageUtil.initText(ToUserName, FromUserName, MessageUtil.menuText());
				}else{
					message = MessageUtil.initTextByTuLing(ToUserName, FromUserName, Content);
				}
			}else if(MessageUtil.MESSAGE_EVENT.equals(MsgType)){
				String eventType = map.get("Event");
				if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){
					message = MessageUtil.initText(ToUserName, FromUserName, "欢迎来到魔法的世界！");
				}else if(MessageUtil.MESSAGE_CLICK.equals(eventType)){
					String key = map.get("EventKey");
					if("33".equals(key)){
						message = MessageUtil.initNewsMessage(ToUserName, FromUserName);
					}else if("34".equals(key)){
						message = MessageUtil.initNewsMessage(ToUserName, FromUserName);
					}else if("35".equals(key)){
						message = MessageUtil.initNewsMessage(ToUserName, FromUserName);
					}else{
						message = MessageUtil.initText(ToUserName, FromUserName, MessageUtil.menuText());
					}
				}else if(MessageUtil.MESSAGE_VIEW.equals(eventType)){
					String url = map.get("EventKey");
					message = MessageUtil.initText(ToUserName, FromUserName, url);
				}else if(MessageUtil.MESSAGE_SCANCODE_PUSH.equals(eventType)){
					String key = map.get("EventKey");
					message = MessageUtil.initText(ToUserName, FromUserName, key);
				}
			}else if(MessageUtil.MESSAGE_LOCATION.equals(MsgType)){
				String Label = map.get("Label");
				message = MessageUtil.initText(ToUserName, FromUserName, Label);
			}else if(MessageUtil.MESSAGE_IMAGE.equals(MsgType)){
				message = MessageUtil.initImageMessage(ToUserName, FromUserName);
			}else if(MessageUtil.MESSAGE_VOICE.equals(MsgType)){
				
				message = MessageUtil.initMusicMessage(ToUserName, FromUserName);
			}else if(MessageUtil.MESSAGE_VIDEO.equals(MsgType)){
				message = MessageUtil.initMusicMessage(ToUserName, FromUserName);
			}
			System.out.println(message);
			out.print(message);
		} catch (DocumentException e) {
			e.printStackTrace();
		}finally {
			out.close();
		}
	}
	
	@RequestMapping(value="LoginServlet",method={RequestMethod.GET,RequestMethod.POST})
	public void LoginServlet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String backUrl = "http://demo.magicabc.com.cn/CallBackServlet";
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+AuthUtil.APPID
			+"&redirect_uri="+URLEncoder.encode(backUrl)
			+"&response_type=code"
			+"&scope=snsapi_userinfo"
			+"&state=STATE#wechat_redirect";
		response.sendRedirect(url);
	}
	@RequestMapping(value="CallBackServlet",method={RequestMethod.GET,RequestMethod.POST})
	public void CallBackServlet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String code = request.getParameter("code");
		System.out.println(code);
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+AuthUtil.APPID
				+ "&secret="+AuthUtil.APPSECRET
				+ "&code="+code
				+ "&grant_type=authorization_code";
		JSONObject jsonObject = HttpClientUtil.doGetStr(url);
		String openid = jsonObject.getString("openid");
		String access_token = jsonObject.getString("access_token");
		String infoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token
				+ "&openid="+openid
				+ "&lang=zh_CN";
		JSONObject jsonObject2 = HttpClientUtil.doGetStr(infoUrl);
		System.out.println(jsonObject2);
		
		//1、使用微信用户信息直接登录，无需注册和绑定
//		request.setAttribute("info", jsonObject2);
//		request.getRequestDispatcher("http://www.ordos-edu.com").forward(request, response);
		response.sendRedirect("http://demo.magicabc.com.cn");
		//2、将微信与当前系统的账号进行绑定
		
	}
	
	public static String upload(String filePath) throws IOException{
		String accessToken = AuthUtil.getInstance().get_access_token().get("access_token");
		File file = new File(filePath);
		if(!file.exists()||!file.isFile()){
			throw new IOException("文件不存在");
		}
		String url = AuthUtil.UPLOADIMG_URL.replace("ACCESS_TOKEN", accessToken);
		URL urlObj = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		
		connection.setRequestProperty("Connection", "Keep-Alive");
		connection.setRequestProperty("Charset", "UTF-8");
		
		String BOUNDARY = "-------"+System.currentTimeMillis();
		connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+BOUNDARY);
		
		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition:form-data;name=\"file\";filename=\""+file.getName()+"\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");
		
		byte[] head = sb.toString().getBytes("UTF-8");
		OutputStream out = new DataOutputStream(connection.getOutputStream());
		
		out.write(head);
		
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while((bytes = in.read(bufferOut))!=-1){
			out.write(bufferOut, 0, bytes);
		}
		in.close();
		
		byte[] foot = ("\r\n--"+BOUNDARY+"--\r\n").getBytes("utf-8");
		out.write(foot);
		out.flush();
		out.close();
		
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		String result =null;
		try {
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = null;
			while((line = reader.readLine())!=null){
				buffer.append(line);
			}
			if(result==null){
				result = buffer.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(reader!=null){
				reader.close();
			}
		}
		
		JSONObject jsonObject = JSONObject.fromObject(result);
		System.out.println(jsonObject);
		String typeName = "media_id";
		String mediaId = jsonObject.getString(typeName);
		return mediaId;
	}
	/**
	 * 创建卡券
	 * @param token
	 * @param json
	 * @return
	 */
	@RequestMapping("createCard")
	@ResponseBody
	public JSONObject createCard(HttpServletRequest request, HttpServletResponse response){
//		String json = inputToString(request);
		String json = CardUtil.initCard();
		String token = AuthUtil.getInstance().get_access_token().get("access_token");
		String url = AuthUtil.CARD_CREATE.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = HttpClientUtil.doPostStr(url,json);
		return jsonObject;
	}
	/**
	 * 投放卡券
	 * @param token
	 * @param json
	 * @return
	 */
	@RequestMapping("createQrcode")
	@ResponseBody
	public JSONObject createQrcode(HttpServletRequest request, HttpServletResponse response){
		String cardId = request.getParameter("cardId");
		String code = request.getParameter("code");
		String json = CardUtil.initQrcode(cardId, code);
		String token = AuthUtil.getInstance().get_access_token().get("access_token");
		String url = AuthUtil.QRCODE_CREATE.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = HttpClientUtil.doPostStr(url,json);
		return jsonObject;
	}
	/**
	 * 查询CODE接口
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("getCode")
	@ResponseBody
	public JSONObject getCode(HttpServletRequest request, HttpServletResponse response){
		String cardId = request.getParameter("cardId");
		String code = request.getParameter("code");
		String check_consume = request.getParameter("check_consume");
		Code c = new Code();
		c.setCard_id(cardId);
		c.setCode(code);
		c.setCheck_consume(Boolean.valueOf(check_consume));
		String json = JSONObject.fromObject(c).toString();
		String token = AuthUtil.getInstance().get_access_token().get("access_token");
		String url = AuthUtil.QRCODE_CREATE.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = HttpClientUtil.doPostStr(url,json);
		return jsonObject;
	}
	/**
	 * 显示二维码
	 * @param ticket
	 * @return
	 */
	public static int viewQrcode(HttpServletRequest request, HttpServletResponse response){
		int result = 0;
		String ticket = request.getParameter("ticket");
		String url = AuthUtil.SHOW_QRCODE.replace("TICKET", ticket);
		JSONObject jsonObject = HttpClientUtil.doGetStr(url);
		System.out.println(jsonObject.toString());
		if(jsonObject!=null){
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
	public static String inputToString(HttpServletRequest request){
		StringBuffer json = new StringBuffer();
	    String line = null;
	    try {
			request.setCharacterEncoding("utf-8");
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				json.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}
	/**
	 * 自定义菜单创建
	 * @param token
	 * @param menu
	 * @return
	 */
	@RequestMapping("createMenu")
	@ResponseBody
	public JSONObject createMenu(HttpServletRequest request, HttpServletResponse response){
//		String menu = inputToString(request);
		String menu = JSONObject.fromObject(initMenu()).toString();
		String token = AuthUtil.getInstance().get_access_token().get("access_token");
		String url = AuthUtil.CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = HttpClientUtil.doPostStr(url,menu);
		return jsonObject;
	}
	/**
	 * 自定义菜单查询
	 * @param token
	 * @return
	 */
	public static JSONObject queryMenu(String token){
		String url = AuthUtil.QUERY_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = HttpClientUtil.doGetStr(url);
		return jsonObject;
	}
	/**
	 * 自定义菜单删除
	 * @param token
	 * @return
	 */
	public static JSONObject deleteMenu(String token){
		String url = AuthUtil.DELETE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = HttpClientUtil.doGetStr(url);
		return jsonObject;
	}
	/**
	 * 组装菜单
	 * @return
	 */
	public static Menu initMenu(){
		Menu menu = new Menu();
		ClickButton button11 = new ClickButton();
		button11.setName("ClickButton");
		button11.setType("click");
		button11.setKey("11");
		
		ViewButton button21 = new ViewButton();
		button21.setName("ViewButton");
		button21.setType("view");
		button21.setUrl("http://magicabc.com.cn");
		
		ClickButton button31 = new ClickButton();
		button31.setName("扫码");
		button31.setType("scancode_push");
		button31.setKey("31");
		
		ClickButton button32 = new ClickButton();
		button32.setName("地理位置");
		button32.setType("location_select");
		button32.setKey("32");
		
		ClickButton button33 = new ClickButton();
		button33.setName("联系我们");
		button33.setType("click");
		button33.setKey("33");
		
		ClickButton button34 = new ClickButton();
		button34.setName("客服帮助");
		button34.setType("click");
		button34.setKey("34");
		
		ClickButton button35 = new ClickButton();
		button35.setName("我是老师");
		button35.setType("click");
		button35.setKey("35");
		
		Button button = new Button();
		button.setName("关于我们");
		button.setSub_button(new Button[]{button31,button32,button33,button34,button35});
		
		menu.setButton(new Button[]{button11,button21,button});
		return menu;
	}
}
