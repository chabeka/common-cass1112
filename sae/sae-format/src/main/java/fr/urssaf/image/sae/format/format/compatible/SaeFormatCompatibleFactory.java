package fr.urssaf.image.sae.format.format.compatible;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.format.format.compatible.model.SaeFormatCompatible;
import fr.urssaf.image.sae.format.referentiel.exceptions.ReferentielRuntimeException;

/**
 * Factory permettant de créer un objet {@link SaeFormatCompatible}.
 * 
 * Permet de lire le fichier properites :
 * sae_format_compatible_format.properties
 * 
 * Ainsi à partir d'une clé il est possible de récupérer la valeur associée.
 * Dans notre cas : idFormat (clé) -> renvoie tous les formats compatibles.
 * Exemple : fmt/354=fmt/18,fmt/95
 * 
 */
@Component
public final class SaeFormatCompatibleFactory {

   /**
    * Methode permettant de créer un objet SaeFormatCompatible.
    * 
    * @param saeFormatCompatibleFile
    *           fichier properties contenant les formats compatibles
    * @return SaeFormatCompatible contenant les informations compatibles liées
    *         au format.
    */
   public SaeFormatCompatible createSaeFormatCompatible(
         ClassPathResource saeFormatCompatibleFile) {

      Properties prop = new Properties();
      SaeFormatCompatible saeFormat = new SaeFormatCompatible();

      // Chargement du fichier .properties
      InputStream inputStream = null;
      try {
         inputStream = saeFormatCompatibleFile.getInputStream();
         prop.load(inputStream);
      } catch (IOException exception) {
         throw new ReferentielRuntimeException(
               "Erreur dans la récupération des formats compatibles.",
               exception);
      } finally {
         if (inputStream != null) {
            try {
               inputStream.close();
            } catch (IOException e) {
               throw new ReferentielRuntimeException(
                     "Erreur dans la récupération des formats compatibles.", e);
            }
         }
      }

      // On parcourt toutes les clés
      // Clé = id du format
      // Valeur associée = liste des id des formats compatibles, séparés par
      // une virgule
      Enumeration<?> propNames = prop.propertyNames();
      String idFormat;
      List<String> compatibles;
      while (propNames.hasMoreElements()) {

         // Clé = identifiant du format
         idFormat = (String) propNames.nextElement();

         // Liste des formats compatibles
         compatibles = Arrays.asList(StringUtils.split(prop
               .getProperty(idFormat), ','));

         // Ajoute la liste de formats compatibles
         saeFormat.addFormatsCompatible(idFormat, compatibles);

      }

      return saeFormat;

   }

}
