package AuthServer.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import AuthServer.Dto.AuthUserDto;
import AuthServer.Dto.TokenInfoDto;
import AuthServer.Service.LoginService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class Controller {

	private LoginService loginService;

	public Controller(LoginService loginService) {
		this.loginService = loginService;
	}

	@GetMapping("/")
	public Flux<String> main() throws Exception {
		return Flux.just("??????");
	}

	@PostMapping("/login")
	public Mono<ResponseEntity<TokenInfoDto>> login(@RequestBody AuthUserDto authUserDto) throws Exception {
		
		return loginService.login(authUserDto).map(e -> {
			System.out.println(e.getJwtToken());
			if (e.getJwtToken() != null) {
				return ResponseEntity.status(HttpStatus.OK).body(e);
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		});
	}

	@PostMapping("/regist")
	public Mono<ResponseEntity<?>> regist(@RequestBody AuthUserDto authUserDto) throws Exception {
		return loginService.regist(authUserDto).map(e -> {
			if (e == true)
				return ResponseEntity.status(HttpStatus.CREATED).body(null);
			else
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		});
	}
}
