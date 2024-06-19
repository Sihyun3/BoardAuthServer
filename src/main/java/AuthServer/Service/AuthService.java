package AuthServer.Service;

import AuthServer.Dto.AuthUserDto;
import AuthServer.Model.UserInfo;
import reactor.core.publisher.Mono;

public interface AuthService {

	Mono<UserInfo> getUserInfo(String subject);


}
