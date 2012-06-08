package fr.urssaf.image.sae.vi.exception;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.UnmarshalException;
import javax.xml.bind.ValidationEvent;

import fr.urssaf.image.sae.vi.configuration.VIConfiguration;

/**
 * Classe d'exception hérite de {@link Exception}<br>
 * Un ou plusieurs problème on été levés lors d'une opération de lecture ou
 * d'écriture d'un XML<br>
 * Le XML est non conforme au schéma XSD
 */
public class VIException extends Exception {

   private static final long serialVersionUID = 1L;

   private final String xsd;

   private final List<String> errors = new ArrayList<String>();

   /**
    * initialise l'attribut <code>xsd</code> avec {@link VIConfiguration#name()}<br>
    * initialise l'attribut <code>errors</code> avec la liste des
    * {@link ValidationEvent#toString()}
    * 
    * @param validationEvents
    *           liste des erreurs du XML
    */
   public VIException(List<ValidationEvent> validationEvents) {
      
      super("Erreur de validation XSD du VI");
      
      this.xsd = VIConfiguration.name();

      for (ValidationEvent validationEvent : validationEvents) {
         errors.add(validationEvent.toString());
      }
      
   }

   /**
    * initialise l'attribut <code>xsd</code> avec
    * {@link UnmarshalException#getMessage()}
    * 
    * @param exception
    *           levée lors de la lecture du XML
    */
   public VIException(UnmarshalException exception) {
      super(exception);
      this.xsd = VIConfiguration.name();

      if (exception.getLinkedException() == null) {
         errors.add(exception.getMessage());
      }else{
         errors.add(exception.getLinkedException().getMessage());
      }
      
   }

   /**
    * @return nom du schéma XSD
    */
   public final String getXsd() {
      return xsd;
   }

   /**
    * 
    * @return liste des messages d'erreurs
    */
   public final String[] getErrors() {
      return errors.toArray(new String[errors.size()]);
   }

}
