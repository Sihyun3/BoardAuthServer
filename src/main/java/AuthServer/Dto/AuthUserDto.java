package AuthServer.Dto;

import lombok.Data;

@Data
public class AuthUserDto {
	private String userId;
	private String userPassword;
	private String userName;
	private int userRole;
	private String userBirth;
}
