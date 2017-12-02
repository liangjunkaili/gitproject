package gitweb.com.card;
/**
 * 团购券
 * @author lj
 *
 */
public class GrouponCard {

	private BaseInfo base_info;
//	private AdvancedInfo advanced_info;
	private String deal_detail;//团购券专用，团购详情。
	public BaseInfo getBase_info() {
		return base_info;
	}
	public void setBase_info(BaseInfo base_info) {
		this.base_info = base_info;
	}
//	public AdvancedInfo getAdvanced_info() {
//		return advanced_info;
//	}
//	public void setAdvanced_info(AdvancedInfo advanced_info) {
//		this.advanced_info = advanced_info;
//	}
	public String getDeal_detail() {
		return deal_detail;
	}
	public void setDeal_detail(String deal_detail) {
		this.deal_detail = deal_detail;
	}
}