package com.esc.identity.utility;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JWTService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("jwt.privatekey")
	private String privateKey;
	
	@Value("jwt.publicKey")
	private String publicKey;

	private final RsaKeyProvider keyProvider;

	public JWTService(RsaKeyProvider keyProvider) {
		super();
		this.keyProvider = keyProvider;
	}

	public String generateToken(UserDetails user) {
		
		if (this.logger.isDebugEnabled()) {
			this.logger.debug(String.format("Generate Authorization token for %s", user.getUsername()));
		}

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expiryDate = now.plusMinutes(SecurityConstants.EXPIRATION_TIME);

		return Jwts.builder()
				.setExpiration(Date.from(expiryDate.atZone(ZoneId.systemDefault()).toInstant()))
				.setIssuer("Identity Provider Service")
				.setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
				.setSubject(user.getUsername())
				.claim("roles", user.getAuthorities().stream().map(role -> role.getAuthority())
                		.filter(Objects::nonNull).collect(Collectors.toList()))
				.signWith(SignatureAlgorithm.RS256, keyProvider.getPrivateKey(privateKey.replaceAll("\\n", "")))
				.compact();
	}
	
	public Boolean validateToken(String token, UserDetails user) {
		final String username = extractSubject(token);
		return username.equals(user.getUsername()) && !isTokenExpired(token);
	}
	
	public String extractSubject(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	public Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = Jwts.parser()
								.setSigningKey(keyProvider.getPublicKey(publicKey.replaceAll("\\n", "")))
								.parseClaimsJwt(token)
								.getBody();
		
		return claimsResolver.apply(claims);
	}

}
