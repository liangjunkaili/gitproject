package gitweb.com.menu;


import gitweb.com.httpUtil.HttpClientUtil;
import net.sf.json.JSONObject;
/**
 * 1、自定义菜单最多包括3个一级菜单，每个一级菜单最多包含5个二级菜单。
 * 2、一级菜单最多4个汉字，二级菜单最多7个汉字，多出来的部分将会以“...”代替。
 * 3、创建自定义菜单后，菜单的刷新策略是，在用户进入公众号会话页或公众号profile页时，
 * 如果发现上一次拉取菜单的请求在5分钟以前，就会拉取一下菜单，如果菜单有更新，就会刷新客户端的菜单。
 * 测试时可以尝试取消关注公众账号后再次关注，则可以看到创建后的效果。
 * @author lj
 *
 */
public class MenuUtil {

	private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	private static final String QUERY_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	private static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	/**
	 * 自定义菜单创建
	 * @param token
	 * @param menu
	 * @return
	 */
	public static int createMenu(String token,String menu){
		int result = 0;
		String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = HttpClientUtil.doPostStr(url,menu);
		System.out.println(jsonObject.toString());
		if(jsonObject!=null){
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
	/**
	 * 自定义菜单查询
	 * @param token
	 * @return
	 */
	public static JSONObject queryMenu(String token){
		String url = QUERY_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = HttpClientUtil.doGetStr(url);
		return jsonObject;
	}
	/**
	 * 自定义菜单删除
	 * @param token
	 * @return
	 */
	public static JSONObject deleteMenu(String token){
		String url = DELETE_MENU_URL.replace("ACCESS_TOKEN", token);
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
	public static void main(String[] args) {
	}
}
