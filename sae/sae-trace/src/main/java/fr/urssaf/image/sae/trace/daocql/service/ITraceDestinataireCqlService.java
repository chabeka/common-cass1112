/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.daocql.service;

import java.util.List;
import java.util.Optional;

import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;

/**
 * TODO (AC75095028) Description du type
 */
public interface ITraceDestinataireCqlService {

  /**
   * Récupération des codes evenements par type de traces
   *
   * @param typeTrace
   *          Trace à créer
   * @return Liste des codes Evenements
   */
  List<String> getCodeEvenementByTypeTrace(String typeTrace);

  /**
   * Création d'une colonne
   *
   * @param trace
   *          trace à créer
   * @param clock
   *          horloge de la création
   */
  public void create(TraceDestinataire trace, long clock);

  /**
   * Méthode de suppression d'une ligne
   *
   * @param code
   *          identifiant de la ligne
   * @param clock
   *          horloge de suppression
   */
  public void delete(final String code, final long clock);

  /**
   * Recherche et retourne l'enregistrement de la trace destinataire en
   * fonction du code fourni
   *
   * @param code
   *          code de la trace destinataire
   * @return l'enregistrement de la trace destinataire correspondante
   */
  public Optional<TraceDestinataire> findById(final String code);

  /**
   * Retourne l'ensemble des destinataires des traces
   *
   * @return l'ensemble des destinataires des traces
   */
  public List<TraceDestinataire> findAll();

  /**
   * @param trace
   */
  void update(TraceDestinataire trace);

}
