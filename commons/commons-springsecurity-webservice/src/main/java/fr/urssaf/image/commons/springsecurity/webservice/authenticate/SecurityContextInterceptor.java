package fr.urssaf.image.commons.springsecurity.webservice.authenticate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.collections.IteratorUtils;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.security.SecurityContext;
import org.apache.cxf.ws.security.wss4j.AbstractWSS4JInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.log4j.Logger;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSSecurityEngineResult;
import org.apache.ws.security.WSUsernameTokenPrincipal;
import org.apache.ws.security.handler.WSHandlerResult;
import org.apache.ws.security.util.WSSecurityUtil;
import org.opensaml.SAMLAssertion;
import org.opensaml.SAMLAttribute;
import org.opensaml.SAMLAttributeStatement;
import org.opensaml.SAMLStatement;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

public class SecurityContextInterceptor extends AbstractWSS4JInterceptor {

   private static final Logger LOG = Logger
         .getLogger(SecurityContextInterceptor.class);

   public SecurityContextInterceptor() {

      super();

      setPhase(Phase.PRE_PROTOCOL);
      getAfter().add(WSS4JInInterceptor.class.getName());

   }

   @SuppressWarnings("unchecked")
   @Override
   public final void handleMessage(SoapMessage msg) {

      SecurityContext context = msg.get(SecurityContext.class);

      WSUsernameTokenPrincipal userPrincipal = (WSUsernameTokenPrincipal) context
            .getUserPrincipal();

      List<Object> results = (List<Object>) msg.get("RECV_RESULTS");
      for (Object result : results) {

         if (result instanceof WSHandlerResult) {

            List<SAMLAssertion> assertions = this
                  .getAssertions((WSHandlerResult) result);

            authenticate(userPrincipal, assertions);

            break;

         }

      }

   }

   @SuppressWarnings( { "PMD.UseArrayListInsteadOfVector",
         "PMD.ReplaceVectorWithList" })
   private List<SAMLAssertion> getAssertions(WSHandlerResult wsHandlerResult) {

      List<SAMLAssertion> assertions = new ArrayList<SAMLAssertion>();

      Vector<WSSecurityEngineResult> samlResults = new Vector<WSSecurityEngineResult>();

      WSSecurityUtil.fetchAllActionResults(wsHandlerResult.getResults(),
            WSConstants.ST_UNSIGNED, samlResults);

      for (WSSecurityEngineResult wsResult : samlResults) {

         SAMLAssertion assertion = (SAMLAssertion) wsResult
               .get(WSSecurityEngineResult.TAG_SAML_ASSERTION);

         assertions.add(assertion);

      }

      return assertions;
   }

   @SuppressWarnings("unchecked")
   private void authenticate(WSUsernameTokenPrincipal userPrincipal,
         List<SAMLAssertion> assertions) {

      for (SAMLAssertion samlAssertion : assertions) {

         LOG.debug(samlAssertion);

         Iterator<SAMLStatement> statements = samlAssertion.getStatements();
         while (statements.hasNext()) {
            SAMLStatement samlStatement = statements.next();

            if (samlStatement instanceof SAMLAttributeStatement) {

               SAMLAttributeStatement samlAttributes = (SAMLAttributeStatement) samlStatement;
               Iterator<SAMLAttribute> attributes = samlAttributes
                     .getAttributes();
               while (attributes.hasNext()) {
                  SAMLAttribute samlAttribute = attributes.next();
                  LOG.debug(samlAttribute.getName());

                  if ("PAGM".equals(samlAttribute.getName())) {

                     Iterator<String> values = samlAttribute.getValues();

                     List<GrantedAuthority> authorities = AuthorityUtils
                           .createAuthorityList(StringUtils
                                 .toStringArray(IteratorUtils.toList(values)));

                     Authentication authentication = AuthenticationFactory
                           .createAuthentication(userPrincipal, authorities);

                     SecurityContextHolder.getContext().setAuthentication(
                           authentication);

                  }

               }

            }
         }
      }
   }

}
