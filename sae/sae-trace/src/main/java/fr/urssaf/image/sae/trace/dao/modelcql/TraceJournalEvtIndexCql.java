/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.dao.modelcql;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import fr.urssaf.image.sae.trace.dao.model.TraceIndex;

/**
 * TODO (AC75095028) Description du type
 */
@Table(name = "tracejournalevtindexcql")
public class TraceJournalEvtIndexCql extends TraceIndex {

  @PartitionKey
  @Column(name = "identifiantindex")
  private String identifiantIndex;

  /**
   * Contexte de l'évenement
   */
  private String contexte;

  /**
   * Code du contrat de service
   */
  private String contratService;

  /**
   * Constructeur par défaut
   */
  public TraceJournalEvtIndexCql() {
    super();
  }

  /**
   * Constructeur
   *
   * @param exploitation
   *          trace d'exploitation
   */
  public TraceJournalEvtIndexCql(final TraceJournalEvtCql exploitation) {
    super(exploitation);
    this.contexte = exploitation.getContexte();
    this.contratService = exploitation.getContratService();
  }

  
  /**
   * @return the identifiantIndex
   */
  public String getIdentifiantIndex() {
    return identifiantIndex;
  }

  /**
   * @param identifiantIndex the identifiantIndex to set
   */
  public void setIdentifiantIndex(String identifiantIndex) {
    this.identifiantIndex = identifiantIndex;
  }

  /**
   * @return le contexte de l'événement
   */
  public final String getContexte() {
    return contexte;
  }

  /**
   * @param contexte
   *          le contexte de l'événement
   */
  public final void setContexte(final String contexte) {
    this.contexte = contexte;
  }

  /**
   * @return the contratService
   */
  public String getContratService() {
    return contratService;
  }

  /**
   * @param contratService
   *          the contratService to set
   */
  public void setContratService(final String contratService) {
    this.contratService = contratService;
  }

}
