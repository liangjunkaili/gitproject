package gitweb.com.message;
/**
 * 接收事件推送
 * subscribe(订阅)、unsubscribe(取消订阅)、SCAN、LOCATION、CLICK、VIEW
 * @author lj
 *
 */
public class EventMessage extends BaseMessage{

	private String Event;//事件类型
	/**
	 * 事件KEY值，未关注：qrscene_为前缀，后面为二维码的参数值
	 * 已关注：是一个32位无符号整数，即创建二维码时的二维码scene_id
	 */
	private String EventKey;
	private String Ticket;//二维码的ticket，可用来换取二维码图片
	private String Latitude;//地理位置纬度
	private String Longitude;//地理位置经度
	private String Precision;//地理位置精度
	public String getEvent() {
		return Event;
	}
	public void setEvent(String event) {
		Event = event;
	}
	public String getEventKey() {
		return EventKey;
	}
	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}
	public String getTicket() {
		return Ticket;
	}
	public void setTicket(String ticket) {
		Ticket = ticket;
	}
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	public String getPrecision() {
		return Precision;
	}
	public void setPrecision(String precision) {
		Precision = precision;
	}
}
