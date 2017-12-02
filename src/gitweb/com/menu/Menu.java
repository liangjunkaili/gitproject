package gitweb.com.menu;
/**
 * 一级菜单数组，个数应为1~3个
 * @author lj
 *
 */
public class Menu {

	private Button[] button;

	public Button[] getButton() {
		return button;
	}

	public void setButton(Button[] button) {
		this.button = button;
	}
}
