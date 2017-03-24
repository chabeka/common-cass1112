package fr.urssaf.image.commons.springsecurity.webservice.custom.wssecurity;

import java.util.Collections;
import java.util.Set;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.phase.PhaseInterceptor;
import org.apache.cxf.ws.security.wss4j.AbstractWSS4JInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class SecurityContextByRoleInterceptor extends AbstractWSS4JInterceptor {

   public SecurityContextByRoleInterceptor() {

      super();
      setPhase(Phase.POST_PROTOCOL);

   }

   private String[] roles;

   public final void setRoles(String... roles) {
      this.roles = roles;
   }

   @Override
   public final void handleMessage(SoapMessage msg) {

      msg.getInterceptorChain().add(new SecurityContextInterceptorInternal());
   }

   final class SecurityContextInterceptorInternal implements
         PhaseInterceptor<SoapMessage> {

      @Override
      public Set<String> getAfter() {
         return Collections.emptySet();
      }

      @Override
      public Set<String> getBefore() {
         return Collections.emptySet();
      }

      @Override
      public String getId() {
         return SecurityContextInterceptorInternal.class.getName();
      }

      @Override
      public String getPhase() {
         return Phase.POST_PROTOCOL;
      }

      @Override
      public void handleFault(SoapMessage message) {
         // nothing
      }

      @Override
      public void handleMessage(SoapMessage msg) {

         Assert.notEmpty(roles);

         SOAPMessage soapMsg = msg.getContent(SOAPMessage.class);

         try {
            SOAPHeader soapHeader = soapMsg.getSOAPHeader();
            SOAPElement wsseSecurity = (SOAPElement) soapHeader
                  .getElementsByTagName("wsse:Security").item(0);

            SOAPElement assertion = (SOAPElement) wsseSecurity
                  .getElementsByTagName("Assertion").item(0);

            SOAPFactory soapFactory = SOAPFactory.newInstance();

            SOAPElement assertionSOAP = soapFactory
                  .createElement(SAMLTokenFactory.createPAGM(roles));

            assertion.addChildElement(assertionSOAP);

         } catch (SOAPException e) {

            throw new Fault(e);

         }

      }
   }

}
