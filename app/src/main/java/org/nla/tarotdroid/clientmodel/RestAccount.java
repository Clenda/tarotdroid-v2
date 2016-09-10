package org.nla.tarotdroid.clientmodel;

import com.google.common.base.Objects;

public class RestAccount {

    private String email;
    private String name;
    private String password;

    public RestAccount() {
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                      .add("name", this.name)
                      .add("email", this.email)
                      .add("password", this.password)
                      .toString();
    }
}