package fr.urssaf.image.sae.trace.dao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Classe de modèle générique pour les CF des traces
 */
public class TraceIndex {

  /**
   * Identifiant de la trace
   */
  private UUID identifiant;

  /**
   * Date de création de la trace
   */
  private Date timestamp;

  /**
   * Login de l'utilisateur
   */
  private String login;

  /**
   * Code événement
   */
  private String codeEvt;

  /** Le ou les PAGM */
  private List<String> pagms = new ArrayList<String>();

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
   *          trace d'exploitation
   */
  public TraceIndex(final Trace exploitation) {
    this.codeEvt = exploitation.getCodeEvt();
    this.pagms.addAll(exploitation.getPagms());
    this.identifiant = exploitation.getIdentifiant();
    this.login = exploitation.getLogin();
    this.timestamp = exploitation.getTimestamp();
  }

  /**
   * @return l'identifiant de la trace
   */
  public final UUID getIdentifiant() {
    return identifiant;
  }

  /**
   * @param identifiant
   *          l'identifiant de la trace
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
   *          la date de création de la trace
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
   *          le login de l'utilisateur
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
   *          les pagms
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
   *          le code événement
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

}
