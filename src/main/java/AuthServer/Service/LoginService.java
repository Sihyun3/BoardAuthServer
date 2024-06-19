package AuthServer.Service;

import AuthServer.Dto.AuthUserDto;
import AuthServer.Dto.TokenInfoDto;
import reactor.core.publisher.Mono;

public interface LoginService {

	Mono<TokenInfoDto> login(AuthUserDto authUserDto);

	Mono<Boolean> regist(AuthUserDto authUserDto);
}
