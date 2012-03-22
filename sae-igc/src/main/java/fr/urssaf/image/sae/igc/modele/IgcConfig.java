package fr.urssaf.image.sae.igc.modele;

import java.net.URL;
import java.util.List;

/**
 * Configuration des éléments de l'IGC
 * 
 * 
 */
public class IgcConfig {

   private String acRacinesRep;

   private String crlsRep;

   private List<URL> crlsURLs;

   /**
    * 
    * Exemples de valeur de retour :
    * <ul>
    * <li>c:\sae\certificats\ACRacines</li>
    * <li>
    * /appl/sae/certificats/ACRacines</li>
    * </ul>
    * 
    * On s'attend à trouver dans ce répertoire un ensemble de fichiers .crt
    * 
    * 
    * @return Répertoire où se trouvent les certificats des AC racine en
    *         lesquelles le SAE a confiance
    */
   public final String getRepertoireACRacines() {
      return acRacinesRep;
   }

   /**
    * 
    * @param repertory
    *           Répertoire où se trouvent les certificats des AC racine en
    *           lesquelles le SAE a confiance
    */
   public final void setRepertoireACRacines(String repertory) {
      this.acRacinesRep = repertory;
   }

   /**
    * 
    * Exemples de valeur de retour :
    * <ul>
    * <li>c:\sae\certificats\CRL</li>
    * <li>
    * /appl/sae/certificats/CRL</li>
    * </ul>
    * 
    * On s'attend à trouver dans ce répertoire un ensemble de fichiers.crl
    * 
    * 
    * @return Répertoire où se trouvent les CRL de toutes les AC utilisés par le
    *         SAE
    */
   public final String getRepertoireCRLs() {
      return crlsRep;
   }

   /**
    * 
    * @param repertory
    *           Répertoire où se trouvent les CRL de toutes les AC utilisés par
    *           le SAE
    */
   public final void setRepertoireCRLs(String repertory) {
      this.crlsRep = repertory;
   }

   /**
    * Chaque URL se présente sous la forme d’un pattern<br>
    * exemple : http://serveur/repertoire/*.crl
    * 
    * @return Liste des URL de téléchargement des CRL
    */
   public final List<URL> getUrlsTelechargementCRLs() {
      return crlsURLs;
   }

   /**
    * 
    * @param urls
    *           Liste des URL de téléchargement des CRL
    */
   public final void setUrlsTelechargementCRLs(List<URL> urls) {
      this.crlsURLs = urls;
   }

}
