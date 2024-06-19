package AuthServer.Service;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import AuthServer.Dto.AuthUserDto;
import AuthServer.Dto.TokenInfoDto;
import AuthServer.Model.UserInfo;
import AuthServer.Repository.UserInfoRepository;
import AuthServer.Security.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService{
	@Autowired
	UserInfoRepository userInfoRepository;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Mono<TokenInfoDto> login(AuthUserDto authUserDto) {
		String userId = authUserDto.getUserId();
		String userPassword = authUserDto.getUserPassword();

		Mono<UserInfo> getData = userInfoRepository.findByUserId(userId);		
		TokenInfoDto tokenInfoDto = new TokenInfoDto();

		return getData.flatMap(e->{
			String dbPassword = e.getUserPassword();
			boolean match = passwordEncoder.matches(userPassword, dbPassword);
			if(match == true) {
				AuthUserDto setUserDto = modelMapper.map(e,AuthUserDto.class);
				Date date = new Date();
				String token = jwtTokenUtil.generateToken(setUserDto);
				Date expireTime = jwtTokenUtil.getExpirationDateFromToken(token);
				tokenInfoDto.setJwtToken(token);
				tokenInfoDto.setExpireTime(expireTime.toString());
				tokenInfoDto.setGenerateTime(date.toString());
				return Mono.just(tokenInfoDto);
			}else {
				return Mono.just(tokenInfoDto);
			}
//			return null;
		}).switchIfEmpty(Mono.just(tokenInfoDto));		
	}

	@Override
	public Mono<Boolean> regist(AuthUserDto authUserDto) {
		String userPassword = authUserDto.getUserPassword();
		String EncryptPassword = passwordEncoder.encode(userPassword);
		authUserDto.setUserPassword(EncryptPassword);
		UserInfo userInfo = modelMapper.map(authUserDto, UserInfo.class);
		
		Mono<Boolean> save = userInfoRepository.save(userInfo).then(Mono.just(true))
				.onErrorReturn(false);
		return save;

	}
}
