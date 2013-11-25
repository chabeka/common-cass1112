package fr.urssaf.image.sae.services.capturemasse.utils;

import java.io.File;

import javax.xml.stream.XMLStreamException;

/**
 * Interface permettant de réaliser les opérations d'écriture de fichiers XML
 * volumineux.
 * <i><u>NB. Cette interface a été réalisée pour pouvoir simuler les cas de coupure
 * réseau lors de l'écriture du fichier de résultat.</u></i>
 * 
 * 
 */
public interface StaxUtils {

   /**
    * Initialisation du stream
    * 
    * @param file
    *           fichier à écrire
    */
   void initStream(File file);

   /**
    * Fermeture des flux d'écriture
    */
   void closeAll();

   /**
    * Ajout d'un prefixe dans le document
    * 
    * @param prefix
    *           préfixe à utiliser
    * @param uri
    *           uri à utiliser
    * @throws XMLStreamException
    *            exception levée si erreur d'écriture
    */
   void addPrefix(String prefix, String uri);

   /**
    * Ajout d'un prefixe dans le document
    * 
    * @param uri
    *           uri à utiliser
    */
   void addDefaultPrefix(String uri);

   /**
    * Création du tag de départ
    * 
    * @param name
    *           nom de la balise
    * @param prefix
    *           préfixe
    * @param url
    *           namespace
    * @throws XMLStreamException
    *            exception levée si erreur d'écriture
    */
   void addStartElement(String name, String prefix, String url);

   /**
    * Ajout d'un élément de fin
    * 
    * @param name
    *           nom de la balise
    * @param prefix
    *           préfixe
    * @param url
    *           namespace
    */
   void addEndElement(String name, String prefix, String url);

   /**
    * Ecriture du début du document
    */
   void startDocument();

   /**
    * Création du début de tag
    * 
    * @param name
    *           Nom de la balise
    * @param prefix
    *           préfixe
    * @param url
    *           namespace
    */
   void addStartTag(String name, String prefix, String url);

   /**
    * création du tag de fin
    * 
    * @param name
    *           nom de la balise
    * @param prefix
    *           préfixe
    * @param url
    *           namespace
    */
   void addEndTag(String name, String prefix, String url);

   /**
    * Insertion de la valeur
    * 
    * @param value
    *           valeur
    * @throws XMLStreamException
    *            exception levée s'il est impossible de créer le tag
    */
   void addValue(String value);

   /**
    * Création du tag complet :
    * <ul>
    * <li>balise de début</li>
    * <li>valeur</li>
    * <li>balise de fin</li>
    * </ul>
    * 
    * @param name
    *           nom de la balise
    * @param value
    *           valeur
    * @param prefix
    *           préfixe
    * @param url
    *           namespace
    */
   void createTag(String name, String value, String prefix, String url);

}