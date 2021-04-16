package invoice.auth;

import org.springframework.stereotype.Component;

import java.util.Base64;

@Component("Base64Auth")
public class Base64Auth implements AuthDecoder {

    @Override
    public Credentials decode(String s) {
        try {
            String base64Code = s.split("^Basic ")[1];
            String userNameAndPasswordStr = new String(Base64.getDecoder().decode(base64Code));
            String[] userNameAndPasswordSplited = userNameAndPasswordStr.split(":");
            String userName = userNameAndPasswordSplited[0];
            String password = userNameAndPasswordSplited[1];

            return new Credentials(userName, password);
        } catch (Exception e) {
            throw new BadAuthHeaderException();
        }
    }
}
