package thiagosbarros.com.servicehub.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Service
public class JwtService {

    private static final String HMAC_SHA256 = "HmacSHA256";

    private final String secret;
    private final long expirationSeconds;

    public JwtService(@Value("${security.jwt.secret:servicehub-dev-secret-key-please-change}") String secret,
                      @Value("${security.jwt.expiration-seconds:3600}") long expirationSeconds) {
        this.secret = secret;
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(ServiceHubUserDetails userDetails) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(expirationSeconds);

        String headerJson = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payloadJson = """
                {"sub":"%s","empresaId":%d,"userId":%d,"role":"%s","iat":%d,"exp":%d}
                """.formatted(
                userDetails.getUsername(),
                userDetails.getEmpresaId(),
                userDetails.getUsuarioId(),
                userDetails.getUsuario().getRole().name(),
                now.getEpochSecond(),
                expiration.getEpochSecond()
        ).replace("\r", "").replace("\n", "").trim();

        String header = base64UrlEncode(headerJson);
        String payload = base64UrlEncode(payloadJson);
        String content = header + "." + payload;
        String signature = sign(content);

        return content + "." + signature;
    }

    private String sign(String content) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
            mac.init(keySpec);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(content.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("Erro ao assinar JWT.", exception);
        }
    }

    private String base64UrlEncode(String value) {
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }
}
