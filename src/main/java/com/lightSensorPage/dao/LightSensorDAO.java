package com.lightSensorPage.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.lightSensorPage.bean.LightSensorBean;

/**
 * DBをセッティングする
 *
 * @author kodai
 */

public class LightSensorDAO implements Serializable {

	/**
	 * インスタンス
	 *
	 */
	public LightSensorDAO() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * センサーの値をDBに格納する
	 *
	 * @param lightSensorValue センサーの値
	 * @return result 行数
	 */
	public int insertLightSensorValue(int lightSensorValue){
		int result = 0;
		String sql = "insert into lightsensorvalue(lightsensorvalue, inserttime) values (?, ?)";

		try (Connection conn = DriverManager.getConnection(DBSetting.URL, DBSetting.USER, DBSetting.PASSWORD)) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				LocalDateTime nowTime = LocalDateTime.now();
				stmt.setInt(1, lightSensorValue);
				stmt.setTimestamp(2, Timestamp.valueOf(nowTime));
				result = stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public List<LightSensorBean> selectLightSensorValue(LocalDateTime choiceDate) throws SQLException {
        String sql = "SELECT * FROM lightsensorvalue WHERE inserttime BETWEEN ? AND ?";
        List<LightSensorBean> lightSensorBeanList = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(DBSetting.URL, DBSetting.USER, DBSetting.PASSWORD)){
            try(PreparedStatement stmt = conn.prepareStatement(sql)){
            	stmt.setTimestamp(1, Timestamp.valueOf(choiceDate));
				stmt.setTimestamp(2, Timestamp.valueOf(choiceDate.plusHours(1)));

                ResultSet results = stmt.executeQuery();
                while(results.next()){
                	lightSensorBeanList.add(new LightSensorBean(
                        results.getLong("id"),
                        results.getInt("lightsensorvalue"),
                		results.getTimestamp("inserttime")));
                }
            }
            return lightSensorBeanList;
        }
	}

	public LightSensorBean selectFirstLightSensorValue(LocalDateTime choiceDate) throws SQLException {
        String sql = "SELECT * FROM lightsensorvalue WHERE lightsensorvalue <= 250 AND inserttime BETWEEN ? AND"
        		+ " ? ORDER BY inserttime ASC LIMIT 1;";
        LightSensorBean lightSensorBean = new LightSensorBean();
        try(Connection conn = DriverManager.getConnection(DBSetting.URL, DBSetting.USER, DBSetting.PASSWORD)){
            try(PreparedStatement stmt = conn.prepareStatement(sql)){
            	stmt.setTimestamp(1, Timestamp.valueOf(choiceDate));
				stmt.setTimestamp(2, Timestamp.valueOf(choiceDate.plusDays(1)));
                ResultSet results = stmt.executeQuery();
                while(results.next()){
                	lightSensorBean = new LightSensorBean(
                        results.getLong("id"),
                        results.getInt("lightsensorvalue"),
                		results.getTimestamp("inserttime"));
                }
            }
            return lightSensorBean;
        }
	}

	public LightSensorBean selectFinalLightSensorValue(LocalDateTime choiceDate) throws SQLException {
        String sql = "SELECT * FROM lightsensorvalue WHERE lightsensorvalue <= 250 AND inserttime BETWEEN ? AND"
        		+ " ? ORDER BY inserttime DESC LIMIT 1;";
        LightSensorBean lightSensorBean = new LightSensorBean();
        try(Connection conn = DriverManager.getConnection(DBSetting.URL, DBSetting.USER, DBSetting.PASSWORD)){
            try(PreparedStatement stmt = conn.prepareStatement(sql)){
            	stmt.setTimestamp(1, Timestamp.valueOf(choiceDate));
				stmt.setTimestamp(2, Timestamp.valueOf(choiceDate.plusDays(1)));
                ResultSet results = stmt.executeQuery();
                while(results.next()){
                	lightSensorBean = new LightSensorBean(
                        results.getLong("id"),
                        results.getInt("lightsensorvalue"),
                		results.getTimestamp("inserttime"));
                }
            }
            return lightSensorBean;
        }
	}
}
