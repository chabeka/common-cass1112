package fr.urssaf.image.sae.webservices;

import fr.cirtil.www.saeservice.ArchivageMasse;
import fr.cirtil.www.saeservice.ArchivageMasseResponse;
import fr.cirtil.www.saeservice.Recherche;
import fr.cirtil.www.saeservice.RechercheResponse;

/**
 * Service du SAE
 * 
 * 
 */
public interface SaeStorageService {

   /**
    * Service pour l'opération <b>archivageMasse</b>
    * 
    * <pre>
    * &lt;wsdl:operation name="archivageMasse">
    *    &lt;wsdl:documentation>Service d'archivage de documents multiples&lt;/wsdl:documentation>
    *    ...
    * &lt;/wsdl:operation>
    * </pre>
    * 
    * @param request
    *           Message de demande pour archiver des documents multiples
    * @return instance de {@link ArchivageMasseResponse}
    */
   ArchivageMasseResponse bulkCapture(ArchivageMasse request);

   /**
    * Service pour l'opération <b>Recherche</b>
    * 
    * <pre>
    * &lt;wsdl:operation name="recherche">
    *    &lt;wsdl:documentation>Service de recherche documentaire sur le SAE&lt;/wsdl:documentation>
    *    ...
    * &lt;/wsdl:operation>
    * </pre>
    * 
    * @param request
    *           Message de demande d'une recherche documentaire sur le SAE
    * @return instance de {@link RechercheResponse}
    */
   RechercheResponse search(Recherche request);

}
