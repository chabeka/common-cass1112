/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.dao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;

import fr.urssaf.image.sae.trace.commons.Constantes;
import fr.urssaf.image.sae.trace.model.TraceToCreate;

/**
 * TODO (AC75095028) Description du type
 */
public class TraceCql {

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
  private List<String> pagms = new ArrayList<String>();

  /** informations supplémentaires de la trace */
  private Map<String, String> infos;

  /**
   *
   */
  public TraceCql() {

  }

  /**
   * Constructeur
   *
   * @param idTrace
   *          l'identifiant unique à affecter à la trace
   * @param timestamp
   *          le timestamp à affecter à la trace
   */
  public TraceCql(final UUID idTrace, final Date timestamp) {
    this.identifiant = idTrace;
    this.timestamp = getDateCopy(timestamp);
  }

  /**
   * Constructeur
   *
   * @param trace
   *          trace d'origine
   * @param listInfos
   *          la liste des informations supplémentaires à récupérer
   * @param idTrace
   *          l'identifiant unique à affecter à la trace
   * @param timestamp
   *          le timestamp à affecter à la trace
   */
  public TraceCql(final TraceToCreate trace, final List<String> listInfos,
                  final UUID idTrace, final Date timestamp) {
    this.codeEvt = trace.getCodeEvt();
    this.contratService = trace.getContrat();
    if (CollectionUtils.isNotEmpty(trace.getPagms())) {
      this.pagms.addAll(trace.getPagms());
    }
    this.login = trace.getLogin();
    this.timestamp = getDateCopy(timestamp);
    this.identifiant = idTrace;

    if (CollectionUtils.isNotEmpty(listInfos)
        && MapUtils.isNotEmpty(trace.getInfos())) {
      this.infos = new HashMap<String, String>();
      for (final String info : listInfos) {
        if (trace.getInfos().get(info) != null) {
          this.infos.put(info, trace.getInfos().get(info).toString());
        }
      }

      // on récupère toutes les infos
      if (listInfos.size() == 1
          && Constantes.REG_ALL_INFOS.equals(listInfos.get(0))) {
        infos.putAll(trace.getInfosCql());
      }
    }
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
   *          code de l'événement
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
   *          login de l'utilisateur
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
   *          code du contrat de service
   */
  public final void setContratService(final String contrat) {
    this.contratService = contrat;
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
   *          Le ou les PAGM
   */
  public final void setPagms(final List<String> pagms) {
    this.pagms = pagms;
  }

  /**
   * @return les informations supplémentaires de la trace
   */
  public final Map<String, String> getInfos() {
    return infos;
  }

  /**
   * @param infos
   *          tinformations supplémentaires de la trace
   */
  public final void setInfos(final Map<String, String> infos) {
    this.infos = infos;
  }

  private Date getDateCopy(final Date date) {
    Date tDate = null;
    if (date != null) {
      tDate = new Date(date.getTime());
    }
    return tDate;
  }

}
