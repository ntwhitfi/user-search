package ntwhitfi.user.search.request.authorizer.util;

import static com.nimbusds.jose.JWSAlgorithm.RS256;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import lombok.extern.slf4j.Slf4j;
import ntwhitfi.user.search.request.authorizer.config.Constants;

import java.net.MalformedURLException;
import java.net.URL;


@Slf4j
public class AuthTokenProcessor {

    public static ConfigurableJWTProcessor<SecurityContext> getJWTProcessor() {
        DefaultResourceRetriever resourceRetriever = new DefaultResourceRetriever(2000, 2000);
        URL jwkSetUrl;
        try {
            jwkSetUrl = new URL(Constants.COGNITO_JWK_URL);
        } catch (MalformedURLException ex) {
            log.error("Invalid format for URL provided. Exception: " + ex.getMessage());
            return null;
        }

        //Creates the JSON Web Key
        RemoteJWKSet<SecurityContext> keySource = new RemoteJWKSet<SecurityContext>(jwkSetUrl, resourceRetriever);
        DefaultJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<SecurityContext>();

        //Set the key selector algorithm and attach to jwt processor
        JWSVerificationKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<SecurityContext>(RS256, keySource);
        jwtProcessor.setJWSKeySelector(keySelector);

        return jwtProcessor;
    }
}
