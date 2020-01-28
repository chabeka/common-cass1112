package fr.urssaf.image.sae.trace.dao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;

import fr.urssaf.image.sae.trace.model.TraceToCreate;

/**
 * Classe de modèle générique d'une trace
 */
public class Trace {

   /** Identifiant de la trace */
  @PartitionKey
  private UUID identifiant;

   /** Date de création de la trace */
  private Date timestamp;

   /** code de l'événement */
  @Column(name = "codeevt")
   private String codeEvt;

   /** login de l'utilisateur */
   private String login;

   /** code du contrat de service */
  @Column(name = "contratservice")
   private String contratService;

   /** Le ou les PAGM */
   private List<String> pagms = new ArrayList<>();

   
  /**
   *
   */
  public Trace() {

  }

   /**
    * Constructeur
    * 
    * @param idTrace
    *           l'identifiant unique à affecter à la trace
    * @param timestamp
    *           le timestamp à affecter à la trace
    */
  public Trace(final UUID idTrace, final Date timestamp) {
      identifiant = idTrace;
      this.timestamp = getDateCopy(timestamp);
   }

   /**
    * Constructeur
    * 
    * @param trace
    *           trace d'origine
    * @param listInfos
    *           la liste des informations supplémentaires à récupérer
    * @param idTrace
    *           l'identifiant unique à affecter à la trace
    * @param timestamp
    *           le timestamp à affecter à la trace
    */
  public Trace(final TraceToCreate trace, final List<String> listInfos,
               final UUID idTrace, final Date timestamp) {
      codeEvt = trace.getCodeEvt();
      contratService = trace.getContrat();
      if (CollectionUtils.isNotEmpty(trace.getPagms())) {
         pagms.addAll(trace.getPagms());
      }
      login = trace.getLogin();
      this.timestamp =getDateCopy(timestamp);
      identifiant = idTrace;

   }

   /**
    * @return l'Identifiant de la trace
    */
   public final UUID getIdentifiant() {
      return identifiant;
   }

   /**
    * @return la Date de création de la trace
    */
   public final Date getTimestamp() {
      return getDateCopy(timestamp);
   }

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
    * @return le login de l'utilisateur
    */
   public final String getLogin() {
      return login;
   }

   /**
    * @param login
    *           login de l'utilisateur
    */
  public final void setLogin(final String login) {
      this.login = login;
   }

   /**
    * @return le code du contrat de service
    */
   public final String getContratService() {
      return contratService;
   }

   /**
    * @param contrat
    *           code du contrat de service
    */
  public final void setContratService(final String contrat) {
      contratService = contrat;
   }

   /**
    * Le ou les PAGM
    * 
    * @return Le ou les PAGM
    */
   public final List<String> getPagms() {
      return pagms;
   }

   /**
    * Le ou les PAGM
    * 
    * @param pagms
    *           Le ou les PAGM
    */
  public final void setPagms(final List<String> pagms) {
      this.pagms = pagms;
   }

  
  private Date getDateCopy(final Date date) {
      Date tDate = null;
      if (date != null) {
         tDate = new Date(date.getTime());
      }
      return tDate;
   }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeEvt == null) ? 0 : codeEvt.hashCode());
		result = prime * result + ((contratService == null) ? 0 : contratService.hashCode());
		result = prime * result + ((identifiant == null) ? 0 : identifiant.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((pagms == null) ? 0 : pagms.hashCode());
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
		Trace other = (Trace) obj;
		if (codeEvt == null) {
			if (other.codeEvt != null)
				return false;
		} else if (!codeEvt.equals(other.codeEvt))
			return false;
		if (contratService == null) {
			if (other.contratService != null)
				return false;
		} else if (!contratService.equals(other.contratService))
			return false;
		if (identifiant == null) {
			if (other.identifiant != null)
				return false;
		} else if (!identifiant.equals(other.identifiant))
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (pagms == null) {
			if (other.pagms != null)
				return false;
		} else if (!pagms.equals(other.pagms))
			return false;
		return true;
	}
	   
	  
}
