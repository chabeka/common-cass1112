package fr.urssaf.image.sae.regionalisation.dao.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.sae.regionalisation.bean.Trace;
import fr.urssaf.image.sae.regionalisation.dao.TraceDao;
import fr.urssaf.image.sae.regionalisation.exception.ErreurTechniqueException;
import fr.urssaf.image.sae.regionalisation.util.Constants;

/**
 * Implémentation du service {@link TraceDao}
 */
@Repository
public class TraceDaoImpl implements TraceDao {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(TraceDaoImpl.class);

   private final File repository;
   private File file;
   private FileWriter writer;

   /**
    * Constructeur
    * 
    * @param repository
    *           répertoire² où le fichier de suivi sera créé
    */
   @Autowired
   public TraceDaoImpl(File repository) {
      this.repository = repository;
   }

   /**
    * {@inheritDoc}
    */
   public final void open(String uuid) {

      try {
         if (!repository.exists()) {
            repository.createNewFile();
         }

         this.file = new File(repository, "suivi_" + uuid + ".log");
         boolean fileExist = file.exists();
         this.writer = new FileWriter(file, true);

         if (!fileExist) {
            writer.write(Constants.ENTETE_OUT_CSV);
            writer.write("\n");
         }

      } catch (IOException e) {
         throw new ErreurTechniqueException(e);
      }
   }

   /**
    * {@inheritDoc}
    */
   public final void close() {

      if (writer != null) {
         try {
            writer.close();

         } catch (IOException e) {
            LOGGER.info("impossible de fermer le flux {}", file.getName());
         }
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @Transactional
   public final void addTraceMaj(Trace trace) {

      try {

         // Mode mise à jour ou tir à blanc
         if (trace.isModeMiseAjour()) {
            writer.write("MAJ;");
         } else {
            writer.write("TAB;");
         }

         // Numéro de la ligne du fichier d'entrée
         writer.write(Integer.toString(trace.getLineNumber()));
         writer.write(';');

         // Identifiant unique du document
         writer.write(trace.getIdDocument().toString());
         writer.write(';');

         // nce
         writer.write(Boolean.toString(trace.isNceIsRenum()));
         writer.write(';');
         writer.write(trace.getNceAncienneValeur());
         writer.write(';');
         writer.write(nullToEmpty(trace.getNceNouvelleValeurSiRenum()));
         writer.write(';');

         // nci
         writer.write(Boolean.toString(trace.isNciIsRenum()));
         writer.write(';');
         writer.write(trace.getNciAncienneValeur());
         writer.write(';');
         writer.write(nullToEmpty(trace.getNciNouvelleValeurSiRenum()));
         writer.write(';');

         // npe
         writer.write(Boolean.toString(trace.isNpeIsRenum()));
         writer.write(';');
         writer.write(trace.getNpeAncienneValeur());
         writer.write(';');
         writer.write(nullToEmpty(trace.getNpeNouvelleValeurSiRenum()));
         writer.write(';');

         // cog
         writer.write(Boolean.toString(trace.isCogIsRenum()));
         writer.write(';');
         writer.write(trace.getCogAncienneValeur());
         writer.write(';');
         writer.write(nullToEmpty(trace.getCogNouvelleValeurSiRenum()));
         writer.write(';');

         // cop
         writer.write(Boolean.toString(trace.isCopIsRenum()));
         writer.write(';');
         writer.write(trace.getCopAncienneValeur());
         writer.write(';');
         writer.write(nullToEmpty(trace.getCopNouvelleValeurSiRenum()));
         writer.write(';');

         // Termine la ligne de trace
         writer.write("\n");
         writer.flush();

      } catch (IOException e) {
         throw new ErreurTechniqueException(trace.getLineNumber(), e);
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final File getFile() {
      return file;
   }

   private String nullToEmpty(String str) {
      if (str == null) {
         return StringUtils.EMPTY;
      } else {
         return str;
      }
   }

}
