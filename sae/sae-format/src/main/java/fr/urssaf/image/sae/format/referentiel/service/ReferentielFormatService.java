package fr.urssaf.image.sae.format.referentiel.service;

import java.util.List;

import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;

/**
 * Interface pour la manipulation du référentiel des formats.
 * 
 */
public interface ReferentielFormatService {

   /**
    * Ajoute un nouveau format de fichier
    * 
    * @param refFormat
    *           le referentiel format à ajouter.
    * 
    */
   void addFormat(FormatFichier refFormat);

   /**
    * Supprime un format de fichier
    * 
    * @param idFormat
    *           identifiant du format à supprimer - paramètre obligatoire
    * @throws UnknownFormatException
    *            : Le format à supprimer n'existe pas en base
    */
   void deleteFormat(String idFormat) throws UnknownFormatException;

   /**
    * Récupère les informations relatives à un format donné
    * 
    * @param idFormat
    *           Identifiant du format de fichier à recupérer - paramètre
    *           obligatoire
    * 
    * @return le ReferentielFormat correspondant à l'idFormat donné en param.
    * 
    * @throws UnknownFormatException
    *            : Le format à supprimer n'existe pas en base
    */
   FormatFichier getFormat(String idFormat) throws UnknownFormatException;

   /**
    * Récupère tous les formats du référentiel.
    * 
    * @return List<ReferentielFormat>
    */
   List<FormatFichier> getAllFormat();

   /**
    * @param idFormat
    *           identifiant du format
    * @return <ul>
    *         <li><b>true</b> si l'identifiant du format existe en base</li>
    *         <li><b>false</b> sinon</li>
    *         </ul>
    */
   boolean exists(String idFormat);

}
