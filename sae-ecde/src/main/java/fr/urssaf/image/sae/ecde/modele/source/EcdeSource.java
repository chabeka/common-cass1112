package fr.urssaf.image.sae.ecde.modele.source;

import java.io.File;

/**
 *  Cette classe permet la configuration d'un ECDE
 *  <br>
 *  Elle a comme attribut :
 *  <ul>
 *    <li>Host : DNS de l'ECDE. Son format doit être conforme à la syntaxe du host dans une URL</li>
 *    <li>basePath : Chemin absolu du répertoire racine de l'ECDE accessible en local.</li>
 *  </ul>
 *  
 *  
 * 
 * */
@SuppressWarnings("PMD")
public class EcdeSource {
   
   /**
    * Constructeur
    * 
    * @param host de l'uri
    * @param basePath chemin  
    */
   public EcdeSource(String host, File basePath) {
      
      this.host = host;
      this.basePath = basePath;
   }
   
   // Attributs
   private String host; 
   private File basePath;
   /**
    * @return l'hote
    */
   public final String getHost() {
      return host;
   }
   /**
    * @param host l'hote à configurer
    */
   public final void setHost(String host) {
      this.host = host;
   }
   /**
    * @return le chemin absolu
    */
   public final File getBasePath() {
      return basePath;
   }
   /**
    * @param basePath le chemin a configurer
    */
   public final void setBasePath(File basePath) {
      this.basePath = basePath;
   }
}
