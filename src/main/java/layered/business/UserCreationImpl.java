package layered.business;

import layered.persistence.user.Users;

public class UserCreationImpl implements UserCreation {

    @Override
    public void createUser(String id) {
        Users.createUser(id);
        Users.getUserWithIdOptional(id).get().rechargeCredit(100);
    }
}
