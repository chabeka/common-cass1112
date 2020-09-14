package fr.urssaf.image.sae.pile.travaux.model;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;

/**
 * Un nouveau travail à ajouter à la pile des travaux
 */
@SuppressWarnings("pmd:MethodReturnsInternalArray")
// Pour des raisons de performance on ignore la règle sonar ci-dessus
public class JobToCreate {

  private UUID idJob;

  private String type;

  @Deprecated
  private String parameters;

  private Map<String,String> jobParameters;

  private Date creationDate;

  private String saeHost;

  private String clientHost;

  private Integer docCount;

  private Integer docCountTraite;

  private VIContenuExtrait viExtrait;

  byte[] jobKey;

  /**
   * @return the idJob
   */
  public final UUID getIdJob() {
    return idJob;
  }

  /**
   * @param idJob
   *           the idJob to set
   */
  public final void setIdJob(final UUID idJob) {
    this.idJob = idJob;
  }

  /**
   * @return the type
   */
  public final String getType() {
    return type;
  }

  /**
   * @param type
   *           the type to set
   */
  public final void setType(final String type) {
    this.type = type;
  }

  /**
   * @return the parameters
   */
  @Deprecated
  public final String getParameters() {
    return parameters;
  }

  /**
   * @param parameters
   *           the parameters to set
   */
  @Deprecated
  public final void setParameters(final String parameters) {
    this.parameters = parameters;
  }

  /**
   * @return the creationDate
   */
  public final Date getCreationDate() {
    // On ne renvoie pas la date directement, car c'est un objet mutable
    return creationDate == null ? null : new Date(creationDate.getTime());
  }

  /**
   * @param creationDate
   *           the creationDate to set
   */
  public final void setCreationDate(final Date creationDate) {
    this.creationDate = creationDate == null ? null : new Date(creationDate
                                                               .getTime());
  }

  /**
   * @return le nom de machine ou l'IP de la machine SAE ayant traité la
   *         demande
   */
  public final String getSaeHost() {
    return saeHost;
  }

  /**
   * @param saeHost
   *           le nom de machine ou l'IP de la machine SAE ayant traité la
   *           demande
   * 
   */
  public final void setSaeHost(final String saeHost) {
    this.saeHost = saeHost;
  }

  /**
   * @return le nom de machine ou l'IP de la machine cliente ayant traité la
   *         demande
   */
  public final String getClientHost() {
    return clientHost;
  }

  /**
   * @param clientHost
   *           le nom de machine ou l'IP de la machine cliente ayant traité la
   *           demande
   */
  public final void setClientHost(final String clientHost) {
    this.clientHost = clientHost;
  }

  /**
   * @return le nombre de documents présents dans le fichier sommaire
   */
  public final Integer getDocCount() {
    return docCount;
  }

  /**
   * @param docCount
   *           le nombre de documents présents dans le fichier sommaire
   */
  public final void setDocCount(final Integer docCount) {
    this.docCount = docCount;
  }

  /**
   * @return le contenu du VI
   */
  public final VIContenuExtrait getVi() {
    return viExtrait;
  }

  /**
   * @param viExtrait le contenu du VI
   */
  public final void setVi(final VIContenuExtrait viExtrait) {
    this.viExtrait = viExtrait;
  }

  /**
   * 
   * @return Tous les paramètres du job
   */
  public final Map<String, String> getJobParameters() {
    return jobParameters;
  }

  /**
   * 
   * @param jobParameters Les paramètres du job
   */
  public final void setJobParameters(final Map<String, String> jobParameters) {
    this.jobParameters = jobParameters;
  }

  /**
   * Getter pour jobKey
   * 
   * @return the jobKey
   */
  public byte[] getJobKey() {
    return jobKey;
  }

  /**
   * Setter pour jobKey
   * 
   * @param jobKey
   *           the jobKey to set
   */
  public void setJobKey(final byte[] jobKey) {
    this.jobKey = jobKey;
  }

  /**
   * Getter pour docCountTraite
   * 
   * @return the docCountTraite
   */
  public Integer getDocCountTraite() {
    return docCountTraite;
  }

  /**
   * Setter pour docCountTraite
   * 
   * @param docCountTraite
   *           the docCountTraite to set
   */
  public void setDocCountTraite(final Integer docCountTraite) {
    this.docCountTraite = docCountTraite;
  }

}
