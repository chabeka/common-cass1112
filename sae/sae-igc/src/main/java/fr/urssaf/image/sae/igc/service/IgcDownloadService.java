package fr.urssaf.image.sae.igc.service;

import fr.urssaf.image.sae.igc.exception.IgcDownloadException;
import fr.urssaf.image.sae.igc.modele.IgcConfigs;

/**
 * Téléchargements d'éléments liés à l'IGC (les CRL par exemple)
 * 
 * 
 */
public interface IgcDownloadService {

   /**
    * Téléchargement des CRLs
    * 
    * @param igcConfigs
    *           La configuration contenant les informations permettant le
    *           téléchargement des CRL
    * @throws IgcDownloadException
    *            Une erreur s'est produite lors du téléchargement des éléments
    *            de l'IGC
    */
   void telechargeCRLs(IgcConfigs igcConfigs) throws IgcDownloadException;
}
