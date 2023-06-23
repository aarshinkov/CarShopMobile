package com.vasil.carshop.responses.users;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserGetResponse implements Serializable {

    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private Timestamp createdOn;
    private Timestamp editedOn;

    public String getFullName() {
        return lastName != null ? firstName + " " + lastName : firstName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public Timestamp getEditedOn() {
        return editedOn;
    }

    public void setEditedOn(Timestamp editedOn) {
        this.editedOn = editedOn;
    }
}
