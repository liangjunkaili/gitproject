package gitweb.com.card;

public class TimeLimit {

	/**
	 * 限制类型枚举值：支持填入MONDAY 周一 TUESDAY 周二  WEDNESDAY 周三THURSDAY 周四 FRIDAY 
	 * 周五 SATURDAY 周六 SUNDAY 周日 此处只控制显示，不控制实际使用逻辑，不填默认不显示
	 */
	private String type;
	//当前type类型下的起始时间（小时），如当前结构体内填写了MONDAY，此处填写了10，则此处表示周一 10:00可用
	private int begin_hour;
	//当前type类型下的起始时间（分钟），如当前结构体内填写了MONDAY，begin_hour填写10，此处填写了59，则此处表示周一 10:59可用
	private int begin_minute;
	//当前type类型下的结束时间（小时），如当前结构体内填写了MONDAY，此处填写了20，则此处表示周一 10:00-20:00可用
	private int end_hour;
	//当前type类型下的结束时间（分钟），如当前结构体内填写了MONDAY，begin_hour填写10，此处填写了59，则此处表示周一 10:59-00:59可用
	private int end_minute;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getBegin_hour() {
		return begin_hour;
	}
	public void setBegin_hour(int begin_hour) {
		this.begin_hour = begin_hour;
	}
	public int getBegin_minute() {
		return begin_minute;
	}
	public void setBegin_minute(int begin_minute) {
		this.begin_minute = begin_minute;
	}
	public int getEnd_hour() {
		return end_hour;
	}
	public void setEnd_hour(int end_hour) {
		this.end_hour = end_hour;
	}
	public int getEnd_minute() {
		return end_minute;
	}
	public void setEnd_minute(int end_minute) {
		this.end_minute = end_minute;
	}
}
