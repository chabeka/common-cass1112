package fr.urssaf.image.sae.anais.framework.service.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import recouv.cirti.anais.api.source.AnaisExceptionAuthFailure;
import fr.urssaf.image.sae.anais.framework.component.ConnectionFactory;
import fr.urssaf.image.sae.anais.framework.component.DataSource;
import fr.urssaf.image.sae.anais.framework.service.exception.AucunDroitException;
import fr.urssaf.image.sae.anais.framework.service.exception.SaeAnaisApiException;
import fr.urssaf.image.sae.anais.framework.util.CTD;
import fr.urssaf.image.sae.anais.framework.util.InitFactory;
import fr.urssaf.image.sae.anais.framework.util.TokenAssert;
import fr.urssaf.image.sae.vi.exception.VIException;

@SuppressWarnings("PMD")
public class AuthentificationDAOTest {

   private static final Logger LOG = LoggerFactory
         .getLogger(AuthentificationDAOTest.class);

   private static ConnectionFactory factory;

   private static CTD ctd;

   @BeforeClass
   public static void initClass() {

      DataSource dataSource = InitFactory.initDataSource();
      factory = new ConnectionFactory(dataSource);

      ctd = InitFactory.initCTD("ctd_rights");
   }

   private AuthentificationDAO dao;

   @Before
   public void initDAO() {
      dao = new AuthentificationDAO(factory);
   }

   @Test
   public void authSucess() throws IOException, VIException, AucunDroitException {

      String xml = dao.createXMLToken(ctd.getUserLogin(),
            ctd.getUserPassword(), ctd.getCodeir(), ctd.getCodeorg());
      
      LOG.debug(xml);
      TokenAssert.assertCTD_rights(xml);

   }

   @Test
   public void authFailure() throws VIException, AucunDroitException {

      try {
         dao.createXMLToken(ctd.getUserLogin(), "inconnu", ctd.getCodeir(), ctd
               .getCodeorg());
         fail("le test ne doit pas passer");
      } catch (SaeAnaisApiException e) {
         assertEquals("le login est incorrect",
               AnaisExceptionAuthFailure.class, e.getCause().getClass());
      }

   }

}
