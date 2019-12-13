import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static FXController.FxMain.passwordSalt;

public class testingClass {
    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String pass1 = bCryptPasswordEncoder.encode("password" + passwordSalt);
        System.out.println("nnnnnn");
        String pass2 = bCryptPasswordEncoder.encode("password" + passwordSalt);
        if(bCryptPasswordEncoder.matches("password" + passwordSalt, pass2)) {
            System.out.println("aaaa");
        }

    }
}
