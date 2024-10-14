package layered.business;

import layered.persistence.user.UserFactory;

public class UserCreationImpl implements UserCreation {
    private final UserFactory factory = UserFactory.getInstance();

    @Override
    public void createUser(String id) {
        factory.createUser(id);
        factory.getUserWithId(id).rechargeCredit(100);
    }
}
