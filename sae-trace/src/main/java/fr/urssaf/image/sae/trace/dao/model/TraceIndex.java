package fr.urssaf.image.sae.trace.dao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.Column;

/**
 * Classe de modèle générique pour les CF des traces
 */
public class TraceIndex {

  /**
   * Identifiant de la trace
   */
  @Column(name = "identifiant")
  private UUID identifiant;

  /**
   * Date de création de la trace
   */
  @Column(name = "timestamp")
  private Date timestamp;

  /**
   * Login de l'utilisateur
   */
  @Column(name = "login")
  private String login;

  /**
   * Code événement
   */
  @Column(name = "codeevt")
  private String codeEvt;

  /** Le ou les PAGM */
  @Column(name = "pagms")
  private List<String> pagms = new ArrayList<>();

  /**
   * Constructeur par défaut
   */
  public TraceIndex() {
    // constructeur par défaut
  }

  /**
   * Constructeur
   * 
   * @param exploitation
   *           trace d'exploitation
   */
  public TraceIndex(final Trace exploitation) {
    codeEvt = exploitation.getCodeEvt();
    pagms.addAll(exploitation.getPagms());
    identifiant = exploitation.getIdentifiant();
    login = exploitation.getLogin();
    timestamp = exploitation.getTimestamp();
  }

  /**
   * @return l'identifiant de la trace
   */
  public final UUID getIdentifiant() {
    return identifiant;
  }

  /**
   * @param identifiant
   *           l'identifiant de la trace
   */
  public final void setIdentifiant(final UUID identifiant) {
    this.identifiant = identifiant;
  }

  /**
   * @return la date de création de la trace
   */
  public final Date getTimestamp() {
    return getDateCopy(timestamp);
  }

  /**
   * @param timestamp
   *           la date de création de la trace
   */
  public final void setTimestamp(final Date timestamp) {
    this.timestamp = getDateCopy(timestamp);
  }

  /**
   * @return le login de l'utilisateur
   */
  public final String getLogin() {
    return login;
  }

  /**
   * @param login
   *           le login de l'utilisateur
   */
  public final void setLogin(final String login) {
    this.login = login;
  }

  /**
   * @return les pagms
   */
  public final List<String> getPagms() {
    return pagms;
  }

  /**
   * @param pagms
   *           les pagms
   */
  public final void setPagms(final List<String> pagms) {
    this.pagms = pagms;
  }

  /**
   * @return le code événement
   */
  public final String getCodeEvt() {
    return codeEvt;
  }

  /**
   * @param codeEvt
   *           le code événement
   */
  public final void setCodeEvt(final String codeEvt) {
    this.codeEvt = codeEvt;
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
	result = prime * result + ((identifiant == null) ? 0 : identifiant.hashCode());
	result = prime * result + ((login == null) ? 0 : login.hashCode());
	result = prime * result + ((pagms == null) ? 0 : pagms.hashCode());
	result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
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
	TraceIndex other = (TraceIndex) obj;
	if (codeEvt == null) {
		if (other.codeEvt != null)
			return false;
	} else if (!codeEvt.equals(other.codeEvt))
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
	if (timestamp == null) {
		if (other.timestamp != null)
			return false;
	} else if (!timestamp.equals(other.timestamp))
		return false;
	return true;
}


}
