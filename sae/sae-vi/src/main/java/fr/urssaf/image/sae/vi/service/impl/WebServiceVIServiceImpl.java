package fr.urssaf.image.sae.vi.service.impl;

import java.net.URI;
import java.security.KeyStore;
import java.security.cert.CertificateEncodingException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PrincipalUtil;
import org.bouncycastle.jce.X509Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

import fr.urssaf.image.sae.droit.exception.ContratServiceNotFoundException;
import fr.urssaf.image.sae.droit.exception.PagmNotFoundException;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.service.SaeDroitService;
import fr.urssaf.image.sae.saml.data.SamlAssertionData;
import fr.urssaf.image.sae.saml.exception.SamlExtractionException;
import fr.urssaf.image.sae.saml.params.SamlAssertionParams;
import fr.urssaf.image.sae.saml.params.SamlCommonsParams;
import fr.urssaf.image.sae.saml.service.SamlAssertionCreationService;
import fr.urssaf.image.sae.saml.service.SamlAssertionExtractionService;
import fr.urssaf.image.sae.saml.util.ConverterUtils;
import fr.urssaf.image.sae.vi.exception.VIAppliClientException;
import fr.urssaf.image.sae.vi.exception.VIInvalideException;
import fr.urssaf.image.sae.vi.exception.VIPagmIncorrectException;
import fr.urssaf.image.sae.vi.exception.VIVerificationException;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.modele.VISignVerifParams;
import fr.urssaf.image.sae.vi.service.VIConfiguration;
import fr.urssaf.image.sae.vi.service.WebServiceVIService;
import fr.urssaf.image.sae.vi.service.WebServiceVIValidateService;

/**
 * Classe de lecture et d'écriture du VI pour les web services<br>
 * <br>
 * Le VI est un jeton SAML 2.0 conforme aux <a
 * href="http://saml.xml.org/saml-specifications#samlv20"/>spécifications de
 * OASIS</a><br>
 * <br>
 * L'implémentation s'appuie sur les classes
 * <ul>
 * <li>{@link SamlAssertionCreationService}</li>
 * <li>{@link SamlAssertionExtractionService}</li>
 * </ul>
 * <br>
 * <br>
 * Les paramètres d'entrées de chaque méthode sont vérifiés par AOP par la
 * classe {@link fr.urssaf.image.sae.vi.component.WebServiceVIServiceValidate}<br>
 * 
 */
@Component
public class WebServiceVIServiceImpl implements WebServiceVIService {

   private final SamlAssertionCreationService createService;
   private final SamlAssertionExtractionService extractService;

   private final WebServiceVIValidateService validateService;

   private final SaeDroitService droitService;

   /**
    * instanciation de {@link SamlAssertionCreationService}<br>
    * instanciation de {@link SamlAssertionExtractionService}<br>
    * instanciation de {@link WebServiceVIValidateServiceImpl}<br>
    * 
    * @param droitService
    *           service permettant de réaliser les opérations sur les droits
    * 
    */
   @Autowired
   public WebServiceVIServiceImpl(SaeDroitService droitService,
         WebServiceVIValidateService validateService) {
      createService = new SamlAssertionCreationService();
      extractService = new SamlAssertionExtractionService();

      this.validateService = validateService;

      this.droitService = droitService;

   }

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

   /**
    * {@inheritDoc}
    */
   public final VIContenuExtrait verifierVIdeServiceWeb(Element identification,
         URI serviceVise, VISignVerifParams signVerifParams)
         throws VIVerificationException {

      // vérification du jeton SAML
      validateService.validate(identification, signVerifParams);

      // extraction du jeton SAML
      SamlAssertionData data;
      try {
         data = extractService.extraitDonnees(identification);
      } catch (SamlExtractionException exception) {
         throw new VIInvalideException(exception.getMessage(), exception);
      }

      // Extraction de l'identifiant de l'application cliente depuis le
      // certificat
      // de la clé publique de signature du VI
      // l'extraction du CN vient du code
      // http://stackoverflow.com/questions/2914521/how-to-extract-cn-from-x509certificate-in-java
      String idAppliCliente;
      try {
         X509Principal principal = PrincipalUtil.getSubjectX509Principal(data
               .getClePublique());
         idAppliCliente = (String) principal.getValues(X509Name.CN).get(0);
      } catch (CertificateEncodingException e) {
         throw new IllegalStateException(e);
      }

      // vérification supplémentaires sur le jeton SAML
      validateService.validate(data, serviceVise, idAppliCliente, new Date());

      List<String> pagms = data.getAssertionParams().getCommonsParams()
            .getPagm();

      // Extraction des PAGM du VI
      SaeDroits saeDroits;
      try {
         saeDroits = this.droitService.loadSaeDroits(idAppliCliente, pagms);

      } catch (ContratServiceNotFoundException exception) {
         throw new VIPagmIncorrectException(exception.getMessage());

      } catch (PagmNotFoundException exception) {
         throw new VIAppliClientException(exception.getMessage());

      } catch (RuntimeException exception) {
         throw new VIInvalideException(exception.getMessage(), exception);
      }

      // instanciation de la valeur retour
      VIContenuExtrait extrait = new VIContenuExtrait();
      extrait.setSaeDroits(saeDroits);
      extrait.setIdUtilisateur(data.getAssertionParams().getSubjectId2());
      extrait.setCodeAppli(idAppliCliente);

      // Renvoie du résultat
      return extrait;

   }

}
