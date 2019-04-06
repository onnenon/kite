package com.example.httpcommunicationwithjwt;

public interface HTTPInterface {

    String login(final String username, final String password);

    boolean getUserInfo(String userName);

    boolean setModeratorStatus(String userName, boolean isModer);

    boolean setAdministratorStatus(String userName, boolean isAdmin);

    boolean setPassword(String userName, String newPassword);

    boolean setBio(String userName, String newBio);

    boolean setAll(String userName, String newPassword, String newBio, boolean isModer, boolean isAdmin);

    boolean deleteUser(String userName);
}
