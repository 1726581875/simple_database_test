package com.moyu.test.session;

/**
 * @author xiaomingzhang
 * @date 2023/5/15
 */
public class ConnectSession {

    private String userName;

    private Integer databaseId;


    public ConnectSession(String userName, Integer databaseId) {
        this.userName = userName;
        this.databaseId = databaseId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(Integer databaseId) {
        this.databaseId = databaseId;
    }
}