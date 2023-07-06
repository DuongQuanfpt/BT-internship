package finalproject.group1.BE.web.security;

import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtHelper {
    private final String SECRET_KEY = "===========secret key===========";

    private final int DURATION = 1000 * 60 * 60 * 10; //10h

    public String createToken(UserDetails userDetails) {
        return createToken(new HashMap<>(), userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + DURATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public String extractEmail(String token) {
        if (token.isEmpty()) {
            return null;
        }

        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            return claims.getSubject();
        }catch (UnsupportedJwtException | MalformedJwtException
                |SignatureException | ExpiredJwtException | IllegalArgumentException exception){
            return null;
        }

    }

}
