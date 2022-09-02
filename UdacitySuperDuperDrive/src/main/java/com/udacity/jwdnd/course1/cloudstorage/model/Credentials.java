package com.udacity.jwdnd.course1.cloudstorage.model;

public class Credentials {
    private Integer credentialId;
    private String url;
    private String credUsername;
    private String key;
    private String credPassword;
    private Integer userId;

    public Integer getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(Integer credentialId) {
        this.credentialId = credentialId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCredUsername() {
        return credUsername;
    }

    public void setCredUsername(String credUsername) {
        this.credUsername = credUsername;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCredPassword() {
        return credPassword;
    }

    public void setCredPassword(String credPassword) {
        this.credPassword = credPassword;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
