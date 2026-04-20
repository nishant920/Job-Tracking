package Job.Track_site.utility;

import Job.Track_site.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtility {

    @Value("${jwt.expiration.time}")
    long expirationTime;
    @Value("${jwt.secret.password}")
    String secretPass;

    // JWT Token -> HEADER, PAYLOAD, SIGNATURE (Secure Password + Algorithm)
    // JWT Token -> We are encrypting the data with the help of Algorithm + SecretPassword
    /*
     JWT payload should NEVER contain sensitive data(i added password in my last project, it still work tho).

     signWith(Key, SignatureAlgorithm) forces you to use a Key object. By using Keys.hmacShaKeyFor(bytes),
      the library checks your secret. If your secretPass is too short (e.g., only 4 characters),
      the new version will actually throw an error and refuse to start
     */
    public String generateToken(String email, String password, Role  role){
        return Jwts.builder()
                .setSubject(email) // who the user is
                .claim("role", role) // extra info
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(Keys.hmacShaKeyFor(secretPass.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    /* we use Claims becouse the JWT payload is a structured DATA(JSON)*/

   public String verifyJwtToken(String token){
       Claims claims = Jwts.parserBuilder().setSigningKey(secretPass.getBytes()).
               build().parseClaimsJws(token).getBody();
       return claims.getSubject();
   }

}
