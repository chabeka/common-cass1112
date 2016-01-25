package fr.urssaf.image.sae.webservices.service;

import fr.cirtil.www.saeservice.ArchivageUnitaire;
import fr.cirtil.www.saeservice.ArchivageUnitairePJ;
import fr.cirtil.www.saeservice.ArchivageUnitairePJResponse;
import fr.cirtil.www.saeservice.ArchivageUnitaireResponse;
import fr.cirtil.www.saeservice.GetDocFormatOrigine;
import fr.cirtil.www.saeservice.GetDocFormatOrigineResponse;
import fr.cirtil.www.saeservice.StockageUnitaire;
import fr.cirtil.www.saeservice.StockageUnitaireResponse;
import fr.urssaf.image.sae.webservices.exception.CaptureAxisFault;
import fr.urssaf.image.sae.webservices.exception.GetDocFormatOrigineAxisFault;

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
    *           Objet contenent un nom de fichier, son contenu et une liste de
    *           métadonnées
    * @return instance de {@link ArchivageUnitairePJResponse} contenant l'UUID
    *         d'archivage
    * @throws CaptureAxisFault
    *            Une exception est levée lors de la capture unitaire
    */
   ArchivageUnitairePJResponse archivageUnitairePJ(ArchivageUnitairePJ request)
         throws CaptureAxisFault;

   /**
    * Service pour l'opération <b>stockageUnitaire</b>
    * 
    * <pre>
    * &lt;wsdl:operation name="stockageUnitaire" parameterOrder="input">
    *    &lt;wsdl:documentation>Opération de stockage unitaire d'un document&lt;/wsdl:documentation>   
    *    ...
    * &lt;/wsdl:operation>
    * </pre>
    * 
    * @param request
    *           Objet contenent un nom de fichier, son contenu, une liste de
    *           métadonnées et éventuellement un document attaché
    * @return instance de {@link StockageUnitaireResponse} contenant l'UUID
    *         d'archivage
    * @throws CaptureAxisFault
    *            Une exception est levée lors de la capture unitaire
    */
   StockageUnitaireResponse stockageUnitaire(StockageUnitaire request)
         throws CaptureAxisFault;


}
