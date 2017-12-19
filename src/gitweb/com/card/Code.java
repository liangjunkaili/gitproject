package gitweb.com.card;

public class Code {

	private String card_id;
	private String code;
	private boolean check_consume;
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public boolean isCheck_consume() {
		return check_consume;
	}
	public void setCheck_consume(boolean check_consume) {
		this.check_consume = check_consume;
	}
}
