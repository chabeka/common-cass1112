package fr.urssaf.image.commons.controller.spring3.exemple.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.format.Formatter;

public class DateFormatter implements Formatter<Date> {

	private final String pattern;

	public DateFormatter(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public String print(Date date, Locale locale) {

		return getFormat(locale).format(date);
	}

	@Override
	public Date parse(String source, Locale locale) throws ParseException {

		return getFormat(locale).parse(source);

	}

	private SimpleDateFormat getFormat(Locale locale) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, locale);
		dateFormat.setLenient(false);

		return dateFormat;
	}
}
