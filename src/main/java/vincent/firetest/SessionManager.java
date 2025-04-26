package vincent.firetest;

// used to store user info
public class SessionManager
{
    private static String userId;

    public static void setUserId(String id) {
        userId = id;
    }

    public static String getUserId() {
        return userId;
    }

}
