package AuthServer.Filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import AuthServer.Security.JwtFilter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

	private JwtFilter jwtFilter;

	public AuthFilter(JwtFilter jwtFilter) {
		super(Config.class);
		this.jwtFilter = jwtFilter;
	}

	@Data
	public static class Config {
		private int role;
	}

	@Override
	public GatewayFilter apply(Config config) {

		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			ServerHttpResponse response = exchange.getResponse();

			return jwtFilter.Filter(request, response, config.getRole()).flatMap((data) -> {
//	        	exchange.getRequest().mutate().header("키", "값").build();
				if (data == true) {
					return chain.filter(exchange).then(Mono.fromRunnable(() -> {
					}));
				} else {
					response.setRawStatusCode(403);
					return response.setComplete();
				}
			});
		};
	}
}
