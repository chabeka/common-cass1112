package fr.urssaf.image.commons.controller.spring3.exemple.controller.base;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

public class BaseExempleController<F> {

	protected static final Logger LOG = Logger
			.getLogger(BaseExempleController.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	protected Validator validator;

	@RequestMapping(value = "/populateField", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, ? extends Object> populateField(F formulaire, Errors errors,
			@RequestParam String field, HttpServletRequest request,
			HttpServletResponse response) {

		Map<String, ? extends Object> reponseBody;

		if (errors.hasFieldErrors()) {

			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

			reponseBody = validationMessages(errors.getFieldErrors(), request);

		} else {
			Set<ConstraintViolation<F>> failures = validator.validateProperty(
					formulaire, field);

			if (failures.isEmpty()) {

				response.setStatus(HttpServletResponse.SC_OK);
				reponseBody = Collections.singletonMap(field, "");
			} else {
				response
						.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				reponseBody = validationMessages(failures);

			}
		}

		return reponseBody;

	}

	private Map<String, String> validationMessages(List<FieldError> errors,
			HttpServletRequest request) {
		Map<String, String> failureMessages = new HashMap<String, String>();
		for (FieldError error : errors) {

			String message = messageSource.getMessage(error, request
					.getLocale());
			failureMessages.put(error.getField(), message);
		}
		return failureMessages;
	}

	private Map<String, String> validationMessages(
			Set<ConstraintViolation<F>> failures) {
		Map<String, String> failureMessages = new HashMap<String, String>();
		for (ConstraintViolation<F> failure : failures) {
			failureMessages.put(failure.getPropertyPath().toString(), failure
					.getMessage());
		}
		return failureMessages;
	}

}
