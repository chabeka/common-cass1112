package fr.urssaf.image.sae.services.batch.suppression.support.lucene.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.services.batch.common.support.VerifDroitRequeteLuceneSupport;
import fr.urssaf.image.sae.services.batch.suppression.exception.SuppressionMasseRequeteValidationException;
import fr.urssaf.image.sae.services.batch.suppression.support.lucene.RequeteLuceneValidationSupport;
import fr.urssaf.image.sae.services.exception.SAESearchQueryParseException;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;
import fr.urssaf.image.sae.services.util.SAESearchUtil;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Implémentation du support {@link RequeteLuceneValidationSupport}
 * 
 */
@Component
public class RequeteLuceneValidationSupportImpl implements
      RequeteLuceneValidationSupport {
   
   private static final Logger LOG = LoggerFactory
         .getLogger(RequeteLuceneValidationSupportImpl.class);
   
   @Autowired
   private VerifDroitRequeteLuceneSupport verifDroitRequeteLuceneSupport;
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String validationRequeteLucene(String requeteLucene)
         throws SuppressionMasseRequeteValidationException {
      
      // Traces debug - entrée méthode
      String prefixeTrc = "validationRequeteLucene()";
      LOG.debug("{} - Début", prefixeTrc);
      
      // Trim la requête de recherche
      String requeteTrim = SAESearchUtil.trimRequeteClient(requeteLucene);
      LOG.debug("{} - Requête de recherche après trim : {}", prefixeTrc,
            requeteTrim);
      
      try {
         // Vérification de la requête lucène
         SAESearchUtil.verifieSyntaxeLucene(requeteTrim);
      } catch (SyntaxLuceneEx e) {
         throw new SuppressionMasseRequeteValidationException(new SyntaxLuceneEx(e.getMessage()));
      }
      
      LOG.debug("{} - Sortie", prefixeTrc);
      return requeteTrim;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String verificationDroitRequeteLucene(String requeteLucene)
         throws SuppressionMasseRequeteValidationException {
      
      // Traces debug - entrée méthode
      String prefixeTrc = "verificationDroitRequeteLucene()";
      LOG.debug("{} - Début", prefixeTrc);
      
      // gestion des droits
      LOG.debug("{} - Récupération des droits", prefixeTrc);
      AuthenticationToken token = (AuthenticationToken) AuthenticationContext
            .getAuthenticationToken();
      List<SaePrmd> prmds = token.getSaeDroits().get("suppression_masse");
      
      String requeteFinal;
      try {
         requeteFinal = verifDroitRequeteLuceneSupport.verifDroitRequeteLucene(requeteLucene, prmds);
      } catch (SyntaxLuceneEx e) {
         throw new SuppressionMasseRequeteValidationException(e);
      } catch (SAESearchQueryParseException e) {
         throw new SuppressionMasseRequeteValidationException(e);
      } catch (SAESearchServiceEx e) {
         throw new SuppressionMasseRequeteValidationException(e);
      } catch (UnknownLuceneMetadataEx e) {
         throw new SuppressionMasseRequeteValidationException(e);
      } catch (MetaDataUnauthorizedToSearchEx e) {
         throw new SuppressionMasseRequeteValidationException(e);
      }
      
      LOG.debug("{} - Sortie", prefixeTrc);
      return requeteFinal;
   }
}
