package fr.urssaf.image.sae.vi.service;

import java.net.URI;

import org.w3c.dom.Element;

import fr.urssaf.image.sae.saml.service.SamlAssertionCreationService;
import fr.urssaf.image.sae.vi.exception.VIVerificationException;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.modele.VISignVerifParams;

/**
 * Classe de lecture du VI pour les web services<br>
 * <br>
 * Le VI est un jeton SAML 2.0 conforme aux <a
 * href="http://saml.xml.org/saml-specifications#samlv20"/>spécifications de
 * OASIS</a><br>
 * <br>
 * L'implémentation s'appuie sur les classes
 * <ul>
 * <li>{@link SamlAssertionCreationService}</li>
 * </ul>
 * <br>
 * <br>
 * Les paramètres d'entrées de chaque méthode sont vérifiés par AOP par la
 * classe {@link fr.urssaf.image.sae.vi.component.WebServiceVIServiceValidate}<br>
 * 
 */
public interface WebServiceVIService {

   /**
    * Vérification d'un Vecteur d'Identification (VI) généré pour s'authentifier
    * auprès d'un service web du SAE<br>
    * 
    * @param identification
    *           Le Vecteur d'Identification à vérifier
    * @param serviceVise
    *           URI décrivant le service visé
    * @param signVerifParams
    *           Les éléments permettant de vérifier la signature électronique du
    *           VI
    * @param acceptOldWs
    *           permet de savoir si le fonctionnement des droits accepte les
    *           appels des WS sans gestion des droits
    * @return Des valeurs extraits du VI qui peuvent être exploités pour mettre
    *         en place un contexte de sécurité basé sur l’authentification,
    *         et/ou pour de la traçabilité
    * @throws VIVerificationException
    *            Les informations extraites du VI sont invalides
    */
   VIContenuExtrait verifierVIdeServiceWeb(Element identification,
         URI serviceVise, VISignVerifParams signVerifParams, boolean acceptOldWs)
         throws VIVerificationException;

}