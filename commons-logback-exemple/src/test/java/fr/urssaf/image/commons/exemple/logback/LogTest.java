package fr.urssaf.image.commons.exemple.logback;

import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import fr.urssaf.image.commons.exemple.logback.util.AppenderTestBase;

/**
 * Attention ces tests ne peuvent être influencés par le niveau du root logger
 * dans le fichier logback-test.xml situé dans le classpath.<br>
 * Le niveau du root logger ne peut pas être inférieur à celui qu'on veut tester
 * dans chaque test<br>
 * ex :
 * 
 * <pre>
 * &lt;root level="WARN">
 *      &lt;appender-ref ref="console" />
 * &lt;/root>
 * </pre>
 * 
 * L'instance :
 * 
 * <pre>
 * new AppenderTestBase(Level.DEBUG, &quot;fr.urssaf.image.commons&quot;);
 * </pre>
 * 
 * ne renvoie pas avec {@link AppenderTestBase#getLoggingEvents()} les instance
 * {@link ILoggingEvent} de niveau <code>DEBUG</code> ou <code>INFO</code>
 * 
 */
@SuppressWarnings("PMD.MethodNamingConventions")
public class LogTest {

   private static LeService leService;

   @SuppressWarnings("PMD.LoggerIsNotStaticFinal")
   private Logger rootLogger;

   private AppenderTestBase appenderTest;

   @BeforeClass
   public static void beforeClass() {

      leService = new LeService();
   }

   @Before
   public void before() {

      // on récupère le root logger
      rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

   }

   @After
   public void after() {

      // on détache l'instance Appender du root logger à chaque fin de tests
      // ceci pour éviter de mémoriser à chaque test unitaire un Appender
      // nouveau
      rootLogger.detachAppender(appenderTest);

   }

   private void assertLog(ILoggingEvent actual, Level expectedLevel,
         String expectedMessage) {

      Assert.assertEquals("le niveau du log est inattendu", expectedLevel,
            actual.getLevel());
      Assert.assertEquals("le message du log est inattendu", expectedMessage,
            actual.getFormattedMessage());
   }

   @Test
   public void lamethode_warn() {

      // instanciation d'un Appender
      appenderTest = new AppenderTestBase(Level.WARN, "fr.urssaf.image.commons");
      // l'instance est attachée au root logger
      rootLogger.addAppender(appenderTest);

      // on test la méthode où doit se trouver des logs
      leService.laMethode();

      // on récupère la liste des logs
      List<ILoggingEvent> loggingEvents = appenderTest.getLoggingEvents();

      // on teste le nombre de logs retrouvés
      Assert.assertEquals("le nombre de logs est inattendu", 2, loggingEvents
            .size());

      // on teste pour chacun son message et son niveau

      assertLog(loggingEvents.get(0), Level.WARN, "Une trace de niveau WARN");
      assertLog(loggingEvents.get(1), Level.ERROR, "Une trace de niveau ERROR");

   }

   @Test
   public void lamethode_debug() {

      appenderTest = new AppenderTestBase(Level.DEBUG,
            "fr.urssaf.image.commons");
      rootLogger.addAppender(appenderTest);

      leService.laMethode();

      List<ILoggingEvent> loggingEvents = appenderTest.getLoggingEvents();

      Assert.assertEquals("le nombre de logs est inattendu", 4, loggingEvents
            .size());

      assertLog(loggingEvents.get(0), Level.DEBUG, "Une trace de niveau DEBUG");
      assertLog(loggingEvents.get(1), Level.INFO, "Une trace de niveau INFO");
      assertLog(loggingEvents.get(2), Level.WARN, "Une trace de niveau WARN");
      assertLog(loggingEvents.get(3), Level.ERROR, "Une trace de niveau ERROR");

   }

}
