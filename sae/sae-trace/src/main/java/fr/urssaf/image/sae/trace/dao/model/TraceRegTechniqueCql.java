package fr.urssaf.image.sae.trace.dao.model;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.Table;

import fr.urssaf.image.sae.trace.model.TraceToCreate;

@Table(name = "traceregtechnique")
public class TraceRegTechniqueCql extends Trace {

  /** Contexte de la trace */
  private String contexte;

  /** Trace technique de l'exception */
  private String stacktrace;

  /** informations supplémentaires de la trace */
  private Map<String, String> infos;

  /**
   * Constructeur
   *
   * @param idTrace
   *          l'identifiant unique à affecter à la trace
   * @param timestamp
   *          le timestamp à affecter à la trace
   */
  public TraceRegTechniqueCql(final UUID idTrace, final Date timestamp) {
    super(idTrace, timestamp);
  }

  /**
   * Constructeur
   *
   * @param trace
   *          trace d'origine
   * @param listInfos
   *          liste des informations supplémentaires à récupérer
   * @param idTrace
   *          l'identifiant unique à affecter à la trace
   * @param timestamp
   *          le timestamp à affecter à la trace
   */
  public TraceRegTechniqueCql(final TraceToCreate trace, final List<String> listInfos,
                              final UUID idTrace, final Date timestamp) {

    super(trace, listInfos, idTrace, timestamp);
    this.contexte = trace.getContexte();
    this.stacktrace = trace.getStracktrace();
  }

  /**
   * @return le Contexte de la trace
   */
  public final String getContexte() {
    return contexte;
  }

  /**
   * @param contexte
   *          Contexte de la trace
   */
  public final void setContexte(final String contexte) {
    this.contexte = contexte;
  }

  /**
   * @return la Trace technique de l'exception
   */
  public final String getStacktrace() {
    return stacktrace;
  }

  /**
   * @param stacktrace
   *          Trace technique de l'exception
   */
  public final void setStacktrace(final String stacktrace) {
    this.stacktrace = stacktrace;
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
}
