package gitweb.com.menu;
/**
 * miniprogram类型必须	
 * @author lj
 *
 */
public class MiniprogramButton extends Button{

	private String appid;// 小程序的appid（仅认证公众号可配置）
	private String pagepath;//小程序的页面路径

	public String getPagepath() {
		return pagepath;
	}

	public void setPagepath(String pagepath) {
		this.pagepath = pagepath;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}
}
