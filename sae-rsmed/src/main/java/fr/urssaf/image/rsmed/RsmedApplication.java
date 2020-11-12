package fr.urssaf.image.rsmed;

import fr.urssaf.image.rsmed.bean.PropertiesBean;
import fr.urssaf.image.rsmed.job.TasksLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties(PropertiesBean.class)
@ComponentScan("fr.urssaf.image.rsmed.job")
@ComponentScan("fr.urssaf.image.rsmed.bean")
@ComponentScan("fr.urssaf.image.rsmed.service")
@ComponentScan("fr.urssaf.image.rsmed.utils")
public class RsmedApplication {

    private static ApplicationContext applicationContext;
    private static Logger LOGGER = LoggerFactory.getLogger(RsmedApplication.class);

    private static TasksLauncher tasksLauncher;


    public static void main(String[] args) {

        applicationContext = SpringApplication.run(RsmedApplication.class, args);
        tasksLauncher = applicationContext.getBean(TasksLauncher.class);


        LOGGER.debug("***********************************************************");
        LOGGER.debug("***************** Lancement du script *********************");
        LOGGER.debug("***********************************************************");

        tasksLauncher.launch();

        LOGGER.debug("***********************************************************");
        LOGGER.debug("***************** Fin du script ***************************");
        LOGGER.debug("***********************************************************");
    }
}
