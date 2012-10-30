package fr.urssaf.image.sae.regionalisation.mock;

import org.easymock.EasyMock;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.regionalisation.dao.SaeDocumentDao;
import fr.urssaf.image.sae.regionalisation.dao.TraceDao;
import fr.urssaf.image.sae.regionalisation.support.ServiceProviderSupport;

/**
 * Classe de mock
 * 
 * 
 */
@Component
public class DaoMock {

   /**
    * 
    * @return instance de {@link SaeDocumentDao}
    */
   public final SaeDocumentDao createSaeDocumentDao() {

      SaeDocumentDao service = EasyMock.createMock(SaeDocumentDao.class);

      return service;
   }

   /**
    * 
    * @return instance de {@link TraceDao}
    */
   public final TraceDao createTraceDao() {

      TraceDao service = EasyMock.createMock(TraceDao.class);

      return service;
   }

   /**
    * 
    * @return instance de {@link ServiceProviderSupport}
    */
   public final ServiceProviderSupport createServiceProviderSupport() {

      ServiceProviderSupport service = EasyMock
            .createMock(ServiceProviderSupport.class);

      return service;
   }

}
