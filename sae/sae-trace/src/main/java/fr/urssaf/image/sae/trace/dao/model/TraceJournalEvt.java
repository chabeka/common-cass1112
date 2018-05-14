/**
 *
 */
package fr.urssaf.image.sae.trace.dao.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.Table;

import fr.urssaf.image.sae.trace.model.TraceToCreate;

/**
 * Classe de modèle d'une trace du journal des événements
 */
@Table(name = "tracejournalevt")
public class TraceJournalEvt extends Trace {

  /**
   * Contexte de l'événement
   */
  private String contexte;

  /**
   * Constructeur
   * 
   * @param idTrace
   *          l'identifiant unique à affecter à la trace
   * @param timestamp
   *          le timestamp à affecter à la trace
   */
  public TraceJournalEvt(final UUID idTrace, final Date timestamp) {
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
  public TraceJournalEvt(final TraceToCreate trace, final List<String> listInfos,
                         final UUID idTrace, final Date timestamp) {
    super(trace, listInfos, idTrace, timestamp);
    this.contexte = trace.getContexte();
  }

  /**
   * @return the contexte
   */
  public final String getContexte() {
    return contexte;
  }

  /**
   * @param contexte
   *          the contexte to set
   */
  public final void setContexte(final String contexte) {
    this.contexte = contexte;
  }

}
