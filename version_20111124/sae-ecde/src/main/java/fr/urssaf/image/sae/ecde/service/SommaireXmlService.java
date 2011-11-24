package fr.urssaf.image.sae.ecde.service;

import java.io.File;
import java.io.InputStream;

import fr.urssaf.image.sae.ecde.exception.EcdeXsdException;
import fr.urssaf.image.sae.ecde.modele.sommaire.SommaireType;
/**
 * Service permettant la lecture des fichiers sommaire.xml 
 * <br>
 * Fichier sommaire.xml:<br>
 * Pour demander la réalisation d'une capture de mass, une application cliente du SAE doit, notamment, écrire<br>
 * dans le répertoire adéquat de l'ECDE un fichier sommaire.xml, qui contient la liste des documents à mettre en archive.
 * <br>Dans ce sommaire.xml chaque document est défini globalement par un chemin de fichier ainsi que <br>
 * par des metadonnées décrivant le document.
 * <br>
 * Cette fonctionnalité permet la lecture des fichiers sommaires.xml. Un fichier sommaire.xml<br>
 * doit être representé via le modèle objet par un objet de type
 * <br>{@link fr.urssaf.image.sae.ecde.modele.sommaire.SommaireType}  
 * 
 * 
 * 
 */
public interface SommaireXmlService {

   /**
    * Methode permettant la lecture du fichier sommaire.xml
    * <br>avec en entree un flux
    *
    * @param input de type InputStream
    * @return SommaireType objet representant le contenu d'un fichier sommaire.xml
    *
    * @throws EcdeXsdException une erreur de structure a été detectée sur le sommaire.xml 
    */
    SommaireType readSommaireXml(InputStream input) throws EcdeXsdException;
   /**
    * Methode permettant la lecture du fichier sommaire.xml
    * <br>avec en entree un flux
    *
    * @param input de type File
    * @return SommaireType objet representant le contenu d'un fichier sommaire.xml
    *
    * @throws EcdeXsdException une erreur de structure a été detectée sur le sommaire.xml
    */
    SommaireType readSommaireXml(File input) throws EcdeXsdException;
}
