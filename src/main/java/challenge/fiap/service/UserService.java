package challenge.fiap.service;

import challenge.fiap.models.Admin;
import challenge.fiap.models.Operator;
import challenge.fiap.models.User;

public class UserService {

    public static boolean createUserCheck(User user) {
        return user.getUsername() != null &&
                user.getEmail() != null &&
                user.getPassword() != null &&
                ((!(user instanceof Admin) || ((Admin) user).getAcessLevel() >= 0) &&
                        (!(user instanceof Operator) || ((Operator) user).getSector() != null));
    }

}
