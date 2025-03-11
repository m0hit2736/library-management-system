package com.library.management.system.utils;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.library.management.system.entities.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	// Generate a secure secret key using HS256 algorithm
	private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	// Set to store invalidated tokens
	private final Set<String> invalidatedTokens = ConcurrentHashMap.newKeySet();

	/**
	 * Generate JWT Token for the given user.
	 */
	public String generateToken(UserEntity userDetails) {
		List<String> roles = List.of(userDetails.getRole().getValue());

		return Jwts.builder().setSubject(userDetails.getUsername()).claim("roles", roles).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1-day expiration
				.signWith(secretKey, SignatureAlgorithm.HS256) // Updated method
				.compact();
	}

	/**
	 * Extract username from JWT token.
	 */
	public String getUsernameFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
	}

	/**
	 * Extract claims from JWT token.
	 */
	public Claims extractClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
	}

	/**
	 * Validate the token, ensuring it's not expired or invalidated.
	 */
	public boolean validateToken(String token) {
		try {
			if (invalidatedTokens.contains(token)) {
				return false;
			}
			Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Invalidate a token (for logout functionality).
	 */
	public void invalidateToken(String token) {
		invalidatedTokens.add(token);
	}
}
