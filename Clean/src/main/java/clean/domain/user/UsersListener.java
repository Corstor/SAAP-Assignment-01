package clean.domain.user;


public interface UsersListener {
    void userCreditChanged(String userId, UserSnapshot user);
}
