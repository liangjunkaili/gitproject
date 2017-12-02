package gitweb.com.card;

public class AdvancedInfo {

	//使用门槛（条件）字段，若不填写使用条件则在券面拼写：无最低消费限制，全场通用，不限品类；并在使用说明显示：可与其他优惠共享
	private UseCondition use_condition;
	private Abstract abstracts;//封面摘要结构体名称
	//图文列表，显示在详情内页，优惠券券开发者须至少传入一组图文列表
	private TextImage[] text_image_list;
	//商家服务类型：BIZ_SERVICE_DELIVER 外卖服务；BIZ_SERVICE_FREE_PARK 停车位；BIZ_SERVICE_WITH_PET 可带宠物；BIZ_SERVICE_FREE_WIFI 免费wifi，可多选
	private String[] business_service;
	private TimeLimit time_limit;//使用时段限制
	public UseCondition getUse_condition() {
		return use_condition;
	}
	public void setUse_condition(UseCondition use_condition) {
		this.use_condition = use_condition;
	}
	public Abstract getAbstracts() {
		return abstracts;
	}
	public void setAbstracts(Abstract abstracts) {
		this.abstracts = abstracts;
	}
	public TextImage[] getText_image_list() {
		return text_image_list;
	}
	public void setText_image_list(TextImage[] text_image_list) {
		this.text_image_list = text_image_list;
	}
	public String[] getBusiness_service() {
		return business_service;
	}
	public void setBusiness_service(String[] business_service) {
		this.business_service = business_service;
	}
	public TimeLimit getTime_limit() {
		return time_limit;
	}
	public void setTime_limit(TimeLimit time_limit) {
		this.time_limit = time_limit;
	}
}
