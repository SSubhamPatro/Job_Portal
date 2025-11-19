package com.ssp.Security;
//Step 1: JWT Utility Class
import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ssp.Entity.Role;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
@Component
public class JwtUtil {

	private static final String SECRET="your_super_secret_key_which_is_long_enough_for_security";
//	private static final long EXPIRATION=1000*60*60;//1 hour
	// 7 days
	private final long EXPIRATION = 1000 * 60 * 60 * 24 * 7;
	
	private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
	
	// ✅ Generate token with username + role
	public String generateToken(String username,Role role) {
		return Jwts.builder()
				.setSubject(username)
				.addClaims(Map.of("role",role))
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+EXPIRATION))
				.signWith(key,SignatureAlgorithm.HS256)
				.compact();
	}
	
	// ✅ Extract username
	public String extractUserName(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build()
			   .parseClaimsJws(token).getBody().getSubject();	
	}
	
	 // ✅ Extract role from token
	public String extractUserRole(String token) {
		
		return (String)Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.get("role");
	}

	// ✅ Validate token
     public boolean isTokenValid(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (JwtException e) {
            return false;
		}
	}
	
}
