package fr.urssaf.image.parser_opencsv.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.velocity.exception.ParseErrorException;

import fr.urssaf.image.parser_opencsv.application.constantes.FileConst;
import fr.urssaf.image.parser_opencsv.application.exception.HashInexistantException;
import fr.urssaf.image.parser_opencsv.application.exception.MetaFormatCSVException;
import fr.urssaf.image.parser_opencsv.jaxb.model.DocumentType;
import fr.urssaf.image.parser_opencsv.jaxb.model.FichierType;
import fr.urssaf.image.parser_opencsv.jaxb.model.ListeMetadonneeType;
import fr.urssaf.image.parser_opencsv.jaxb.model.MetadonneeType;

/**
 * Permet de convertir des métadonnées au format String vers des objets
 */
public class MetadataUtils {

   private MetadataUtils() {
      throw new RuntimeException("Cette classe ne peut pas être instanciée");
   }

   public static MetadonneeType convertMetaFromString(final String stringMetadata) throws MetaFormatCSVException {

      if (stringMetadata == null || stringMetadata.equals("") || !stringMetadata.contains(":")) {
         throw new MetaFormatCSVException();
      }

      final String[] splitMetaArray = stringMetadata.split(":");
      final int length = splitMetaArray.length;

      String metaCode = "";
      String metaValue = "";

      if (length == 1) {
         metaCode = splitMetaArray[0];
      } else {
         metaCode = splitMetaArray[0];
         metaValue = splitMetaArray[1];
         if (metaCode.equals("")) {
            throw new MetaFormatCSVException();
         }
      }

      final MetadonneeType meta = new MetadonneeType();
      meta.setCode(metaCode);
      meta.setValeur(metaValue);
      return meta;
   }

   public static ListeMetadonneeType convertListMetasFromString(final String stringMetadata) throws MetaFormatCSVException {
      final String[] splitMetasArrays = stringMetadata.split(",");

      final ListeMetadonneeType metas = new ListeMetadonneeType();
      for (final String metaValue : splitMetasArrays) {
         final MetadonneeType meta = convertMetaFromString(metaValue);
         metas.getMetadonnee().add(meta);
      }

      return metas;
   }

   public static DocumentType convertDocumentTypeFromArray(final String[] csvLigne) throws MetaFormatCSVException {

      final DocumentType document = new DocumentType();

      // Set Objet numérique
      final FichierType fichier = new FichierType();
      fichier.setCheminEtNomDuFichier(csvLigne[1]);
      document.setObjetNumerique(fichier);

      // Set métadonnées
      final ListeMetadonneeType metadonnees = MetadataUtils.convertListMetasFromString(csvLigne[2]);
      document.setMetadonnees(metadonnees);

      return document;
   }

   public static String convertStringFromArray(final String[] csvLigne) {
      final StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("\"");
      for(final String col : csvLigne) {
         stringBuilder.append(col);
         stringBuilder.append(" ; ");
      }
      stringBuilder.append("\"");
      return stringBuilder.toString();
   }

   /**
    * Convertit une ligne du CSV en un DocumentType
    * 
    * @param csvLigne
    * @param bndDateFormat
    * @param gnsDateFormat
    * @return
    * @throws ParseException
    * @throws HashInexistantException
    */
  public static DocumentType convertLigneArrayToDocument(final String[] csvLigne, final String extension, final SimpleDateFormat bndDateFormat,
                                                         final SimpleDateFormat gnsDateFormat, final int lineNum)
      throws ParseException, HashInexistantException {
      final DocumentType documentType = new DocumentType();
      final FichierType fichierType = new FichierType();
      final String path = csvLigne[22];
      // nom du fichier original
      final String nomFichierFromPath = getNomFichierFromPath(path);
      
      // dans le référentiel des formats, on peut avoir plusieurs extensions
      // correspondant à un type mime Exemple: tif,tiff ou jpg,jpeg
      final String[] tabEx = extension.split(","); 
      if(tabEx[0].isEmpty()) {
        throw new ParseException("Problème d'extension du fichier", lineNum);
      }
      // remplacer le .bin par la vraie extension
      final String nomFichier = nomFichierFromPath.replaceFirst(FileConst.Extension.BIN, "." + tabEx[0]);
      
      fichierType.setCheminEtNomDuFichier(nomFichier);
      fichierType.setPath(path);

      // Nombre de pages par défaut
      documentType.setNombreDePages(1);

      documentType.setObjetNumerique(fichierType);
      final ListeMetadonneeType metadatas = enrichirMetadatas(csvLigne, bndDateFormat, gnsDateFormat);
      documentType.setMetadonnees(metadatas);

      return documentType;
   }

   /**
    * Enrichir les méta du document par rapport aux données
    * récupérées du CSV
    * 
    * @param csvLigne
    * @param gnsDateFormat
    * @param bndDateFormat
    * @return
    * @throws ParseException
    * @throws HashInexistantException
    */
   private static ListeMetadonneeType enrichirMetadatas(final String[] csvLigne, final SimpleDateFormat bndDateFormat, final SimpleDateFormat gnsDateFormat)
         throws ParseException, HashInexistantException {
      final ListeMetadonneeType metadonnees = new ListeMetadonneeType();

      final List<MetadonneeType> metaList = metadonnees.getMetadonnee();

      // Meta FormatFichier
      setMetaValue(metaList, "FormatFichier", csvLigne[24]);

      // Meta Hash
      final String hashValue = csvLigne[26].trim();
      if (hashValue.isEmpty()) {
         throw new HashInexistantException();
      }
      setMetaValue(metaList, "Hash", hashValue);

      // Meta TypeHash
      setMetaValue(metaList, "TypeHash", "SHA-1");

      // Meta DateCreation et DateDebutConservation
      final String dateCreationString = changeFormatDate(csvLigne[21].trim(), bndDateFormat, gnsDateFormat);
      setMetaValue(metaList, "DateCreation", dateCreationString);
      setMetaValue(metaList, "DateDebutConservation", dateCreationString);

      // Meta Titre
      setOptionalMetaValue(metaList, "Titre", csvLigne[15]);

      // Meta CodeRND avec CodeObjet
      setOptionalMetaValue(metaList, "CodeRND", csvLigne[14]);
      setOptionalMetaValue(metaList, "CodeDocument", csvLigne[14]);

      // Meta UUID
      setOptionalMetaValue(metaList, "IdGed", csvLigne[13]);

      // Meta CodeOrganismeProprietaire - caisse-xx à convertir
      setOptionalMetaValue(metaList, "CodeOrganismeProprietaire", csvLigne[12]);

      // Meta CodeOrganismeGestionnaire - caisse-xx à convertir to RSIxx
      setOptionalMetaValue(metaList, "CodeOrganismeGestionnaire", csvLigne[12]);

      // Meta nir
      setOptionalMetaValue(metaList, "NniEmployeur", csvLigne[0]);

      // Meta Denomination
      final String nom = csvLigne[1].trim();
      String denominationValue = nom;
      final String prenom = csvLigne[3].trim();
      if (!prenom.isEmpty()) {
         denominationValue = denominationValue + " " + prenom;
      }
      final String raisonSocial = csvLigne[6].trim();
      if (!raisonSocial.isEmpty()) {
         denominationValue = denominationValue + " - " + raisonSocial;
      }
      setOptionalMetaValue(metaList, "Denomination", denominationValue);

      // Meta DateNaissance
      final String dateNaissanceAsString = csvLigne[4].trim();
      if (!dateNaissanceAsString.isEmpty()) {
         setMetaValue(metaList, "DateNaissanceCotisant", changeFormatDate(dateNaissanceAsString, bndDateFormat, gnsDateFormat));
      }

      // Meta NumeroDeLot
      setOptionalMetaValue(metaList, "NumerotLot", csvLigne[19]);

      // Meta Riba
      setOptionalMetaValue(metaList, "RIBA", csvLigne[9]);

      // Meta Siren
      setOptionalMetaValue(metaList, "Siren", csvLigne[10]);

      // Meta NumTi
      setOptionalMetaValue(metaList, "NumeroCompteExterne", csvLigne[5]);

      // Meta NbPages
      setMetaValue(metaList, "NbPages", "1");

      // Meta ApplicationProductrice
      setMetaValue(metaList, "ApplicationProductrice", csvLigne[23]);

      // Meta ApplicationTraitement
      setMetaValue(metaList, "ApplicationTraitement", "BND SSTI");

      // Meta ApplicationMetier
      setMetaValue(metaList, "ApplicationMetier", "BND SSTI");

      return metadonnees;
   }

   private static void setMetaValue(final List<MetadonneeType> metaList, final String metaCode, final String metaValue) {
      final MetadonneeType meta = new MetadonneeType();
      meta.setCode(metaCode);
      meta.setValeur(metaValue.trim());
      metaList.add(meta);
      }

   private static void setOptionalMetaValue(final List<MetadonneeType> metaList, final String metaCode, final String metaValue) {
      final String trimedValue = metaValue.trim();
      if (!metaValue.isEmpty()) {
         final MetadonneeType meta = new MetadonneeType();
         meta.setCode(metaCode);
         meta.setValeur(trimedValue);
         metaList.add(meta);
      }
   }

   /**
    * Extrait le nom du fichier dans le chemin absolu vers le fichier
    * 
    * @param absolutePath
    * @return
    */
  public static String getNomFichierFromPath(final String absolutePath) {
      final String[] splitArray = absolutePath.split("/");
      return splitArray[splitArray.length - 1];
   }

   private static String changeFormatDate(final String bndDate, final SimpleDateFormat bndDateFormat, final SimpleDateFormat gnsDateFormat)
         throws ParseException {
      final Date date = bndDateFormat.parse(bndDate);
      return gnsDateFormat.format(date);
   }

}
