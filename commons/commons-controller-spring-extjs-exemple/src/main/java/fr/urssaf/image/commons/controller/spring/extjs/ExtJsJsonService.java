package fr.urssaf.image.commons.controller.spring.extjs;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.validation.FieldError;


/**
 * Fonctions utilitaires pour ExtJs
 *
 */
public final class ExtJsJsonService {

   
   /**
    * Le constructeur est privé
    */
   private ExtJsJsonService()
   {
      
   }
   
   
   /**
    * Transforme un objet de type ExtJsJsonReturnValue en une chaîne JSON interprétable directement par ExtJs
    * La chaîne JSON est au format indiqué dans la fiche de développement "F022 : Javascript : Utilisation du framework extjs"
    * @param value l'objet ExtJsJsonReturnValue à convertir en JSON
    * @return la chaîne JSON
    * @throws IOException en cas d'erreur d'E/S
    */
   public static String returnValueToJson(ExtJsJsonReturnValue value) throws IOException
   {
      
      // Création de la mécanique
      ObjectMapper mapper = new ObjectMapper();
      JsonNode rootNode = mapper.createObjectNode();
      
      // Succès ou Echec
      ((ObjectNode) rootNode).put("success", value.getSuccess());
      
      // Message
      if (value.getMessage()!=null)
      {
         String message = StringEscapeUtils.escapeHtml(value.getMessage());
         ((ObjectNode) rootNode).put("message", message);
      }
      
      // Erreurs
      if ((value.getErrors()!=null) && (!value.getErrors().isEmpty())) 
      {
         ObjectNode errors = ((ObjectNode) rootNode).putObject("errors");
         String nomChampForm;
         String messageErreur;
         for (Iterator<String> i = value.getErrors().keySet().iterator(); i.hasNext() ; )
         {
            nomChampForm = i.next();
            messageErreur = value.getErrors().get(nomChampForm);
            messageErreur = StringEscapeUtils.escapeHtml(messageErreur);
            errors.put(nomChampForm, messageErreur);
         }
      }
      
      // Mécanique de transformation en String
      StringWriter stringWriter = new StringWriter();
      JsonFactory jsonFactory = new JsonFactory();
      JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(stringWriter);
      mapper.writeTree(jsonGenerator, rootNode);
      return stringWriter.toString();
      
   }
   
   
   /**
    * Transforme les erreurs de saisie utilisateur passées en paramètres dans une chaîne JSON
    * interprétable directement par ExtJs.
    * La chaîne JSON est au format indiqué dans la fiche de développement "F022 : Javascript : Utilisation du framework extjs".
    * Les erreurs de saisie sont issues de la validation de la classe de formulaire par Spring
    * @param errors les erreurs de saisie utilisateur
    * @return la chaîne JSON pour ExtJs
    * @throws IOException en cas d'erreur d'E/S
    */
   public static String fieldErrorsToJson(List<FieldError> errors) throws IOException
   {

      // Initialisation de la valeur de retour ExtJs
      ExtJsJsonReturnValue retour = new ExtJsJsonReturnValue();
      retour.setSuccess(false);
      
      // Vérifie qu'il y ait au moins une erreur à traiter
      if ((errors!=null) && (!errors.isEmpty()))
      {
         String nomChamp;
         String messageErreur;
         for(FieldError fieldError : errors)
         {
            nomChamp = fieldError.getField();
            messageErreur = fieldError.getDefaultMessage();
            retour.ajouteErreur(nomChamp, messageErreur);
         }
      }
      
      // Création de la chaîne JSON
      return returnValueToJson(retour);
      
   }
   
}
