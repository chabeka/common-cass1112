package fr.urssaf.image.commons.spring.batch.util;

import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.springframework.util.Assert;

import fr.urssaf.image.commons.spring.batch.model.flat.Livre;

public class LivreEquals implements IArgumentMatcher {

   private final Integer identifiant;

   public LivreEquals(Integer identifiant) {
      this.identifiant = identifiant;
      Assert.notNull(identifiant);
   }

   @Override
   public void appendTo(StringBuffer buffer) {

      buffer.append("livre identifiant ");
      buffer.append(identifiant);

   }

   @Override
   public boolean matches(Object argument) {

      boolean matches = false;

      if (argument instanceof Livre) {
         Livre actual = (Livre) argument;
         matches = identifiant.equals(actual.getIdentifiant());

      }

      return matches;

   }

   public static Livre compareTo(Integer identifiant) {
      EasyMock.reportMatcher(new LivreEquals(identifiant));

      Livre livre = new Livre();
      livre.setIdentifiant(identifiant);

      return livre;

   }
}
