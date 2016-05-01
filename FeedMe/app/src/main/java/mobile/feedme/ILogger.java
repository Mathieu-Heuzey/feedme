package mobile.feedme;

import mobile.feedme.POCO.Utilisateur;

/**
 * Created by Quentin on 5/1/2016.
 */
public interface ILogger {

    public void loginSuccessfull();
    public void loginError(String msg);
    public void userInfoUpdated(Utilisateur user);
}
