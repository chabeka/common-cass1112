package fr.urssaf.image.sae.trace.model;

import java.util.UUID;

/**
 * Trace créée par DFCE
 * 
 * 
 */
public class DfceTraceDoc extends DfceTrace {

   /**
    * UUID du journal dans lequel a été archivé l'événement (null si l'événement
    * n'a pas été archivé)
    */
   private UUID idJournal;

   /**
    * @return the idJournal
    */
   public final UUID getIdJournal() {
      return idJournal;
   }

   /**
    * @param idJournal the idJournal to set
    */
   public final void setIdJournal(UUID idJournal) {
      this.idJournal = idJournal;
   }

}
