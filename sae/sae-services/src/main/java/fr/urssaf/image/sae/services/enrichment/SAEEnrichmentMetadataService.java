package fr.urssaf.image.sae.services.enrichment;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEVirtualDocument;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.SAEEnrichmentEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;

/**
 * Service d’enrichissement des métadonnées.
 */
public interface SAEEnrichmentMetadataService {
   /**
    * Enrichie les métadonnées à partir du référentiel RCND qui sont :
    * <ul>
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
    * 
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

   /**
    * Enrichit les métadonnées suivantes :
    * <ul>
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
    * 
    * @param document
    *           document dont il fait enrichir les métadonnées
    * @throws SAEEnrichmentEx
    *            Erreur levée lors de l'enrichissement des métadonnées
    * @throws ReferentialRndException
    *            Exception levée s'il y a des erreurs lors de la récupération
    *            des codes RND
    * @throws UnknownCodeRndEx
    *            Exception levée si le code RND est inconnu
    */
   void enrichmentVirtualMetadata(SAEVirtualDocument document) throws SAEEnrichmentEx,
         ReferentialRndException, UnknownCodeRndEx;
}
