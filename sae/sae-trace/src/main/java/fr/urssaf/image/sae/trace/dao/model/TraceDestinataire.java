/**
 *
 */
package fr.urssaf.image.sae.trace.dao.model;

import java.util.List;
import java.util.Map;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Classe de modèle d'une trace du registre de sécurité
 */
@Table(name = "tracedestinatairecql")
public class TraceDestinataire {

   /** code de l'événement */
   @PartitionKey
   @Column(name = "codeevt")
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
   public final void setCodeEvt(final String codeEvt) {
      this.codeEvt = codeEvt;
   }

   /**
    * @return
    *         <ul>
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
   public final void setDestinataires(final Map<String, List<String>> destinataires) {
      this.destinataires = destinataires;
   }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeEvt == null) ? 0 : codeEvt.hashCode());
		result = prime * result + ((destinataires == null) ? 0 : destinataires.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TraceDestinataire other = (TraceDestinataire) obj;
		if (codeEvt == null) {
			if (other.codeEvt != null)
				return false;
		} else if (!codeEvt.equals(other.codeEvt))
			return false;
		if (destinataires == null) {
			if (other.destinataires != null)
				return false;
		} else if (!destinataires.equals(other.destinataires))
			return false;
		return true;
	}

}
