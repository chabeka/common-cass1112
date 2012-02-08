/**
 * 
 */
package fr.urssaf.image.sae.webservices.comparator;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.easymock.IArgumentMatcher;

import fr.urssaf.image.sae.services.consultation.model.ConsultParams;

/**
 * 
 * 
 */
public class ConsultParamComparator implements IArgumentMatcher {

   private ConsultParams expected;

   /**
    * constructeur
    * 
    * @param expected
    */
   public ConsultParamComparator(ConsultParams expected) {
      this.expected = expected;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void appendTo(StringBuffer buffer) {
      buffer.append("ConsultParam(");
      buffer.append(expected.getClass().getName());
      buffer.append(" with UUID ");
      buffer.append(expected.getIdArchive().toString());
      buffer.append(")");

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean matches(Object actual) {

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
               Collection<String> result = CollectionUtils.disjunction(expected
                     .getMetadonnees(), consultCurrent.getMetadonnees());

               if (CollectionUtils.isEmpty(result)) {
                  match = true;
               }

            }
         }
      }

      return match;
   }
}
