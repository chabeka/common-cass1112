/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.service.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.regionalisation.util.Constants;

import net.docubase.toolkit.model.document.Document;

/**
 * 
 * 
 */
public class TraceDocumentUtils {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(TraceDocumentUtils.class);

   private TraceDocumentUtils() {
   }

   /**
    * Liste les documents qui ne sont pas mis à jour
    * 
    * @param arrayList
    * @param documents
    */
   public static void logDocumentsNotUpdated(ArrayList<String> lines,
         List<Document> documents) {
      String key = lines.get(0).split(";")[0].split(":")[0];

      if (Constants.NUM_CPTE_EXT.equals(key)) {

         List<String> cogs = new ArrayList<String>();
         List<String> elements;
         String cog;
         for (String line : lines) {
            elements = Arrays.asList(line.split(";"));
            int intCog = elements.indexOf(Constants.CODE_ORG_GEST);
            cog = elements.get(intCog + 1).split(">")[0];
            if (!cogs.contains(cog)) {
               cogs.add(cog);
            }
         }

         for (Document document : documents) {
            cog = (String) document.getSingleCriterion(Constants.CODE_ORG_GEST)
                  .getWord();
            if (!cogs.contains(cog)) {
               LOGGER.trace(
                     "le document {} n'est pas modifié. nce = {} / cog = {}",
                     new Object[] {
                           document.getFileUUID(),
                           (String) document.getSingleCriterion(
                                 Constants.NUM_CPTE_EXT).getWord(), cog });
            }
         }
      }

   }

}
