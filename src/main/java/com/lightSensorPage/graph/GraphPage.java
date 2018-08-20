package com.lightSensorPage.graph;



import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.lightSensorPage.service.LightSensorService;

public class GraphPage extends WebPage {
	private static final long serialVersionUID = 1L;

	public GraphPage(LocalDateTime choiceDate) {
		LightSensorService lightSensorService = new LightSensorService();

		IModel<List<DayPercentBean>> dayPercentBeanModel = Model.ofList(lightSensorService.selectLightSensorValue(choiceDate));

		IModel<String> dateLabelModel = Model.of(choiceDate.getYear() + "/"
                + choiceDate.getMonthValue() + "/"
                + choiceDate.getDayOfMonth() + "/");

		Label dateLabel = new Label("date", dateLabelModel);

		add(dateLabel);
		
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
				if(percent == -1){
					percentStr = "データなし";
				} else {
					percentStr = (int)(percent * 100) + "%";
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
				// submit ボタンがクリックされた時の処理
				super.onSubmit();
		        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
				LocalDateTime formatDate = LocalDateTime.parse(dateModel.getObject() + " 00:00:00", dtf);
				System.out.println("datetime : " + formatDate);
				setResponsePage(new GraphPage(formatDate));
			}
		};
		add(form);

		// name を入力する input type="text" 用のコンポーネント
		TextField<String> dateField = new TextField<>("date", dateModel);
		form.add(dateField);
	}
}