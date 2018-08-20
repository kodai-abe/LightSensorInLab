package com.lightSensorPage;

import com.lightSensorPage.service.LightSensorService;

/**
 * センサーの値を取得する
 *
 * @author kodai
 */
public class LightSensorMain {
	/**
	 * Mainメソッド
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		LightSensorService lightSensorService = new LightSensorService();
        try {
        	while(true){
        		lightSensorService.insertLightSensorValue(lightSensorService.getLightSensorValue());
        		Thread.sleep(5000);
        	}
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
