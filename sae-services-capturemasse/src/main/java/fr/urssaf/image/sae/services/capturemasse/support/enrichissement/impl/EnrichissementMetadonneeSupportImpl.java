/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.enrichissement.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.bo.SAEVirtualDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.exception.InvalidPagmsCombinaisonException;
import fr.urssaf.image.sae.droit.exception.UnexpectedDomainException;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.PrmdService;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.support.enrichissement.EnrichissementMetadonneeSupport;
import fr.urssaf.image.sae.services.enrichment.SAEEnrichmentMetadataService;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.SAEEnrichmentEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Implémentation du support {@link EnrichissementMetadonneeSupport}
 * 
 */
@Component
public class EnrichissementMetadonneeSupportImpl implements
      EnrichissementMetadonneeSupport {

   private static final Logger LOGGER = LoggerFactory
      .getLogger(EnrichissementMetadonneeSupportImpl.class);
   
   @Autowired
   private SAEEnrichmentMetadataService service;
   
   @Autowired
   private PrmdService prmdService;
   
   @Autowired
   private MappingDocumentService mapper;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void enrichirMetadonnee(final SAEDocument document) {

      try {
         service.enrichmentMetadata(document);
         enrichissementDomaine(document.getMetadatas());
      } catch (SAEEnrichmentEx e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (ReferentialRndException e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (UnknownCodeRndEx e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (InvalidSAETypeException e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (MappingFromReferentialException e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (UnexpectedDomainException e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (InvalidPagmsCombinaisonException e) {
         throw new CaptureMasseRuntimeException(e);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void enrichirMetadonneesVirtuelles(
         SAEVirtualDocument saeVirtualDocument) {

      try {
         service.enrichmentVirtualMetadata(saeVirtualDocument);
         enrichissementDomaine(saeVirtualDocument.getMetadatas());
      } catch (SAEEnrichmentEx e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (ReferentialRndException e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (UnknownCodeRndEx e) {
         throw new CaptureMasseRuntimeException(e);
      }  catch (InvalidSAETypeException e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (MappingFromReferentialException e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (UnexpectedDomainException e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (InvalidPagmsCombinaisonException e) {
         throw new CaptureMasseRuntimeException(e);
      }
   }
   
   /**
    * Enrichissement du domaine
    * 
    * @param metas
    * @throws InvalidSAETypeException
    * @throws MappingFromReferentialException
    * @throws InvalidPagmsCombinaisonException 
    */
   private void enrichissementDomaine(List<SAEMetadata> metas) 
      throws InvalidSAETypeException, MappingFromReferentialException, UnexpectedDomainException, InvalidPagmsCombinaisonException {
      
      //-- Liste pour récupérer le domaine
      List<UntypedMetadata> newMetas = new ArrayList<UntypedMetadata>();
      
      LOGGER.debug("enrichissementDomaine - Récupération du Vi");
      AuthenticationToken token = (AuthenticationToken) SecurityContextHolder
            .getContext().getAuthentication();
      List<SaePrmd> prmds = token.getSaeDroits().get("archivage_masse");
      
      //-- Récupération du domaine en fonction du contrat du service
      LOGGER.debug("enrichissementDomaine - Récupération du domaine en fct contrat du service");
      prmdService.addDomaine(newMetas, prmds);
      List<SAEMetadata> newSaeMetas = mapper.untypedMetadatasToSaeMetadatas(newMetas);
      
      //-- Enrichissement des méta du document
      LOGGER.debug("enrichissementDomaine - Ajout du domaine aux métas du document");
      for (SAEMetadata newMeta : newSaeMetas) {
         Boolean found = false;
         for (SAEMetadata docMetas : metas) {
            //-- Test si la newMeta n'est pas déjà présente sur le doc
            if(docMetas.getLongCode().equals(newMeta.getLongCode())){
               found = true;
            }
         }
         if(!found){
            //-- Ajout de la méta si elle n'était pas déjà présente
            metas.add(newMeta);
         }
      }
   }
}
