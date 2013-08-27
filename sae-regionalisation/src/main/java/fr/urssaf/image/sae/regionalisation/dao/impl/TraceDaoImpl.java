package fr.urssaf.image.sae.regionalisation.dao.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;
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
 * 
 * 
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
         Map<String, String> map = new HashMap<String, String>();
         map.put(Constants.TRACE_LIGNE, String.valueOf(trace.getLineNumber()));
         map.put(Constants.TRACE_ID_DOCUMENT, trace.getIdDocument().toString());
         map.put(Constants.TRACE_META_NAME, trace.getMetaName());
         map.put(Constants.TRACE_OLD_VALUE, trace.getOldValue());
         map.put(Constants.TRACE_NEW_VALUE, trace.getNewValue());

         writer.write(StrSubstitutor.replace(Constants.TRACE_OUT_MAJ, map));
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

}
