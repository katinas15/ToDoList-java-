import WebControllers.Token;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static FXController.FxMain.passwordSalt;

public class testingClass {
    public static void main(String[] args) {
        Token token = new Token();
        token.createToken(1);

    }
}
