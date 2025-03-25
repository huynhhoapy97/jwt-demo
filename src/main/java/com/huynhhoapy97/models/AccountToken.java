package com.huynhhoapy97.models;

public class AccountToken {
    private String sub;
    private String exp;

    public AccountToken() {
    }

    public AccountToken(String sub, String exp) {
        this.sub = sub;
        this.exp = exp;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }
}
