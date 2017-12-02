package gitweb.com.card;

public class Card {

	private String card_type;
	private GrouponCard groupon;
	public String getCard_type() {
		return card_type;
	}
	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}
	public GrouponCard getGroupon() {
		return groupon;
	}
	public void setGroupon(GrouponCard groupon) {
		this.groupon = groupon;
	}
}
