package fr.urssaf.image.parser_opencsv.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.velocity.exception.ParseErrorException;

import fr.urssaf.image.parser_opencsv.application.constantes.FileConst;
import fr.urssaf.image.parser_opencsv.application.exception.HashInexistantException;
import fr.urssaf.image.parser_opencsv.application.exception.MetaFormatCSVException;
import fr.urssaf.image.parser_opencsv.jaxb.model.DocumentType;
import fr.urssaf.image.parser_opencsv.jaxb.model.FichierType;
import fr.urssaf.image.parser_opencsv.jaxb.model.ListeMetadonneeType;
import fr.urssaf.image.parser_opencsv.jaxb.model.MetadonneeType;

/**
 * Permet de convertir des metadonnées au format String vers des objets
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

      // Set Objet nnumerique
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
    * @return
    * @throws ParseException
    * @throws HashInexistantException
    */
  public static DocumentType convertLigneArrayToDocument(final String[] csvLigne, final String extension)
      throws ParseException, HashInexistantException {
      final DocumentType documentType = new DocumentType();
      final FichierType fichierType = new FichierType();
      final String path = csvLigne[22];
      // nom du fichier original
      final String nomFichierFromPath = getNomFichierFromPath(path);
      
      // dans le referentiel des formats, on peut avoir plusieurs extension
      // correspondant à un type mime Exemple: tif,tiff ou jpg,jpeg
      final String[] tabEx = extension.split(","); 
      if(tabEx[0].isEmpty()) {
        throw new ParseErrorException("Problème d'extension du fichier");
      }
      // remplacer le .bin par la vraie extension
      final String nomFichier = nomFichierFromPath.replaceFirst(FileConst.Extension.BIN, "." + tabEx[0]);
      
      fichierType.setCheminEtNomDuFichier(nomFichier);
      fichierType.setPath(path);

      // Nombre de pages par défaut
      documentType.setNombreDePages(1);

      documentType.setObjetNumerique(fichierType);
      documentType.setMetadonnees(enrichirMetadatas(csvLigne));

      return documentType;
   }

   /**
    * Enrichir les méta du document par rapport au données
    * récupérées du CSV
    * 
    * @param csvLigne
    * @return
    * @throws ParseException
    * @throws HashInexistantException
    */
   private static ListeMetadonneeType enrichirMetadatas(final String[] csvLigne) throws ParseException, HashInexistantException {
      final ListeMetadonneeType metadonnees = new ListeMetadonneeType();

      // Meta NomFichier
      final MetadonneeType nomFichier = new MetadonneeType();
      nomFichier.setCode("NomFichier");
      nomFichier.setValeur(csvLigne[20]);

      // Meta FormatFichier
      final MetadonneeType formatFichier = new MetadonneeType();
      formatFichier.setCode("FormatFichier");
      formatFichier.setValeur(csvLigne[24]);

      // Meta HashFichier
      final MetadonneeType hashFichier = new MetadonneeType();
      hashFichier.setCode("Hash");
      if (csvLigne[26].trim().isEmpty()) {
         throw new HashInexistantException();
      }
      hashFichier.setValeur(csvLigne[26]);

      // Meta TypeHash
      final MetadonneeType typeHash = new MetadonneeType();
      typeHash.setCode("TypeHash");
      typeHash.setValeur("SHA-1");

      final String dateCreationString = changeFormatDate(csvLigne[21]);
      final MetadonneeType dateCreation = new MetadonneeType();
      dateCreation.setCode("DateCreation");
      dateCreation.setValeur(dateCreationString);
      // Meta DateDebutConservation
      final MetadonneeType dateDebutConservation = new MetadonneeType();
      dateDebutConservation.setCode("DateDebutConservation");
      dateDebutConservation.setValeur(dateCreationString);

      // Meta Titre
      final MetadonneeType titre = new MetadonneeType();
      titre.setCode("Titre");
      titre.setValeur(csvLigne[15]);

      // Meta CodeRND avec CodeObjet
      final String codeObjet = csvLigne[14];
      final MetadonneeType codeRND = new MetadonneeType();
      codeRND.setCode("CodeRND");
      codeRND.setValeur(codeObjet);
      // Ajout de la meta CodeDocument
      final MetadonneeType metaCodeObjet = new MetadonneeType();
      metaCodeObjet.setCode("CodeDocument");
      metaCodeObjet.setValeur(codeObjet);

      // Meta UUID
      final MetadonneeType idGED = new MetadonneeType();
      idGED.setCode("IdGed");
      idGED.setValeur(csvLigne[13]);
      // idGED.setValeur(UUID.randomUUID().toString());

      // Meta CodeOrganismeProprietaire - caisse-xx à convertir
      final MetadonneeType codeOrgaProp = new MetadonneeType();
      codeOrgaProp.setCode("CodeOrganismeProprietaire");
      codeOrgaProp.setValeur(csvLigne[12]);

      // Meta CodeOrganismeGestionnaire - caisse-xx à convertir to RSIxx
      final MetadonneeType codeOrganismeGest = new MetadonneeType();
      codeOrganismeGest.setCode("CodeOrganismeGestionnaire");
      codeOrganismeGest.setValeur(csvLigne[12]);

      // Meta DomaineCotisant à convertir
      final MetadonneeType domaineCotisant = new MetadonneeType();
      domaineCotisant.setCode("DomaineCotisant");
      domaineCotisant.setValeur(String.valueOf(true));

      // Meta nir à convertir
      final MetadonneeType nir = new MetadonneeType();
      nir.setCode("NniEmployeur");
      nir.setValeur(csvLigne[0]);

      // Meta Denomination
      final String nom = csvLigne[1];
      String metaValue = nom;
      final String prenom = csvLigne[3];
      if (!prenom.trim().isEmpty()) {
         metaValue = metaValue + " " + prenom;
      }
      final String raisonSocial = csvLigne[6];
      final MetadonneeType denomination = new MetadonneeType();
      denomination.setCode("Denomination");

      if (!raisonSocial.trim().isEmpty()) {
         metaValue = metaValue + " - " + raisonSocial;
      }
      denomination.setValeur(metaValue);

      // Meta DateNaissance
      final MetadonneeType dateNaissance = new MetadonneeType();
      dateNaissance.setCode("DateNaissanceCotisant");
      dateNaissance.setValeur(changeFormatDate(csvLigne[4]));

      // Meta NumeroDeLot
      final MetadonneeType numeroLot = new MetadonneeType();
      numeroLot.setCode("NumerotLot");
      numeroLot.setValeur(csvLigne[19]);

      // Meta Riba
      final MetadonneeType riba = new MetadonneeType();
      riba.setCode("RIBA");
      riba.setValeur(csvLigne[9]);

      // Meta Siren
      final MetadonneeType siren = new MetadonneeType();
      siren.setCode("Siren");
      siren.setValeur(csvLigne[10]);

      // Meta NumTi
      final MetadonneeType numTi = new MetadonneeType();
      numTi.setCode("NumeroCompteExterne");
      numTi.setValeur(csvLigne[5]);

      // Meta NbPages
      final MetadonneeType nombreDePage = new MetadonneeType();
      nombreDePage.setCode("NbPages");
      nombreDePage.setValeur("0");

      // Meta ApplicationProductrice
      final String source = csvLigne[23];
      final MetadonneeType applicationProductrice = new MetadonneeType();
      applicationProductrice.setCode("ApplicationProductrice");
      applicationProductrice.setValeur(source);

      // Meta ApplicationTraitement && ApplicationMetier
      final String applicationTraitementValue = "BND SSTI";
      final MetadonneeType applicationTraitement = new MetadonneeType();
      applicationTraitement.setCode("ApplicationTraitement");
      applicationTraitement.setValeur(applicationTraitementValue);
      final MetadonneeType applicationMetier = new MetadonneeType();
      applicationMetier.setCode("ApplicationMetier");
      applicationMetier.setValeur(applicationTraitementValue);



      /*
       * ********* Ajout des méta obligatoires ***********
       * Liste des méta obligatoire remplies automatiquement par la GED
       * versionRND
       * dateArchivage
       * dateFinConservation
       * isVirtuel
       * contratService
       */
      metadonnees.getMetadonnee().add(codeOrganismeGest);
      metadonnees.getMetadonnee().add(codeOrgaProp);
      if (!codeRND.getValeur().trim().isEmpty()) {
         metadonnees.getMetadonnee().add(codeRND);
         metadonnees.getMetadonnee().add(metaCodeObjet);
      }
      metadonnees.getMetadonnee().add(dateCreation);
      metadonnees.getMetadonnee().add(applicationProductrice);
      metadonnees.getMetadonnee().add(titre);
      metadonnees.getMetadonnee().add(dateDebutConservation);
      metadonnees.getMetadonnee().add(typeHash);
      metadonnees.getMetadonnee().add(hashFichier);
      metadonnees.getMetadonnee().add(formatFichier);
      metadonnees.getMetadonnee().add(nombreDePage);

      /**
       * Ajout des meta non obligatoires
       */
      if (!numTi.getValeur().trim().isEmpty()) {
         metadonnees.getMetadonnee().add(numTi);
      }

      metadonnees.getMetadonnee().add(denomination);
      // metadonnees.getMetadonnee().add(domaineCotisant);
      metadonnees.getMetadonnee().add(idGED);
      // metadonnees.getMetadonnee().add(nomFichier);
      metadonnees.getMetadonnee().add(nir);
      metadonnees.getMetadonnee().add(applicationTraitement);
      metadonnees.getMetadonnee().add(applicationMetier);
      metadonnees.getMetadonnee().add(dateNaissance);

      return metadonnees;
   }

   /**
    * Extrait le nom du fichier dans le chemin absolu vers le fichier
    * 
    * @param absolutePath
    * @return
    */
   private static String getNomFichierFromPath(final String absolutePath) {
      final String[] splitArray = absolutePath.split("/");
      return splitArray[splitArray.length - 1];
   }

   private static String changeFormatDate(final String oldDate) throws ParseException {
      final String newFormat = "yyyy-MM-dd";
      final String oldFormat = "dd/MM/yyyy";
      final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(newFormat);
      String newDate = simpleDateFormat.format(new Date());
      if (!oldDate.trim().isEmpty()) {
         final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(oldFormat);
         final Date date = simpleDateFormat2.parse(oldDate);
         simpleDateFormat2.applyPattern(newFormat);
         newDate = simpleDateFormat2.format(date);
      }

      return newDate;
   }

}
