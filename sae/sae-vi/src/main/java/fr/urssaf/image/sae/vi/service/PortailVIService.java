/**
 * 
 */
package fr.urssaf.image.sae.vi.service;

import fr.urssaf.image.sae.vi.exception.VIException;
import fr.urssaf.image.sae.vi.modele.VIPortailContenuExtrait;
import fr.urssaf.image.sae.vi.modele.VIPortailCreateParams;

/**
 * opérations de lecture et d'écriture du VI pour le mode portail à portail
 * 
 */
public interface PortailVIService {

   /**
    * Création d'un VI de type portail à portail
    * 
    * @param viParams
    *           les informations permettant de construire le VI
    * @return le VI sous la forme d'une chaîne de caractères (XML)
    * @throws VIException
    *            si un problème survient pendant la génération du VI
    */
   String creerVI(VIPortailCreateParams viParams) throws VIException;

   /**
    * Lecture d'un VI de type "portail à portail" et extraction des données
    * "intéressantes" de son contenu dans un objet du modèle facilement
    * utilisable
    * 
    * @param viXml
    *           le VI "portail à portail" au format XML (tel que généré par la
    *           méthode creerVIpourPortailAPortail
    * @return les informations extraites du VI
    * @throws VIException
    *            si une erreur se produit pendant la lecture du VI
    */
   VIPortailContenuExtrait lireVI(String viXml) throws VIException;
}
