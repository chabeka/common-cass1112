package fr.urssaf.image.sae.ecde.service;

import java.io.File;
import java.io.OutputStream;

import fr.urssaf.image.sae.ecde.exception.EcdeXsdException;
import fr.urssaf.image.sae.ecde.modele.resultats.ResultatsType;
/**
 * Service permettant l'ecriture des fichiers resultat.xml  
 * <br>
 * Lorsqu'un traitement de capture de masse est terminé, le SAE doit générer un fichier resultats.xml au meme
 * <br>emplacement que le fichier sommaire.xml. Le fichier resultats.xml décrit le résultat<br>
 * de cette capture de masse.Il est généré quelque soit le résultat du traitement(réussite, echec partiel, echec complet).
 * <br>
 * 
 */
public interface ResultatsXmlService {
   
   /**
    * Methode permettant l'ecriture du fichier resultats.xml
    * 
    * @param resultatsXml objet representant le contenu du resultats.xml
    * @param output flux dans lequel écrire resultats.xml
    * @throws EcdeXsdException erreur de structure a été détectée sur le resultats.xml
    */
   void writeResultatsXml(ResultatsType resultatsXml, OutputStream output) throws EcdeXsdException;
   /**
    * Methode permettant l'ecriture du fichier resultats.xml
    * 
    * @param resultatsXml objet representant le contenu du resultats.xml
    * @param output fichier dans lequel écrire resultats.xml
    * @throws EcdeXsdException erreur de structure a été détectée sur le resultats.xml
    */
   void writeResultatsXml(ResultatsType resultatsXml, File output)  throws EcdeXsdException;
}
