package fr.urssaf.image.commons.controller.spring3.exemple.resource;

import java.util.Date;

import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

public class MyFormattingConversionServiceFactoryBean extends
		FormattingConversionServiceFactoryBean {

	
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();

		// formatage des dates en dd/MM/yyyy
		this.getObject().addFormatterForFieldType(Date.class,
				new DateFormatter(DATE_FORMAT));
		// conversion des dates en dd/MM/yyyy
		this.getObject().addConverter(new DateConverter(DATE_FORMAT));
		
		
	}
}
