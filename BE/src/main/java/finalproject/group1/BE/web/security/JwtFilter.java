package finalproject.group1.BE.web.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import finalproject.group1.BE.commons.Constants;
import finalproject.group1.BE.domain.entities.User;
import finalproject.group1.BE.domain.repository.UserRepository;
import finalproject.group1.BE.web.dto.response.error.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private JwtHelper jwtHelper;
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        if(tokenHeader == null || !tokenHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        String token = tokenHeader.substring(7);

        String email = null;

        try{
            email = jwtHelper.extractEmail(token);
        }catch (SignatureException | MalformedJwtException |
                IllegalArgumentException | ExpiredJwtException ex){
            ex.printStackTrace();
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .message(Constants.COMMON_TOKEN_EXCEPTION_RESPONSE)
                    .statusCode("403")
                    .build();
            responseToClient(response,errorResponse, HttpStatus.FORBIDDEN.value());
            return;
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userRepository.findByEmail(email).get();

            if (jwtHelper.validateToken(token, user)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null
                        , user.getAuthorities());
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }else {
                ErrorResponse errorResponse = ErrorResponse.builder()
                        .message(Constants.COMMON_TOKEN_EXCEPTION_RESPONSE)
                        .statusCode("403")
                        .build();
                responseToClient(response,errorResponse, HttpStatus.FORBIDDEN.value());
                return;
            }
        }
        filterChain.doFilter(request,response);
    }

    private void responseToClient(HttpServletResponse response, ErrorResponse customError, int httpStatus)
            throws IOException {

        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        response.setStatus(httpStatus);
//        Map<String, ErrorMessage> map = new HashMap<>();
//        map.put("error", customError);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        response.getOutputStream().print(mapper.writeValueAsString(customError));
        response.flushBuffer();
    }
}
