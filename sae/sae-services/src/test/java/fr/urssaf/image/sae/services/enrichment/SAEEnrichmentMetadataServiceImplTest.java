package fr.urssaf.image.sae.services.enrichment;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.services.CommonsServices;
import fr.urssaf.image.sae.services.controles.SAEControlesCaptureService;
import fr.urssaf.image.sae.services.enrichment.xml.model.SAEArchivalMetadatas;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.SAEEnrichmentEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@SuppressWarnings("all")
public class SAEEnrichmentMetadataServiceImplTest extends CommonsServices {
   
   @Autowired
   @Qualifier("saeEnrichmentMetadataService")
   private SAEEnrichmentMetadataService saeEnrichmentMetadataService;
   
   @Autowired
   @Qualifier("saeControlesCaptureService")
   private SAEControlesCaptureService controlesCaptureService;
   
   @Autowired
   private MappingDocumentService mappingService;

   /**
    * @return Le service de mappingService
    */
   public final MappingDocumentService getMappingService() {
      return mappingService;
   }

   /**
    * @param mappingService
    *           : Le service de mappingService.
    */
   public final void setMappingService(MappingDocumentService mappingService) {
      this.mappingService = mappingService;
   }

   /**
    * @return Le service d'enrichment des metadonnées.
    */
   public final SAEEnrichmentMetadataService getSaeEnrichmentMetadataService() {
      return saeEnrichmentMetadataService;
   }

   /**
    * @param saeEnrichmentMetadataService
    *           the saeEnrichmentMetadataService to set
    */
   public final void setSaeEnrichmentMetadataService(
         SAEEnrichmentMetadataService saeEnrichmentMetadataService) {
      this.saeEnrichmentMetadataService = saeEnrichmentMetadataService;
   }

   /**
    * Test de la méthode
    * {@link fr.urssaf.image.sae.services.enrichment.impl.SAEEnrichmentMetadataServiceImpl#enrichmentMetadata(SAEDocument)}
    * .
    */
   @Test
   public final void enrichmentMetadata() throws SAECaptureServiceEx,
         IOException, ParseException, SAEEnrichmentEx, InvalidSAETypeException,
         MappingFromReferentialException, ReferentialRndException,
         UnknownCodeRndEx, RequiredStorageMetadataEx {
      
      initDroits();
      
      SAEDocument saeDocument = mappingService
            .untypedDocumentToSaeDocument(getUntypedDocumentMockData());
      saeEnrichmentMetadataService.enrichmentMetadata(saeDocument);
      Assert.assertNotNull(saeDocument);
      controlesCaptureService.checkSaeMetadataForStorage(saeDocument);
   }

   /**
    * Test de la méthode
    * {@link fr.urssaf.image.sae.services.enrichment.impl.SAEEnrichmentMetadataServiceImpl#enrichmentMetadata(SAEDocument)}
    * .
    */
   @Test(expected = UnknownCodeRndEx.class)
   public final void enrichmentMetadataFailed() throws SAECaptureServiceEx,
         IOException, ParseException, SAEEnrichmentEx, ReferentialRndException,
         UnknownCodeRndEx {
      SAEDocument saeDocument = getSAEDocumentMockData();
      for (SAEMetadata saeMetadata : saeDocument.getMetadatas()) {
         if (saeMetadata.getLongCode().equals(
               SAEArchivalMetadatas.CODE_RND.getLongCode())) {
            saeMetadata.setValue("121212");
            break;
         }
      }
      saeEnrichmentMetadataService.enrichmentMetadata(saeDocument);
   }
   
   @After
   public void end() {
      AuthenticationContext.setAuthenticationToken(null);
   }
   
   private void initDroits() {
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
      String[] roles = new String[] { "capture_masse" };
      saePrmds.add(saePrmd);

      saeDroits.put("capture_masse", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles, viExtrait
                  .getSaeDroits());
      AuthenticationContext.setAuthenticationToken(token);
   }
}
