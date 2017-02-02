package fr.urssaf.image.sae.integration.ihmweb.service.referentiels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.modele.SoapFault;
import fr.urssaf.image.sae.integration.ihmweb.modele.soapfaults.ListeSoapFaultType;
import fr.urssaf.image.sae.integration.ihmweb.modele.soapfaults.SoapFaultType;
import fr.urssaf.image.sae.integration.ihmweb.utils.JAXBUtils;


/**
 * Service de manipulation du référentiel interne des SoapFault
 */
@Service
public class ReferentielSoapFaultService {

   
   private final List<SoapFault> soapFaults = new ArrayList<SoapFault>();
   
   
   /**
    * Constructeur
    */
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public ReferentielSoapFaultService() {
      
      try {
         
         // Lecture du fichier SoapFault.xml
         ListeSoapFaultType listeSoapFaults = JAXBUtils.unmarshalResource(
               ListeSoapFaultType.class,
               "/SoapFaults/SoapFaults.xml",
               "/SoapFaults/SoapFaults.xsd");
         
         // Construction de la liste privée soapFaults
         Iterator<SoapFaultType> iterator = listeSoapFaults.getSoapFault().iterator();
         SoapFaultType soapFaultXml;
         SoapFault soapFault;
         while (iterator.hasNext())
         {
            
            // Récupère l'objet SoapFaultType
            soapFaultXml = iterator.next();
            
            // Construction d'une SoapFault maison
            soapFault = new SoapFault();
            soapFault.setIdentifiant(soapFaultXml.getId());
            soapFault.setMessage(soapFaultXml.getMessage());
            soapFault.setCode(
                  new QName(
                        soapFaultXml.getNamespace(),
                        soapFaultXml.getLocalPart(),
                        soapFaultXml.getPrefixe()));
            soapFaults.add(soapFault);
            
         }
         
         
      } catch (Exception e) {
         throw new IntegrationRuntimeException(e);
      }
      
   }
   
   
   /**
    * Renvoie l'objet de SoapFault du référentiel interne dont l'identifiant est passé en paramètre
    * 
    * @param identifiant l'identifiant
    * @return l'objet décrivant la Soapfault
    */
   public final SoapFault findSoapFault(String identifiant) {
      
      SoapFault result = null;
      
      for(SoapFault fault: this.soapFaults) {
         if (fault.getIdentifiant().equals(identifiant)) {
            result = fault;
            break;
         }
      }
      
      if (result==null) {
         throw new IntegrationRuntimeException(
               "La SoapFault " + identifiant + " n'a pas été retrouvée dans le référentiel interne");
      }
      
      return result;
      
   }
   
}
