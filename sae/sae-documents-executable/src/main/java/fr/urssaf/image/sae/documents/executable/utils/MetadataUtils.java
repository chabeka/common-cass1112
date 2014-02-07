package fr.urssaf.image.sae.documents.executable.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import net.docubase.toolkit.model.document.Document;

/**
 * Cette classe contient les methodes pour récupérer les métadonnées du document
 * DFCE.
 */
public final class MetadataUtils {

   /** Cette classe n'est pas faite pour être instanciée. */
   private MetadataUtils() {
      assert false;
   }

   /**
    * Methode permettant de recuperer la valeur d'une metadonnée par son code.
    * Attention, cette methode ne marche que pour les metadonnées qui ne
    * commencent pas par <b>SM_</b>.
    * 
    * @param document
    *           document DFCE
    * @param code
    *           code
    * @return Object
    */
   public static Object getMetadataByCd(final Document document,
         final String code) {
      Object value = "";
      if (!document.getCriterions(code).isEmpty()) {
         value = document.getCriterions(code).get(0).getWord();
      }
      return value;
   }

   /**
    * Methode permettant de recuperer la valeur d'une metadonnée par son code.
    * 
    * @param document
    *           document DFCE
    * @param metadonnees
    *           metadonnees
    * @return String
    */
   public static String getMetadatasForLog(final Document document,
         final List<String> metadonnees) {
      final StringBuffer buffer = new StringBuffer();
      boolean first = true;
      for (String metadonnee : metadonnees) {
         // recupere la valeur
         Object valeur;
         if (metadonnee.startsWith("SM_")) {
            valeur = getSystemMetadataByCd(document, metadonnee);
         } else if (metadonnee.equals(Constantes.METADONNEES_NOM_FICHIER)) {
            valeur = document.getFilename() + "." + document.getExtension();
         } else {
            valeur = getMetadataByCd(document, metadonnee);
         }
         // constitue la trace
         if (!first) {
            buffer.append(", ");
         }
         buffer.append(metadonnee);
         buffer.append(": ");
         buffer.append(formatValeur(valeur));
         first = false;
      }
      return buffer.toString();
   }

   /**
    * Methode permettant de formatter la valeur. En fait, si la valeur est une
    * date, on la formatte au bon format. Sinon, l'objet reste le même.
    * 
    * @param value
    *           valeur
    * @return Object
    */
   private static Object formatValeur(final Object value) {
      Object retour;
      if (value != null && value.getClass().isAssignableFrom(Date.class)) {
         retour = Constantes.FORMATTER_DATE.format((Date) value);
      } else {
         retour = value;
      }
      return retour;
   }

   /**
    * Methode permettant de recuperer la valeur d'une metadonnée par son code.
    * Attention, cette methode ne marche que pour les metadonnées qui commencent
    * par <b>SM_</b>.
    * 
    * @param document
    *           document DFCE
    * @param code
    *           code
    * @return Object
    */
   public static Object getSystemMetadataByCd(final Document document,
         final String code) {
      Object value = null;
      if ("SM_TITLE".equals(code)) {
         value = document.getTitle();
      } else if ("SM_CREATION_DATE".equals(code)) {
         value = document.getCreationDate();
      } else if ("SM_DOCUMENT_TYPE".equals(code)) {
         value = document.getType();
      } else if ("SM_SIZE".equals(code)) {
         value = document.getSize();
      } else if ("SM_VIRTUAL".equals(code)) {
         value = document.isVirtual();
      } else if ("SM_START_PAGE".equals(code)) {
         value = document.getStartPage();
      } else if ("SM_END_PAGE".equals(code)) {
         value = document.getEndPage();
      } else if ("SM_ARCHIVAGE_DATE".equals(code)) {
         value = document.getArchivageDate();
      }
      return value;
   }

   /**
    * Methode permettant de vérifier si la liste de métadonnées passées en
    * paramètres contient des métadonnées non autorisées.
    * 
    * @param metadonnees
    *           liste de métadonnées
    * @return List<String>
    */
   public static List<String> checkMetadonneesNonAutorisees(
         final List<String> metadonnees) {
      List<String> erreurs = new ArrayList<String>();
      List<String> metasNonAutorisees = Arrays
            .asList(Constantes.METADONNEES_NON_AUTORISEES);
      for (String metadonnee : metadonnees) {
         if (metasNonAutorisees.contains(metadonnee)) {
            erreurs.add(metadonnee);
         }
      }
      return erreurs;
   }
}
