package fr.urssaf.image.sae.services.document.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml"})

public class SAESearchServiceImplTest {
   private static final Logger LOG = LoggerFactory
         .getLogger(SAESearchServiceImplTest.class);
   @Autowired
   private SAESearchQueryParserServiceImpl queryParseService;
   @Autowired
   private SAEConvertMetadataService convertService;  
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
    * Appel de la méthode de conversion avec des requêtes définies et on compare le résultat à ceux attendu
    * @throws LongCodeNotFoundException
    */
   @Test
   public void parseLongCodeToShort() throws LongCodeNotFoundException {

      List<String> operateurList = new ArrayList<String>();
      operateurList.add("+");
      operateurList.add("-");
      operateurList.add("NOT");
      operateurList.add(" ");
      operateurList.add("(");
      operateurList.add(")");

      List<String> listeQuery = new ArrayList<String>();
      List<String> listeConvertedQuery = new ArrayList<String>();

      // défintion des requêtes de test
      listeQuery
            .add("Denomination:\"valeur avec espace\" AND CodeOrganismeProprietaire:valeur AND (CodeOrganismeGestionnaire:valeur) OR Titre:\"rtfqTitre:fdksfqsjmdlk\"");
      listeQuery
            .add("Denomination:\"valeur avec espace\" AND CodeOrganismeProprietaire:valeur AND (CodeOrganismeGestionnaire:valeur) OR Titre:rtfqTitre:fdksfqsjmdlk");
      listeQuery
            .add("Denomination:\"valeur avec espace\" +CodeOrganismeProprietaire:valeur");
      listeQuery
            .add("Denomination:valeur\\ avec\\ espace +CodeOrganismeProprietaire:valeur");
      listeQuery
            .add("Denomination:valeur\\ avec\\ espace +CodeOrganismeProprietaire:valeur AND Titre:rtfqTitre:fdksfqsjmdlk");
      listeQuery
            .add("Denomination:valeur\\ avec\\ espace AND CodeOrganismeProprietaire:valeur +Titre:rtfqTitre:fdksfqsjmdlk");
      listeQuery
            .add("Denomination:\"Test 103-CaptureUnitaire-OK-ToutesMetasSpecifiables\" AND (-(+(Siret:12345678912345)) +NumeroPersonne:123854)");
      listeQuery
            .add("Denomination:\"Test 103-CaptureUnitaire-OK-ToutesMetasSpecifiables\" AND ((-(+(-(NOT (-Siret:12345678912345)))) OR +NumeroPersonne:123854))");
      listeQuery
            .add("Denomination:\"Test 103-CaptureUnitaire-OK-ToutesMetasSpecifiables\" AND ((-(+(-(NOT Siret:12345678912345))) OR +NumeroPersonne:123854))");
      listeQuery
            .add("Denomination:\"Test 103-CaptureUnitaire-OK-ToutesMetasSpecifiables\" AND ((-(+(-(NOT Siret:12345678912345))) +NumeroPersonne:123854))");
      listeQuery
            .add("Denomination:\"Test 103-CaptureUnitaire-OK-ToutesMetasSpecifiables\" AND ((-(+(-(NOT Siret:12345678912345))) -NumeroPersonne:123854))");
      listeQuery
            .add("Denomination:\"valeur avec espace\" AND CodeOrganismeProprietaire:valeur AND ((CodeOrganismeGestionnaire:valeur OR Titre:\"rtfqTitre:fdksfqsjmdlk\"))");
      listeQuery
            .add("Denomination:\"valeur avec espace\" AND (CodeOrganismeProprietaire:valeur OR CodeOrganismeGestionnaire:valeur) OR Titre:\"rtfqTitre:fdksfqsjmdlk\"");
      listeQuery
            .add("(RUM:24534Y8465435413Y012312356690123 AND Siret:12345678912345) AND (CodeRND:1.2.2.4.3 AND ApplicationProductrice:CTC AND ApplicationTraitement:CTC)");
      listeQuery
            .add("(RUM:24534Y8465435413Y012312356690123 AND Siret:12345678912345) AND ((CodeRND:1.2.2.4.3 AND ApplicationProductrice:CTC AND ApplicationTraitement:CTC))");
      listeQuery
            .add("(RUM:\"24534Y8465435413Y012312356690123\" AND Siret:\"12345678912345\") AND (CodeRND:1.2.2.4.3 AND ApplicationProductrice:CTC AND ApplicationTraitement:CTC)");
      listeQuery
            .add("(RUM:\"24534Y8465435413Y012312356690123\" AND Siret:12345678912345) AND (CodeRND:1.2.2.4.3 AND ApplicationProductrice:CTC AND ApplicationTraitement:CTC)");
      // Siret
      listeQuery
            .add("Denomination:\"Test 319-Recherche-OK-Toutes-Metadonnees-Recherchables\" AND Siret:12345678912345");
      // Numéro de compte externe
      listeQuery
            .add("Denomination:\"Test 319-Recherche-OK-Toutes-Metadonnees-Recherchables\" AND NumeroCompteExterne:30148032541101600");
      // RUM: AND Siret + PRMD
      listeQuery
            .add("RUM:24534Y8465435413Y012312356690123 AND Siret:12345678912345 AND CodeRND:1.2.2.4.3 AND ApplicationProductrice:CTC AND ApplicationTraitement:CTC");
      // Requette complexe date
      listeQuery
            .add("Denomination:\"Test 304-Recherche-OK-Complexe-Dates\" AND (DateCreation:20050618 OR DateCreation:[20050718 TO 20050722] OR DateCreation:{20050818 TO 20050822})");
      // Attestation
      listeQuery
            .add("CodeRND:2.3.1.1.12 AND Siren:3090000001 AND DateCreation:20070401 AND  Denomination:\"Test 309-Recherche-OK-ProjetAttestations\"");

      
      
      // définiton des requêtes coverties attentudes
      listeConvertedQuery
            .add("den:\"valeur avec espace\" AND cop:valeur AND (cog:valeur) OR SM_TITLE:\"rtfqTitre:fdksfqsjmdlk\"");
      listeConvertedQuery
            .add("den:\"valeur avec espace\" AND cop:valeur AND (cog:valeur) OR SM_TITLE:rtfqTitre:fdksfqsjmdlk");
      listeConvertedQuery.add("den:\"valeur avec espace\" +cop:valeur");
      listeConvertedQuery.add("den:valeur\\ avec\\ espace +cop:valeur");
      listeConvertedQuery
            .add("den:valeur\\ avec\\ espace +cop:valeur AND SM_TITLE:rtfqTitre:fdksfqsjmdlk");
      listeConvertedQuery
            .add("den:valeur\\ avec\\ espace AND cop:valeur +SM_TITLE:rtfqTitre:fdksfqsjmdlk");
      listeConvertedQuery
            .add("den:\"Test 103-CaptureUnitaire-OK-ToutesMetasSpecifiables\" AND (-(+(srt:12345678912345)) +npe:123854)");
      listeConvertedQuery
            .add("den:\"Test 103-CaptureUnitaire-OK-ToutesMetasSpecifiables\" AND ((-(+(-(NOT (-srt:12345678912345)))) OR +npe:123854))");
      listeConvertedQuery
            .add("den:\"Test 103-CaptureUnitaire-OK-ToutesMetasSpecifiables\" AND ((-(+(-(NOT srt:12345678912345))) OR +npe:123854))");
      listeConvertedQuery
            .add("den:\"Test 103-CaptureUnitaire-OK-ToutesMetasSpecifiables\" AND ((-(+(-(NOT srt:12345678912345))) +npe:123854))");
      listeConvertedQuery
            .add("den:\"Test 103-CaptureUnitaire-OK-ToutesMetasSpecifiables\" AND ((-(+(-(NOT srt:12345678912345))) -npe:123854))");
      listeConvertedQuery
            .add("den:\"valeur avec espace\" AND cop:valeur AND ((cog:valeur OR SM_TITLE:\"rtfqTitre:fdksfqsjmdlk\"))");
      listeConvertedQuery
            .add("den:\"valeur avec espace\" AND (cop:valeur OR cog:valeur) OR SM_TITLE:\"rtfqTitre:fdksfqsjmdlk\"");
      listeConvertedQuery
            .add("(rum:24534Y8465435413Y012312356690123 AND srt:12345678912345) AND (SM_DOCUMENT_TYPE:1.2.2.4.3 AND apr:CTC AND atr:CTC)");
      listeConvertedQuery
            .add("(rum:24534Y8465435413Y012312356690123 AND srt:12345678912345) AND ((SM_DOCUMENT_TYPE:1.2.2.4.3 AND apr:CTC AND atr:CTC))");
      listeConvertedQuery
            .add("(rum:\"24534Y8465435413Y012312356690123\" AND srt:\"12345678912345\") AND (SM_DOCUMENT_TYPE:1.2.2.4.3 AND apr:CTC AND atr:CTC)");
      listeConvertedQuery
            .add("(rum:\"24534Y8465435413Y012312356690123\" AND srt:12345678912345) AND (SM_DOCUMENT_TYPE:1.2.2.4.3 AND apr:CTC AND atr:CTC)");
      // Siret
      listeConvertedQuery
            .add("den:\"Test 319-Recherche-OK-Toutes-Metadonnees-Recherchables\" AND srt:12345678912345");
      // Numéro de compte externe
      listeConvertedQuery
            .add("den:\"Test 319-Recherche-OK-Toutes-Metadonnees-Recherchables\" AND nce:30148032541101600");
      // RUM: AND Siret + PRMD
      listeConvertedQuery
            .add("rum:24534Y8465435413Y012312356690123 AND srt:12345678912345 AND SM_DOCUMENT_TYPE:1.2.2.4.3 AND apr:CTC AND atr:CTC");
      // Requette complexe date
      listeConvertedQuery
            .add("den:\"Test 304-Recherche-OK-Complexe-Dates\" AND (SM_CREATION_DATE:20050618 OR SM_CREATION_DATE:[20050718 TO 20050722] OR SM_CREATION_DATE:{20050818 TO 20050822})");
      // Attestation
      listeConvertedQuery
            .add("SM_DOCUMENT_TYPE:2.3.1.1.12 AND srn:3090000001 AND SM_CREATION_DATE:20070401 AND  den:\"Test 309-Recherche-OK-ProjetAttestations\"");


      

      // pour toutes les requêtes définies
      int index = 0;
      for (String requeteFinal : listeQuery) {
         String requete = StringUtils.EMPTY;
         try {
            requete= queryParseService.convertFromLongToShortCode(requeteFinal, listeCodeLong);
         } catch (SyntaxLuceneEx e1) {
            LOG.error("Erreur :", e1);
            fail();
         } catch (SAESearchServiceEx e1) {
            LOG.error("Erreur :", e1);
            fail();
         } catch (SAESearchQueryParseException e1) {
            LOG.error("Erreur :", e1);
            fail();
         }        

         LOG.info("Reqête attentdue : {}", listeConvertedQuery.get(index));
         LOG.info("Reqête obtenue : {}", requete);
         assertEquals(listeConvertedQuery.get(index), requete);
         index++;
      }
   }

   

   
}
