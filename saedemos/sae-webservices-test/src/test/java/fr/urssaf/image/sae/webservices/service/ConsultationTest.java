package fr.urssaf.image.sae.webservices.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ConsultationResponseType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ListeMetadonneeType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.MetadonneeType;

/**
 * Tests de l'opération "consultation" pour lesquels on attend une réponse
 * correcte (pas de SoapFault)<br>
 * <br>
 * Il faut penser à modifier la variable privée UUID_EXISTANT, après avoir
 * archivé un document grâce au TU ConsultationUtilsTest.prepareData()
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-webservices.xml" })
public class ConsultationTest {

   /**
    * TODO : remplacer ici l'UUID par un UUID existant
    * 
    * Pour archiver un document, utiliser le TU
    * ConsultationUtilsTest.prepareData()
    */
   private static final String UUID_EXISTANT = "2FFE2FF0-28E3-4DC6-AA47-8B1331F5BCFC";

   private static final String[] DEFAULT_META = new String[] { "Titre",
         "DateCreation", "DateReception", "CodeOrganismeProprietaire",
         "CodeOrganismeGestionnaire", "CodeRND", "Hash", "NomFichier",
         "FormatFichier", "TailleFichier", "ContratDeService", "DateArchivage" };

   private static final String[] WANTED_META = new String[] { "TypeHash",
         "NbPages", "ReferenceDocumentaire" };

   /**
    * map contenant les metadatas
    */
   private Map<String, Object> expectedMetadatas;

   @Autowired
   private ConsultationService consultationService;

   /**
    * Methodes d'initialisation des metadatas
    */
   @Before
   public final void init() {
      expectedMetadatas = ConsultationUtilsTest.getExpectedMetadatas();
   }

   /**
    * Test en succes avec uid not null et metadata null
    * 
    * @throws RemoteException
    *            remoteException
    */
   @Test
   @Ignore
   public final void consultation_success_ListMetaNull() throws RemoteException {

      ConsultationResponseType responseType = consultationService
            .consultation(UUID_EXISTANT);
      ListeMetadonneeType listeMD = responseType.getMetadonnees();

      assertNotNull("L'objet contenant les metadonnees ne doit pas etre null",
            listeMD);

      MetadonneeType[] tabMD = listeMD.getMetadonnee();

      assertNotNull("la liste des metadonnées ne doit pas être null", tabMD);

      Collection<String> colResMD = new ArrayList<String>();
      for (MetadonneeType metaData : tabMD) {
         colResMD.add(metaData.getCode().getMetadonneeCodeType());
      }

      Collection<String> colDefaultMD = Arrays.asList(DEFAULT_META);

      assertTrue(
            "Toutes les metadatas contenues dans le résultat doivent appartenir au résultat par défaut",
            colResMD.containsAll(colDefaultMD));

      assertTrue(
            "Il ne doit y avoir que des metadatas rendues par défaut dans le résultat",
            colDefaultMD.containsAll(colResMD));

      String code;
      for (MetadonneeType metaData : tabMD) {
         code = metaData.getCode().getMetadonneeCodeType();
         assertEquals("Vérification de la valeur du champ " + code,
               expectedMetadatas.get(code), metaData.getValeur()
                     .getMetadonneeValeurType());
      }

      checkContenu(responseType.getObjetNumerique()
            .getObjetNumeriqueConsultationTypeChoice_type0().getContenu());

   }

   /**
    * Test en succes avec uid not null et metadata vide
    * 
    * @throws RemoteException
    */
   @Test
   @Ignore
   public final void consultation_success_ListMetaVide() throws RemoteException {

      ConsultationResponseType responseType = consultationService.consultation(
            UUID_EXISTANT, new ArrayList<String>());
      ListeMetadonneeType listeMD = responseType.getMetadonnees();

      assertNotNull("L'objet contenant les metadonnees ne doit pas etre null",
            listeMD);

      MetadonneeType[] tabMD = listeMD.getMetadonnee();

      assertNotNull("la liste des metadonnées ne doit pas être null", tabMD);

      Collection<String> colResMD = new ArrayList<String>();
      for (MetadonneeType metaData : tabMD) {
         colResMD.add(metaData.getCode().getMetadonneeCodeType());
      }

      Collection<String> colDefaultMD = Arrays.asList(DEFAULT_META);

      assertTrue(
            "Toutes les metadatas contenues dans le résultat doivent appartenir au résultat par défaut",
            colResMD.containsAll(colDefaultMD));

      assertTrue(
            "Il ne doit y avoir que des metadatas rendues par défaut dans le résultat",
            colDefaultMD.containsAll(colResMD));

      String code;
      for (MetadonneeType metaData : tabMD) {
         code = metaData.getCode().getMetadonneeCodeType();
         assertEquals("Vérification de la valeur du champ " + code,
               expectedMetadatas.get(code), metaData.getValeur()
                     .getMetadonneeValeurType());
      }

      checkContenu(responseType.getObjetNumerique()
            .getObjetNumeriqueConsultationTypeChoice_type0().getContenu());

   }

   /**
    * Test en succes avec uid not null avec metadata
    * 
    * @throws RemoteException
    *            remoteException
    */
   @Test
   @Ignore
   public final void consultation_success_ListMetaRemplie()
         throws RemoteException {

      List<String> listMdString = Arrays.asList(WANTED_META);

      ConsultationResponseType responseType = consultationService.consultation(
            UUID_EXISTANT, listMdString);
      ListeMetadonneeType listeMD = responseType.getMetadonnees();

      assertNotNull("L'objet contenant les metadonnees ne doit pas etre null",
            listeMD);

      MetadonneeType[] tabMD = listeMD.getMetadonnee();

      assertNotNull("la liste des metadonnées ne doit pas être null", tabMD);

      Collection<String> colResMD = new ArrayList<String>();
      for (MetadonneeType metaData : tabMD) {
         colResMD.add(metaData.getCode().getMetadonneeCodeType());
      }

      Collection<String> colWantedMD = Arrays.asList(WANTED_META);

      assertTrue(
            "Toutes les metadatas contenues dans le résultat doivent appartenir au résultat voulu",
            colResMD.containsAll(colWantedMD));

      assertTrue(
            "Il ne doit y avoir que des metadatas désirées dans le résultat",
            colWantedMD.containsAll(colResMD));

      String code;
      for (MetadonneeType metaData : tabMD) {
         code = metaData.getCode().getMetadonneeCodeType();
         assertEquals("Vérification de la valeur du champ " + code,
               expectedMetadatas.get(code), metaData.getValeur()
                     .getMetadonneeValeurType());
      }

      checkContenu(responseType.getObjetNumerique()
            .getObjetNumeriqueConsultationTypeChoice_type0().getContenu());

   }

   private void checkContenu(DataHandler contenu) {

      // Non null
      assertNotNull("Le contenu ne devrait pas être null", contenu);

      // Taille
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
      try {
         contenu.writeTo(byteStream);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
      byte[] tabOctets = byteStream.toByteArray();
      assertEquals("La taille du contenu est incorrect", 73791,
            tabOctets.length);

      // Type MIME
      // Non setté dans la consultation non MTOM
      // System.out.println(contenu.getContentType()) donne
      // "application/octet-string"

   }

}
