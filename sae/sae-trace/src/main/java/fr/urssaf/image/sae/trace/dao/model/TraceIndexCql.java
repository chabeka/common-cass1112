/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.dao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * TODO (AC75095028) Description du type
 */
@Table(name = "traceindexcql")
public class TraceIndexCql {

  /**
   * Identifiant de la trace
   */
  @PartitionKey
  private String identifiant;

  /**
   * Identifiant de la trace
   */
  @Column(name = "traceid")
  private UUID traceId;

  /**
   * Date de création de la trace
   */
  // @ClusteringColumn
  private Date timestamp;

  /**
   * Login de l'utilisateur
   */
  private String login;

  /**
   * Code événement
   */
  @Column(name = "codeevt")
  private String codeEvt;

  /** Le ou les PAGM */
  private List<String> pagms = new ArrayList<String>();

  /**
   * Constructeur par défaut
   */
  public TraceIndexCql() {
    // constructeur par défaut
  }

  /**
   * Constructeur
   *
   * @param exploitation
   *          trace d'exploitation
   */
  public TraceIndexCql(final Trace exploitation) {
    this.codeEvt = exploitation.getCodeEvt();
    this.pagms.addAll(exploitation.getPagms());
    this.traceId = exploitation.getIdentifiant();
    this.login = exploitation.getLogin();
    this.timestamp = exploitation.getTimestamp();
  }

  /**
   * @return the identifiant
   */
  public String getIdentifiant() {
    return identifiant;
  }

  /**
   * @param identifiant
   *          the identifiant to set
   */
  public void setIdentifiant(final String identifiant) {
    this.identifiant = identifiant;
  }

  /**
   * @return l'identifiant de la trace
   */
  public final UUID getTraceId() {
    return traceId;
  }

  /**
   * @param identifiant
   *          l'identifiant de la trace
   */
  public final void setTraceId(final UUID identifiant) {
    this.traceId = identifiant;
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
