/**
 * 
 */
package fr.urssaf.image.sae.vi.service.impl;

import java.security.KeyStore;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import fr.urssaf.image.sae.saml.params.SamlAssertionParams;
import fr.urssaf.image.sae.saml.params.SamlCommonsParams;
import fr.urssaf.image.sae.saml.service.SamlAssertionCreationService;
import fr.urssaf.image.sae.saml.util.ConverterUtils;
import fr.urssaf.image.sae.vi.service.VIConfiguration;
import fr.urssaf.image.sae.vi.service.WebServiceVICreateService;

/**
 * Classe d'implémentation du service {@link WebServiceVICreateService}
 * 
 */
@Component
public class WebServiceVICreateServiceImpl implements WebServiceVICreateService {

   private SamlAssertionCreationService createService = new SamlAssertionCreationService();

   /**
    * {@inheritDoc}
    */
   public final Element creerVIpourServiceWeb(List<String> pagm, String issuer,
         String idUtilisateur, KeyStore keystore, String alias, String password) {

      Date systemDate = new Date();

      SamlAssertionParams assertionParams = new SamlAssertionParams();
      SamlCommonsParams commonsParams = new SamlCommonsParams();

      assertionParams.setCommonsParams(commonsParams);

      commonsParams.setIssuer(issuer);
      commonsParams.setNotOnOrAfter(DateUtils.addHours(systemDate, 1));
      commonsParams.setNotOnBefore(DateUtils.addHours(systemDate, -1));
      commonsParams.setAudience(ConverterUtils.uri("http://sae.urssaf.fr"));
      commonsParams.setPagm(pagm);

      String subjectId2 = StringUtils.isNotBlank(idUtilisateur) ? idUtilisateur
            : "NON_RENSEIGNE";
      assertionParams.setSubjectId2(subjectId2);
      assertionParams.setSubjectFormat2(ConverterUtils
            .uri("urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified"));
      assertionParams.setMethodAuthn2(VIConfiguration.METHOD_AUTH2);
      assertionParams.setRecipient(ConverterUtils.uri("urn:URSSAF"));

      return createService.genererAssertion(assertionParams, keystore, alias,
            password);

   }

}
