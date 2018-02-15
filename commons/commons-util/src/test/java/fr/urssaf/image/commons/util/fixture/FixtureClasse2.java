package fr.urssaf.image.commons.util.fixture;

import org.apache.commons.lang.NotImplementedException;

/**
 * Classe de fixture<br>
 * <br>
 * Un constructeur privé sans paramètre qui lève une exception
 */
@SuppressWarnings("PMD")
public final class FixtureClasse2 {

   private FixtureClasse2() {
      throw new NotImplementedException("FixtureClasse2Test");
   }
   
}
