package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockConfiguration;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.cassandra.support.clock.impl.JobClockSupportImpl;
import fr.urssaf.image.sae.lotinstallmaj.dao.SAECassandraDao;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRuntimeException;
import fr.urssaf.image.sae.lotinstallmaj.modele.SaeCategory;
import fr.urssaf.image.sae.lotinstallmaj.modele.metadata.IndexReference;
import fr.urssaf.image.sae.lotinstallmaj.modele.metadata.IndexesComposites;
import fr.urssaf.image.sae.lotinstallmaj.modele.metadata.MetaReference;
import fr.urssaf.image.sae.lotinstallmaj.modele.metadata.ReferentielMeta;
import fr.urssaf.image.sae.lotinstallmaj.service.cql.impl.SAEKeyspaceConnecter;
import fr.urssaf.image.sae.lotinstallmaj.service.utils.XmlUtils;
import fr.urssaf.image.sae.metadata.referential.dao.SaeMetadataDao;
import fr.urssaf.image.sae.metadata.referential.dao.cql.IMetadataDaoCql;
import fr.urssaf.image.sae.metadata.referential.dao.cql.impl.MetadataCqlDaoImpl;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.support.SaeMetadataSupport;
import fr.urssaf.image.sae.metadata.referential.support.cql.SaeMetadataCqlSupport;
import fr.urssaf.image.sae.metadata.utils.Constantes;
import me.prettyprint.hector.api.Keyspace;

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

   private static final String XSD_METADONNEES = "/xsd/metadata/Metadonnees.xsd";
   private static final String XSD_INDEXES_COMPOSITES = "/xsd/metadata/IndexesComposites.xsd";

   // A MODIFIER LORS DES EVOLUTIONS DE FICHIERS !!
   private static final String FICHIER_METADONNEES = "Metadonnees.4.5.xml";

   private static final String NOM_FICHIER_INDEX_COMPOSITE = "IndexesComposites4.5.xml";

   private static final Logger LOG = LoggerFactory
         .getLogger(RefMetaInitialisationService.class);

   private List<MetadataReference> listMetas;

   private Map<String[], String> indexesComposites;

   @Autowired
   private SAEKeyspaceConnecter saecf;

   @Autowired
   private SAECassandraDao saeCassandraDao;

   @Autowired
   private ModeApiCqlSupport modeApiCqlSupport;

   private final String cfName = Constantes.CF_METADATA;

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
         } catch (final JAXBException e) {
            throw new MajLotRuntimeException(e);
         } catch (final SAXException e) {
            throw new MajLotRuntimeException(e);
         } catch (final IOException e) {
            throw new MajLotRuntimeException(e);
         }
      }
      return listMetas;
   }

   /**
    * Récupération après chargement du fichier,
    * de la liste des index composite pour la GNT
    * 
    * @return : La liste des index composite pour la GNT
    */
   public Map<String[], String> getIndexesCompositesGNT(){
      if(indexesComposites == null){
         try {
            //-- Lecture du fichier XML, et remplissage 
            // d'une liste d'objets métadonnées
            indexesComposites = chargerFichierIdxCompositesGNT(true);
         } catch (final JAXBException e) {
            throw new MajLotRuntimeException(e);
         } catch (final SAXException e) {
            throw new MajLotRuntimeException(e);
         } catch (final IOException e) {
            throw new MajLotRuntimeException(e);
         }
      }
      return indexesComposites;
   }

   /**
    * Récupération après chargement du fichier,
    * de la liste des index composite pour la GNS
    * 
    * @return : La liste des index composite pour la GNS
    */
   public Map<String[], String> getIndexesCompositesGNS(){
      if(indexesComposites == null){
         try {
            //-- Lecture du fichier XML, et remplissage 
            // d'une liste d'objets métadonnées
            indexesComposites = chargerFichierIdxCompositesGNS(true);
         } catch (final JAXBException e) {
            throw new MajLotRuntimeException(e);
         } catch (final SAXException e) {
            throw new MajLotRuntimeException(e);
         } catch (final IOException e) {
            throw new MajLotRuntimeException(e);
         }
      }
      return indexesComposites;
   }

   /**
    * Récupération après chargement du fichier,
    * de la liste des index composite à supprimer de la GNT
    * 
    * @return : La liste des index composite à supprimer de la GNT
    */
   public Map<String[], String> getIndexesCompositesASupprimerGNT() {
      Map<String[], String> indexASupprimer;
      try {
         //-- Lecture du fichier XML, et remplissage 
         // d'une liste d'objets métadonnées
         indexASupprimer = chargerFichierIdxCompositesGNT(false);
      }
      catch (final JAXBException | SAXException | IOException e) {
         throw new MajLotRuntimeException(e);
      }

      return indexASupprimer;
   }


   /**
    * Récupération après chargement du fichier,
    * de la liste des index composite à supprimer de la GNS
    * 
    * @return : La liste des index composite à supprimer de la GNS
    */
   public Map<String[], String> getIndexesCompositesASupprimerGNS() {
      Map<String[], String> indexASupprimer;
      try {
         //-- Lecture du fichier XML, et remplissage 
         // d'une liste d'objets métadonnées
         indexASupprimer = chargerFichierIdxCompositesGNS(false);
      }
      catch (final JAXBException | SAXException | IOException e) {
         throw new MajLotRuntimeException(e);
      }

      return indexASupprimer;
   }


   /**
    * Initialisation du référentiel des métadonnées en version 3.3 
    * 
    * @param keyspace
    *           le Keyspace Cassandra
    */
   public void initialiseRefMeta() {

      //-- Trace
      LOG.info("Initialisation du nouveau référentiel des métadonnées");

      //-- On récupère la liste des métas
      final List<MetadataReference> metadonnees = getListMetas();

      LOG.info("Nombre de métadonnées à créer : {}", metadonnees.size());
      if (modeApiCqlSupport.isModeThrift(cfName)) {
         persisteMetadonnees(metadonnees);
      } else if (modeApiCqlSupport.isModeCql(cfName)) {
         persisteMetadonneesCQL(metadonnees);
      }
      //-- Trace
      LOG.info("Fin de l'initialisation du nouveau référentiel des métadonnées");
   }

   /**
    * Initialisation du référentiel des métadonnées en version 4.4 en base SAE CQL
    * 
    * @param keyspace
    *           le Keyspace Cassandra : uniquement pour le mode Thrift
    */
   public void initialiseRefMetaCQL() {
      // -- Trace
      LOG.info("Initialisation du nouveau référentiel des métadonnées CQL");

      // -- On récupère la liste des métas
      final List<MetadataReference> metadonnees = getListMetas();

      LOG.info("Nombre de métadonnées à créer : {}", metadonnees.size());
      persisteMetadonneesCQL(metadonnees);
      // -- Trace
      LOG.info("Fin de l'initialisation du nouveau référentiel des métadonnées CQL");
   }

   protected Map<String[], String> chargerFichierIdxCompositesGNT(final boolean aCreer) throws IOException, JAXBException, SAXException  {


      final ClassPathResource ressourceXml = new ClassPathResource(NOM_FICHIER_INDEX_COMPOSITE);
      final InputStream xmlStream = ressourceXml.getInputStream();

      final ClassPathResource ressourceXsd = new ClassPathResource(XSD_INDEXES_COMPOSITES);
      final IndexesComposites ref = XmlUtils.unmarshalStream(IndexesComposites.class, xmlStream, ressourceXsd);

      final List<IndexReference> indexes = ref.getIndexReference();

      final Map<String[], String> indexesAcreer = new HashMap<>();

      for (int i = 0; i < indexes.size(); i++) {
         final IndexReference indexXml = indexes.get(i);

         //-- On ignore les indexes qui ne sont pas "aCreer"
         if(readBoolean(indexXml.getACreerGNT()) != aCreer) {
            continue;
         }

         final String composition = indexXml.getComposition();
         final String[] metasList = readString(composition).split(":");
         final String aIndexerVide = indexXml.getAIndexerVide();
         indexesAcreer.put(metasList, aIndexerVide);
      }
      return indexesAcreer;
   }

   protected Map<String[], String> chargerFichierIdxCompositesGNS(final boolean aCreer) throws IOException, JAXBException, SAXException  {

      final ClassPathResource ressourceXml = new ClassPathResource(NOM_FICHIER_INDEX_COMPOSITE);
      final InputStream xmlStream = ressourceXml.getInputStream();

      final ClassPathResource ressourceXsd = new ClassPathResource(XSD_INDEXES_COMPOSITES);
      final IndexesComposites ref = XmlUtils.unmarshalStream(IndexesComposites.class, xmlStream, ressourceXsd);

      final List<IndexReference> indexes = ref.getIndexReference();

      final Map<String[], String> indexesAcreer = new HashMap<>();

      for (int i = 0; i < indexes.size(); i++) {
         final IndexReference indexXml = indexes.get(i);

         //-- On ignore les indexes qui ne sont pas "aCreer"
         if(readBoolean(indexXml.getACreerGNS()) != aCreer) {
            continue;
         }

         final String composition = indexXml.getComposition();
         final String[] metasList = readString(composition).split(":");
         final String aIndexerVide = indexXml.getAIndexerVide();
         indexesAcreer.put(metasList, aIndexerVide);
      }
      return indexesAcreer;
   }

   public List<MetadataReference> chargeFichierMeta() throws JAXBException, SAXException, IOException {


      final ClassPathResource ressourceXml = new ClassPathResource(FICHIER_METADONNEES);
      final InputStream xmlStream = ressourceXml.getInputStream();

      final ClassPathResource ressourceXsd = new ClassPathResource(XSD_METADONNEES);
      final ReferentielMeta ref = XmlUtils.unmarshalStream(ReferentielMeta.class, xmlStream, ressourceXsd);
      final List<MetaReference> metas = ref.getMetaReference();

      final List<MetadataReference> metadonneesAcreer = new ArrayList<>();

      for (int i = 0; i < metas.size(); i++) {

         final MetaReference metaXml = metas.get(i);

         //-- On ignore les métas qui sont pas "aCreer" ou "aModifier"
         if(!readBoolean(metaXml.getACreer()) && !readBoolean(metaXml.getAModifier())){
            continue;
         }

         final MetadataReference metadonnee = new MetadataReference();
         metadonneesAcreer.add(metadonnee);

         //-- Code long
         final String longCode = readString(metaXml.getLongCode());
         metadonnee.setLongCode(longCode);

         //-- Code court
         final String shortCode = readString(metaXml.getShortCode());
         metadonnee.setShortCode(shortCode);

         //-- Type DFCE
         final String typeDfce = readString(metaXml.getType());
         metadonnee.setType(typeDfce);

         //-- Libellé
         final String libelle = readString(metaXml.getLabel());
         metadonnee.setLabel(libelle);

         //-- Description
         final String description = readString(metaXml.getDescription());
         metadonnee.setDescription(description);

         //-- Spécifiable à l'archivage
         final boolean isArchivable = readBoolean(metaXml.getArchivable());
         metadonnee.setArchivable(isArchivable);

         //-- Obligatoire à l'archivage
         final boolean requiredForArchival = readBoolean(metaXml.getRequiredForArchival());
         metadonnee.setRequiredForArchival(requiredForArchival);

         //-- Consultée par défaut
         final boolean defaultConsultable = readBoolean(metaXml.getDefaultConsultable());
         metadonnee.setDefaultConsultable(defaultConsultable);

         //-- Consultable
         final boolean consultable = readBoolean(metaXml.getConsultable());
         metadonnee.setConsultable(consultable);

         //-- Critère de recherche
         final boolean isSearchable = readBoolean(metaXml.getSearchable());
         metadonnee.setSearchable(isSearchable);

         //-- Indexée
         final boolean isIndexed = readBoolean(metaXml.getIsIndexed());
         metadonnee.setIsIndexed(isIndexed);

         //-- Formatage
         final String pattern = "";
         metadonnee.setPattern(pattern);

         //-- Taille maximum autorisée en archivage
         final int length = readInt(metaXml.getLength());
         metadonnee.setLength(length);

         //-- Nom du dictionnaire
         final String dictionaryName = readString(metaXml.getDictionaryName());
         metadonnee.setDictionaryName(dictionaryName);

         //-- Possède un dictionnaire ?
         final boolean hasDictionary = readBoolean(metaXml.getHasDictionary());
         metadonnee.setHasDictionary(hasDictionary);

         //-- Diffusable client
         final boolean dispo = readBoolean(metaXml.getClientAvailable());
         metadonnee.setClientAvailable(dispo);

         //-- Métadonnée gérée directement par DFCE
         final boolean isInternal = readBoolean(metaXml.getInternal());
         metadonnee.setInternal(isInternal);

         //-- Obligatoire au stockage
         final boolean requiredForStorage = readBoolean(metaXml.getRequiredForStorage());
         metadonnee.setRequiredForStorage(requiredForStorage);

         //-- Modifiable par le client
         final boolean modifiableParClient = readBoolean(metaXml.getModifiable());
         metadonnee.setModifiable(modifiableParClient);

         //-- Trim à gauche
         final boolean trimGauche = readBoolean(metaXml.getLeftTrimable());
         metadonnee.setLeftTrimable(trimGauche);

         //-- Trim à droite
         final boolean trimDroite = readBoolean(metaXml.getRightTrimable());
         metadonnee.setRightTrimable(trimDroite);

         //-- Transferable
         final boolean transferable = readBoolean(metaXml.getTransferable());
         metadonnee.setTransferable(transferable);
      }
      return metadonneesAcreer;
   }

   private String readString(final String str) {
      return StringUtils.trimToEmpty(str);
   }

   private boolean readBoolean(final String boolStr) {
      final String str = readString(boolStr);
      if ("oui".equalsIgnoreCase(str)) {
         return true;
      } else if ("non".equalsIgnoreCase(str)) {
         return false;
      } else {
         final String msssg = "La valeur " + str + " n'est pas convertible en boolean";
         throw new MajLotRuntimeException(msssg);
      }
   }

   private int readInt(final String intStr){
      final String str = readString(intStr);
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
                                                                 final List<MetadataReference> metadonnees) {

      final List<String> lines = new ArrayList<>();

      // String retourCharriot = "\r\n";

      lines.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      lines.add("<referentiel>");

      for (final MetadataReference metadonnee : metadonnees) {
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

   protected void verification1(final List<MetadataReference> metadonnees) {
      final List<String> lignesGenerees = genereFichierXmlAncienneVersionRefMeta(metadonnees);
      compareDeuxListeLignes("1", "refmeta/MetadataReferential_Lot160900_ameliore.xml", lignesGenerees);
   }

   @SuppressWarnings("unchecked")
   private void compareDeuxListeLignes(final String numeroVerif,
                                       final String ficRessourceOriginal, final List<String> lignesGenerees) {
      final String trcPrefix = "compareDeuxListeLignes()";
      final ClassPathResource resource = new ClassPathResource(ficRessourceOriginal);

      List<String> lignesOriginales;

      InputStream stream = null;
      try {
         stream = resource.getInputStream();
         lignesOriginales = IOUtils.readLines(stream, "UTF-8");
      } catch (final IOException e) {
         throw new MajLotRuntimeException(e);
      } finally {
         if (stream != null) {
            try {
               stream.close();
            } catch (final IOException exception) {
               LOG.info("{} - fermeture du flux " + ficRessourceOriginal
                        + " impossible", trcPrefix);
            }
         }
      }

      // Vérifie le nombre global de lignes
      //      if (lignesGenerees.size() != lignesOriginales.size()) {
      //         throw new MajLotRuntimeException("La vérification interne #"
      //               + numeroVerif
      //               + " a échoué. Nombre de lignes dans le fichier d'origine : "
      //               + lignesOriginales.size()
      //               + ". Nombre de lignes dans le fichier regénéré : "
      //               + lignesGenerees.size());
      //      }

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
                                                                  final List<MetadataReference> metadonnees) {

      final List<String> lines = new ArrayList<>();

      // String retourCharriot = "\r\n";

      lines.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      lines.add("<docuBase>");
      lines.add("   <base>");
      lines.add("      <categories>");

      for (final MetadataReference metadonnee : metadonnees) {

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
   protected List<SaeCategory> genereMetaBaseDfce(final List<MetadataReference> metas) {

      final List<SaeCategory> saeCategories = new ArrayList<>();

      for (final MetadataReference meta : metas) {

         if (!meta.isInternal()) {     
            final SaeCategory saeCat = new SaeCategory();
            saeCat.setDescriptif(meta.getLongCode());
            saeCat.setName(meta.getShortCode());
            saeCat.setDataType(meta.getType());
            saeCat.setIndex(meta.getIsIndexed());

            final int maxVals = 1;
            final int minVals = meta.isRequiredForStorage() ? 1 : 0;
            saeCat.setMinimumValues(minVals);
            saeCat.setMaximumValues(maxVals);
            saeCat.setSingle(false);
            saeCat.setEnableDictionary(false);      

            saeCategories.add(saeCat);
         }
      } 
      return saeCategories;
   }

   protected void verification2(final List<MetadataReference> metadonnees) {
      final List<String> lignesGenerees = genereFichierXmlAncienneVersionBaseDfce(metadonnees);
      compareDeuxListeLignes("2", "refmeta/saeBase_Lot160900_ameliore.xml", lignesGenerees);
   }

   private void persisteMetadonnees(final List<MetadataReference> metadonnees) {

      LOG.info("Persistence des métadonnées");

      final Keyspace keyspace = saeCassandraDao.getKeyspace();
      //-- Instantiation de la DAO, de son support, et du support des clock Cassandra
      final SaeMetadataDao metaDao = new SaeMetadataDao(keyspace);
      final SaeMetadataSupport metaSupport = new SaeMetadataSupport(metaDao);

      final JobClockConfiguration clockConfiguration = new JobClockConfiguration();
      clockConfiguration.setMaxTimeSynchroError(10000000);
      clockConfiguration.setMaxTimeSynchroWarn(2000000);

      final JobClockSupport clockSupport = new JobClockSupportImpl(keyspace, clockConfiguration);

      // -- Création des métadonnées en base Cassandra uniquement (pas dans DFCE)
      for (final MetadataReference metadonnee : metadonnees) {
         if (metaSupport.find(metadonnee.getLongCode()) != null) {
            metaSupport.modify(metadonnee, clockSupport.currentCLock());
         } else {
            metaSupport.create(metadonnee, clockSupport.currentCLock());
         }
      }
   }

   private void persisteMetadonneesCQL(final List<MetadataReference> metadonnees) {

      LOG.info("Persistence des métadonnées CQL");

      // -- Instantiation de la DAO, de son support, et du support des clock Cassandra
      final  IMetadataDaoCql metadataDaoCql = new MetadataCqlDaoImpl(saecf.getCcf());
      final SaeMetadataCqlSupport metadataCqlSupport = new SaeMetadataCqlSupport(metadataDaoCql);



      //-- Création des métadonnées en base Cassandra uniquement (pas dans DFCE)
      for (final MetadataReference metadonnee : metadonnees) {
         if (metadataCqlSupport.find(metadonnee.getLongCode()) != null) {
            metadataCqlSupport.modify(metadonnee);
         } else {
            metadataCqlSupport.create(metadonnee);
         }
      }
   }

   /**
    * Permet de vérifier si la liste de méta à crééer (En provenance du fichier XML) existe dejà en base SAE
    * 
    * @param keyspace
    * @return
    */
   public boolean findMetaReferencesInSAEDB(final Keyspace keyspace) {

      LOG.info("Test d'existence d'une liste de méta ");
      boolean isOK = false;

      // -- Instantiation de la DAO, de son support, et du support des clock Cassandra
      final SaeMetadataDao metaDao = new SaeMetadataDao(keyspace);
      final SaeMetadataSupport metaSupport = new SaeMetadataSupport(metaDao);

      try {
         final List<MetadataReference> metadataReferences = chargeFichierMeta();
         int countExistMeta = 0;
         for (final MetadataReference metadataReference : metadataReferences) {
            final MetadataReference meta = metaSupport.find(metadataReference.getLongCode());
            if (meta != null) {
               countExistMeta++;
            }
         }
         LOG.info("Méta à crééer : {},  Méta déjà existantes : {}", metadataReferences.size(), countExistMeta);
         if (countExistMeta == metadataReferences.size()) {
            isOK = true;
         }
      }
      catch (JAXBException | SAXException | IOException e) {
         throw new MajLotRuntimeException(e);
      }

      return isOK;
   }

   /**
    * Vérifier l'existence d'une liste de métadonnées
    * 
    * @param keyspace
    * @param codesLong
    * @return
    */
   public boolean findListMeta(final Keyspace keyspace, final List<String> codesLong) {
      boolean trouvee = false;

      // -- Instantiation de la DAO, de son support, et du support des clock Cassandra
      final SaeMetadataDao metaDao = new SaeMetadataDao(keyspace);
      final SaeMetadataSupport metaSupport = new SaeMetadataSupport(metaDao);

      int count = 0;
      // -- Création des métadonnées en base Cassandra uniquement (pas dans DFCE)
      for (final String codeLong : codesLong) {
         if (metaSupport.find(codeLong) != null) {
            count++;
         } else {
            return false;
         }
      }

      if (count == codesLong.size()) {
         trouvee = true;
      }

      return trouvee;
   }

   public MetadataReference findMeta(final Keyspace keyspace, final String codeCourt) {
      // -- Instantiation de la DAO, de son support, et du support des clock Cassandra
      final SaeMetadataDao metaDao = new SaeMetadataDao(keyspace);
      final SaeMetadataSupport metaSupport = new SaeMetadataSupport(metaDao);

      return metaSupport.find(codeCourt);
   }

}
