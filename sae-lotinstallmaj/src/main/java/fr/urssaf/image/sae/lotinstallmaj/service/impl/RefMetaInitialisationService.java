package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import me.prettyprint.hector.api.Keyspace;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVReader;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockConfiguration;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.cassandra.support.clock.impl.JobClockSupportImpl;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRuntimeException;
import fr.urssaf.image.sae.metadata.referential.dao.SaeMetadataDao;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.support.SaeMetadataSupport;

/**
 * Service d'initialisation du référentiel des métadonnées suite au passage au
 * stockage en bdd des métadonnées, en remplacement d'un fichier XML dans
 * sae-metadata pour la partie SAE, et d'un ou plusieurs fichiers XML dans
 * sae-livrable et sae-lotinstallmaj pour la partie DFCE <br>
 * <br>
 * La méthode d'initialisation comporte un certain nombre de vérifications afin
 * d'assurer la non-régression par rapport à la définition des métadonnées
 * telles qu'elles existaient avant ce changement de mode de stockage.
 */
@Service
public final class RefMetaInitialisationService {

   private static final Logger LOG = LoggerFactory
         .getLogger(RefMetaInitialisationService.class);

   /**
    * Initialisation du référentiel des métadonnées en version 1.8 (sans les
    * nouvelles métadonnées).
    * 
    * @param keyspace
    *           le Keyspace Cassandra
    */
   public void initialiseRefMeta(Keyspace keyspace) {

      // Trace
      LOG.info("Initialisation du nouveau référentiel des métadonnées");

      // Lecture du fichier CSV en entier, et remplissage d'une liste d'objets
      // métadonnées
      List<MetadataReference> metadonnees = chargeFichierMeta();
      LOG.info("Nombre de métadonnées à créer : " + metadonnees.size());

      // Vérification #1
      // On compare les métadonnées chargées depuis le fichier CSV
      // avec les métadonnées du dernier fichier XML qui stockait le
      // référentiel des métadonnées avant le passage en bdd
      // verification1(metadonnees);

      // Vérification #2
      // On compare les métadonnées chargées depuis le fichier CSV
      // avec la définition de la structure de base DFCE stockée en
      // fichier XML
      // verification2(metadonnees);

      // Enregistrement des métadonnées en base de données
      persisteMetadonnees(keyspace, metadonnees);

      // Trace
      LOG
            .info("Fin de l'initialisation du nouveau référentiel des métadonnées");

   }

   protected List<MetadataReference> chargeFichierMeta() {
      
      String nomFichierMetas = "Metadonnees.2.0.txt";

      LOG.info(String.format("Chargement du fichier de métadonnées: %s",nomFichierMetas));
      // L'objet de résultat de la méthode
      List<MetadataReference> metadonneesAcreer = new ArrayList<MetadataReference>();

      // Chargement du fichier CSV du référentiel des métadonnées
      ClassPathResource resource = new ClassPathResource(nomFichierMetas);
      CSVReader reader;
      
      try {
         reader = new CSVReader(new InputStreamReader(
               resource.getInputStream(), Charset.forName("UTF-8")), '\t');
      } catch (IOException e) {
         throw new MajLotRuntimeException(e);
      }

      // Gros try/catch des IOException levées par reader.readNext();
      try {

         // Saute les 2 premières lignes, qui sont des lignes d'en-tête
         reader.readNext();
         reader.readNext();

         // Boucle sur la liste des lignes
         String[] nextLine;

         while ((nextLine = reader.readNext()) != null) {

            // Saute les lignes vides (dues à l'export Excel)
            if (StringUtils.isBlank(nextLine[0])) {
               continue;
            }

            // Création de l'objet "Métadonnée"
            MetadataReference metadonnee = new MetadataReference();
            metadonneesAcreer.add(metadonnee);

            // Affectation des propriétés de l'objet à partir du fichier CSV

            // Code long
            String longCode = readString(nextLine, "A");
            metadonnee.setLongCode(longCode);
            LOG.info("METADONNEE : " + longCode);

            // Libellé
            String label = readString(nextLine, "B");
            metadonnee.setLabel(label);
            // LOG.info("lib : " + label);

            // Description
            String description = readString(nextLine, "C");
            metadonnee.setDescription(description);
            // LOG.info("description : " + description);

            // Spécifiable à l'archivage
            boolean isArchivable = readBoolean(nextLine, "E");
            metadonnee.setArchivable(isArchivable);
            // LOG.info("isArchivable : " + isArchivable);

            // Obligatoire à l'archivage
            boolean requiredForArchival = readBoolean(nextLine, "F");
            metadonnee.setRequiredForArchival(requiredForArchival);
            // LOG.info("requiredForArchival : " + requiredForArchival);

            // Consultée par défaut
            boolean defaultConsultable = readBoolean(nextLine, "G");
            metadonnee.setDefaultConsultable(defaultConsultable);
            // LOG.info("defaultConsultable : " + defaultConsultable);

            // Consultable
            boolean consultable = readBoolean(nextLine, "H");
            metadonnee.setConsultable(consultable);
            // LOG.info("consultable : " + consultable);

            // Critère de recherche
            boolean isSearchable = readBoolean(nextLine, "I");
            metadonnee.setSearchable(isSearchable);
            // LOG.info("isSearchable : " + isSearchable);

            // Indexée
            boolean isIndexed = readBoolean(nextLine, "J");
            metadonnee.setIsIndexed(isIndexed);
            // LOG.info("isIndexed : " + isIndexed);

            // Formatage
            String pattern = ""; // TODO K
            metadonnee.setPattern(pattern);

            // Taille maximum autorisée en archivage
            int length = readInt(nextLine, "L");
            metadonnee.setLength(length);
            // LOG.info("length : " + length);

            // Nom du dictionnaire
            String dictionaryName = readString(nextLine, "O");
            metadonnee.setDictionaryName(dictionaryName);
            // LOG.info("dictionaryName : " + dictionaryName);

            // Possède un dictionnaire ?
            boolean hasDictionary = StringUtils.isNotBlank(dictionaryName);
            metadonnee.setHasDictionary(hasDictionary);
            // LOG.info("hasDictionary : " + hasDictionary);

            // Diffusable client
            boolean dispo = readBoolean(nextLine, "Q");
            // LOG.debug(longCode + " : " + dispo);
            metadonnee.setClientAvailable(dispo);

            // Code court
            String shortCode = readString(nextLine, "T");
            metadonnee.setShortCode(shortCode);
            // LOG.info("shortCode : " + shortCode);

            // Métadonnée gérée directement par DFCE
            boolean isInternal = readBoolean(nextLine, "U");
            metadonnee.setInternal(isInternal);
            // LOG.info("isInternal : " + isInternal);

            // Type DFCE
            String typeDfce = readString(nextLine, "W");
            metadonnee.setType(typeDfce);
            // LOG.info("typeDfce : " + typeDfce);

            // Obligatoire au stockage
            boolean requiredForStorage = readBoolean(nextLine, "Y");
            metadonnee.setRequiredForStorage(requiredForStorage);
            // LOG.info("requiredForStorage : " + requiredForStorage);

            // Modifiable par le client
            boolean modifiableParClient = readBoolean(nextLine, "AA");
            metadonnee.setModifiable(modifiableParClient);
            // LOG.info("modifiableParClient : " + modifiableParClient);

            // Trim à gauche
            boolean trimGauche = readBoolean(nextLine, "AC");
            metadonnee.setLeftTrimable(trimGauche);
            // LOG.info("trimGauche : " + trimGauche);

            // Trim à droite
            boolean trimDroite = readBoolean(nextLine, "AD");
            metadonnee.setRightTrimable(trimDroite);
            // LOG.debug("trimDroite : " + trimDroite);
            
            // Transferable
            boolean transferable = readBoolean(nextLine, "AE");
            metadonnee.setTransferable(transferable);
             LOG.debug("transferable : " + transferable);

         }

      } catch (IOException e) {
         throw new MajLotRuntimeException(e);
      }

      // Renvoie du résultat
      return metadonneesAcreer;

   }

   private int getIndiceColonne(String colonneExcel) {

      // On fera commencer les indices à 0
      // code Ascii de A = 65
      // code Ascii de Z = 90

      char colonne;
      int codeAscii;

      if (colonneExcel.length() == 1) {
         colonne = colonneExcel.charAt(0);
         codeAscii = (int) colonne;
         if ((codeAscii < (int) ('A')) || (codeAscii > (int) ('Z'))) {
            throw new MajLotRuntimeException(
                  "Erreur de récupération de l'indice de colonne Excel");
         }
         return codeAscii - (int) ('A');
      }

      if (colonneExcel.length() == 2) {
         colonne = colonneExcel.charAt(1);
         codeAscii = (int) colonne;
         return codeAscii - (int) ('A') + 26;
      }

      throw new MajLotRuntimeException(
            "Erreur de récupération de l'indice de colonne Excel");

   }

   private String readString(String[] nextLine, String colonneExcel) {

      return StringUtils.trimToEmpty(nextLine[getIndiceColonne(colonneExcel)]);

   }

   private boolean readBoolean(String[] nextLine, String colonneExcel) {

      String str = readString(nextLine, colonneExcel);

      if ("oui".equals(str)) {
         return true;
      } else if ("non".equals(str)) {
         return false;
      } else {
         throw new MajLotRuntimeException("La valeur " + str
               + " n'est pas convertible en boolean");
      }

   }

   private int readInt(String[] nextLine, String colonneExcel) {

      String str = readString(nextLine, colonneExcel);

      if (StringUtils.isBlank(str)) {
         return -1;
      } else {
         return Integer.parseInt(str);
      }

   }

   /**
    * Méthode de contrôle. A partir de la liste des métadonnées lues depuis
    * l'export du fichier Excel, on reconstitue un fichier XML de l'ancien
    * référentiel des métadonnées, et on vérifie que le fichier est identifique
    * à celui qui a été effectivement utilisé auparavant.<br>
    * <br>
    * Cela permet de s'assurer de la non régression par rapport à avant.
    * 
    * @param metadonnees
    *           la liste des métadonnées lues depuis l'export Excel du
    *           référentiel des métadonnées
    * @return le fichier XML reconstitué (la liste des lignes du fichier)
    */
   protected List<String> genereFichierXmlAncienneVersionRefMeta(
         List<MetadataReference> metadonnees) {

      List<String> lines = new ArrayList<String>();

      // String retourCharriot = "\r\n";

      lines.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      lines.add("<referentiel>");

      for (MetadataReference metadonnee : metadonnees) {
         lines.add("   <metaDataReference>");
         lines.add(String.format("      <shortCode>%s</shortCode>", metadonnee
               .getShortCode()));
         lines.add(String.format("      <longCode>%s</longCode>", metadonnee
               .getLongCode()));
         lines
               .add(String
                     .format("      <type>%s</type>", metadonnee.getType()));
         lines.add(String.format(
               "      <requiredForArchival>%s</requiredForArchival>",
               metadonnee.isRequiredForArchival()));
         lines.add(String.format(
               "      <requiredForStorage>%s</requiredForStorage>", metadonnee
                     .isRequiredForStorage()));
         lines.add(String.format("      <length>%s</length>", metadonnee
               .getLength()));
         lines.add(String.format("      <pattern>%s</pattern>", metadonnee
               .getPattern()));
         lines.add(String.format("      <consultable>%s</consultable>",
               metadonnee.isConsultable()));
         lines.add(String.format(
               "      <defaultConsultable>%s</defaultConsultable>", metadonnee
                     .isDefaultConsultable()));
         lines.add(String.format("      <searchable>%s</searchable>",
               metadonnee.isSearchable()));
         lines.add(String.format("      <internal>%s</internal>", metadonnee
               .isInternal()));
         lines.add(String.format("      <archivable>%s</archivable>",
               metadonnee.isArchivable()));
         lines.add(String.format("      <label>%s</label>", metadonnee
               .getLabel()));
         lines.add(String.format("      <description>%s</description>",
               metadonnee.getDescription()));
         lines.add("   </metaDataReference>");
      }

      lines.add("</referentiel>");

      return lines;

   }

   protected void verification1(List<MetadataReference> metadonnees) {

      List<String> lignesGenerees = genereFichierXmlAncienneVersionRefMeta(metadonnees);

      compareDeuxListeLignes("1",
            "refmeta/MetadataReferential_Lot150100_ameliore.xml",
            lignesGenerees);

   }

   @SuppressWarnings("unchecked")
   private void compareDeuxListeLignes(String numeroVerif,
         String ficRessourceOriginal, List<String> lignesGenerees) {
      String trcPrefix = "compareDeuxListeLignes()";
      ClassPathResource resource = new ClassPathResource(ficRessourceOriginal);

      List<String> lignesOriginales;

      InputStream stream = null;
      try {
         stream = resource.getInputStream();

         lignesOriginales = (List<String>) IOUtils.readLines(stream, "UTF-8");
      } catch (IOException e) {
         throw new MajLotRuntimeException(e);
      } finally {
         if (stream != null) {
            try {
               stream.close();
            } catch (IOException exception) {
               LOG.info("{} - fermeture du flux " + ficRessourceOriginal
                     + " impossible", trcPrefix);
            }
         }
      }

      // Vérifie le nombre global de lignes
      if (lignesGenerees.size() != lignesOriginales.size()) {
         throw new MajLotRuntimeException("La vérification interne #"
               + numeroVerif
               + " a échoué. Nombre de lignes dans le fichier d'origine : "
               + lignesOriginales.size()
               + ". Nombre de lignes dans le fichier regénéré : "
               + lignesGenerees.size());
      }

      // Vérifie ligne par ligne
      for (int i = 0; i < lignesOriginales.size(); i++) {
         if (!lignesOriginales.get(i).equals(lignesGenerees.get(i))) {
            throw new MajLotRuntimeException("La vérification interne #"
                  + numeroVerif + " a échoué sur la ligne " + (i + 1)
                  + ". Ligne d'origine : '" + lignesOriginales.get(i)+ "'"
                  + ". Ligne regénérée : '" + lignesGenerees.get(i)  + "'");
         }
      }

   }

   /**
    * Méthode de contrôle. A partir de la liste des métadonnées lues depuis
    * l'export du fichier Excel, on reconstitue un fichier XML de la structure
    * la base DFCE, et on vérifie que le fichier est identifique à celui qui a
    * été effectivement utilisé auparavant.<br>
    * <br>
    * Cela permet de s'assurer de la non régression par rapport à avant.
    * 
    * @param metadonnees
    *           la liste des métadonnées lues depuis l'export Excel du
    *           référentiel des métadonnées
    * @return le fichier XML reconstitué (la liste des lignes du fichier)
    */
   protected List<String> genereFichierXmlAncienneVersionBaseDfce(
         List<MetadataReference> metadonnees) {

      List<String> lines = new ArrayList<String>();

      // String retourCharriot = "\r\n";

      lines.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      lines.add("<docuBase>");
      lines.add("   <base>");
      lines.add("      <categories>");

      for (MetadataReference metadonnee : metadonnees) {

         if (!metadonnee.isInternal()) {

            lines.add("         <category>");
            lines.add(String.format("            <descriptif>%s</descriptif>",
                  metadonnee.getLongCode()));
            lines.add(String.format("            <name>%s</name>", metadonnee
                  .getShortCode()));
            lines.add(String.format("            <dataType>%s</dataType>",
                  metadonnee.getType()));
            lines.add(String.format("            <index>%s</index>", metadonnee
                  .getIsIndexed()));
            if (metadonnee.isRequiredForStorage()) {
               lines.add("            <minimumValues>1</minimumValues>");
            } else {
               lines.add("            <minimumValues>0</minimumValues>");
            }
            lines.add("            <maximumValues>1</maximumValues>");
            lines.add("            <single>false</single>");
            lines.add("            <enableDictionary>false</enableDictionary>");
            lines.add("         </category>");

         }

      }

      lines.add("      </categories>");
      lines.add("   </base>");
      lines.add("</docuBase>");

      return lines;

   }

   protected void verification2(List<MetadataReference> metadonnees) {

      List<String> lignesGenerees = genereFichierXmlAncienneVersionBaseDfce(metadonnees);

      compareDeuxListeLignes("2", "refmeta/saeBase_Lot150100_ameliore.xml",
            lignesGenerees);

   }

   private void persisteMetadonnees(Keyspace keyspace,
         List<MetadataReference> metadonnees) {

      LOG.info("Persistence des métadonnées");
      // Instantiation de la DAO, de son support, et du support des clock
      // Cassandra

      SaeMetadataDao metaDao = new SaeMetadataDao(keyspace);

      SaeMetadataSupport metaSupport = new SaeMetadataSupport(metaDao);

      JobClockConfiguration clockConfiguration = new JobClockConfiguration();
      clockConfiguration.setMaxTimeSynchroError(10000000);
      clockConfiguration.setMaxTimeSynchroWarn(2000000);

      JobClockSupport clockSupport = new JobClockSupportImpl(keyspace,
            clockConfiguration);

      // Création des métadonnées en base Cassandra uniquement (pas dans DFCE)

      for (MetadataReference metadonnee : metadonnees) {
         if (metaSupport.find(metadonnee.getLongCode()) != null) {
            metaSupport.modify(metadonnee, clockSupport.currentCLock());
         } else {
            metaSupport.create(metadonnee, clockSupport.currentCLock());
         }
      }

   }

}
