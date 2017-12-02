package gitweb.com.message;
/**
 * shortvideo and video
 * @author lj
 *
 */
public class VideoMessage extends BaseMessage{
	
	private String MediaId;//视频消息媒体id，可以调用多媒体文件下载接口拉取数据。
	private String ThumbMediaId;//视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
	private String Title;//视频消息的标题
	private String Description;//视频消息的描述
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getMediaId() {
		return MediaId;
	}
	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}
	public String getThumbMediaId() {
		return ThumbMediaId;
	}
	public void setThumbMediaId(String thumbMediaId) {
		ThumbMediaId = thumbMediaId;
	}
	
}
