/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.factory;

import org.easymock.EasyMock;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.DocInfoDao;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.DocumentDao;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.service.DocInfoService;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.service.DocumentService;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.support.CassandraSupport;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.support.ServiceProviderSupport;

/**
 * Classe permettant de réaliser les mocks
 * 
 */
public final class MockFactory {

   /**
    * Constructeur
    */
   private MockFactory() {
   }

   /**
    * @return un mock de {@link DocInfoDao}
    */
   public static DocInfoDao createDocInfoDao() {
      return EasyMock.createMock(DocInfoDao.class);
   }

   /**
    * @return un mock de {@link CassandraSupport}
    */
   public static CassandraSupport createCassandraSupport() {
      return EasyMock.createMock(CassandraSupport.class);
   }

   /**
    * @return un mock de {@link DocInfoService}
    */
   public static DocInfoService createDocInfoService() {
      return EasyMock.createMock(DocInfoService.class);
   }

   /**
    * @return un mock de {@link DocumentDao}
    */
   public static DocumentDao createDocumentDao() {
      return EasyMock.createMock(DocumentDao.class);
   }

   /**
    * @return un mock de {@link DocumentService}
    */
   public static DocumentService createDocumentService() {
      return EasyMock.createMock(DocumentService.class);
   }

   /**
    * @return un mock de {@link ServiceProviderSupport}
    */
   public static ServiceProviderSupport createProviderSupport() {
      return EasyMock.createMock(ServiceProviderSupport.class);
   }

}
