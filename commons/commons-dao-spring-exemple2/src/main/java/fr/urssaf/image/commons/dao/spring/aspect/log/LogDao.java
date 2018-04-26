package fr.urssaf.image.commons.dao.spring.aspect.log;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;


/**
 * Classe AOP pour logger le temps d'exécution des méthodes DAO
 */
@Aspect
public class LogDao {

   protected static final Logger LOGGER = Logger.getLogger(LogDao.class);

	private static final String TIME = "target(fr.urssaf.image.commons.dao.spring.support.MyHibernateDaoSupport) && execution(public * *(..))";
	
	private static final long NB_MILLI_IN_SEC = 1000;
	
	
	/**
	 * Log le temps d'exécution des méthodes DAO
	 * 
	 * @param pjp le point de jointure
	 * @return le résultat de la méthode
	 * @throws Throwable en cas de problème
	 */
	@Around(TIME)
	public final Object logControllerTime(ProceedingJoinPoint pjp) throws Throwable {

	   Calendar calendar1 = Calendar.getInstance();

		Object retVal = pjp.proceed();
		Calendar calendar2 = Calendar.getInstance();

		// On écrit un message du type :
		//  [NomCourtClasse] time:[Temps en secondes]
		long tempsExecutionMs = calendar2.getTimeInMillis() - calendar1.getTimeInMillis();
		long tempsExecutionSec = tempsExecutionMs / NB_MILLI_IN_SEC;
		LOGGER.debug(
		      String.format(
		            "%s time: %s",
		            pjp.getThis().getClass().getSimpleName(),
		            tempsExecutionSec));

		return retVal;
	}

}
