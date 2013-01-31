package fr.urssaf.image.sae.regionalisation.fond.documentaire.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Criterion;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.BaseAdministrationService;
import net.docubase.toolkit.service.ged.SearchService;
import net.docubase.toolkit.service.ged.StoreService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVReader;

import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.TagControlException;
import com.netflix.astyanax.query.AllRowsQuery;

import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.DocInfoDao;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf.DocInfoKey;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.ErreurTechniqueException;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.iterator.CassandraIterator;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.service.RegioParDocumentService;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.support.CassandraSupport;

/**
 * Service des traitements de régionalisation se basant sur le document (par
 * opposition aux traitements de régionalisation se basant sur les fichiers de
 * la V2)
 */
@Service
public class RegioParDocumentServiceImpl implements RegioParDocumentService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(RegioParDocumentServiceImpl.class);

   @Autowired
   private CassandraSupport cassandraSupport;

   @Autowired
   private DocInfoDao infoDao;

   @Autowired
   private Properties cassandraConf;

   @Autowired
   private DFCEConnectionService dfceConnectionService;

   /**
    * {@inheritDoc}
    * 
    * @throws IOException
    */
   @Override
   public void prepareFichiersV2(File pathFichiersV2, File pathFichiersSortie) {

      LOGGER.info("Début des traitement");

      try {

         // Récupère la liste des sous-répertoires :
         // 1 répertoire par code organisme régionalisé
         File[] directories = pathFichiersV2.listFiles();

         // Les 3 fichiers de sortie
         File fileCoti = new File(pathFichiersSortie,
               "regionalisation_coti.csv");
         File fileCpte = new File(pathFichiersSortie,
               "regionalisation_cpte.csv");
         File filePers = new File(pathFichiersSortie,
               "regionalisation_pers.csv");
         fileCoti.delete();
         fileCpte.delete();
         filePers.delete();
         FileWriter fileWriterCoti = new FileWriter(fileCoti, false);
         FileWriter fileWriterCpte = new FileWriter(fileCpte, false);
         FileWriter fileWriterPers = new FileWriter(filePers, false);
         try {

            // Les lignes d'en-tête des 3 fichiers
            fileWriterCoti
                  .write("code,old_cog,old_reference,new_reference,new_cog,old_cog_avec_ur\n");
            fileWriterCpte
                  .write("code,old_cog,old_reference,new_reference,new_cog,old_cog_avec_ur\n");
            fileWriterPers
                  .write("code,old_cog,old_reference,new_reference,new_cog,old_cog_avec_ur,old_reference_sans_zeros_devant,new_reference_sans_zeros_devant\n");

            // Boucle sur la liste des répertoires "organisme"
            String line;
            Reader reader = null;
            BufferedReader bReader = null;
            for (File directory : directories) {

               LOGGER.info("Début de traitement du répertoire {}", directory
                     .getName());

               // Boucle sur la liste des fichiers de l'organisme
               // Normalement, 3 fichiers :
               // - fusion_regi_COTI.lst
               // - fusion_regi_CPTE.lst
               // - fusion_regi_PERS.lst
               for (File file : directory.listFiles()) {

                  try {

                     reader = new FileReader(file);
                     bReader = new BufferedReader(reader);

                     if (file.getName().contains("COTI")) {

                        while ((line = bReader.readLine()) != null) {
                           fileWriterCoti.write(traiteLigneCoti(line));
                           fileWriterCoti.write("\n");
                        }

                     } else if (file.getName().contains("CPTE")) {

                        while ((line = bReader.readLine()) != null) {
                           fileWriterCpte.write(traiteLigneCpte(line));
                           fileWriterCpte.write("\n");
                        }

                     } else if (file.getName().contains("PERS")) {

                        while ((line = bReader.readLine()) != null) {
                           fileWriterPers.write(traiteLignePers(line));
                           fileWriterPers.write("\n");
                        }

                     }

                  } catch (IOException e) {
                     throw new ErreurTechniqueException(e);
                  } finally {

                     if (reader != null) {

                        try {
                           reader.close();
                        } catch (Exception e) {
                           e.printStackTrace();
                        }
                     }
                  }
               }

               LOGGER.info("Fin de traitement du répertoire {}", directory
                     .getName());
            }
         } finally {
            closeWriter(fileWriterCoti);
            closeWriter(fileWriterCpte);
            closeWriter(fileWriterPers);
         }
      } catch (IOException e) {
         throw new ErreurTechniqueException(e);
      }

      LOGGER.info("Fin des traitement");

   }

   private String traiteLigneCoti(String line) {

      String[] items = StringUtils.split(StringUtils.trim(line), '$');

      String oldCodeOrga = items[1];

      String oldCodeOrgaAvecUR = "UR" + oldCodeOrga;

      return String.format("%s,%s", StringUtils.join(items, ','),
            oldCodeOrgaAvecUR);

   }

   private String traiteLigneCpte(String line) {

      String[] items = StringUtils.split(StringUtils.trim(line), '$');

      String oldCodeOrga = items[1];

      String oldCodeOrgaAvecUR = "UR" + oldCodeOrga;

      return String.format("%s,%s", StringUtils.join(items, ','),
            oldCodeOrgaAvecUR);

   }

   private String traiteLignePers(String line) {

      String[] items = StringUtils.split(StringUtils.trim(line), '$');

      String oldCodeOrga = items[1];
      String oldNumPers = items[2];
      String newNumPers = items[3];

      String oldCodeOrgaAvecUR = "UR" + oldCodeOrga;
      String oldNumPersSansZerosDevant = new Integer(oldNumPers).toString();
      String newNumPersSansZerosDevant = new Integer(newNumPers).toString();

      return String.format("%s,%s,%s,%s", StringUtils.join(items, ','),
            oldCodeOrgaAvecUR, oldNumPersSansZerosDevant,
            newNumPersSansZerosDevant);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void extractionFondsDocumentaire(File fichierSortieCsv) {

      // Liste des métadonnées que l'on va lire
      List<String> reqMetas = new ArrayList<String>();
      reqMetas.add("SM_UUID");
      reqMetas.add("cog");
      reqMetas.add("cop");
      reqMetas.add("nce");
      reqMetas.add("npe");
      reqMetas.add("nci");
      reqMetas.add("SM_BASE_ID");

      // Récupère le nom de la base DFCE sur laquelle travailler
      String nomBaseDfceAttendue = cassandraConf.getProperty("db.baseName");

      Writer writer = null;
      try {

         cassandraSupport.connect();

         writer = new FileWriter(fichierSortieCsv);

         AllRowsQuery<DocInfoKey, String> query = infoDao.getQuery(reqMetas
               .toArray(new String[0]));
         CassandraIterator<DocInfoKey> iterator = new CassandraIterator<DocInfoKey>(
               query);

         Map<String, String> map;

         int nbDocsTraites = 0;
         int nbDocsSortis = 0;

         String idDoc;
         String cog;
         String nomBaseDfce;

         while (iterator.hasNext()) {

            map = iterator.next();

            idDoc = map.get("SM_UUID");
            cog = map.get("cog");
            nomBaseDfce = map.get("SM_BASE_ID");

            // Vérifie que l'on se trouve bien sur un document
            // de la base documentaire attendu
            if (StringUtils.equals(nomBaseDfce, nomBaseDfceAttendue)
                  && StringUtils.isNotBlank(idDoc)
                  && StringUtils.isNotBlank(cog)) {

               writer.write(idDoc);
               writer.write(";");
               writer.write(cog);
               writer.write(";");
               writer.write(map.get("cop"));
               writer.write(";");
               writer.write(StringUtils.trimToEmpty(map.get("nce")));
               writer.write(";");
               writer.write(StringUtils.trimToEmpty(map.get("nci")));
               writer.write(";");
               writer.write(StringUtils.trimToEmpty(map.get("npe")));
               writer.write("\n");

               nbDocsSortis++;

            }

            nbDocsTraites++;
            if ((nbDocsTraites % 1000) == 0) {
               LOGGER.info("Nombre de docs traités : {}", nbDocsTraites);
            }

         }

         LOGGER.info("Nombre total de docs traités : {}", nbDocsTraites - 1);
         LOGGER.info("Nombre total de docs sortis dans le fichier : {}",
               nbDocsSortis - 1);

      } catch (IOException e) {
         throw new ErreurTechniqueException(e);

      } finally {
         closeWriter(writer);
         cassandraSupport.disconnect();
      }

   }

   private void closeWriter(Writer writer) {
      try {
         if (writer != null) {
            writer.close();
         }
      } catch (IOException e) {
         throw new ErreurTechniqueException(e);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void miseAjourDocuments(File fichierCsv, int numeroPremiereLigne,
         File fichierTraces) {

      // TODO: Gérer l'arrêt de Tomcat

      // Trace
      LOGGER.info("Début du traitement");

      // Indices des éléments du fichier CSV
      int IDX_ID_DOC = 0;
      int IDX_COG_ENCOURS = 1;
      int IDX_COP_ENCOURS = 2;
      int IDX_NCE_ENCOURS = 3;
      int IDX_NCI_ENCOURS = 4;
      int IDX_NPE_ENCOURS = 5;
      int IDX_COG_RENUM = 6;
      int IDX_COP_RENUM = 7;
      int IDX_NCE_RENUM = 8;
      int IDX_NCI_RENUM = 9;
      int IDX_NPE_RENUM = 10;
      int IDX_IS_NCE = 11;
      int IDX_IS_NCI = 12;
      int IDX_IS_NPE = 13;
      int IDX_IS_COG = 14;
      int IDX_IS_COP = 15;

      // Récupère le nom de la base DFCE sur laquelle travailler
      String nomBaseDfce = cassandraConf.getProperty("db.baseName");

      // Préparation du fichier de traces
      FileWriter fileWriter;
      try {
         fileWriter = new FileWriter(fichierTraces);
      } catch (IOException e) {
         throw new ErreurTechniqueException(e);
      }
      try {

         // Préparation de la lecture du fichier CSV de traitement
         CSVReader reader = new CSVReader(new FileReader(fichierCsv), ';');

         // Se positionne à la 1ère ligne souhaitée
         for (int i = 1; i < numeroPremiereLigne; i++) {
            reader.readNext();
         }

         // Préparation de l'accès à DFCE
         ServiceProvider serviceProvider = dfceConnectionService
               .openConnection();
         BaseAdministrationService baseService = serviceProvider
               .getBaseAdministrationService();
         Base base = baseService.getBase(nomBaseDfce);
         SearchService searchService = serviceProvider.getSearchService();
         StoreService storeService = serviceProvider.getStoreService();

         // Boucle sur la liste des documents à traiter
         int cptDoc = 0;
         String[] nextLine;
         UUID idDoc;
         Document document;

         String is_nci;
         String is_nce;
         String is_npe;
         String is_cog;
         String is_cop;

         int numeroLigne = numeroPremiereLigne;
         while ((nextLine = reader.readNext()) != null) {

            // Lecture de l'UUID du document à traiter
            idDoc = UUID.fromString(nextLine[IDX_ID_DOC]);

            // Recherche du document dans DFCE
            document = searchService.getDocumentByUUID(base, idDoc);
            if (document == null) {
               throw new ErreurTechniqueException("Anomalie sur document " + idDoc);
            }

            // Lecture des flag de traitement par métadonnées
            is_nce = nextLine[IDX_IS_NCE];
            is_nci = nextLine[IDX_IS_NCI];
            is_npe = nextLine[IDX_IS_NPE];
            is_cog = nextLine[IDX_IS_COG];
            is_cop = nextLine[IDX_IS_COP];

            // Mise à jour des métadonnées
            if (is_nce.equals("1")) {
               updateMeta(fileWriter, numeroLigne, idDoc, document, "nce",
                     nextLine[IDX_NCE_ENCOURS], nextLine[IDX_NCE_RENUM]);
            }
            if (is_nci.equals("1")) {
               updateMeta(fileWriter, numeroLigne, idDoc, document, "nci",
                     nextLine[IDX_NCI_ENCOURS], nextLine[IDX_NCI_RENUM]);
            }
            if (is_npe.equals("1")) {
               updateMeta(fileWriter, numeroLigne, idDoc, document, "npe",
                     nextLine[IDX_NPE_ENCOURS], nextLine[IDX_NPE_RENUM]);
            }
            if (is_cog.equals("1")) {
               updateMeta(fileWriter, numeroLigne, idDoc, document, "cog",
                     nextLine[IDX_COG_ENCOURS], nextLine[IDX_COG_RENUM]);
            }
            if (is_cop.equals("1")) {
               updateMeta(fileWriter, numeroLigne, idDoc, document, "cop",
                     nextLine[IDX_COP_ENCOURS], nextLine[IDX_COP_RENUM]);
            }

            // Ecriture de la mise à jour du document dans DFCE
            updateDocument(storeService, document);

            // Comptages
            cptDoc++;
            if ((cptDoc % 100) == 0) {
               LOGGER.info("Nombre de documents traités : {}", cptDoc);
            }

            // Incrémente le numéro de la ligne en cours
            numeroLigne++;

         }

         LOGGER.info("Nombre de documents traités : {}", cptDoc);

         LOGGER.info("Opération terminée");

      } catch (IOException e) {
         throw new ErreurTechniqueException(e);
      } catch (TagControlException e) {
         throw new ErreurTechniqueException(e);
      } catch (FrozenDocumentException e) {
         throw new ErreurTechniqueException(e);
      } finally {
         try {
            fileWriter.close();
         } catch (IOException e) {
            throw new ErreurTechniqueException(e);
         }
      }

   }

   private void updateDocument(StoreService storeService, Document document)
         throws TagControlException, FrozenDocumentException {

      storeService.updateDocument(document);

   }

   private void updateMeta(FileWriter fileWriterTraces, int numeroLigne,
         UUID idDoc, Document document, String codeCourt,
         String valeurEnCoursAttendue, String valeurRenum) throws IOException {

      Criterion criterion = document.getSingleCriterion(codeCourt);
      String valeurEnCours = (String) criterion.getWord();

      if (valeurEnCours.equals(valeurEnCoursAttendue)) {

         criterion.setWord((Serializable) valeurRenum);

         fileWriterTraces.write(numeroLigne + "%" + idDoc + "%" + codeCourt
               + "%" + valeurEnCours + "%" + valeurRenum + "\r\n");

      } else {
         throw new ErreurTechniqueException("Anomalie sur document " + idDoc);
      }

   }

}
