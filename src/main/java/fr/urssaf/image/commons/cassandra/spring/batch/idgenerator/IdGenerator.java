package fr.urssaf.image.commons.cassandra.spring.batch.idgenerator;

/**
 * Interface commune aux générateurs d'id uniques
 * 
 *
 */
public interface IdGenerator {

   /**
    * Génère un id
    * @return  id généré
    */
   long getNextId();
}
