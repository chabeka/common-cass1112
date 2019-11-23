package fr.urssaf.image.sae.webservices.security;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.w3c.dom.Element;

import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.vi.exception.VIVerificationException;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.modele.VISignVerifParams;
import fr.urssaf.image.sae.vi.service.WebServiceVIService;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;
import fr.urssaf.image.sae.webservices.modele.WebServiceConfiguration;
import fr.urssaf.image.sae.webservices.security.igc.IgcService;
import fr.urssaf.image.sae.webservices.security.igc.exception.LoadCertifsAndCrlException;
import fr.urssaf.image.sae.webservices.security.igc.modele.CertifsAndCrl;

/**
 * Service de sécurisation du service web par authentification
 */
@Service
public class SecurityService {

  private static final Logger LOG = LoggerFactory
                                                 .getLogger(SecurityService.class);

  private static final String SPRING_ROLE_PRFX = "ROLE_";

  private final WebServiceVIService service;

  private final URI serviceVise;

  private final IgcService igcService;

  private final boolean acceptOldWs;

  // private final VISignVerifParams signVerifParams;

  /**
   * Instanciation de {@link WebServiceVIService}
   * 
   * @param igcService
   *          instance IgcService
   * @param service
   *          service du VI
   * @param configuration
   *          configuration du webservice
   */
  @Autowired
  public SecurityService(final IgcService igcService, final WebServiceVIService service,
                         final WebServiceConfiguration configuration) {

    Assert.notNull(igcService);

    this.service = service;
    this.igcService = igcService;

    try {
      serviceVise = new URI("http://sae.urssaf.fr");
    }
    catch (final URISyntaxException e) {
      throw new IllegalStateException(e);
    }

    acceptOldWs = configuration.isAncienWsActif();

  }

  /**
   * Création d'un contexte de sécurité par partir du Vecteur d'indentifcation<br>
   * <br>
   * Paramètres du {@link org.springframework.security.core.Authentication} de
   * type Anonymous
   * <ul>
   * <li>Credentials : empty</li>
   * <li>Principal : {@link VIContenuExtrait#getIdUtilisateur()}</li>
   * <li>Authorities : {@link VIContenuExtrait#getPagms()}</li>
   * </ul>
   * 
   * @param identification
   *          Vecteur d'identification
   * @throws VIVerificationException
   *           exception levée par le VI
   * @throws LoadCertifsAndCrlException
   *           exception levée lors du chargement des crls
   */
  public void authentification(final Element identification)
      throws VIVerificationException, LoadCertifsAndCrlException {

    Assert
          .notNull(
                   identification,
                   "Le paramètre 'identification' n'est pas renseigné alors qu'il est obligatoire ");

    VIContenuExtrait viExtrait;

    final VISignVerifParams signVerifParams = new VISignVerifParams();
    signVerifParams.setPatternsIssuer(igcService.getPatternIssuers());

    final CertifsAndCrl certifsAndCrl = igcService.getInstanceCertifsAndCrl();
    signVerifParams.setCertifsACRacine(certifsAndCrl.getCertsAcRacine());
    signVerifParams.setCrls(certifsAndCrl.getCrl());

    viExtrait = service.verifierVIdeServiceWeb(identification,
                                               serviceVise,
                                               signVerifParams,
                                               acceptOldWs);

    logVI(viExtrait);

    final Set<String> rolesSet = new HashSet<>();
    for (final String role : viExtrait.getSaeDroits().keySet()) {
      rolesSet.add(SPRING_ROLE_PRFX + role);
    }
    final String[] roles = new String[rolesSet.size()];
    rolesSet.toArray(roles);

    final AuthenticationToken authentication = AuthenticationFactory
                                                                    .createAuthentication(viExtrait.getIdUtilisateur(),
                                                                                          viExtrait,
                                                                                          roles);

    AuthenticationContext.setAuthenticationToken(authentication);
  }

  private void logVI(final VIContenuExtrait viExtrait) {

    final String prefixeLog = "Informations extraites du VI : ";

    // LOG des PAGM
    if (viExtrait != null && viExtrait.getSaeDroits() != null) {
      final StringBuffer sBufferMsgLog = new StringBuffer();
      sBufferMsgLog.append(prefixeLog);
      sBufferMsgLog.append("PAGM(s) : ");
      for (final String key : viExtrait.getSaeDroits().keySet()) {
        for (final SaePrmd prmd : viExtrait.getSaeDroits().get(key)) {
          sBufferMsgLog.append(key);
          sBufferMsgLog.append(';');
          sBufferMsgLog.append(prmd.getPrmd().getCode());
          sBufferMsgLog.append(' ');
        }
      }
      LOG.info(sBufferMsgLog.toString());

      // LOG du code application
      LOG
         .info(prefixeLog + "Code application : "
             + viExtrait.getCodeAppli());

      // LOG de l'identifiant utilisateur
      LOG.info(prefixeLog + "Identifiant utilisateur : "
          + viExtrait.getIdUtilisateur());
    }

  }

}
