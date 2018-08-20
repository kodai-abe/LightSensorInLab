package com.lightSensorPage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.apache.wicket.Application;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;

import com.lightSensorPage.graph.GraphPage;
import com.lightSensorPage.service.LightSensorService;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

	public HomePage() {
		LightSensorService lightSensorService = new LightSensorService();

		WebMarkupContainer messageContainer = new WebMarkupContainer("messageContainer");
		add(messageContainer);

		@SuppressWarnings("deprecation")
		Label luminousLabel = new Label("luminousLabel", new AbstractReadOnlyModel<String>() {
			@Override
			public String getObject() {
				int lightSensorValue = lightSensorService.getLightSensorValue();
				String lightSensorValueStr = "";

				if (lightSensorValue == -1) {
					lightSensorValueStr = "光センサーが正常に稼働していません";
				} else if (lightSensorValue <= 250) {
					lightSensorValueStr = "研究室に人はいます (光センサー:" + lightSensorValue + ")";
				} else {
					lightSensorValueStr = "研究室に人はいません (光センサー:" + lightSensorValue + ")";
				}

				return lightSensorValueStr;
			}
		});
		luminousLabel.setOutputMarkupId(true);

		messageContainer.add(luminousLabel);

		AjaxSelfUpdatingTimerBehavior behavior = new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5)) {
			@Override
			protected void onPostProcessTarget(AjaxRequestTarget target) {
				super.onPostProcessTarget(target);
				target.add(messageContainer);
			}
		};
		messageContainer.add(behavior);

		IModel<String> dateModel = Model.of("");

		Form<Void> form = new Form<Void>("form") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				try{
					super.onSubmit();
			        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
					LocalDateTime formatDate = LocalDateTime.parse(dateModel.getObject() + " 00:00:00", dtf);
					System.out.println("datetime : " + formatDate);
					setResponsePage(new GraphPage(formatDate));
				}catch(DateTimeParseException e){
					e.getStackTrace();
				}
			}
		};
		add(form);

		TextField<String> dateField = new TextField<>("date", dateModel);
		form.add(dateField);

	}

}