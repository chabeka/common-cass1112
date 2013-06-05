/**
 * 
 */
package fr.urssaf.image.sae.client.vi.beans;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.Parameter;

import fr.urssaf.image.sae.client.vi.VIHandler;
import fr.urssaf.image.sae.client.vi.signature.DefaultKeystore;

/**
 * 
 * 
 */
public class ViMessageContext extends MessageContext {

   /**
    * {@inheritDoc}
    */
   @Override
   public Parameter getParameter(String param) {
      if (VIHandler.KEY_KEYSTORE.equals(param)) {
         return new Parameter(param, DefaultKeystore.class.getName());

      } else if (VIHandler.KEY_ISSUER.equals(param)) {
         return new Parameter(param, "PNR");
      } else {
         return new Parameter(param, "ROLE_TOUS;FULL");
      }
   }

}
