package fr.urssaf.image.sae.trace.dao.modelcql;

import java.util.Map;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import fr.urssaf.image.sae.trace.dao.model.TraceIndex;

@Table(name = "tracejournalevtindexdoc")
public class TraceJournalEvtIndexDocCql extends TraceIndex {

  @PartitionKey
  @Column(name = "identifiantindex")
  private UUID identifiantIndex;

  /**
   * Contexte de l'évenement
   */
  private String contexte;

  /**
   * Code du contrat de service
   */
  private String contratService;

  /**
   * Informations supplémentaires de la trace
   */
  private Map<String, String> infos;

  /**
   * Constructeur par défaut
   */
  public TraceJournalEvtIndexDocCql() {
    super();
  }

  /**
   * Constructeur
   *
   * @param traceJournal
   *          trace du journal des événements
   */
  public TraceJournalEvtIndexDocCql(final TraceJournalEvtCql traceJournal) {
    super(traceJournal);
    this.contexte = traceJournal.getContexte();
    this.contratService = traceJournal.getContratService();
  }

  /**
   * @return the identifiantIndex
   */
  public UUID getIdentifiantIndex() {
    return identifiantIndex;
  }

  /**
   * @param identifiantIndex
   *          the identifiantIndex to set
   */
  public void setIdentifiantIndex(final UUID identifiantIndex) {
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
  public final String getContratService() {
    return contratService;
  }

  /**
   * @param contratService
   *          the contratService to set
   */
  public final void setContratService(final String contratService) {
    this.contratService = contratService;
  }

  /**
   * @return the infos
   */
  public Map<String, String> getInfos() {
    return infos;
  }

  /**
   * @param infos
   *          the infos to set
   */
  public void setInfos(final Map<String, String> infos) {
    this.infos = infos;
  }

}