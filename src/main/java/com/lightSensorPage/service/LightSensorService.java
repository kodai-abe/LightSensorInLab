package com.lightSensorPage.service;

import java.io.DataInputStream;
import java.io.Serializable;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.lightSensorPage.bean.DayPercentBean;
import com.lightSensorPage.bean.LightSensorBean;
import com.lightSensorPage.dao.LightSensorDAO;

public class LightSensorService implements Serializable {

	LightSensorDAO lightSensorDAO;

	public LightSensorService() {
		lightSensorDAO = new LightSensorDAO();
	}

	public int getLightSensorValue(){
		byte[] buffer = new byte[4096]; // 配列の定義
		URL url;
		int lightSensorValue = -1;

		// HTMLを読み出す
		try {
			url = new URL("");
			DataInputStream htmlStream = new DataInputStream(url.openStream());
			Boolean bool = true;
			int n;

			while ((n = htmlStream.read(buffer)) > 0) {
				if(bool){
					String str = new String(buffer);
					String lightSensorStr = "";
					for (int i = 0; i < 6; i++) {
						try {
							int num = Integer.parseInt(String.valueOf(str.charAt(i)));
							lightSensorStr += num;
						} catch (NumberFormatException e) {
							//e.printStackTrace();
						}
					}
					lightSensorValue = Integer.parseInt(lightSensorStr);
					System.out.println("lightSensorValue:" + lightSensorValue);
					bool = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lightSensorValue;
	}

	public int insertLightSensorValue (int lightSensorValue){
		return lightSensorDAO.insertLightSensorValue(lightSensorValue);
	}

	public List<DayPercentBean> selectLightSensorValue (LocalDateTime choiceDate){
		List<DayPercentBean> dayPercentBeanList = new ArrayList<>();
		for(int i = 0; i <= 23; i++){
			try {
				List<LightSensorBean> lightSensorBeanList = lightSensorDAO.selectLightSensorValue(choiceDate.plusHours(i));
				DayPercentBean dayPercentBean = new DayPercentBean();
				dayPercentBean.setHour(i);

				int countValue = 0;
				int errorValue = 0;
				for(LightSensorBean lightSensorBean : lightSensorBeanList){
					if (lightSensorBean.getLightSensorValue() == -1) {
						errorValue++;
					} else if (lightSensorBean.getLightSensorValue() <= 250) {
						countValue++;
					}
				}
				if(errorValue >= 10){
					dayPercentBean.setPercent(-1);
				}
				//5分ごとにデータを収集してるので、処理時間含め、１時間で702回前後のデータが収集されている
				//誤差含め、仮に680という値に設定
				else if(lightSensorBeanList.size() <= 680){
					dayPercentBean.setPercent(-1);
				} else {
					dayPercentBean.setPercent((double)countValue / lightSensorBeanList.size());
				}

				dayPercentBeanList.add(dayPercentBean);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dayPercentBeanList;
	}
}
