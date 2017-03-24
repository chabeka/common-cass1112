package fr.urssaf.image.commons.controller.spring3.exemple.aspect;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class ControllerAspect {

	private static final Logger LOG = Logger
			.getLogger(ControllerAspect.class);

	private static final String TIME = "@annotation(org.springframework.web.bind.annotation.RequestMapping) && execution(* *(..))";

	@Around(TIME)
	public Object logControllerTime(ProceedingJoinPoint pjp) throws Throwable {

		Calendar calendar1 = Calendar.getInstance();

		Object retVal = pjp.proceed();
		Calendar calendar2 = Calendar.getInstance();

		LOG.debug(pjp.getThis().getClass().getSimpleName() + " time:"
				+ (calendar2.getTimeInMillis() - calendar1.getTimeInMillis())
				/ 100.0);
		return retVal;
	}
	
}
