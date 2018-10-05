/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import com.datastax.driver.mapping.annotations.Table;

import fr.urssaf.image.sae.trace.commons.Constantes;
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

  /** informations supplémentaires de la trace */
  private Map<String, Object> infos;

  /**
   *
   */
  public TraceJournalEvt() {
    super();
    // TODO Auto-generated constructor stub
  }

   /**
    * Constructeur
    * 
    * @param idTrace
    *           l'identifiant unique à affecter à la trace
    * @param timestamp
    *           le timestamp à affecter à la trace
    */
  public TraceJournalEvt(final UUID idTrace, final Date timestamp) {
      super(idTrace, timestamp);
   }

   /**
    * Constructeur
    * 
    * @param trace
    *           trace d'origine
    * @param listInfos
    *           liste des informations supplémentaires à récupérer
    * @param idTrace
    *           l'identifiant unique à affecter à la trace
    * @param timestamp
    *           le timestamp à affecter à la trace
    */
  public TraceJournalEvt(final TraceToCreate trace, final List<String> listInfos,
                         final UUID idTrace, final Date timestamp) {
      super(trace, listInfos, idTrace, timestamp);
      this.contexte = trace.getContexte();

    if (CollectionUtils.isNotEmpty(listInfos)
        && MapUtils.isNotEmpty(trace.getInfos())) {
      this.infos = new HashMap<String, Object>();
      for (final String info : listInfos) {
        if (trace.getInfos().get(info) != null) {
          this.infos.put(info, trace.getInfos().get(info));
        }
      }

      // on récupère toutes les infos
      if (listInfos.size() == 1
          && Constantes.REG_ALL_INFOS.equals(listInfos.get(0))) {
        infos.putAll(trace.getInfos());
      }
    }
  }

  /**
   * @return les informations supplémentaires de la trace
   */
  public final Map<String, Object> getInfos() {
    return infos;
  }

  /**
   * @param infos
   *          tinformations supplémentaires de la trace
   */
  public final void setInfos(final Map<String, Object> infos) {
    this.infos = infos;
   }

   /**
    * @return the contexte
    */
   public final String getContexte() {
      return contexte;
   }

   /**
    * @param contexte
    *           the contexte to set
    */
  public final void setContexte(final String contexte) {
      this.contexte = contexte;
   }

}
