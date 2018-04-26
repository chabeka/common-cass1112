package fr.urssaf.image.sae.services.batch.restore.support.lucene.impl;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.services.batch.common.support.VerifDroitRequeteLuceneSupport;
import fr.urssaf.image.sae.services.batch.restore.exception.RestoreMasseParamValidationException;
import fr.urssaf.image.sae.services.batch.restore.support.lucene.RestoreParamValidationSupport;
import fr.urssaf.image.sae.services.exception.SAESearchQueryParseException;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;


/**
 * Implémentation du support {@link RestoreParamValidationSupport}
 * 
 */
@Component
public class RestoreParamValidationSupportImpl implements
      RestoreParamValidationSupport {

   private static final Logger LOG = LoggerFactory
         .getLogger(RestoreParamValidationSupportImpl.class);
   
   @Autowired
   private VerifDroitRequeteLuceneSupport verifDroitRequeteLuceneSupport;
   
   /**
    * Code long de la métadonnée identifiant de suppression de masse.
    */
   private final String ID_SUPPRESSION_MASSE = "IdSuppressionMasseInterne";

   /**
    * {@inheritDoc}
    */
   @Override
   public String verificationDroitRestore(UUID idTraitementSuppression)
         throws RestoreMasseParamValidationException {
   
      // Traces debug - entrée méthode
      String prefixeTrc = "verificationDroitRestore()";
      LOG.debug("{} - Début", prefixeTrc);
      
      // Construction de la requete lunce 
      String requeteLucene = ID_SUPPRESSION_MASSE + ":" + idTraitementSuppression.toString();
      
      // gestion des droits
      LOG.debug("{} - Récupération des droits", prefixeTrc);
      AuthenticationToken token = (AuthenticationToken) AuthenticationContext
            .getAuthenticationToken();
      List<SaePrmd> prmds = token.getSaeDroits().get("restore_masse");

      String requeteFinal;
      try {
         requeteFinal = verifDroitRequeteLuceneSupport.verifDroitRequeteLucene(requeteLucene, prmds);
         
      } catch (SyntaxLuceneEx e) {
         throw new RestoreMasseParamValidationException(e);
      } catch (SAESearchQueryParseException e) {
         throw new RestoreMasseParamValidationException(e);
      } catch (SAESearchServiceEx e) {
         throw new RestoreMasseParamValidationException(e);
      } catch (UnknownLuceneMetadataEx e) {
         throw new RestoreMasseParamValidationException(e);
      } catch (MetaDataUnauthorizedToSearchEx e) {
         throw new RestoreMasseParamValidationException(e);
      }
      
      LOG.debug("{} - Sortie", prefixeTrc);
      return requeteFinal;
   }
}
