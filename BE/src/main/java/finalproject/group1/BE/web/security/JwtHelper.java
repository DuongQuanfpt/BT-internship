package finalproject.group1.BE.web.security;

import finalproject.group1.BE.domain.entities.User;
import finalproject.group1.BE.domain.enums.DeleteFlag;
import finalproject.group1.BE.domain.enums.UserStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtHelper {
    @Value("${jwt.secret-key}")
    private String secretKey;

    private final int DURATION = 1000 * 60 * 60 * 10; //10h

    public String createToken(UserDetails userDetails) {
        HashMap<String,Object> claims = new HashMap<>();
        claims.put("role",userDetails.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.toString())
                .collect(Collectors.toList()));
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + DURATION))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public String extractEmail(String token) {

//        return extractClaim(token, Claims::getSubject);
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {

//        return extractClaim(token, Claims::getExpiration);
        return  extractAllClaims(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, User userDetails) {
        final String username = extractEmail(token);
        if((username.equals(userDetails.getUsername()) && !isTokenExpired(token)
                && userDetails.getStatus() != UserStatus.LOCKED)
                && userDetails.getDeleteFlag() != DeleteFlag.DELETED){
            return true;
        }
        return false;
    }

}
