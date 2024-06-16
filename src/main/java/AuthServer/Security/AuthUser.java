package AuthServer.Security;

import lombok.Data;

@Data
public class AuthUser {

	private String userId;
	private String userPassword;
	private String userName;
	private int userRole;
}
