package aze.coders.basic_authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

public class Main {
    private String keyStr = "QnUgYml6aW0gc2VjcmV0IGtleS1kaXIuZnNkZmRiaHBnO25qO2xnaEJ1IGJpemltIHNlY3JldCBrZXktZGlyLmZzZGZkYmhwZztuajtsZ2hCdSBiaXppbSBzZWNyZXQga2V5LWRpci5mc2RmZGJocGc7bmo7bGdoQnUgYml6aW0gc2VjcmV0IGtleS1kaXIuZnNkZmRiaHBnO25qO2xnaA==";
    public static void main(String[] args) {
        Main main = new Main();
        String token =main.createToken();
        System.out.println("token: " + token);
        System.out.println("parse payload: " + main.parseToken(token));

    }
    public String createToken() {
        return Jwts.builder().header()
                .add("typ", "JWT")
                .add("alg", "HS256")
                .and().claims()
                .add("name", "admin")
                .add("roles", List.of("ROLE_USER", "ROLE_ADMIN"))
                .issuedAt(new Date())
                .expiration(new Date (new Date().getTime() + 1000*60*10))
                .and()
                .signWith(getSignKey()).compact();
    }
     public Object parseToken(String token) {
        return Jwts.parser().verifyWith((SecretKey) getSignKey()).build().parse(token).getPayload();
     }

    private Key getSignKey() {
        byte[] keyBytes = keyStr.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
