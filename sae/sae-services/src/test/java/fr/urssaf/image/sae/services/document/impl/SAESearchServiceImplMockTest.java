package fr.urssaf.image.sae.services.document.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.metadata.exceptions.LongCodeNotFoundException;
import fr.urssaf.image.sae.metadata.referential.services.SAEConvertMetadataService;
import fr.urssaf.image.sae.services.exception.SAESearchQueryParseException;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml","/applicationContext-sae-services-test-mock.xml"})

public class SAESearchServiceImplMockTest {

   private static final Logger LOG = LoggerFactory
   .getLogger(SAESearchServiceImplMockTest.class);
   
   @Autowired
   private SAEConvertMetadataService convertService;  
   
   @Autowired
   private SAESearchQueryParserServiceImpl queryParseService;
   
   private List<String> listeCodeLong;

   @Before
   public void init(){
      // extraire la liste de code long de la requete. Pour les tests on
      // construit une liste arbitraire
      listeCodeLong = new ArrayList<String>();

      listeCodeLong.add("Denomination");
      // Titre=Attestation de vigilance
      listeCodeLong.add("Titre");
      // DateCreation=2011-09-01
      listeCodeLong.add("DateCreation");
      // ApplicationProductrice=ADELAIDE
      listeCodeLong.add("ApplicationProductrice");
      // CodeOrganismeProprietaire=AC750
      listeCodeLong.add("CodeOrganismeProprietaire");
      // CodeOrganismeGestionnaire=CER69
      listeCodeLong.add("CodeOrganismeGestionnaire");
      // CodeRND=2.3.1.1.12
      listeCodeLong.add("CodeRND");
      // TypeHash=SHA-1
      listeCodeLong.add("TypeHash");
      // NbPages=2
      listeCodeLong.add("NbPages");
      // FormatFichier=fmt/354
      listeCodeLong.add("FormatFichier");
      listeCodeLong.add("ApplicationTraitement");
      listeCodeLong.add("RUM");
      listeCodeLong.add("Siren");
      listeCodeLong.add("Siret");
      listeCodeLong.add("NumeroCompteExterne");
      listeCodeLong.add("NumeroPersonne");
   }
   /**
    * Test permettant de voir si on sort en exception si les métadonnées ne sont pas correctement converties en codes courts
    * @throws LongCodeNotFoundException 
    * @throws SAESearchQueryParseException 
    * @throws SAESearchServiceEx 
    * @throws SyntaxLuceneEx 
    * 
    */

   @Test(expected = SAESearchQueryParseException.class)
   public void parseLongCodeToShortException() throws LongCodeNotFoundException, SyntaxLuceneEx, SAESearchServiceEx, SAESearchQueryParseException {
      String requete = StringUtils.EMPTY;
      // défintion des requêtes de test
      String query = "Denomination:\"valeur avec espace\" AND CodeOrganismeProprietaire:valeur AND (CodeOrganismeGestionnaire:valeur) OR Titre:\"rtfqTitre:fdksfqsjmdlk\"";

      // définiton de la map de code court de retour sans certaines valeurs pour
      // que la taille des listes ne correspondent pas.
      Map<String, String> codeCourt = new HashMap<String, String>();
      codeCourt.put("den", "Denomination");
      codeCourt.put("cop", "CodeOrganismeProprietaire:valeur");

      EasyMock.createMock(SAEConvertMetadataService.class);
      EasyMock.expect(convertService.longCodeToShortCode(EasyMock.anyObject(List.class)))
               .andReturn(codeCourt).times(1);

      EasyMock.replay(convertService);

         requete = queryParseService.convertFromLongToShortCode(query,
               listeCodeLong);
   }
}
