/**
 *
 */
package fr.urssaf.image.sae.trace.dao.model;

import java.util.List;
import java.util.Map;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Classe de modèle d'une trace du registre de sécurité
 */
@Table(name = "tracedestinataire")
public class TraceDestinataire {

  /** code de l'événement */
  @PartitionKey
  private String codeevt;

  /**
   * <ul>
   * <li>key : type de destinataire</li>
   * <li>values : liste des propriétés à transmettre au destinataire</li>
   * </ul>
   */
  private Map<String, List<String>> destinataires;

  /**
   * @return le code de l'événement
   */
  public final String getCodeEvt() {
    return codeevt;
  }

  /**
   * @param codeEvt
   *          code de l'événement
   */
  public final void setCodeEvt(final String codeEvt) {
    this.codeevt = codeEvt;
  }

  /**
   * @return
   *         <ul>
   *         <li>key : type de destinataire</li>
   *         <li>values : liste des propriétés à transmettre au destinataire</li>
   *         </ul>
   */
  public final Map<String, List<String>> getDestinataires() {
    return destinataires;
  }

  /**
   * @param destinataires
   *          <ul>
   *          <li>key : type de destinataire</li>
   *          <li>values : liste des propriétés à transmettre au destinataire</li>
   *          </ul>
   */
  public final void setDestinataires(final Map<String, List<String>> destinataires) {
    this.destinataires = destinataires;
  }

}
