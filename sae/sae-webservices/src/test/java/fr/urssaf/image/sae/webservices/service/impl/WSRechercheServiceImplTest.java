/**
 * 
 */
package fr.urssaf.image.sae.webservices.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.IdentifiantPageType;
import fr.cirtil.www.saeservice.ListeMetadonneeCodeType;
import fr.cirtil.www.saeservice.ListeMetadonneeType;
import fr.cirtil.www.saeservice.MetadonneeCodeType;
import fr.cirtil.www.saeservice.MetadonneeType;
import fr.cirtil.www.saeservice.MetadonneeValeurType;
import fr.cirtil.www.saeservice.RangeMetadonneeType;
import fr.cirtil.www.saeservice.RechercheParIterateur;
import fr.cirtil.www.saeservice.RechercheParIterateurRequestType;
import fr.cirtil.www.saeservice.RequetePrincipaleType;
import fr.cirtil.www.saeservice.UuidType;
import fr.urssaf.image.sae.bo.model.AbstractMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedRangeMetadata;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.search.DoublonFiltresMetadataEx;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownFiltresMetadataEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;
import fr.urssaf.image.sae.webservices.exception.RechercheAxis2Fault;

/**
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
public class WSRechercheServiceImplTest {

   @Autowired
   @Qualifier("documentService")
   private SAEDocumentService saeService;

   @Autowired
   private WSRechercheServiceImpl rechercheService;

   @Test
   public void testListeNullMDDesired_success() {
      List<String> values = WSRechercheServiceImpl.recupererListMDDesired(null);

      Assert.assertNotNull("la liste retournée est non nulle", values);
      Assert.assertEquals("la liste est vide", 0, values.size());
   }

   @Test
   public void testListeVideMDDesired_success() {
      List<String> values = WSRechercheServiceImpl
            .recupererListMDDesired(new MetadonneeCodeType[] {});

      Assert.assertNotNull("la liste retournée est non nulle", values);
      Assert.assertEquals("la liste est vide", 0, values.size());
   }

   @Test
   public void rechercheParIterateur_erreur_filtres()
         throws RechercheAxis2Fault, MetaDataUnauthorizedToSearchEx,
         MetaDataUnauthorizedToConsultEx, UnknownLuceneMetadataEx,
         SAESearchServiceEx, SyntaxLuceneEx, UnknownDesiredMetadataEx,
         UnknownFiltresMetadataEx, DoublonFiltresMetadataEx {

      RechercheParIterateur request = creationRequest();

      EasyMock.expect(
            saeService.searchPaginated((List<UntypedMetadata>) EasyMock
                  .anyObject(), (UntypedRangeMetadata) EasyMock.anyObject(),
                  (List<AbstractMetadata>) EasyMock.anyObject(),
                  (List<AbstractMetadata>) EasyMock.anyObject(), EasyMock
                        .anyInt(), (UUID) EasyMock.anyObject(),
                  (List<String>) EasyMock.anyObject())).andThrow(
            new UnknownFiltresMetadataEx("test-unitaire : filtres incorrects"));

      EasyMock.replay(saeService);

      try {

         rechercheService.rechercheParIterateur(request);
         fail("C'est l'exception RechercheAxis2Fault qui est attendue");
      } catch (RechercheAxis2Fault ex) {
         assertEquals("Le message d'erreur n'est pas celui attendu",
               "test-unitaire : filtres incorrects", ex.getMessage());
         assertEquals("La cause de l'exception n'est pas la bonne",
               UnknownFiltresMetadataEx.class.getName(), ex.getCause()
                     .getClass().getName());
         assertEquals("La partie local du code de l'erreur n'est pas le bon",
               "RechercheMetadonneesInconnues", ex.getFaultCode()
                     .getLocalPart());
      }

      EasyMock.reset(saeService);
   }

   @Test
   public void rechercheParIterateur_erreur_lucene()
         throws RechercheAxis2Fault, MetaDataUnauthorizedToSearchEx,
         MetaDataUnauthorizedToConsultEx, UnknownLuceneMetadataEx,
         SAESearchServiceEx, SyntaxLuceneEx, UnknownDesiredMetadataEx,
         UnknownFiltresMetadataEx, DoublonFiltresMetadataEx {

      RechercheParIterateur request = creationRequest();

      EasyMock.expect(
            saeService.searchPaginated((List<UntypedMetadata>) EasyMock
                  .anyObject(), (UntypedRangeMetadata) EasyMock.anyObject(),
                  (List<AbstractMetadata>) EasyMock.anyObject(),
                  (List<AbstractMetadata>) EasyMock.anyObject(), EasyMock
                        .anyInt(), (UUID) EasyMock.anyObject(),
                  (List<String>) EasyMock.anyObject())).andThrow(
            new SyntaxLuceneEx("test-unitaire : lucene incorrecte"));

      EasyMock.replay(saeService);

      try {

         rechercheService.rechercheParIterateur(request);
         fail("C'est l'exception RechercheAxis2Fault qui est attendue");
      } catch (RechercheAxis2Fault ex) {
         assertEquals("Le message d'erreur n'est pas celui attendu",
               "test-unitaire : lucene incorrecte", ex.getMessage());
         assertEquals("La cause de l'exception n'est pas la bonne",
               SyntaxLuceneEx.class.getName(), ex.getCause().getClass()
                     .getName());
         assertEquals("La partie local du code de l'erreur n'est pas le bon",
               "SyntaxeLuceneNonValide", ex.getFaultCode().getLocalPart());
      }

      EasyMock.reset(saeService);
   }

   @Test
   public void rechercheParIterateur_erreur_meta_non_recherchable()
         throws RechercheAxis2Fault, MetaDataUnauthorizedToSearchEx,
         MetaDataUnauthorizedToConsultEx, UnknownLuceneMetadataEx,
         SAESearchServiceEx, SyntaxLuceneEx, UnknownDesiredMetadataEx,
         UnknownFiltresMetadataEx, DoublonFiltresMetadataEx {

      RechercheParIterateur request = creationRequest();

      EasyMock.expect(
            saeService.searchPaginated((List<UntypedMetadata>) EasyMock
                  .anyObject(), (UntypedRangeMetadata) EasyMock.anyObject(),
                  (List<AbstractMetadata>) EasyMock.anyObject(),
                  (List<AbstractMetadata>) EasyMock.anyObject(), EasyMock
                        .anyInt(), (UUID) EasyMock.anyObject(),
                  (List<String>) EasyMock.anyObject())).andThrow(
            new MetaDataUnauthorizedToSearchEx(
                  "test-unitaire : meta non recherchable"));

      EasyMock.replay(saeService);

      try {

         rechercheService.rechercheParIterateur(request);
         fail("C'est l'exception RechercheAxis2Fault qui est attendue");
      } catch (RechercheAxis2Fault ex) {
         assertEquals("Le message d'erreur n'est pas celui attendu",
               "test-unitaire : meta non recherchable", ex.getMessage());
         assertEquals("La cause de l'exception n'est pas la bonne",
               MetaDataUnauthorizedToSearchEx.class.getName(), ex.getCause()
                     .getClass().getName());
         assertEquals("La partie local du code de l'erreur n'est pas le bon",
               "RechercheMetadonneesInterdite", ex.getFaultCode()
                     .getLocalPart());
      }

      EasyMock.reset(saeService);
   }

   @Test
   public void rechercheParIterateur_erreur_pendant_recherche()
         throws RechercheAxis2Fault, MetaDataUnauthorizedToSearchEx,
         MetaDataUnauthorizedToConsultEx, UnknownLuceneMetadataEx,
         SAESearchServiceEx, SyntaxLuceneEx, UnknownDesiredMetadataEx,
         UnknownFiltresMetadataEx, DoublonFiltresMetadataEx {

      RechercheParIterateur request = creationRequest();

      EasyMock.expect(
            saeService.searchPaginated((List<UntypedMetadata>) EasyMock
                  .anyObject(), (UntypedRangeMetadata) EasyMock.anyObject(),
                  (List<AbstractMetadata>) EasyMock.anyObject(),
                  (List<AbstractMetadata>) EasyMock.anyObject(), EasyMock
                        .anyInt(), (UUID) EasyMock.anyObject(),
                  (List<String>) EasyMock.anyObject())).andThrow(
            new SAESearchServiceEx(
                  "test-unitaire : erreur lors de la recherche"));

      EasyMock.replay(saeService);

      try {

         rechercheService.rechercheParIterateur(request);
         fail("C'est l'exception RechercheAxis2Fault qui est attendue");
      } catch (RechercheAxis2Fault ex) {
         assertEquals("Le message d'erreur n'est pas celui attendu",
               "test-unitaire : erreur lors de la recherche", ex.getMessage());
         assertEquals("La cause de l'exception n'est pas la bonne",
               SAESearchServiceEx.class.getName(), ex.getCause().getClass()
                     .getName());
         assertEquals("La partie local du code de l'erreur n'est pas le bon",
               "ErreurInterneRecherche", ex.getFaultCode().getLocalPart());
      }

      EasyMock.reset(saeService);
   }

   @Test
   public void rechercheParIterateur_meta_souhaitee()
         throws RechercheAxis2Fault, MetaDataUnauthorizedToSearchEx,
         MetaDataUnauthorizedToConsultEx, UnknownLuceneMetadataEx,
         SAESearchServiceEx, SyntaxLuceneEx, UnknownDesiredMetadataEx,
         UnknownFiltresMetadataEx, DoublonFiltresMetadataEx {

      RechercheParIterateur request = creationRequest();

      EasyMock.expect(
            saeService.searchPaginated((List<UntypedMetadata>) EasyMock
                  .anyObject(), (UntypedRangeMetadata) EasyMock.anyObject(),
                  (List<AbstractMetadata>) EasyMock.anyObject(),
                  (List<AbstractMetadata>) EasyMock.anyObject(), EasyMock
                        .anyInt(), (UUID) EasyMock.anyObject(),
                  (List<String>) EasyMock.anyObject())).andThrow(
            new UnknownDesiredMetadataEx(
                  "test-unitaire : meta souhaitee inconnue"));

      EasyMock.replay(saeService);

      try {

         rechercheService.rechercheParIterateur(request);
         fail("C'est l'exception RechercheAxis2Fault qui est attendue");
      } catch (RechercheAxis2Fault ex) {
         assertEquals("Le message d'erreur n'est pas celui attendu",
               "test-unitaire : meta souhaitee inconnue", ex.getMessage());
         assertEquals("La cause de l'exception n'est pas la bonne",
               UnknownDesiredMetadataEx.class.getName(), ex.getCause()
                     .getClass().getName());
         assertEquals("La partie local du code de l'erreur n'est pas le bon",
               "ConsultationMetadonneesInconnues", ex.getFaultCode()
                     .getLocalPart());
      }

      EasyMock.reset(saeService);
   }

   private RechercheParIterateur creationRequest() {
      RechercheParIterateur request = new RechercheParIterateur();
      request.setRechercheParIterateur(new RechercheParIterateurRequestType());
      request.getRechercheParIterateur().setIdentifiantPage(
            new IdentifiantPageType());
      request.getRechercheParIterateur().getIdentifiantPage().setIdArchive(
            new UuidType());
      request.getRechercheParIterateur().getIdentifiantPage().getIdArchive()
            .setUuidType("00000000-0000-0000-0000-000000000000");
      request.getRechercheParIterateur().getIdentifiantPage().setValeur(
            new MetadonneeValeurType());
      request.getRechercheParIterateur().getIdentifiantPage().getValeur()
            .setMetadonneeValeurType("20140102");
      request.getRechercheParIterateur().setMetadonnees(
            new ListeMetadonneeCodeType());
      MetadonneeCodeType meta = new MetadonneeCodeType();
      meta.setMetadonneeCodeType("FormatFichier");
      request.getRechercheParIterateur().getMetadonnees().addMetadonneeCode(
            meta);
      request.getRechercheParIterateur().setNbDocumentsParPage(10);
      request.getRechercheParIterateur().setRequetePrincipale(
            new RequetePrincipaleType());

      request.getRechercheParIterateur().getRequetePrincipale()
            .setFixedMetadatas(new ListeMetadonneeType());
      MetadonneeType metaType = new MetadonneeType();
      metaType.setCode(new MetadonneeCodeType());
      metaType.getCode().setMetadonneeCodeType("Siret");
      metaType.setValeur(new MetadonneeValeurType());
      metaType.getValeur().setMetadonneeValeurType("123456789123456");
      request.getRechercheParIterateur().getRequetePrincipale()
            .getFixedMetadatas().addMetadonnee(metaType);
      RangeMetadonneeType rangeMeta = new RangeMetadonneeType();
      rangeMeta.setCode(new MetadonneeCodeType());
      rangeMeta.getCode().setMetadonneeCodeType("DateCreation");
      rangeMeta.setValeurMax(new MetadonneeValeurType());
      rangeMeta.getValeurMax().setMetadonneeValeurType("20140102");
      rangeMeta.setValeurMin(new MetadonneeValeurType());
      rangeMeta.getValeurMin().setMetadonneeValeurType("20140101");
      request.getRechercheParIterateur().getRequetePrincipale()
            .setVaryingMetadata(rangeMeta);
      return request;
   }

   @Test
   public void rechercheParIterateur_erreur_meta_non_consultable()
         throws RechercheAxis2Fault, MetaDataUnauthorizedToSearchEx,
         MetaDataUnauthorizedToConsultEx, UnknownLuceneMetadataEx,
         SAESearchServiceEx, SyntaxLuceneEx, UnknownDesiredMetadataEx,
         UnknownFiltresMetadataEx, DoublonFiltresMetadataEx {
   
      RechercheParIterateur request = creationRequest();
   
      EasyMock.expect(
            saeService.searchPaginated((List<UntypedMetadata>) EasyMock
                  .anyObject(), (UntypedRangeMetadata) EasyMock.anyObject(),
                  (List<AbstractMetadata>) EasyMock.anyObject(),
                  (List<AbstractMetadata>) EasyMock.anyObject(), EasyMock
                        .anyInt(), (UUID) EasyMock.anyObject(),
                  (List<String>) EasyMock.anyObject())).andThrow(
            new MetaDataUnauthorizedToConsultEx(
                  "test-unitaire : meta non consultable"));
   
      EasyMock.replay(saeService);
   
      try {
   
         rechercheService.rechercheParIterateur(request);
         fail("C'est l'exception RechercheAxis2Fault qui est attendue");
      } catch (RechercheAxis2Fault ex) {
         assertEquals("Le message d'erreur n'est pas celui attendu",
               "test-unitaire : meta non consultable", ex.getMessage());
         assertEquals("La cause de l'exception n'est pas la bonne",
               MetaDataUnauthorizedToConsultEx.class.getName(), ex.getCause()
                     .getClass().getName());
         assertEquals("La partie local du code de l'erreur n'est pas le bon",
               "ConsultationMetadonneesInterdite", ex.getFaultCode()
                     .getLocalPart());
      }
   
      EasyMock.reset(saeService);
   }

   @Test
   public void rechercheParIterateur_erreur_meta_filtre_doublon()
         throws RechercheAxis2Fault, MetaDataUnauthorizedToSearchEx,
         MetaDataUnauthorizedToConsultEx, UnknownLuceneMetadataEx,
         SAESearchServiceEx, SyntaxLuceneEx, UnknownDesiredMetadataEx,
         UnknownFiltresMetadataEx, DoublonFiltresMetadataEx {
   
      RechercheParIterateur request = creationRequest();
   
      EasyMock.expect(
            saeService.searchPaginated((List<UntypedMetadata>) EasyMock
                  .anyObject(), (UntypedRangeMetadata) EasyMock.anyObject(),
                  (List<AbstractMetadata>) EasyMock.anyObject(),
                  (List<AbstractMetadata>) EasyMock.anyObject(), EasyMock
                        .anyInt(), (UUID) EasyMock.anyObject(),
                  (List<String>) EasyMock.anyObject())).andThrow(
            new DoublonFiltresMetadataEx(
                  "test-unitaire : meta filtre doublon"));
   
      EasyMock.replay(saeService);
   
      try {
   
         rechercheService.rechercheParIterateur(request);
         fail("C'est l'exception RechercheAxis2Fault qui est attendue");
      } catch (RechercheAxis2Fault ex) {
         assertEquals("Le message d'erreur n'est pas celui attendu",
               "test-unitaire : meta filtre doublon", ex.getMessage());
         assertEquals("La cause de l'exception n'est pas la bonne",
               DoublonFiltresMetadataEx.class.getName(), ex.getCause()
                     .getClass().getName());
         assertEquals("La partie local du code de l'erreur n'est pas le bon",
               "RechercheMetadonneesDoublons", ex.getFaultCode()
                     .getLocalPart());
      }
   
      EasyMock.reset(saeService);
   }
}
