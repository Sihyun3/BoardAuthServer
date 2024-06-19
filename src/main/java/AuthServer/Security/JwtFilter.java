package AuthServer.Security;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtFilter {

	private JwtTokenUtil jwtTokenUtil;
	
	public JwtFilter(JwtTokenUtil jwtTokenUtil ) {
		this.jwtTokenUtil = jwtTokenUtil;
	}

	public Mono<Boolean> Filter(ServerHttpRequest request, ServerHttpResponse response,int role) {
		try {
			String jwtToken = null;
			
			HttpHeaders header = request.getHeaders();
			
			List<String> headerList = header.get("Authorization");
			String authorizationHeader = null;
			if(headerList.size() != 0)
				authorizationHeader = headerList.get(0);
			
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				jwtToken = authorizationHeader.substring(7);
			} else {
				log.error("Authoriztion 헤더 누락 또는 토큰 형식 오류");
			}
			return  jwtTokenUtil.validateToken(jwtToken,role);
			
		} catch(Exception e) {
			e.printStackTrace();
			return Mono.just(false);
		}
 
	}

}
