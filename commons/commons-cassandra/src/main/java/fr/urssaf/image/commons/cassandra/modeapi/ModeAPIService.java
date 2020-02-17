package fr.urssaf.image.commons.cassandra.modeapi;

import fr.urssaf.image.commons.cassandra.exception.ModeGestionAPIUnkownException;

/**
 * Service de récupération du modeAPI
 */
public interface ModeAPIService {

  /**
   * Récupère le modeAPI d'une table
   * 
   * @param cfName
   *          nom de la table en minuscule
   * @return le modeAPI
   * @throws ModeGestionAPIUnkownException
   *           Exception levée si le code ModeAPI n'existe pas
   */
  String getModeAPI(String cfName) throws ModeGestionAPIUnkownException;

}
