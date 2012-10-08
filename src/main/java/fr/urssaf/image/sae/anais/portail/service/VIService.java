package fr.urssaf.image.sae.anais.portail.service;

import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisAuth;
import fr.urssaf.image.sae.anais.portail.exception.VIBuildException;

/**
 * Service de génération du VI
 */
public interface VIService {

   /**
    * Construction d'un VI à partir d'une recherche d'habiliation dans ANAIS
    * 
    * @param auth
    *           le résultat de l'authentification dans ANAIS
    * @return le VI au format XML
    * @throws VIBuildException
    *            si un problème se produit lors de la génération du VI
    */
   public String buildVI(SaeAnaisAuth auth) throws VIBuildException;

}
