package com.getinventory.domain;

public class UserTestSamples {

    public static User getSampleUser() {
        return User.builder().id(1001L).login("tester").build();
    }
}
