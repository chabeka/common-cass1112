package fr.urssaf.image.sae.services.enrichment;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.SAEEnrichmentEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;

/**
 * Service d’enrichissement des métadonnées.
 * 
 *@author rhofir.
 */
public interface SAEEnrichmentMetadataService {
   /**
    * Enrichie les métadonnées à partir du référentiel RCND qui sont :
    *  <ul>
    * <li>VersionRND</li>
    * <li>CodeFonction</li>
    * <li>CodeActivite</li>
    * <li>DateDebutConservation</li>
    * <li>DateFinConservation</li>
    * <li>NomFichier</li>
    * <li>DocumentVirtuel</li>
    * <li>ContratDeService</li>
    * <li>DateArchivage</li>
    * </ul>
    * @param saeDoc
    *           Classe représentant un document de type {@link SAEDocument}.
    * @throws SAEEnrichmentEx
    *            {@link SAEEnrichmentEx}
   * @throws ReferentialRndException
    *            {@link ReferentialRndException}
   * @throws UnknownCodeRndEx
    *            {@link UnknownCodeRndEx}
    */
   void enrichmentMetadata(SAEDocument saeDoc) throws SAEEnrichmentEx,
         ReferentialRndException, UnknownCodeRndEx;
}
