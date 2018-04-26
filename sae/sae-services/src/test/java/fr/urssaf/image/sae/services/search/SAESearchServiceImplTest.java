package fr.urssaf.image.sae.services.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.services.document.SAESearchService;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class SAESearchServiceImplTest {

   @Autowired
   @Qualifier("saeSearchService")
   private SAESearchService saeSearchService;

   @After
   public void end() {
      AuthenticationContext.setAuthenticationToken(null);
   }

   private void initDroits() {

      VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("TESTS_UNITAIRES");
      viExtrait.setIdUtilisateur("UTILISATEUR TEST");

      SaeDroits saeDroits = new SaeDroits();
      List<SaePrmd> saePrmds = new ArrayList<SaePrmd>();
      SaePrmd saePrmd = new SaePrmd();
      saePrmd.setValues(new HashMap<String, String>());
      Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      saePrmd.setPrmd(prmd);
      String[] roles = new String[] { "recherche" };
      saePrmds.add(saePrmd);

      saeDroits.put("recherche", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);

   }

   /**
    * Cas de test: Appel du service de recherche avec une requête Lucene vide<br>
    * <br>
    * Résultat attendu: Levée d'une exception IllegalArgumentException
    */
   @Test(expected = IllegalArgumentException.class)
   public final void searchFailureReqEmpty() throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx {

      initDroits();

      String requete = StringUtils.EMPTY;

      List<String> listMetaDesiree = Arrays.asList("Titre");

      saeSearchService.search(requete, listMetaDesiree);

   }

   /**
    * Cas de test: Appel du service de recherche avec une requête Lucene
    * incorrecte<br>
    * <br>
    * Résultat attendu: Levée d'une exception SyntaxLuceneEx
    */
   @Test(expected = SyntaxLuceneEx.class)
   public final void searchFailureSeparateur() throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx {

      initDroits();

      String requete = "Siret:123456 AND IdTraitementMasse:41882:050200023";

      List<String> listMetaDesiree = Arrays.asList("Titre");

      saeSearchService.search(requete, listMetaDesiree);

   }

   /**
    * Cas de test: Appel du service de recherche en demandant dans les résultats
    * une métadonnée qui n'existe pas dans le référentiel des métadonnées<br>
    * <br>
    * Résultat attendu: levée d'une exception UnknownDesiredMetadataEx
    */
   @Test(expected = UnknownDesiredMetadataEx.class)
   public final void searchFailureItem() throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx {

      initDroits();

      String requete = "Siret:123456";

      List<String> listMetaDesiree = Arrays.asList("MetaInexistante");

      saeSearchService.search(requete, listMetaDesiree);

   }

}
