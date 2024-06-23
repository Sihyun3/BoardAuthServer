package AuthServer.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import AuthServer.Model.UserInfo;
import AuthServer.Repository.UserInfoRepository;
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

}
