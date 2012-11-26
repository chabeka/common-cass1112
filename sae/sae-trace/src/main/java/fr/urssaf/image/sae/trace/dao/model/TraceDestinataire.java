/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.model;

import java.util.List;
import java.util.Map;

/**
 * Classe de modèle d'une trace du registre de sécurité
 * 
 */
public class TraceDestinataire {

   /** code de l'événement */
   private String codeEvt;

   /**
    * <ul>
    * <li>key : type de destinataire</li>
    * <li>values : liste des propriétés à transmettre au destinataire</li>
    * </ul>
    */
   private Map<String, List<String>> destinataires;

   /**
    * @return le code de l'événement
    */
   public final String getCodeEvt() {
      return codeEvt;
   }

   /**
    * @param codeEvt
    *           code de l'événement
    */
   public final void setCodeEvt(String codeEvt) {
      this.codeEvt = codeEvt;
   }

   /**
    * @return <ul>
    *         <li>key : type de destinataire</li>
    *         <li>values : liste des propriétés à transmettre au destinataire</li>
    *         </ul>
    */
   public final Map<String, List<String>> getDestinataires() {
      return destinataires;
   }

   /**
    * @param destinataires
    *           <ul>
    *           <li>key : type de destinataire</li>
    *           <li>values : liste des propriétés à transmettre au destinataire</li>
    *           </ul>
    */
   public final void setDestinataires(Map<String, List<String>> destinataires) {
      this.destinataires = destinataires;
   }

}
