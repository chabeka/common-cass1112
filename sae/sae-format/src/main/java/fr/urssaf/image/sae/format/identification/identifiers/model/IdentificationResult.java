package fr.urssaf.image.sae.format.identification.identifiers.model;

import java.util.List;

import fr.urssaf.image.sae.format.model.EtapeEtResultat;

/**
 * 
 * Classe décrivant la structure du résultat à renvoyer après une
 * identification.
 * 
 */
public class IdentificationResult {

   private boolean identified;
   private List<EtapeEtResultat> listEtapEtResultat;

   /**
    * Méthode permettant de savoir si le fichier ou flux a été correctement
    * identifié.
    * 
    * @return true si le fichier ou flux à été correctement identifié.
    */
   public final boolean isIdentified() {
      return identified;
   }

   /**
    * Méthode permettant de savoir si le fichier ou flux a été correctement
    * identifié.
    * 
    * @param identified
    *           : si le fichier est identifié
    */
   public final void setIdentified(boolean identified) {
      this.identified = identified;
   }

   /**
    * Méthode permettant d’avoir la trace d’exécution du processus
    * d’identification.
    * 
    * @return Une liste des étapes réalisées pour arriver à l’identification du
    *         fichier.
    */
   public final List<EtapeEtResultat> getDetails() {
      return listEtapEtResultat;
   }

   /**
    * Méthode permettant d’avoir la trace d’exécution du processus
    * d’identification.
    * 
    * @param listEtapEtResultat
    *           : liste des anomalies
    */
   public final void setDetails(List<EtapeEtResultat> listEtapEtResultat) {
      this.listEtapEtResultat = listEtapEtResultat;
   }

}
