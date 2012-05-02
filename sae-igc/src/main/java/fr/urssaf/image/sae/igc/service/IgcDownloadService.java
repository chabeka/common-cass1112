package fr.urssaf.image.sae.igc.service;

import fr.urssaf.image.sae.igc.exception.IgcDownloadException;
import fr.urssaf.image.sae.igc.modele.IgcConfig;

/**
 * Téléchargements d'éléments liés à l'IGC (les CRL par exemple)
 * 
 * 
 */
public interface IgcDownloadService {

   /**
    * Téléchargement des CRLs
    * 
    * @param igcConfig
    *           La configuration contenant les informations permettant le
    *           téléchargement des CRL
    * @return Le nombre de fichiers CRL téléchargés
    * @throws IgcDownloadException
    *            Une erreur s'est produite lors du téléchargement des éléments
    *            de l'IGC
    */
   int telechargeCRLs(IgcConfig igcConfig) throws IgcDownloadException;
}
