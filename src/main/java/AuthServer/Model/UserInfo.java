package AuthServer.Model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class UserInfo {
	
	@Id
	private int idx;
	
	private String userName;
	
	private String userId;
	
	private String userPassword;
	
	private LocalDate userBirth;
	
	private int userRole;
	
}
