package gitweb.com.menu;
/**
 * media_id类型和view_limited类型必须	
 * 调用新增永久素材接口返回的合法media_id
 * @author lj
 *
 */
public class MediaButton extends Button{

	private String media_id;

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}
}
