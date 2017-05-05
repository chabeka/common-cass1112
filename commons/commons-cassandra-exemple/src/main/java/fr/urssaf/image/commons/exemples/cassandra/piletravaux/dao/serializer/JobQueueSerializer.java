package fr.urssaf.image.commons.exemples.cassandra.piletravaux.dao.serializer;

import fr.urssaf.image.commons.cassandra.serializer.JacksonSerializer;
import fr.urssaf.image.commons.exemples.cassandra.piletravaux.modele.JobQueue;

/**
 * Sérialiseur/dé-sérialiseur de SimpleJobRequest
 * Utilise un sérialiseur jackson (json)
 *
 */
public final class JobQueueSerializer extends JacksonSerializer<JobQueue> {

   private static final JobQueueSerializer INSTANCE = new JobQueueSerializer(JobQueue.class);

   /**
    * Constructeur
    * @param clazz   : La classe, qui est obligatoirement JobQueue.class (merci java)
    */
   private JobQueueSerializer(Class<JobQueue> clazz) {
      super(clazz);
   }
   
   /**
    * Renvoie un singleton
    * @return  singleton
    */
   public static JobQueueSerializer get() {
      return INSTANCE;
   }

}