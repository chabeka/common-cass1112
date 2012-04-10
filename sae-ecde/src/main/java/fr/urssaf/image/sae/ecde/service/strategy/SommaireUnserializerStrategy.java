package fr.urssaf.image.sae.ecde.service.strategy;

import java.io.File;

import fr.urssaf.image.sae.ecde.exception.EcdeGeneralException;
import fr.urssaf.image.sae.ecde.modele.sommaire.Sommaire;

/**
 * 
 * Classe permettant de convertir les attributs d'un fichier XML en BusinessObjcet
 *
 */
public interface SommaireUnserializerStrategy {

   /**
    * Methode permettant de unserializé les attributs du fichier XML en Bussiness Object<br>
    * XML->JAXB->BO 
    * 
    * @param fileXml fichier xml
    * @return Sommaire : BO object obtenu
    * 
    * @throws EcdeGeneralException exception ecde
    */
   Sommaire unserializeSommaire(File fileXml) throws EcdeGeneralException;
}
