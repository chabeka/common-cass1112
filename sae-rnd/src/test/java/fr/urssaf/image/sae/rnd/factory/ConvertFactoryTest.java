package fr.urssaf.image.sae.rnd.factory;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.rnd.exception.RndRecuperationException;
import fr.urssaf.image.sae.rnd.factory.ConvertFactory;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTypeDocument;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-rnd-test.xml" })
public class ConvertFactoryTest {

   @Autowired
   private CassandraServerBean server;

   @After
   public void after() throws Exception {
      server.resetData();
   }

   // @Ignore
   @Test
   public void testWsToDocumentsType() throws RndRecuperationException {

      RNDTypeDocument rndTypDoc1 = new RNDTypeDocument();

      rndTypDoc1.set_dureeArchivage(4);
      rndTypDoc1.set_etat(true);
      rndTypDoc1.set_label("label 1");
      rndTypDoc1.set_reference("1.2.1.1.1");

      RNDTypeDocument rndTypDoc2 = new RNDTypeDocument();

      rndTypDoc2.set_dureeArchivage(5);
      rndTypDoc2.set_etat(false);
      rndTypDoc2.set_label("label 2");
      rndTypDoc2.set_reference("3.4.1.1.1");

      RNDTypeDocument rndTypDoc3 = new RNDTypeDocument();

      rndTypDoc3.set_etat(false);
      rndTypDoc3.set_label("label 2");
      rndTypDoc3.set_reference("5.A.2.2.2");

      RNDTypeDocument[] listeRndTypeDocs = { rndTypDoc1, rndTypDoc2, rndTypDoc3 };

      ConvertFactory convertFact = new ConvertFactory();

      List<TypeDocument> listeTypeDocs = convertFact
            .wsToDocumentsType(listeRndTypeDocs);

      assertNotNull("La liste ne doit pas être nulle", listeTypeDocs);

      for (TypeDocument typeDocument : listeTypeDocs) {
         String codeRnd = typeDocument.getCode();
         String fonction = typeDocument.getCodeFonction();
         String activite = typeDocument.getCodeActivite();
         int duree = typeDocument.getDureeConservation();
         TypeCode type = typeDocument.getType();
         if ("1.2.1.1.1".equals(codeRnd)) {
            if (!fonction.equals("1")) {
               Assert.fail("La fonction doit être égale à 1");
            }
            if (!activite.equals("2")) {
               Assert.fail("La fonction doit être égale à 2");
            }
            if (duree != 1460) {
               Assert.fail("La durée doit être égale à 1460");
            }
            if (!type.equals(TypeCode.ARCHIVABLE_AED)) {
               Assert.fail("Le type doit être égal à NON_ARCHIVABLE_AED");
            }
         } else if ("3.4.1.1.1".equals(codeRnd)) {
            if (!fonction.equals("3")) {
               Assert.fail("La fonction doit être égale à 3");
            }
            if (!activite.equals("4")) {
               Assert.fail("La fonction doit être égale à 3");
            }
            if (duree != 1825) {
               Assert.fail("La durée doit être égale à 1825");
            }
            if (!type.equals(TypeCode.ARCHIVABLE_AED)) {
               Assert.fail("Le type doit être égal à NON_ARCHIVABLE_AED");
            }

         } else if ("5.A.2.2.2".equals(codeRnd)) {
            if (!fonction.equals("5")) {
               Assert.fail("La fonction doit être égale à 1");
            }
            if (!StringUtils.isBlank(activite)) {
               Assert.fail("L'activité ne doit pas être renseignée");
            }
            if (duree != 1095) {
               Assert.fail("La durée doit être égale à 1095");
            }
            if (!type.equals(TypeCode.NON_ARCHIVABLE_AED)) {
               Assert.fail("Le type doit être égal à NON_ARCHIVABLE_AED");
            }
         } else {
            Assert.fail("Code RND incorrect");
         }
      }
   }
}
