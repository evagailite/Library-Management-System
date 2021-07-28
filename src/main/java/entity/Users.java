package entity;

import types.UserType;

public class Users {
    private String username;
    private String name;
    private String email;
    private String password;
    private UserType userType;
    private String libraryCardNumber;

    public Users(String username, String name, String email, String password, UserType userType, String libraryCardNumber) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.libraryCardNumber = libraryCardNumber;
    }

    public Users() {
    }

    public String getLibraryCardNumber() {
        return libraryCardNumber;
    }

    public void setLibraryCardNumber(String libraryCardNumber) {
        this.libraryCardNumber = libraryCardNumber;
    }

    public Users(UserType userType) {
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
