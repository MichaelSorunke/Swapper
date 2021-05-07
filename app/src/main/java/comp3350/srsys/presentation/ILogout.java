package comp3350.srsys.presentation;

public interface ILogout {
    /* Call this method anytime the user logs out */
    void broadCastLogout();

    /* Call this method in onCreate() of any activity we want to make sure the user needs
     * to be logged in to access that activity. For example, the user account page should only
     * be accessible if the user is logged in, so we call this method and pass the activity */
    void ensureUserLoggedIn();
}
