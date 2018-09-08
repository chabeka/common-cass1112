package fr.urssaf.image.sae.webservices.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.ByteArrayDataSource;
import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.ConsultationAffichable;
import fr.cirtil.www.saeservice.ConsultationAffichableRequestType;
import fr.cirtil.www.saeservice.ConsultationAffichableResponse;
import fr.cirtil.www.saeservice.ListeMetadonneeCodeType;
import fr.cirtil.www.saeservice.MetadonneeCodeType;
import fr.cirtil.www.saeservice.MetadonneeType;
import fr.cirtil.www.saeservice.UuidType;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.consultation.model.ConsultParams;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.documentExistant.SAEDocumentExistantService;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationAffichableParametrageException;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.storage.dfce.utils.HashUtils;
import fr.urssaf.image.sae.webservices.exception.ConsultationAxisFault;

/**
 * Tests unitaires de la classe {@link WSConsultationServiceImpl}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
@SuppressWarnings({ "PMD.MethodNamingConventions",
      "PMD.AvoidDuplicateLiterals", "PMD.TooManyMethods" })
public class WSConsultationServiceImplTest {

   @Autowired
   private WSConsultationServiceImpl consultService;

   @Autowired
   @Qualifier("documentService")
   private SAEDocumentService saeService;

   @Autowired
   private SAEDocumentExistantService documentService;

   private static final String FORMAT_FICHIER = "FormatFichier";

   private static final String TYPE_MIME_PDF = "application/pdf";

   @Test
   public void ajouteSiBesoinMetadonneeFormatFichier_success_ListeNull() {

      boolean ajout = consultService
            .ajouteSiBesoinMetadonneeFormatFichier(null);

      assertEquals("La métadonnée FormatFichier n'aurait pas du être ajoutée",
            false, ajout);

   }

   @Test
   public void ajouteSiBesoinMetadonneeFormatFichier_success_ListeVide() {

      List<String> metas = new ArrayList<String>();

      boolean ajout = consultService
            .ajouteSiBesoinMetadonneeFormatFichier(metas);

      assertEquals("La métadonnée FormatFichier n'aurait pas du être ajoutée",
            false, ajout);

   }

   @Test
   public void ajouteSiBesoinMetadonneeFormatFichier_success_ListeRemplieSansFormatFichier() {

      List<String> metas = new ArrayList<String>();
      metas.add("Toto");
      metas.add("Tata");

      boolean ajout = consultService
            .ajouteSiBesoinMetadonneeFormatFichier(metas);

      assertEquals("La métadonnée FormatFichier aurait du être ajoutée", true,
            ajout);

      assertEquals("La métadonnée FormatFichier aurait du être ajoutée", 3,
            metas.size());

      int idx = metas.indexOf(FORMAT_FICHIER);
      assertNotSame("La métadonnée FormatFichier aurait du être ajoutée", idx,
            -1);

   }

   @Test
   public void ajouteSiBesoinMetadonneeFormatFichier_success_ListeRemplieAvecFormatFichier() {

      List<String> metas = new ArrayList<String>();
      metas.add("Toto");
      metas.add(FORMAT_FICHIER);
      metas.add("Tata");

      boolean ajout = consultService
            .ajouteSiBesoinMetadonneeFormatFichier(metas);

      assertEquals("La métadonnée FormatFichier n'aurait pas du être ajoutée",
            false, ajout);
      assertEquals("La métadonnée FormatFichier n'aurait pas du être ajoutée",
            3, metas.size());

   }

   @Test(expected = IllegalArgumentException.class)
   public void convertitPronomEnTypeMime_success_TypePronomNull()
         throws ConsultationAxisFault {

      String typeMime = consultService.convertitPronomEnTypeMime(null);

      // assertEquals("Le type MIME est incorrect", TYPE_MIME_DEFAUT, typeMime);

   }

   @Test(expected = IllegalArgumentException.class)
   public void convertitPronomEnTypeMime_success_TypePronomVide()
         throws ConsultationAxisFault {

      String typeMime = consultService.convertitPronomEnTypeMime("");

      // assertEquals("Le type MIME est incorrect", TYPE_MIME_DEFAUT, typeMime);

   }

   @Test
   public void convertitPronomEnTypeMime_success_TypePronomPdfSlashA()
         throws ConsultationAxisFault {

      String typeMime = consultService.convertitPronomEnTypeMime("fmt/354");

      assertEquals("Le type MIME est incorrect", TYPE_MIME_PDF, typeMime);

   }

   @Test(expected = ConsultationAxisFault.class)
   public void convertitPronomEnTypeMime_success_TypePronomInconnu()
         throws ConsultationAxisFault {

      String typeMime = consultService
            .convertitPronomEnTypeMime("gloubi_boulga");

      // assertEquals("Le type MIME est incorrect", TYPE_MIME_DEFAUT, typeMime);

   }

   @Test
   public void typeMimeDepuisFormatFichier_success_SansSuppr()
         throws ConsultationAxisFault {

      List<UntypedMetadata> listeMetas = new ArrayList<UntypedMetadata>();
      listeMetas.add(new UntypedMetadata(FORMAT_FICHIER, "fmt/354"));

      boolean supprMetaFmtFic = false;

      String typeMime = consultService.typeMimeDepuisFormatFichier(listeMetas,
            supprMetaFmtFic);

      assertEquals("Le type MIME est incorrect", TYPE_MIME_PDF, typeMime);
      assertEquals(
            "La métadonnée FormatFichier n'aurait pas du être supprimée", 1,
            listeMetas.size());

   }

   @Test
   public void typeMimeDepuisFormatFichier_success_AvecSuppr()
         throws ConsultationAxisFault {

      List<UntypedMetadata> listeMetas = new ArrayList<UntypedMetadata>();
      listeMetas.add(new UntypedMetadata(FORMAT_FICHIER, "fmt/354"));

      boolean supprMetaFmtFic = true;

      String typeMime = consultService.typeMimeDepuisFormatFichier(listeMetas,
            supprMetaFmtFic);

      assertEquals("Le type MIME est incorrect", TYPE_MIME_PDF, typeMime);
      assertEquals("La métadonnée FormatFichier aurait du être supprimée", 0,
            listeMetas.size());

   }

   @Test
   public void typeMimeDepuisFormatFichier_failure_ListeMetasNull() {

      List<UntypedMetadata> listeMetas = null;

      boolean supprMetaFmtFic = true;

      try {

         consultService
               .typeMimeDepuisFormatFichier(listeMetas, supprMetaFmtFic);

         fail("Une exception ConsultationAxisFault aurait dû être levée");

      } catch (ConsultationAxisFault fault) {

         checkSoapFaultErreurInterne(fault);

      }

   }

   private void checkSoapFaultErreurInterne(ConsultationAxisFault fault) {

      assertEquals("Le message de la SoapFault est incorrect",
            "Une erreur interne à l'application est survenue.",
            fault.getMessage());

      assertEquals("La partie locale du code de la SoapFault est incorrect",
            "ErreurInterne", fault.getFaultCode().getLocalPart());

      assertEquals("Le préfixe du code de la SoapFault est incorrect", "sae",
            fault.getFaultCode().getPrefix());

   }

   @Test
   public void typeMimeDepuisFormatFichier_failure_ListeMetasVide() {

      List<UntypedMetadata> listeMetas = new ArrayList<UntypedMetadata>();

      boolean supprMetaFmtFic = true;

      try {

         consultService
               .typeMimeDepuisFormatFichier(listeMetas, supprMetaFmtFic);

         fail("Une exception ConsultationAxisFault aurait dû être levée");

      } catch (ConsultationAxisFault fault) {

         checkSoapFaultErreurInterne(fault);

      }

   }

   @Test
   public void typeMimeDepuisFormatFichier_failure_ListeMetasSansFormatFichier() {

      List<UntypedMetadata> listeMetas = new ArrayList<UntypedMetadata>();
      listeMetas.add(new UntypedMetadata("codeLong", "valeur"));

      boolean supprMetaFmtFic = true;

      try {

         consultService
               .typeMimeDepuisFormatFichier(listeMetas, supprMetaFmtFic);

         fail("Une exception ConsultationAxisFault aurait dû être levée");

      } catch (ConsultationAxisFault fault) {

         checkSoapFaultErreurInterne(fault);

      }

   }

   @Test
   public void testHash() throws IOException, NoSuchAlgorithmException {
      ClassPathResource resource = new ClassPathResource(
            "storage/attestation.pdf");
      File file = resource.getFile();

      byte[] content = FileUtils.readFileToByteArray(file);
      InputStream stream = new DataHandler(new FileDataSource(file))
            .getInputStream();

      String contentHash = HashUtils.hashHex(content, "SHA-1");
      System.out.println(contentHash);
      String streamHash = HashUtils.hashHex(stream, "SHA-1");
      System.out.println(streamHash);
      stream.close();
      System.out.println(streamHash);

   }

   @Test
   public void consultationAffichable_doc_inexistant()
         throws ConsultationAxisFault {

      ConsultationAffichable request = new ConsultationAffichable();
      request
            .setConsultationAffichable(new ConsultationAffichableRequestType());
      request.getConsultationAffichable().setIdArchive(new UuidType());
      request.getConsultationAffichable().getIdArchive()
            .setUuidType("00000000-0000-0000-0000-000000000000");

      try {
         consultService.consultationAffichable(request);
         fail("C'est l'exception ConsultationAxisFault qui est attendue");
      } catch (ConsultationAxisFault ex) {
         assertEquals(
               "Le message d'erreur n'est pas celui attendu",
               "Il n'existe aucun document pour l'identifiant d'archivage '00000000-0000-0000-0000-000000000000'",
               ex.getMessage());
         assertEquals("La partie local du code de l'erreur n'est pas le bon",
               "ArchiveNonTrouvee", ex.getFaultCode().getLocalPart());
      }

      EasyMock.reset(saeService);
   }

   // Test fonctionnant en local seulement

   // @Test
   // public void consultationGNTGNS_doc_inexistant() throws SearchingServiceEx,
   // ConnectionServiceEx, SAEConsultationServiceException, RemoteException,
   // UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx,
   // SAEConsultationAffichableParametrageException {
   //
   // ConsultationGNTGNS request = new ConsultationGNTGNS();
   // request.setConsultationGNTGNS(new ConsultationGNTGNSRequestType());
   // request.getConsultationGNTGNS().setIdArchive(new UuidType());
   // request.getConsultationGNTGNS().getIdArchive()
   // .setUuidType("00000000-0000-0000-0000-000000000000");
   //
   // try {
   // consultService.consultationGNTGNS(request);
   // fail("C'est l'exception ConsultationAxisFault qui est attendue");
   // } catch (ConsultationAxisFault ex) {
   // assertEquals(
   // "Le message d'erreur n'est pas celui attendu",
   // "Il n'existe aucun document pour l'identifiant d'archivage '00000000-0000-0000-0000-000000000000'",
   // ex.getMessage());
   // assertEquals("La partie local du code de l'erreur n'est pas le bon",
   // "ArchiveNonTrouvee", ex.getFaultCode().getLocalPart());
   // }
   //
   // EasyMock.reset(saeService);
   // }

   @Test
   public void consultationAffichable_metadonnee_inexistante()
         throws ConsultationAxisFault, SAEConsultationServiceException,
         UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx,
         SAEConsultationAffichableParametrageException {

      ConsultationAffichable request = new ConsultationAffichable();
      request
            .setConsultationAffichable(new ConsultationAffichableRequestType());
      request.getConsultationAffichable().setIdArchive(new UuidType());
      request.getConsultationAffichable().getIdArchive()
            .setUuidType("00000000-0000-0000-0000-000000000000");
      request.getConsultationAffichable().setMetadonnees(
            new ListeMetadonneeCodeType());
      MetadonneeCodeType meta = new MetadonneeCodeType();
      meta.setMetadonneeCodeType("meta-inexistante");
      request.getConsultationAffichable().getMetadonnees()
            .addMetadonneeCode(meta);

      EasyMock.expect(
            saeService.consultationAffichable((ConsultParams) EasyMock
                  .anyObject())).andThrow(
            new UnknownDesiredMetadataEx(
                  "test-unitaire : metadonnee inexistante"));

      EasyMock.replay(saeService);

      try {
         consultService.consultationAffichable(request);
         fail("C'est l'exception ConsultationAxisFault qui est attendue");
      } catch (ConsultationAxisFault ex) {
         assertEquals("Le message d'erreur n'est pas celui attendu",
               "test-unitaire : metadonnee inexistante", ex.getMessage());
         assertEquals("La cause de l'exception n'est pas la bonne",
               UnknownDesiredMetadataEx.class.getName(), ex.getCause()
                     .getClass().getName());
         assertEquals("La partie local du code de l'erreur n'est pas le bon",
               "ConsultationMetadonneesInexistante", ex.getFaultCode()
                     .getLocalPart());
      }

      EasyMock.reset(saeService);
   }

   // Test fonctionnant en local seulement

   // @Test
   // public void consultationGNTGNS_metadonnee_inexistante()
   // throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
   // MetaDataUnauthorizedToConsultEx,
   // SAEConsultationAffichableParametrageException, SearchingServiceEx,
   // ConnectionServiceEx, RemoteException {
   //
   // ConsultationGNTGNS request = new ConsultationGNTGNS();
   // request.setConsultationGNTGNS(new ConsultationGNTGNSRequestType());
   // request.getConsultationGNTGNS().setIdArchive(new UuidType());
   // request.getConsultationGNTGNS().getIdArchive()
   // .setUuidType("00000000-0000-0000-0000-000000000000");
   // request.getConsultationGNTGNS().setMetadonnees(
   // new ListeMetadonneeCodeType());
   // MetadonneeCodeType meta = new MetadonneeCodeType();
   // meta.setMetadonneeCodeType("meta-inexistante");
   // request.getConsultationGNTGNS().getMetadonnees().addMetadonneeCode(meta);
   //
   // EasyMock.expect(
   // saeService.consultationAffichable((ConsultParams) EasyMock
   // .anyObject())).andThrow(
   // new UnknownDesiredMetadataEx(
   // "test-unitaire : metadonnee inexistante"));
   //
   // EasyMock.replay(saeService);
   //
   // try {
   // consultService.consultationGNTGNS(request);
   // fail("C'est l'exception ConsultationAxisFault qui est attendue");
   // } catch (ConsultationAxisFault ex) {
   // assertEquals("Le message d'erreur n'est pas celui attendu",
   // "test-unitaire : metadonnee inexistante", ex.getMessage());
   // assertEquals("La cause de l'exception n'est pas la bonne",
   // UnknownDesiredMetadataEx.class.getName(), ex.getCause()
   // .getClass().getName());
   // assertEquals("La partie local du code de l'erreur n'est pas le bon",
   // "ConsultationMetadonneesInexistante", ex.getFaultCode()
   // .getLocalPart());
   // }
   //
   // EasyMock.reset(saeService);
   // }

   @Test
   public void consultationAffichable_metadonnee_non_autorisee()
         throws ConsultationAxisFault, SAEConsultationServiceException,
         UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx,
         SAEConsultationAffichableParametrageException {

      ConsultationAffichable request = new ConsultationAffichable();
      request
            .setConsultationAffichable(new ConsultationAffichableRequestType());
      request.getConsultationAffichable().setIdArchive(new UuidType());
      request.getConsultationAffichable().getIdArchive()
            .setUuidType("00000000-0000-0000-0000-000000000000");
      request.getConsultationAffichable().setMetadonnees(
            new ListeMetadonneeCodeType());
      MetadonneeCodeType meta = new MetadonneeCodeType();
      meta.setMetadonneeCodeType("meta-non-autorisee");
      request.getConsultationAffichable().getMetadonnees()
            .addMetadonneeCode(meta);

      EasyMock.expect(
            saeService.consultationAffichable((ConsultParams) EasyMock
                  .anyObject())).andThrow(
            new MetaDataUnauthorizedToConsultEx(
                  "test-unitaire : metadonnee non autorisee"));

      EasyMock.replay(saeService);

      try {
         consultService.consultationAffichable(request);
         fail("C'est l'exception ConsultationAxisFault qui est attendue");
      } catch (ConsultationAxisFault ex) {
         assertEquals("Le message d'erreur n'est pas celui attendu",
               "test-unitaire : metadonnee non autorisee", ex.getMessage());
         assertEquals("La cause de l'exception n'est pas la bonne",
               MetaDataUnauthorizedToConsultEx.class.getName(), ex.getCause()
                     .getClass().getName());
         assertEquals("La partie local du code de l'erreur n'est pas le bon",
               "ConsultationMetadonneesNonAutorisees", ex.getFaultCode()
                     .getLocalPart());
      }

      EasyMock.reset(saeService);
   }

   // Test fonctionnant en local seulement

   // @Test
   // public void consultationGNTGNS_metadonnee_non_autorisee()
   // throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
   // MetaDataUnauthorizedToConsultEx,
   // SAEConsultationAffichableParametrageException, SearchingServiceEx,
   // ConnectionServiceEx, RemoteException {
   //
   // ConsultationGNTGNS request = new ConsultationGNTGNS();
   // request.setConsultationGNTGNS(new ConsultationGNTGNSRequestType());
   // request.getConsultationGNTGNS().setIdArchive(new UuidType());
   // request.getConsultationGNTGNS().getIdArchive()
   // .setUuidType("00000000-0000-0000-0000-000000000000");
   // request.getConsultationGNTGNS().setMetadonnees(
   // new ListeMetadonneeCodeType());
   // MetadonneeCodeType meta = new MetadonneeCodeType();
   // meta.setMetadonneeCodeType("meta-non-autorisee");
   // request.getConsultationGNTGNS().getMetadonnees().addMetadonneeCode(meta);
   //
   // EasyMock.expect(
   // saeService.consultationAffichable((ConsultParams) EasyMock
   // .anyObject())).andThrow(
   // new MetaDataUnauthorizedToConsultEx(
   // "test-unitaire : metadonnee non autorisee"));
   //
   // EasyMock.replay(saeService);
   //
   // try {
   // consultService.consultationGNTGNS(request);
   // fail("C'est l'exception ConsultationAxisFault qui est attendue");
   // } catch (ConsultationAxisFault ex) {
   // assertEquals("Le message d'erreur n'est pas celui attendu",
   // "test-unitaire : metadonnee non autorisee", ex.getMessage());
   // assertEquals("La cause de l'exception n'est pas la bonne",
   // MetaDataUnauthorizedToConsultEx.class.getName(), ex.getCause()
   // .getClass().getName());
   // assertEquals("La partie local du code de l'erreur n'est pas le bon",
   // "ConsultationMetadonneesNonAutorisees", ex.getFaultCode()
   // .getLocalPart());
   // }
   //
   // EasyMock.reset(saeService);
   // }

   @Test
   public void consultationAffichable_erreur_parametrage()
         throws ConsultationAxisFault, SAEConsultationServiceException,
         UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx,
         SAEConsultationAffichableParametrageException {

      ConsultationAffichable request = new ConsultationAffichable();
      request
            .setConsultationAffichable(new ConsultationAffichableRequestType());
      request.getConsultationAffichable().setIdArchive(new UuidType());
      request.getConsultationAffichable().getIdArchive()
            .setUuidType("00000000-0000-0000-0000-000000000000");
      request.getConsultationAffichable().setMetadonnees(
            new ListeMetadonneeCodeType());
      MetadonneeCodeType meta = new MetadonneeCodeType();
      meta.setMetadonneeCodeType("FormatFichier");
      request.getConsultationAffichable().getMetadonnees()
            .addMetadonneeCode(meta);
      request.getConsultationAffichable().setNumeroPage(1);
      request.getConsultationAffichable().setNombrePages(0);

      EasyMock.expect(
            saeService.consultationAffichable((ConsultParams) EasyMock
                  .anyObject())).andThrow(
            new SAEConsultationAffichableParametrageException(
                  "test-unitaire : parametrage incorrect"));

      EasyMock.replay(saeService);

      try {
         consultService.consultationAffichable(request);
         fail("C'est l'exception ConsultationAxisFault qui est attendue");
      } catch (ConsultationAxisFault ex) {
         assertEquals("Le message d'erreur n'est pas celui attendu",
               "test-unitaire : parametrage incorrect", ex.getMessage());
         assertEquals("La cause de l'exception n'est pas la bonne",
               SAEConsultationAffichableParametrageException.class.getName(),
               ex.getCause().getClass().getName());
         assertEquals("La partie local du code de l'erreur n'est pas le bon",
               "ConsultationAffichableParametrageIncorrect", ex.getFaultCode()
                     .getLocalPart());
      }

      EasyMock.reset(saeService);
   }

   @Test
   public void consultationAffichable_exception_consultation()
         throws ConsultationAxisFault, SAEConsultationServiceException,
         UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx,
         SAEConsultationAffichableParametrageException {

      ConsultationAffichable request = new ConsultationAffichable();
      request
            .setConsultationAffichable(new ConsultationAffichableRequestType());
      request.getConsultationAffichable().setIdArchive(new UuidType());
      request.getConsultationAffichable().getIdArchive()
            .setUuidType("00000000-0000-0000-0000-000000000000");
      request.getConsultationAffichable().setMetadonnees(
            new ListeMetadonneeCodeType());
      MetadonneeCodeType meta = new MetadonneeCodeType();
      meta.setMetadonneeCodeType("FormatFichier");
      request.getConsultationAffichable().getMetadonnees()
            .addMetadonneeCode(meta);

      EasyMock.expect(
            saeService.consultationAffichable((ConsultParams) EasyMock
                  .anyObject())).andThrow(
            new SAEConsultationServiceException(new Exception(
                  "test-unitaire : exception de consultation")));

      EasyMock.replay(saeService);

      try {
         consultService.consultationAffichable(request);
         fail("C'est l'exception ConsultationAxisFault qui est attendue");
      } catch (ConsultationAxisFault ex) {
         assertEquals("Le message d'erreur n'est pas celui attendu",
               "Une erreur s'est produite lors de la consultation",
               ex.getMessage());
         assertEquals("La cause de l'exception n'est pas la bonne",
               SAEConsultationServiceException.class.getName(), ex.getCause()
                     .getClass().getName());
         assertEquals("La partie local du code de l'erreur n'est pas le bon",
               "ErreurInterneConsultation", ex.getFaultCode().getLocalPart());
      }

      EasyMock.reset(saeService);
   }

   // Test fonctionnant en local seulement

   // @Test
   // public void consultationGNTGNS_exception_consultation()
   // throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
   // MetaDataUnauthorizedToConsultEx,
   // SAEConsultationAffichableParametrageException, SearchingServiceEx,
   // ConnectionServiceEx, RemoteException {
   //
   // ConsultationGNTGNS request = new ConsultationGNTGNS();
   // request.setConsultationGNTGNS(new ConsultationGNTGNSRequestType());
   // request.getConsultationGNTGNS().setIdArchive(new UuidType());
   // request.getConsultationGNTGNS().getIdArchive()
   // .setUuidType("00000000-0000-0000-0000-000000000000");
   // request.getConsultationGNTGNS().setMetadonnees(
   // new ListeMetadonneeCodeType());
   // MetadonneeCodeType meta = new MetadonneeCodeType();
   // meta.setMetadonneeCodeType("FormatFichier");
   // request.getConsultationGNTGNS().getMetadonnees().addMetadonneeCode(meta);
   //
   // EasyMock.expect(
   // saeService.consultationAffichable((ConsultParams) EasyMock
   // .anyObject())).andThrow(
   // new SAEConsultationServiceException(new Exception(
   // "test-unitaire : exception de consultation")));
   //
   // EasyMock.replay(saeService);
   //
   // try {
   // consultService.consultationGNTGNS(request);
   // fail("C'est l'exception ConsultationAxisFault qui est attendue");
   // } catch (ConsultationAxisFault ex) {
   // assertEquals("Le message d'erreur n'est pas celui attendu",
   // "Une erreur s'est produite lors de la consultation",
   // ex.getMessage());
   // assertEquals("La cause de l'exception n'est pas la bonne",
   // SAEConsultationServiceException.class.getName(), ex.getCause()
   // .getClass().getName());
   // assertEquals("La partie local du code de l'erreur n'est pas le bon",
   // "ErreurInterneConsultation", ex.getFaultCode().getLocalPart());
   // }
   //
   // EasyMock.reset(saeService);
   // }

   @Test
   public void consultationAffichable_success()
         throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx,
         SAEConsultationAffichableParametrageException, IOException {

      ConsultationAffichable request = new ConsultationAffichable();
      request
            .setConsultationAffichable(new ConsultationAffichableRequestType());
      request.getConsultationAffichable().setIdArchive(new UuidType());
      request.getConsultationAffichable().getIdArchive()
            .setUuidType("00000000-0000-0000-0000-000000000000");
      request.getConsultationAffichable().setMetadonnees(
            new ListeMetadonneeCodeType());
      MetadonneeCodeType meta = new MetadonneeCodeType();
      meta.setMetadonneeCodeType(FORMAT_FICHIER);
      request.getConsultationAffichable().getMetadonnees()
            .addMetadonneeCode(meta);

      UntypedDocument doc = new UntypedDocument();
      List<UntypedMetadata> listeMetas = new ArrayList<UntypedMetadata>();
      listeMetas.add(new UntypedMetadata(FORMAT_FICHIER, "fmt/353"));
      doc.setUMetadatas(listeMetas);
      String contenu = "mon-fichier";
      ByteArrayDataSource rawData = new ByteArrayDataSource(contenu.getBytes(),
            "application/octet-stream");
      DataHandler fileContent = new DataHandler(rawData);
      doc.setContent(fileContent);

      EasyMock.expect(
            saeService.consultationAffichable((ConsultParams) EasyMock
                  .anyObject())).andReturn(doc);

      EasyMock.replay(saeService);

      try {
         ConsultationAffichableResponse reponse = consultService
               .consultationAffichable(request);
         assertNotNull("La reponse ne doit pas etre null", reponse);
         assertNotNull(
               "L'objet ConsultationAffichableResponse ne doit pas etre null",
               reponse.getConsultationAffichableResponse());
         assertNotNull("L'objet ListeMetadonneeType ne doit pas etre null",
               reponse.getConsultationAffichableResponse().getMetadonnees());
         assertNotNull("Le tableau de metadonnees ne doit pas etre null",
               reponse.getConsultationAffichableResponse().getMetadonnees()
                     .getMetadonnee());
         assertEquals(
               "Le nombre d'éléments dans la liste des métadonnées n'est pas correct",
               1, reponse.getConsultationAffichableResponse().getMetadonnees()
                     .getMetadonnee().length);
         MetadonneeType valeurMeta = reponse
               .getConsultationAffichableResponse().getMetadonnees()
               .getMetadonnee()[0];
         assertNotNull("L'objet MetadonneeType ne doit pas etre null",
               valeurMeta);
         assertEquals("Le code de la metadonnee n'est pas celui attendu",
               FORMAT_FICHIER, valeurMeta.getCode().getMetadonneeCodeType());
         assertEquals("La valeur de la metadonnee n'est pas celle attendu",
               "fmt/353", valeurMeta.getValeur().getMetadonneeValeurType());
         assertNotNull("L'objet DataHandler ne doit pas etre null", reponse
               .getConsultationAffichableResponse().getContenu());
         DataHandler dataHandler = reponse.getConsultationAffichableResponse()
               .getContenu();
         final InputStream stream = dataHandler.getInputStream();
         byte[] byteArray = IOUtils.toByteArray(stream);
         assertEquals("Le contenu n'est pas celui attendu", contenu,
               new String(byteArray));

      } catch (ConsultationAxisFault ex) {
         fail("La consultation aurait du bien se passer");
      }

      EasyMock.reset(saeService);
   }

   // Test fonctionnant en local seulement

   // @Test
   // public void consultationGNTGNS_success()
   // throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
   // MetaDataUnauthorizedToConsultEx,
   // SAEConsultationAffichableParametrageException, IOException,
   // SearchingServiceEx, ConnectionServiceEx {
   //
   // ConsultationGNTGNS request = new ConsultationGNTGNS();
   // request.setConsultationGNTGNS(new ConsultationGNTGNSRequestType());
   // request.getConsultationGNTGNS().setIdArchive(new UuidType());
   // request.getConsultationGNTGNS().getIdArchive()
   // .setUuidType("00000000-0000-0000-0000-000000000000");
   // request.getConsultationGNTGNS().setMetadonnees(
   // new ListeMetadonneeCodeType());
   // MetadonneeCodeType meta = new MetadonneeCodeType();
   // meta.setMetadonneeCodeType(FORMAT_FICHIER);
   // request.getConsultationGNTGNS().getMetadonnees().addMetadonneeCode(meta);
   //
   // UntypedDocument doc = new UntypedDocument();
   // List<UntypedMetadata> listeMetas = new ArrayList<UntypedMetadata>();
   // listeMetas.add(new UntypedMetadata(FORMAT_FICHIER, "fmt/353"));
   // doc.setUMetadatas(listeMetas);
   // String contenu = "mon-fichier";
   // ByteArrayDataSource rawData = new ByteArrayDataSource(contenu.getBytes(),
   // "application/octet-stream");
   // DataHandler fileContent = new DataHandler(rawData);
   // doc.setContent(fileContent);
   //
   // EasyMock.expect(
   // saeService.consultationAffichable((ConsultParams) EasyMock
   // .anyObject())).andReturn(doc);
   //
   // EasyMock.replay(saeService);
   //
   // try {
   // ConsultationGNTGNSResponse reponse = consultService
   // .consultationGNTGNS(request);
   // assertNotNull("La reponse ne doit pas etre null", reponse);
   // assertNotNull(
   // "L'objet ConsultationAffichableResponse ne doit pas etre null",
   // reponse.getConsultationGNTGNSResponse());
   // assertNotNull("L'objet ListeMetadonneeType ne doit pas etre null",
   // reponse.getConsultationGNTGNSResponse().getMetadonnees());
   // assertNotNull("Le tableau de metadonnees ne doit pas etre null",
   // reponse.getConsultationGNTGNSResponse().getMetadonnees()
   // .getMetadonnee());
   // assertEquals(
   // "Le nombre d'éléments dans la liste des métadonnées n'est pas correct",
   // 1, reponse.getConsultationGNTGNSResponse().getMetadonnees()
   // .getMetadonnee().length);
   // MetadonneeType valeurMeta = reponse.getConsultationGNTGNSResponse()
   // .getMetadonnees().getMetadonnee()[0];
   // assertNotNull("L'objet MetadonneeType ne doit pas etre null",
   // valeurMeta);
   // assertEquals("Le code de la metadonnee n'est pas celui attendu",
   // FORMAT_FICHIER, valeurMeta.getCode().getMetadonneeCodeType());
   // assertEquals("La valeur de la metadonnee n'est pas celle attendu",
   // "fmt/353", valeurMeta.getValeur().getMetadonneeValeurType());
   // assertNotNull("L'objet DataHandler ne doit pas etre null", reponse
   // .getConsultationGNTGNSResponse().getContenu());
   // DataHandler dataHandler = reponse.getConsultationGNTGNSResponse()
   // .getContenu();
   // final InputStream stream = dataHandler.getInputStream();
   // byte[] byteArray = IOUtils.toByteArray(stream);
   // assertEquals("Le contenu n'est pas celui attendu", contenu,
   // new String(byteArray));
   //
   // } catch (ConsultationAxisFault ex) {
   // fail("La consultation aurait du bien se passer");
   // }
   //
   // EasyMock.reset(saeService);
   // }
}
