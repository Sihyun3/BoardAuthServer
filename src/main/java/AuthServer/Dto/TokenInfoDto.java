package AuthServer.Dto;

import lombok.Data;

@Data
public class TokenInfoDto {
	private String jwtToken;
	private String generateTime;
	private String expireTime;
}
