package ensah.absencemanagement.utils;

import jakarta.servlet.http.HttpSession;

public class SessionManager {

    private static final String USER_KEY = "USER_KEY";

    public static void addUserToSession(HttpSession session, SessionUser sessionUser) {
        session.setAttribute(USER_KEY, sessionUser);
    }

    public static SessionUser getUserSession(HttpSession session) {
        return (SessionUser) session.getAttribute(USER_KEY);
    }

}
