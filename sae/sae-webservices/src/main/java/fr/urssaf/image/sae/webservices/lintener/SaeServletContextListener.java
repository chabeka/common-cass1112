package fr.urssaf.image.sae.webservices.lintener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.WebApplicationContextUtils;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.storage.dfce.bo.DocumentsTypeList;

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

   @Override
   public void contextDestroyed(final ServletContextEvent arg0) {
      System.out.println("ServletContextListener destroyed");
   }

   @Override
   public void contextInitialized(final ServletContextEvent servletContextEvent) {
      System.out.println("ServletContextListener... "
            + " Chargement de la liste des types de documents supportés");

      WebApplicationContextUtils.getRequiredWebApplicationContext(servletContextEvent.getServletContext())
      .getAutowireCapableBeanFactory().autowireBean(this);

      final DocumentTypeLoaderRunnable docTypeRunnable = new DocumentTypeLoaderRunnable(dfceServices, typeList);
      final Thread threadDocumentTypeLoader = new Thread(docTypeRunnable);
      threadDocumentTypeLoader.start();
   }

}
