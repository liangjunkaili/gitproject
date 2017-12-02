package gitweb.com.menu;

public enum ErrorCode {
	
	SUCCESS("ok",0),
	BUTTON_ERROR("invalid button name size",40018);
	
	ErrorCode(String name, int ordinal) {
	}

}
