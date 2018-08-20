package com.lightSensorPage.dao;

/**
 * DBをセッティングする
 *
 * @author kodai
 */
public class DBSetting {
    public static final String URL = "jdbc:postgresql://localhost:5432/lightsensor";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";

    private DBSetting(){
        throw new AssertionError("インスタンス化してはいけない");
    }
}
