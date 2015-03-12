/**
 * 
 */
package fr.urssaf.image.sae.trace.executable.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Support permettant de réaliser l'authentification pour pouvoir accéder à
 * l'archivage unitaire
 * 
 */
@Component
public class AuthentificationSupport {

   /**
    * méthode d'authentification
    */
   public final void authentifier() {
      // initialisation du contexte de sécurité
      VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("SAE");
      viExtrait.setIdUtilisateur("TRACE EXECUTABLE");

      SaeDroits saeDroits = new SaeDroits();
      List<SaePrmd> saePrmds = new ArrayList<SaePrmd>();
      SaePrmd saePrmd = new SaePrmd();
      saePrmd.setValues(new HashMap<String, String>());
      Prmd prmd = new Prmd();
      prmd.setBean("permitDomaineTechnique");
      prmd.setCode("default");
      saePrmd.setPrmd(prmd);
      String[] roles = new String[] { "archivage_unitaire" };
      saePrmds.add(saePrmd);

      saeDroits.put("archivage_unitaire", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);
   }

}
