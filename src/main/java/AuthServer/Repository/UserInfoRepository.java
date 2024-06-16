package AuthServer.Repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import AuthServer.Model.UserInfo;
import reactor.core.publisher.Mono;

public interface UserInfoRepository  extends R2dbcRepository<UserInfo, Integer>{

	Mono<UserInfo> findByUserId(String subject);

}
