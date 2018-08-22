package com.lightSensorPage.graph;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.lightSensorPage.HomePage;
import com.lightSensorPage.bean.DayPercentBean;
import com.lightSensorPage.bean.LightSensorBean;
import com.lightSensorPage.service.LightSensorService;

public class GraphPage extends WebPage {
	private static final long serialVersionUID = 1L;

	public GraphPage(LocalDateTime choiceDate) {
		LightSensorService lightSensorService = new LightSensorService();
		LocalDateTime now = LocalDateTime.now();

		IModel<List<DayPercentBean>> dayPercentBeanModel = Model
				.ofList(lightSensorService.selectLightSensorValue(choiceDate, now));

		IModel<String> dateLabelModel = Model
				.of(choiceDate.getYear() + "/" + choiceDate.getMonthValue() + "/" + choiceDate.getDayOfMonth() + "/");

		Label dateLabel = new Label("date", dateLabelModel);

		add(dateLabel);

		// 最初
		LightSensorBean firstValue = lightSensorService.selectFirstLightSensorValue(choiceDate);
		String firstStr = "";

		if (firstValue.getInserttime() == null) {
			firstStr = "データなし";
		} else {
			LocalDateTime firstDate = firstValue.getInserttime().toLocalDateTime();
			if (firstDate.getHour() == 0 && firstDate.getMinute() == 0 && firstDate.getSecond() <= 10) {
				firstStr = "前日から人がいます";
			} else {
				firstStr = String.format("%02d", firstDate.getHour()) + ":" + String.format("%02d", firstDate.getMinute())
				+ ":" + String.format("%02d", firstDate.getSecond());
			}
		}

		IModel<String> firstLabelModel = Model.of(firstStr);

		Label firstLabel = new Label("first", firstLabelModel);

		add(firstLabel);

		// 最後
		LightSensorBean finalValue = lightSensorService.selectFinalLightSensorValue(choiceDate);
		String finalStr = "";

		if (finalValue.getInserttime() == null) {
			finalStr = "データなし";
		} else {
			LocalDateTime finalDate = finalValue.getInserttime().toLocalDateTime();
			if (finalDate.getYear() == now.getYear() && finalDate.getMonth() == now.getMonth()
					&& finalDate.getDayOfMonth() == now.getDayOfMonth()
					&& lightSensorService.getLightSensorValue() <= 250) {
				finalStr = "現在人がいます";
			} else if (finalDate.getHour() == 23 && finalDate.getMinute() == 59 && finalDate.getSecond() >= 50) {
				finalStr = "日付が変わる瞬間まで研究室に人がいたようです";
			} else {
				finalStr = String.format("%02d", finalDate.getHour()) + ":" + String.format("%02d", finalDate.getMinute())
				+ ":" + String.format("%02d", finalDate.getSecond());
			}
		}

		IModel<String> finalLabelModel = Model.of(finalStr);

		Label finalLabel = new Label("final", finalLabelModel);

		add(finalLabel);

		Link<Void> toHomePageLink = new Link<Void>("toHomePage") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new HomePage());
			}
		};
		add(toHomePageLink);

		ListView<DayPercentBean> usersView = new ListView<DayPercentBean>("dayPercentBean", dayPercentBeanModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<DayPercentBean> item) {
				DayPercentBean dayPercentBean = item.getModelObject();

				Label hourLabel = new Label("hour", Model.of(dayPercentBean.getHour() + "時"));
				item.add(hourLabel);

				String percentStr = "";
				double percent = dayPercentBean.getPercent();
				if (percent == -1) {
					percentStr = "データなし";
				} else if (dayPercentBean.getIsNow()) {
					percentStr = (int) (percent * now.getMinute()) + "分(計測中)";
				} else {
					percentStr = (int) (percent * 60) + "分";
				}

				Label percentLabel = new Label("percent", Model.of(percentStr));
				item.add(percentLabel);
			}
		};
		add(usersView);

		IModel<String> dateModel = Model.of("");

		// Formタグ用の Form コンポーネント
		Form<Void> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				try {
					super.onSubmit();
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
					LocalDateTime formatDate = LocalDateTime.parse(dateModel.getObject() + " 00:00:00", dtf);
					System.out.println("datetime : " + formatDate);
					setResponsePage(new GraphPage(formatDate));
				} catch (DateTimeParseException e) {
					e.getStackTrace();
				}
			}
		};
		add(form);

		// name を入力する input type="text" 用のコンポーネント
		TextField<String> dateField = new TextField<>("date", dateModel);
		form.add(dateField);
	}
}