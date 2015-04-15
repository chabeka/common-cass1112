package fr.urssaf.image.sae.webservices.service;

import fr.cirtil.www.saeservice.ArchivageUnitaire;
import fr.cirtil.www.saeservice.ArchivageUnitairePJ;
import fr.cirtil.www.saeservice.ArchivageUnitairePJResponse;
import fr.cirtil.www.saeservice.ArchivageUnitaireResponse;
import fr.urssaf.image.sae.webservices.exception.CaptureAxisFault;

/**
 * Service web de capture du SAE
 * 
 * 
 */
public interface WSCaptureService {

   /**
    * Service pour l'opération <b>ArchivageUnitaire</b>
    * 
    * <pre>
    * &lt;wsdl:operation name="archivageUnitaire">
    *    &lt;wsdl:documentation>Service d'archivage unitaire de document&lt;/wsdl:documentation>
    *    ...
    * &lt;/wsdl:operation>
    * </pre>
    * 
    * @param request
    *           Objet contenent une url ECDE et une liste de métadonnées
    * @return instance de {@link ArchivageUnitaireResponse} contenant l'UUID
    *         d'archivage
    * @throws CaptureAxisFault
    *            Une exception est levée lors de la capture unitaire
    */
   ArchivageUnitaireResponse archivageUnitaire(ArchivageUnitaire request)
         throws CaptureAxisFault;
   
   /**
    * Service pour l'opération <b>ArchivageUnitairePJ</b>
    * 
    * <pre>
    * &lt;wsdl:operation name="archivageUnitairePJ">
    *    &lt;wsdl:documentation>Service d'archivage unitaire de document&lt;/wsdl:documentation>
    *    ...
    * &lt;/wsdl:operation>
    * </pre>
    * 
    * @param request
    *           Objet contenent un nom de fichier, son contenu et une liste de métadonnées
    * @return instance de {@link ArchivageUnitairePJResponse} contenant l'UUID
    *         d'archivage
    * @throws CaptureAxisFault
    *            Une exception est levée lors de la capture unitaire
    */
   ArchivageUnitairePJResponse archivageUnitairePJ(ArchivageUnitairePJ request)
         throws CaptureAxisFault;

}
