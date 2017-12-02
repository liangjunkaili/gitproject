package gitweb.com.card;

public class Abstract {

	private String abstracts;//封面摘要简介。
	private String icon_url_list;//封面图片列表，仅支持填入一个封面图片链接，上传图片接口上传获取图片获得链接，填写非CDN链接会报错，并在此填入。建议图片尺寸像素850*350
	public String getAbstracts() {
		return abstracts;
	}
	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}
	public String getIcon_url_list() {
		return icon_url_list;
	}
	public void setIcon_url_list(String icon_url_list) {
		this.icon_url_list = icon_url_list;
	}
}
