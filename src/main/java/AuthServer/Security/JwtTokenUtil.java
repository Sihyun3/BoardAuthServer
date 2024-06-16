package AuthServer.Security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import AuthServer.Model.UserInfo;
import AuthServer.Service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JwtTokenUtil {

	private String secret;
	private Long expirationTime;
	private SecretKey hmacKey;
	private AuthService authService;

	public JwtTokenUtil(Environment env, AuthService authService) {
		this.secret = env.getProperty("token.secret");
		this.expirationTime = Long.parseLong(env.getProperty("token.expiration-time"));
		this.hmacKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(this.secret));
		this.authService = authService;
	}

	public String generateToken(AuthUser authUser) {
		Instant now = Instant.now();

		String jwtToken = Jwts.builder().claim("name", authUser.getUserName()).claim("role", authUser.getUserRole())
				.subject(authUser.getUserId()).id(UUID.randomUUID().toString()).issuedAt(Date.from(now))
				.expiration(Date.from(now.plus(this.expirationTime, ChronoUnit.MILLIS))).signWith(this.hmacKey)
				.compact();
		log.debug("**************************************");
		log.debug(jwtToken);
		log.debug("**************************************");
		return jwtToken;
	}

	private Claims getAllClaimsFromToken(String token) {
		Jws<Claims> jwt = Jwts.parser().verifyWith(this.hmacKey).build().parseSignedClaims(token);
		return jwt.getPayload();
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	public String getSubjectFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public int getRoleFromToken(String token) {
		Claims claims = getAllClaimsFromToken(token);
		return (int) claims.get("role");
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public Mono<Boolean> validateToken(String token, int role) {
		if (isTokenExpired(token)) {
			return Mono.just(false);
		}
		String subject = getSubjectFromToken(token);
		int tokenRole = getRoleFromToken(token);

		Mono<UserInfo> getData = authService.getUserInfo(subject);
		Mono<Boolean> returnObj = getData.flatMap((e) -> {
			String userId = e.getUserId();
			int userRole = e.getUserRole();
			if (subject != null && userId != null && subject.equals(userId) && tokenRole == role && tokenRole == userRole) {
				return Mono.just(true);
			} else {
				return Mono.just(false);
			}
		}).switchIfEmpty(Mono.just(false));
		return returnObj;

	}
}