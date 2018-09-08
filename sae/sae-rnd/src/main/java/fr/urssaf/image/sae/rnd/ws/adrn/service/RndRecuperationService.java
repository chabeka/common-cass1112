package fr.urssaf.image.sae.rnd.ws.adrn.service;

import java.util.List;
import java.util.Map;

import fr.urssaf.image.sae.rnd.exception.RndRecuperationException;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;

/**
 * Service de récupération du RND à partir des WS de l'ADRN
 * 
 * 
 */
public interface RndRecuperationService {

   /**
    * Récupère la version en cours dans l'ADRN
    * 
    * @return la version en cours dans l'ADRN
    * @throws RndRecuperationException
    *            Exception levée lors d'une erreur à l'appel du WS
    */
   String getVersionCourante() throws RndRecuperationException;

   /**
    * Récupère la liste des types de documents du RND correspondant à une
    * version donnée
    * 
    * @param version
    *           la version à récupérer
    * @return le liste des types de documents contenus dans cette version du RND
    * @throws RndRecuperationException
    *            Exception levée lors d'une erreur à l'appel du WS
    */
   List<TypeDocument> getListeRnd(String version)
         throws RndRecuperationException;

   /**
    * Récupère la liste des correspondances entre codes types temporaires et
    * codes types définitifs
    * 
    * @param version
    *           la version à récupérer
    * @return Liste des correspondances
    * @throws RndRecuperationException
    *            Exception levée lors d'une erreur à l'appel du WS
    */
   Map<String, String> getListeCorrespondances(String version)
         throws RndRecuperationException;

   /**
    * Récupère la liste des codes temporaires
    * 
    * @return La liste des codes temporaires
    * @throws RndRecuperationException
    *            Exception levée lors d'une erreur à l'appel du WS
    */
   List<TypeDocument> getListeCodesTemporaires()
         throws RndRecuperationException;
}
