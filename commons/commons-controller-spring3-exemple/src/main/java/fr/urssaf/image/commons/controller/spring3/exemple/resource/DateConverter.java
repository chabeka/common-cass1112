package fr.urssaf.image.commons.controller.spring3.exemple.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

public class DateConverter implements Converter<String, Date> {

	private final String pattern;

	public DateConverter(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public Date convert(String source) {

		Date date = null;
		if (StringUtils.hasText(source)) {
			try {
				date = getFormat().parse(source);
			} catch (ParseException e) {
				throw new IllegalArgumentException(e);
			}
		}
		return date;
	}

	private SimpleDateFormat getFormat() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale
				.getDefault());
		dateFormat.setLenient(false);

		return dateFormat;
	}
}
