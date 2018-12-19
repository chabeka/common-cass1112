/**
 * 
 */
package fr.urssaf.image.sae.webservices.comparator;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.easymock.IArgumentMatcher;

import fr.urssaf.image.sae.services.consultation.model.ConsultParams;

/**
 * Comparateur utilisé par EasyMock dans les tests java
 * 
 */
public class ConsultParamComparator implements IArgumentMatcher {

   private final ConsultParams expected;

   /**
    * constructeur
    * 
    * @param expected
    *           le consultParams sur lequel les comparaisons se réaliseront
    */
   public ConsultParamComparator(ConsultParams expected) {
      this.expected = expected;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void appendTo(StringBuffer buffer) {
      buffer.append("ConsultParam(");
      buffer.append(expected.getClass().getName());
      buffer.append(" with UUID ");
      buffer.append(expected.getIdArchive().toString());
      buffer.append(" ) ");

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean matches(Object actual) {

      boolean match = false;

      if (actual instanceof ConsultParams && actual != null) {

         ConsultParams consultCurrent = (ConsultParams) actual;

         if (expected.getIdArchive().equals(consultCurrent.getIdArchive())) {
            if (expected.getMetadonnees() == null
                  && consultCurrent.getMetadonnees() == null) {
               match = true;
            } else if (expected.getMetadonnees() == null
                  || consultCurrent.getMetadonnees() == null) {
               match = false;
            } else {

               @SuppressWarnings("unchecked")
               Collection<String> coll = CollectionUtils.disjunction(

               expected.getMetadonnees(), consultCurrent.getMetadonnees());

               if (CollectionUtils.isEmpty(coll)) {

                  match = true;
               }

            }
         }
      }

      return match;
   }
}
