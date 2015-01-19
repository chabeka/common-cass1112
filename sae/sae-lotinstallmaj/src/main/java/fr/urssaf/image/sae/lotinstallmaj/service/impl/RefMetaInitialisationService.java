package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import me.prettyprint.hector.api.Keyspace;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockConfiguration;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.cassandra.support.clock.impl.JobClockSupportImpl;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRuntimeException;
import fr.urssaf.image.sae.lotinstallmaj.modele.SaeCategory;
import fr.urssaf.image.sae.lotinstallmaj.modele.metadata.IndexReference;
import fr.urssaf.image.sae.lotinstallmaj.modele.metadata.IndexesComposites;
import fr.urssaf.image.sae.lotinstallmaj.modele.metadata.MetaReference;
import fr.urssaf.image.sae.lotinstallmaj.modele.metadata.ReferentielMeta;
import fr.urssaf.image.sae.lotinstallmaj.service.utils.XmlUtils;
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
   
   private List<MetadataReference> listMetas;
   
   private List<String[]> indexesComposites;
   
   /**
    * Récupération après chargement du fichier,
    * de la liste des métadonnées
    * 
    * @return : La liste des métadonnées
    */
   public List<MetadataReference> getListMetas(){
      if(listMetas == null){
         try {
            //-- Lecture du fichier XML, et remplissage 
            // d'une liste d'objets métadonnées
            listMetas = chargeFichierMeta();
         } catch (JAXBException e) {
            throw new MajLotRuntimeException(e);
         } catch (SAXException e) {
            throw new MajLotRuntimeException(e);
         } catch (IOException e) {
            throw new MajLotRuntimeException(e);
         }
      }
      return listMetas;
   }
   
   /**
    * Récupération après chargement du fichier,
    * de la liste des métadonnées
    * 
    * @return : La liste des métadonnées
    */
   public List<String[]> getIndexesComposites(){
      if(indexesComposites == null){
         try {
            //-- Lecture du fichier XML, et remplissage 
            // d'une liste d'objets métadonnées
            indexesComposites = chargerFichierIdxComposites();
         } catch (JAXBException e) {
            throw new MajLotRuntimeException(e);
         } catch (SAXException e) {
            throw new MajLotRuntimeException(e);
         } catch (IOException e) {
            throw new MajLotRuntimeException(e);
         }
      }
      return indexesComposites;
   }

   /**
    * Initialisation du référentiel des métadonnées en version 1.8 (sans les
    * nouvelles métadonnées).
    * 
    * @param keyspace
    *           le Keyspace Cassandra
    */
   public void initialiseRefMeta(Keyspace keyspace) {

      //-- Trace
      LOG.info("Initialisation du nouveau référentiel des métadonnées");
      
      //-- On récupère la liste des métas
      List<MetadataReference> metadonnees = getListMetas();

      LOG.info("Nombre de métadonnées à créer : " + metadonnees.size());

      //-- Enregistrement des métadonnées en base de données
      persisteMetadonnees(keyspace, metadonnees);

      //-- Trace
      LOG.info("Fin de l'initialisation du nouveau référentiel des métadonnées");
   }
   
   protected List<String[]> chargerFichierIdxComposites() throws IOException, JAXBException, SAXException  {
      
      String cheminRessourceXml = "IndexesComposites1.0.xml";
      String xsdResPath = "/xsd/metadata/IndexesComposites.xsd";
      
      ClassPathResource ressourceXml = new ClassPathResource(cheminRessourceXml);
      InputStream xmlStream = ressourceXml.getInputStream();

      ClassPathResource ressourceXsd = new ClassPathResource(xsdResPath);
      IndexesComposites ref = XmlUtils.unmarshalStream(IndexesComposites.class, xmlStream, ressourceXsd);
      
      List<IndexReference> indexes = ref.getIndexReference();
      
      List<String[]> indexesAcreer = new ArrayList<String[]>();
      
      for (int i = 0; i < indexes.size(); i++) {
         IndexReference indexXml = indexes.get(i);
         
         //-- On ignore les indexes qui ne sont pas "aCreer"
         if(!readBoolean(indexXml.getACreer())) {
            continue;
         }
         
         String composition = indexXml.getComposition();
         String[] metasList = readString(composition).split(":");
         indexesAcreer.add(metasList);
      }
      return indexesAcreer;
   }
   
   protected List<MetadataReference> chargeFichierMeta() throws JAXBException, SAXException, IOException {
      
      String cheminRessourceXml = "Metadonnees2.1.xml";
      String xsdResPath = "/xsd/metadata/Metadonnees.xsd";
      
      ClassPathResource ressourceXml = new ClassPathResource(cheminRessourceXml);
      InputStream xmlStream = ressourceXml.getInputStream();

      ClassPathResource ressourceXsd = new ClassPathResource(xsdResPath);
      ReferentielMeta ref = XmlUtils.unmarshalStream(ReferentielMeta.class, xmlStream, ressourceXsd);
      List<MetaReference> metas = ref.getMetaReference();
      
      List<MetadataReference> metadonneesAcreer = new ArrayList<MetadataReference>();
      
      for (int i = 0; i < metas.size(); i++) {
         
         MetaReference metaXml = metas.get(i);
         
         //-- On ignore les métas qui sont pas "aCreer" ou "aModifier"
         if(!readBoolean(metaXml.getACreer()) && !readBoolean(metaXml.getAModifier())){
            continue;
         }
         
         MetadataReference metadonnee = new MetadataReference();
         metadonneesAcreer.add(metadonnee);
         
         //-- Code long
         String longCode = readString(metaXml.getLongCode());
         metadonnee.setLongCode(longCode);

         //-- Code court
         String shortCode = readString(metaXml.getShortCode());
         metadonnee.setShortCode(shortCode);

         //-- Type DFCE
         String typeDfce = readString(metaXml.getType());
         metadonnee.setType(typeDfce);
         
         //-- Libellé
         String libelle = readString(metaXml.getLabel());
         metadonnee.setLabel(libelle);

         //-- Description
         String description = readString(metaXml.getDescription());
         metadonnee.setDescription(description);

         //-- Spécifiable à l'archivage
         boolean isArchivable = readBoolean(metaXml.getArchivable());
         metadonnee.setArchivable(isArchivable);

         //-- Obligatoire à l'archivage
         boolean requiredForArchival = readBoolean(metaXml.getRequiredForArchival());
         metadonnee.setRequiredForArchival(requiredForArchival);

         //-- Consultée par défaut
         boolean defaultConsultable = readBoolean(metaXml.getDefaultConsultable());
         metadonnee.setDefaultConsultable(defaultConsultable);

         //-- Consultable
         boolean consultable = readBoolean(metaXml.getConsultable());
         metadonnee.setConsultable(consultable);

         //-- Critère de recherche
         boolean isSearchable = readBoolean(metaXml.getSearchable());
         metadonnee.setSearchable(isSearchable);

         //-- Indexée
         boolean isIndexed = readBoolean(metaXml.getIsIndexed());
         metadonnee.setIsIndexed(isIndexed);

         //-- Formatage
         String pattern = "";
         metadonnee.setPattern(pattern);

         //-- Taille maximum autorisée en archivage
         int length = readInt(metaXml.getLength());
         metadonnee.setLength(length);

         //-- Nom du dictionnaire
         String dictionaryName = readString(metaXml.getDictionaryName());
         metadonnee.setDictionaryName(dictionaryName);

         //-- Possède un dictionnaire ?
         boolean hasDictionary = readBoolean(metaXml.getHasDictionary());
         metadonnee.setHasDictionary(hasDictionary);

         //-- Diffusable client
         boolean dispo = readBoolean(metaXml.getClientAvailable());
         metadonnee.setClientAvailable(dispo);
         
         //-- Métadonnée gérée directement par DFCE
         boolean isInternal = readBoolean(metaXml.getInternal());
         metadonnee.setInternal(isInternal);

         //-- Obligatoire au stockage
         boolean requiredForStorage = readBoolean(metaXml.getRequiredForStorage());
         metadonnee.setRequiredForStorage(requiredForStorage);

         //-- Modifiable par le client
         boolean modifiableParClient = readBoolean(metaXml.getModifiable());
         metadonnee.setModifiable(modifiableParClient);

         //-- Trim à gauche
         boolean trimGauche = readBoolean(metaXml.getLeftTrimable());
         metadonnee.setLeftTrimable(trimGauche);

         //-- Trim à droite
         boolean trimDroite = readBoolean(metaXml.getRightTrimable());
         metadonnee.setRightTrimable(trimDroite);
         
         //-- Transferable
         boolean transferable = readBoolean(metaXml.getTransferable());
         metadonnee.setTransferable(transferable);
      }
      return metadonneesAcreer;
   }

   private String readString(String str) {
      return StringUtils.trimToEmpty(str);
   }
   
   private boolean readBoolean(String boolStr) {
      String str = readString(boolStr);
      if ("oui".equalsIgnoreCase(str)) {
         return true;
      } else if ("non".equalsIgnoreCase(str)) {
         return false;
      } else {
         String msssg = "La valeur " + str + " n'est pas convertible en boolean";
         throw new MajLotRuntimeException(msssg);
      }
   }
   
   private int readInt(String intStr){
      String str = readString(intStr);
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
      compareDeuxListeLignes("1", "refmeta/MetadataReferential_Lot150400_ameliore.xml", lignesGenerees);
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
   protected List<SaeCategory> genereMetaBaseDfce(List<MetadataReference> metas) {
      
      List<SaeCategory> saeCategories = new ArrayList<SaeCategory>();
      
      for (MetadataReference meta : metas) {
         
         if (!meta.isInternal()) {     
            SaeCategory saeCat = new SaeCategory();
            saeCat.setDescriptif(meta.getLongCode());
            saeCat.setName(meta.getShortCode());
            saeCat.setDataType(meta.getType());
            saeCat.setIndex(meta.getIsIndexed());
            
            int maxVals = 1;
            int minVals = (meta.isRequiredForStorage()) ? 1 : 0;
            saeCat.setMinimumValues(minVals);
            saeCat.setMinimumValues(maxVals);
            saeCat.setSingle(false);
            saeCat.setEnableDictionary(false);      
            
            saeCategories.add(saeCat);
         }
      } 
      return saeCategories;
   }

   protected void verification2(List<MetadataReference> metadonnees) {
      List<String> lignesGenerees = genereFichierXmlAncienneVersionBaseDfce(metadonnees);
      compareDeuxListeLignes("2", "refmeta/saeBase_Lot150400_ameliore.xml", lignesGenerees);
   }

   private void persisteMetadonnees(Keyspace keyspace, List<MetadataReference> metadonnees) {

      LOG.info("Persistence des métadonnées");
      
      //-- Instantiation de la DAO, de son support, et du support des clock Cassandra
      SaeMetadataDao metaDao = new SaeMetadataDao(keyspace);
      SaeMetadataSupport metaSupport = new SaeMetadataSupport(metaDao);

      JobClockConfiguration clockConfiguration = new JobClockConfiguration();
      clockConfiguration.setMaxTimeSynchroError(10000000);
      clockConfiguration.setMaxTimeSynchroWarn(2000000);

      JobClockSupport clockSupport = new JobClockSupportImpl(keyspace, clockConfiguration);

      //-- Création des métadonnées en base Cassandra uniquement (pas dans DFCE)
      for (MetadataReference metadonnee : metadonnees) {
         if (metaSupport.find(metadonnee.getLongCode()) != null) {
            metaSupport.modify(metadonnee, clockSupport.currentCLock());
         } else {
            metaSupport.create(metadonnee, clockSupport.currentCLock());
         }
      }
   }

}
