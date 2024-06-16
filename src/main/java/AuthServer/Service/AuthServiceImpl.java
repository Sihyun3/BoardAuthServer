package AuthServer.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import AuthServer.Model.UserInfo;
import AuthServer.Repository.UserInfoRepository;
import reactor.core.publisher.Mono;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	UserInfoRepository userInfoRepository;
	
	@Override
	public Mono<UserInfo> getUserInfo(String subject) {
		return userInfoRepository.findByUserId(subject);
	}

}
