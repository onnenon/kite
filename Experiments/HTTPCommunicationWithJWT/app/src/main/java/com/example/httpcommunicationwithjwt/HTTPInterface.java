package com.example.httpcommunicationwithjwt;

public interface HTTPInterface {

    String login(final String username, final String password);

    String getUserInfo(String userName);

    void setModeratorStatus(String userName, boolean isModer);

    void setAdministratorStatus(String userName, boolean isAdmin);

    void setPassword(String userName, String newPassword);

    void setBio(String userName, String newBio);

    void setAll(String userName, String newPassword, String newBio, boolean isModer, boolean isAdmin);

    void deleteUser(String userName);
}
