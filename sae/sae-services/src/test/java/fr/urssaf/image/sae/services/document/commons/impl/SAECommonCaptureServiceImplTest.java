package fr.urssaf.image.sae.services.document.commons.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.services.CommonsServices;
import fr.urssaf.image.sae.services.document.commons.SAECommonCaptureService;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.SAEEnrichmentEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/***
 * Classe de test pour le service commun de capture en masse et capture
 * unitaire.
 * 
 * @author Rhofir
 */
@SuppressWarnings("all")
public class SAECommonCaptureServiceImplTest extends CommonsServices {
   @Autowired
   @Qualifier("saeCommonCaptureService")
   SAECommonCaptureService saeCommonCaptureService;

   /**
    * @return Le service saeCommonCaptureService
    */
   public final SAECommonCaptureService getSaeCommonCaptureService() {
      return saeCommonCaptureService;
   }

   /**
    * @param saeCommonCaptureService
    *           : Le service saeCommonCaptureService.
    */
   public final void setSaeCommonCaptureService(
         SAECommonCaptureService saeCommonCaptureService) {
      this.saeCommonCaptureService = saeCommonCaptureService;
   }

   @Before
   public void init() {
      // initialisation du contexte de sécurité
      VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("TESTS_UNITAIRES");
      viExtrait.setIdUtilisateur("UTILISATEUR TEST");

      SaeDroits saeDroits = new SaeDroits();
      List<SaePrmd> saePrmds = new ArrayList<SaePrmd>();
      SaePrmd saePrmd = new SaePrmd();
      saePrmd.setValues(new HashMap<String, String>());
      Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      saePrmd.setPrmd(prmd);
      String[] roles = new String[] { "archivage_unitaire" };
      saePrmds.add(saePrmd);

      saeDroits.put("archivage_unitaire", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles, viExtrait
                  .getSaeDroits());
      AuthenticationContext.setAuthenticationToken(token);
   }

   @After
   public void end() {
      AuthenticationContext.setAuthenticationToken(null);
   }

   /**
    * Test de la méthode
    * {@link fr.urssaf.image.sae.services.document.commons.SAECommonCaptureService#buildStorageDocumentForCapture(UntypedDocument)}
    * .
    */
   @Test
   public final void buildStorageDocumentForCapture()
         throws SAECaptureServiceEx, IOException, ParseException,
         SAEEnrichmentEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotArchivableMetadataEx,
         NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, MappingFromReferentialException,
         InvalidSAETypeException, UnknownHashCodeEx, ReferentialRndException,
         UnknownCodeRndEx, MetadataValueNotInDictionaryEx {

      UntypedDocument untypedDocument = getUntypedDocumentMockData();
      saeCommonCaptureService.buildStorageDocumentForCapture(untypedDocument);
   }

}
