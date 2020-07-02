package fr.urssaf.image.parser_opencsv.application.dao.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.opencsv.CSVReader;

import fr.urssaf.image.parser_opencsv.application.dao.ICorrespondanceTableSSTIGedDao;
import fr.urssaf.image.parser_opencsv.application.model.CorrespondanceMetaObject;
import fr.urssaf.image.parser_opencsv.application.reader.correspondance.CorrespondanceReaderBuilder;

/**
 * implementation du dao en mode lecture du CSV comme source de données
 */
@Repository
public class CorrespondanceTableSSTIGedDaoImpl implements ICorrespondanceTableSSTIGedDao {

   private static final int NOMBRE_COLONNES_CSV_CAISSE = 3;

   private static final int NOMBRE_COLONNES_CSV_RND = 2;

   @Autowired
   @Qualifier("reader_correspondance_caisse")
   private CorrespondanceReaderBuilder matcherBuilderCaisse;

   @Autowired
   @Qualifier("reader_correspondance_rnd")
   private CorrespondanceReaderBuilder matcherBuilderRnd;

   /**
    * {@inheritDoc}
    * 
    * @throws IOException
    */
   @Override
   public Map<String, CorrespondanceMetaObject> getAllCaisseCorresp() {

      String[] nextLine;
      final Map<String, CorrespondanceMetaObject> listMappings = new HashMap<>();
      CSVReader reader;
      try {
         reader = matcherBuilderCaisse.getCsvBuilder();
         while ((nextLine = reader.readNext()) != null) {
            // Ce permet d'ignorer les lignes vides
            if (nextLine.length != NOMBRE_COLONNES_CSV_CAISSE || nextLine[0].startsWith("#")) {
               continue;
            }
            final String key = nextLine[1];
            final CorrespondanceMetaObject value = new CorrespondanceMetaObject(nextLine[1], nextLine[2]);
            listMappings.put(key, value);
         }
      }
      catch (final IOException e) {
         throw new RuntimeException("Le fichier de correspondance des codes TI est introuvable", e);
      }
      finally {
         try {
            matcherBuilderCaisse.closeStream();
         }
         catch (final IOException e) {
            throw new RuntimeException("Erreur lors de la fermeture de flux du fichier de correspondance des codes TI est introuvable", e);
         }
      }

      return listMappings;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Map<String, CorrespondanceMetaObject> getAllRNDCorresp() {
      String[] nextLine;
      final Map<String, CorrespondanceMetaObject> listMappings = new HashMap<>();
      CSVReader reader;
      try {
         reader = matcherBuilderRnd.getCsvBuilder();
         while ((nextLine = reader.readNext()) != null) {
            // Ce permet d'ignorer les lignes vides
            if (nextLine.length != NOMBRE_COLONNES_CSV_RND || nextLine[0].startsWith("#")) {
               continue;
            }
            final String key = nextLine[0];
            final CorrespondanceMetaObject value = new CorrespondanceMetaObject(nextLine[0], nextLine[1]);
            listMappings.put(key, value);
         }
      }
      catch (final IOException e) {
         throw new RuntimeException("Le fichier de correspondance des codes TI est introuvable", e);
      }
      finally {
         try {
            matcherBuilderRnd.closeStream();
         }
         catch (final IOException e) {
            throw new RuntimeException("Erreur lors de la fermeture de flux du fichier de correspondance des codes TI est introuvable", e);
         }
      }

      return listMappings;
   }

}
