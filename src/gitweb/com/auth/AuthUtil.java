package gitweb.com.auth;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import gitweb.com.AccessToken;
import gitweb.com.httpUtil.HttpClientUtil;
import net.sf.json.JSONObject;

public class AuthUtil {

	//公司测试号
	public static final String APPID="wx722905523d0bd24e";
	public static final String APPSECRET="098709e8e05a54ced0fcb44ae3d5e871";
//	public static final String APPID="wxb56f5819ae6376d8";
//	public static final String APPSECRET="2d6ec128245185c8bc90ce34075a3fff";
	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+APPID+"&secret="+APPSECRET;
	private static final String CALLBACK_IP_URL = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=ACCESS_TOKEN";
	public static final String UPLOADIMG_URL = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=ACCESS_TOKEN";
	public static final String CARD_CREATE = "https://api.weixin.qq.com/card/create?access_token=ACCESS_TOKEN";
	public static final String QRCODE_CREATE = "https://api.weixin.qq.com/card/qrcode/create?access_token=TOKEN";
	public static final String SHOW_QRCODE = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
	public static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	public static final String QUERY_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	public static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	private AuthUtil(){};
	private static AuthUtil instance = null;
	public static AuthUtil getInstance(){
		if(instance==null){
			instance = new AuthUtil();
		}
		return instance;
	}
	private Map<String, String> map = new HashMap<>();
	private static AccessToken getAccessToken(){
		AccessToken token = new AccessToken();
		JSONObject jsonObject = HttpClientUtil.doGetStr(ACCESS_TOKEN_URL);
		if(jsonObject!=null){
			token.setExpiresIn(jsonObject.getInt("expires_in"));
			token.setToken(jsonObject.getString("access_token"));
		}
		return token;
	}
	//获取微信服务器IP地址
	private static String getcallbackip(String token){
		String result = "";
		String url = CALLBACK_IP_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = HttpClientUtil.doGetStr(url);
		if(jsonObject!=null){
			result = jsonObject.getString("ip_list");
		}
		return result;
	}
	public Map<String, String> get_access_token(){
		String time = map.get("time");//更新token的时间
        String accessToken = map.get("access_token");
        Long nowDate = new Date().getTime();//现在的时间
		if (accessToken != null && time != null && nowDate - Long.parseLong(time) < 7000 * 1000) {
            System.out.println("accessToken存在，且没有超时 ， 返回单例");
        } else {
            System.out.println("accessToken 超时 ， 或者不存在 ， 重新获取");
            AccessToken access_token=getAccessToken();
            map.put("time", nowDate + "");
            map.put("access_token", access_token.getToken());
        }
		
		return map;
	}
	public static void main(String[] args) {
		String token = AuthUtil.getInstance().get_access_token().get("access_token");
		getcallbackip(token);
	}
}
