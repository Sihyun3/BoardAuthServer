package AuthServer.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import AuthServer.Dto.AuthUserDto;
import AuthServer.Model.UserInfo;
import AuthServer.Repository.UserInfoRepository;
import AuthServer.Security.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

	@Autowired
	UserInfoRepository userInfoRepository;

	@Override
	public Mono<UserInfo> getUserInfo(String subject) {
		return userInfoRepository.findByUserId(subject);
	}

//	Mono<Boolean> returnObj = getData.flatMap((e) -> {
//		String userId = e.getUserId();
//		int userRole = e.getUserRole();
//		if (subject != null && userId != null && subject.equals(userId) && tokenRole == role && tokenRole == userRole) {
//			return Mono.just(true);
//		} else {
//			return Mono.just(false);
//		}
//	}).switchIfEmpty(Mono.just(false));


}
