package com.lms.app.session;

import com.lms.app.model.User;

public interface SessionService {

    void saveSession(User user);

    UserSession loadSession();

    void clearSession();

    boolean hasSession();

}