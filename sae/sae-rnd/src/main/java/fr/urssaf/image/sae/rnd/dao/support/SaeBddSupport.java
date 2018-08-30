/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.image.sae.rnd.dao.support;

import java.util.List;
import java.util.Map;

import fr.urssaf.image.sae.rnd.exception.SaeBddRuntimeException;
import fr.urssaf.image.sae.rnd.modele.Correspondance;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.rnd.modele.VersionRnd;

/**
 * TODO (ac75007394) Description du type
 *
 */
public interface SaeBddSupport {

   /**
    * Récupère les informations due la version actuelle du RND dans le SAE
    *
    * @return Un objet {@link VersionRnd} contenant les informations sur la
    *         version RND
    * @throws SaeBddRuntimeException
    *            Exception levée lors de la mise à jour de la BDD
    */
   VersionRnd getVersionRnd() throws SaeBddRuntimeException;

   /**
    * Met à jour les informations sur la version actuelle du RND dans le SAE
    *
    * @param versionRnd
    *           Version à mettre à jour
    * @throws SaeBddRuntimeException
    * @throws SaeBddRuntimeException
    *            Exception levée lors de la mise à jour de la BDD
    */
   void updateVersionRnd(VersionRnd versionRnd)
         throws SaeBddRuntimeException;

   /**
    * Met à jour la CF Rnd dans la bdd Cassandra
    *
    * @param listeTypeDocs
    *           Liste des types de document à mettre à jour
    * @throws SaeBddRuntimeException
    *            Exception levée lors de la mise à jour de la BDD
    */
   void updateRnd(List<TypeDocument> listeTypeDocs)
         throws SaeBddRuntimeException;

   /**
    * Met à jour la CF CorrespondancesRnd dans la base de données Cassandra et
    * passe les codes temporaires ayant une correspondance à l'état clôturé
    *
    * @param listeCorrespondances
    *           Correspondances entre codes temporaires et code
    * @param version
    *           la version en cours dans le SAE
    * @throws SaeBddRuntimeException
    *            Exception levée lors de la mise à jour de la BDD
    */
   void updateCorrespondances(
                              Map<String, String> listeCorrespondances, String version)
         throws SaeBddRuntimeException;

   /**
    * Récupère la liste de toutes les correspondances en cours dans le SAE
    *
    * @return une liste de {@link Correspondance}
    * @throws SaeBddRuntimeException
    *            Exception levée lors de la mise à jour de la BDD
    */
   List<Correspondance> getAllCorrespondances()
         throws SaeBddRuntimeException;

   /**
    * Met la correspondance à l'état démarré et positionne la date de début
    *
    * @param correspondance
    *           La correspondance dont la mise à jour des docs a commencé
    * @throws SaeBddRuntimeException
    *            Exception levée lors de la mise à jour de la BDD
    */
   void startMajCorrespondance(Correspondance correspondance)
         throws SaeBddRuntimeException;

}