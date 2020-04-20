package fr.urssaf.image.sae.webservices.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.WebApplicationContextUtils;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.storage.dfce.bo.DocumentsTypeList;
import fr.urssaf.image.sae.storage.dfce.bo.IndexCompositeComponent;

/**
 * Déclenche le thread permettant de charger la liste des types de documents au démarrage de dfce
 */
public class SaeServletContextListener implements
                                       ServletContextListener {

  @Autowired
  @Qualifier("dfceServices")
  private DFCEServices dfceServices;

  @Autowired
  private DocumentsTypeList typeList;

  @Autowired
  private IndexCompositeComponent indexCompositeComponent;

  @Override
  public void contextDestroyed(final ServletContextEvent arg0) {
    System.out.println("ServletContextListener destroyed");
  }

  @Override
  public void contextInitialized(final ServletContextEvent servletContextEvent) {
    System.out.println("ServletContextListener... "
        + " Chargement de la liste des types de documents et index composites supportés ");

    WebApplicationContextUtils.getRequiredWebApplicationContext(servletContextEvent.getServletContext())
                              .getAutowireCapableBeanFactory()
                              .autowireBean(this);

    final CacheSaeLoaderRunnable docTypeRunnable = new CacheSaeLoaderRunnable(dfceServices, typeList, indexCompositeComponent);
    final Thread threadDocumentTypeLoader = new Thread(docTypeRunnable);
    threadDocumentTypeLoader.start();
  }

}
