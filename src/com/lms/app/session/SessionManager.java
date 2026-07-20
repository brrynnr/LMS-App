package com.lms.app.session;

import com.lms.app.model.User;

import java.io.*;

public class SessionManager implements SessionService {

    private static final String SESSION_FILE = "session.dat";

    @Override
    public void saveSession(User user) {

        UserSession session =
                new UserSession(
                        user.getUserId(),
                        user.getRole()
                );

        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(SESSION_FILE))) {

            out.writeObject(session);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserSession loadSession() {

        File file = new File(SESSION_FILE);

        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(file))) {

            return (UserSession) in.readObject();

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void clearSession() {

        File file = new File(SESSION_FILE);

        if (file.exists()) {
            file.delete();
        }

    }

    @Override
    public boolean hasSession() {
        return new File(SESSION_FILE).exists();
    }

}