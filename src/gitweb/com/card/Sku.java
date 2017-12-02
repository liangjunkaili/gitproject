package gitweb.com.card;

public class Sku {

	private int quantity;//卡券库存的数量，上限为100000000。

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
