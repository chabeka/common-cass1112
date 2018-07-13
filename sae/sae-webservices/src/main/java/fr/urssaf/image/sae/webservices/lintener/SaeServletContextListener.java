package fr.urssaf.image.sae.webservices.lintener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.WebApplicationContextUtils;

import fr.urssaf.image.sae.storage.dfce.bo.DocumentsTypeList;
import fr.urssaf.image.sae.storage.dfce.manager.DFCEServicesManager;

/**
 * Déclenche le thread permettant de charger la liste des types de documents au démarrage de dfce
 */
public class SaeServletContextListener implements
      ServletContextListener {

    @Autowired
    @Qualifier("dfceServicesManager")
    private DFCEServicesManager dfceServicesManager;
    
    @Autowired
    private DocumentsTypeList typeList;

   @Override
   public void contextDestroyed(ServletContextEvent arg0) {
      System.out.println("ServletContextListener destroyed");
   }

   @Override
   public void contextInitialized(ServletContextEvent servletContextEvent) {
      System.out.println("ServletContextListener... "
            + " Chargement de la liste des types de documents supportés");
      
      WebApplicationContextUtils.getRequiredWebApplicationContext(servletContextEvent.getServletContext())
         .getAutowireCapableBeanFactory().autowireBean(this);
      
      DocumentTypeLoaderRunnable docTypeRunnable = new DocumentTypeLoaderRunnable(dfceServicesManager, typeList);
      Thread threadDocumentTypeLoader = new Thread(docTypeRunnable);
      threadDocumentTypeLoader.start();
   }
  
}