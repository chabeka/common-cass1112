package fr.urssaf.image.administration.modele;

import me.prettyprint.hector.api.ddl.ComparatorType;
import fr.urssaf.image.helper.ComparatorTypes;

public class ComparatorTypeFinder {
   public static ComparatorType comparatorTypeFinder(
         final String codeComparatorType) {
      for (ComparatorTypes technical : ComparatorTypes.values()) {
         if (technical.getCodeComparatorType().equals(codeComparatorType)) {
            return technical.getComparatorType();
         }
      }
      return ComparatorTypes.NOVALUE.getComparatorType();
   }
}