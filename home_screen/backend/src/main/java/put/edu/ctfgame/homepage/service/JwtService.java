package put.edu.ctfgame.homepage.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {
    @Value("${security.jwt.secret-key}")
    String secretKey;

    @Value("${security.jwt.expiration-time}")
    long jwtExpiration;

    public String extractUsernameFromRequest(HttpServletRequest request) {
        log.info("Extracting username from request");
        String token = request.getHeader("Authorization").substring(7); // Remove "Bearer " prefix
        String username = extractUsername(token);
        log.info("Extracted username: {}", username);
        return username;
    }

    public String extractUsername(String token) {
        log.info("Extracting username from token");
        String username = extractClaim(token, Claims::getSubject);
        log.info("Extracted username: {}", username);
        return username;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        log.info("Extracting claim from token");
        final Claims claims = extractAllClaims(token);
        T claim = claimsResolver.apply(claims);
        log.info("Extracted claim: {}", claim);
        return claim;
    }

    public String generateToken(UserDetails userDetails) {
        log.info("Generating token for user: {}", userDetails.getUsername());
        String token = generateToken(new HashMap<>(), userDetails);
        log.info("Generated token: {}", token);
        return token;
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        log.info("Generating token with extra claims for user: {}", userDetails.getUsername());
        String token = buildToken(extraClaims, userDetails, jwtExpiration);
        //log.info("Generated token: {}", token);
        return token;
    }

    public long getExpirationTime() {
        log.info("Getting expiration time");
        long expirationTime = jwtExpiration;
        log.info("Expiration time: {}", expirationTime);
        return expirationTime;
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        log.info("Building token for user: {}", userDetails.getUsername());
        String token = Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
        log.info("Built token: {}", token);
        return token;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        log.info("Validating token for user: {}", userDetails.getUsername());
        boolean isValid = extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
        log.info("Token is valid: {}", isValid);
        return isValid;
    }

    private boolean isTokenExpired(String token) {
        log.info("Checking if token is expired");
        boolean isExpired = extractClaim(token, Claims::getExpiration).before(new Date());
        log.info("Token is expired: {}", isExpired);
        return isExpired;
    }

    private Claims extractAllClaims(String token) {
        log.info("Extracting all claims from token");
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        log.info("Extracted claims: {}", claims);
        return claims;
    }

    private Key getSignInKey() {
        log.info("Getting signing key");
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        log.info("Got signing key");
        return key;
    }


}