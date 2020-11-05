package fr.urssaf.image.rsmed;

import fr.urssaf.image.rsmed.bean.PropertiesBean;
import fr.urssaf.image.rsmed.exception.FunctionalException;
import fr.urssaf.image.rsmed.job.TasksLauncher;
import fr.urssaf.image.rsmed.job.service.impl.XmlReaderServiceImpl;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl.BusinessFaultMessage;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl.TechnicalFaultMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.NoSuchElementException;

@SpringBootApplication
@EnableConfigurationProperties(PropertiesBean.class)
@ComponentScan("fr.urssaf.image.rsmed.job")
@ComponentScan("fr.urssaf.image.rsmed.bean")
public class RsmedApplication {

    private static ApplicationContext applicationContext;
    private static Logger LOGGER = LoggerFactory.getLogger(XmlReaderServiceImpl.class);

    @Autowired
    private static TasksLauncher tasksLauncher;


    public static void main(String[] args) {

        applicationContext = SpringApplication.run(RsmedApplication.class, args);
        tasksLauncher = applicationContext.getBean(TasksLauncher.class);


        try {
            LOGGER.debug("***********************************************************");
            LOGGER.debug("***************** Lancement du script *********************");
            LOGGER.debug("***********************************************************");

            tasksLauncher.launch();

        } catch (FunctionalException exception) {
            LOGGER.error("Une erreur fonctionnelle est survenue: ", exception);
            throw new RuntimeException("Fichier xml en entrée introuble", exception);

        } catch (NoSuchElementException | IOException exception) {
            LOGGER.error("Une erreur de lecture de fichier est survenue: ", exception);
            throw new RuntimeException("Fichier xml en entrée introuble", exception);
        } catch (XMLStreamException exception) {
            LOGGER.error("Une erreur est survenue lors du traitement du fichier xml: ", exception);
            throw new RuntimeException("Une erreur est survenue lors du traitement du fichier xml", exception);
        } catch (TechnicalFaultMessage | BusinessFaultMessage exception) {
            LOGGER.error("Une erreur est survenue au moment de l'appel des WS rei: ", exception);
            throw new RuntimeException("Une erreur est survenue au moment de l'appel des WS rei", exception);
        }

        LOGGER.debug("***********************************************************");
        LOGGER.debug("***************** Fin du script ***************************");
        LOGGER.debug("***********************************************************");
    }
}
