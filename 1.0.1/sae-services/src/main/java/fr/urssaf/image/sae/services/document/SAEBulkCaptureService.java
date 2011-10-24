package fr.urssaf.image.sae.services.document;


/**
 * Fournit le service :<BR/>
 * <li>Capture en masse.</li> </lu>
 * 
 * @author rhofir.
 */
public interface SAEBulkCaptureService {

   /**
    * Service pour l'opération <b>capture en Masse</b>
    * 
    * @param urlSommaire
    *           : L’URL ECDE du fichier sommaire.xml décrivant le traitement de
    *           masse.
    */
   void bulkCapture(String urlSommaire);

}
