package fr.urssaf.image.commons.cassandra.spring.batch.idgenerator;

import java.util.Random;

/**
 * Générateur d'id utilisant un générateur de nombre aléatoires.
 * A utiliser pour des tests seulement
 *
 */
public class RandomIdGenerator implements IdGenerator {

   private final Random rnd = new Random();

   @Override
   public final long getNextId() {
      return rnd.nextLong();
   }

}
