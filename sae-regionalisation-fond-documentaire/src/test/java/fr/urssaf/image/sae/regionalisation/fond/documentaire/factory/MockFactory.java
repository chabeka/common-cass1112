/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.factory;

import org.easymock.EasyMock;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.DocInfoDao;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.TermInfoRangeUuidDao;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.service.DocInfoService;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.support.CassandraSupport;

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
    * @return un mock de {@link TermInfoRangeUuidDao}
    */
   public static TermInfoRangeUuidDao createTermInfoRangeUuidDao() {
      return EasyMock.createMock(TermInfoRangeUuidDao.class);
   }

}
