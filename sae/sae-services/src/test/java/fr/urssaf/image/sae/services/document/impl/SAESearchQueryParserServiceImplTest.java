package fr.urssaf.image.sae.services.document.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.services.document.model.SAESearchQueryParserResult;
import fr.urssaf.image.sae.services.exception.SAESearchQueryParseException;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class SAESearchQueryParserServiceImplTest {

   @Autowired
   private SAESearchQueryParserServiceImpl queryParseService;

   private void parse(SAESearchQueryParserResult attendu)
         throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult obtenu = queryParseService
            .convertFromLongToShortCode(attendu.getRequeteOrigine());

      assertEquals("Echec technique lors du parsing de la requête", attendu
            .getRequeteOrigine(), obtenu.getRequeteOrigine());

      assertEquals("La requête avec les codes courts n'est pas celle attendu",
            attendu.getRequeteCodeCourts(), obtenu.getRequeteCodeCourts());

      assertEquals("Les métadonnées détectées sont incorrectes", attendu
            .getMetaUtilisees(), obtenu.getMetaUtilisees());

   }

   @Test
   public void cas1() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("Denomination:\"valeur avec espace\" AND CodeOrganismeProprietaire:valeur AND (CodeOrganismeGestionnaire:valeur) OR Titre:\"rtfqTitre:fdksfqsjmdlk\"");

      attendu
            .setRequeteCodeCourts("den:\"valeur avec espace\" AND cop:valeur AND (cog:valeur) OR SM_TITLE:\"rtfqTitre:fdksfqsjmdlk\"");

      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("CodeOrganismeProprietaire", "cop");
      attendu.getMetaUtilisees().put("CodeOrganismeGestionnaire", "cog");
      attendu.getMetaUtilisees().put("Titre", "SM_TITLE");

      parse(attendu);

   }

   @Test
   public void cas2() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("Denomination:\"valeur avec espace\" AND CodeOrganismeProprietaire:valeur AND (CodeOrganismeGestionnaire:valeur) OR Titre:rtfqTitre:fdksfqsjmdlk");

      attendu
            .setRequeteCodeCourts("den:\"valeur avec espace\" AND cop:valeur AND (cog:valeur) OR SM_TITLE:rtfqTitre:fdksfqsjmdlk");

      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("CodeOrganismeProprietaire", "cop");
      attendu.getMetaUtilisees().put("CodeOrganismeGestionnaire", "cog");
      attendu.getMetaUtilisees().put("Titre", "SM_TITLE");

      parse(attendu);

   }

   @Test
   public void cas3() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("Denomination:\"valeur avec espace\" +CodeOrganismeProprietaire:valeur");

      attendu.setRequeteCodeCourts("den:\"valeur avec espace\" +cop:valeur");

      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("CodeOrganismeProprietaire", "cop");

      parse(attendu);

   }

   @Test
   public void cas4() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("Denomination:valeur\\ avec\\ espace +CodeOrganismeProprietaire:valeur");

      attendu.setRequeteCodeCourts("den:valeur\\ avec\\ espace +cop:valeur");

      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("CodeOrganismeProprietaire", "cop");

      parse(attendu);

   }

   @Test
   public void cas5() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("Denomination:valeur\\ avec\\ espace +CodeOrganismeProprietaire:valeur AND Titre:rtfqTitre:fdksfqsjmdlk");

      attendu
            .setRequeteCodeCourts("den:valeur\\ avec\\ espace +cop:valeur AND SM_TITLE:rtfqTitre:fdksfqsjmdlk");

      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("CodeOrganismeProprietaire", "cop");
      attendu.getMetaUtilisees().put("Titre", "SM_TITLE");

      parse(attendu);

   }

   @Test
   public void cas6() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("Denomination:valeur\\ avec\\ espace AND CodeOrganismeProprietaire:valeur +Titre:rtfqTitre:fdksfqsjmdlk");

      attendu
            .setRequeteCodeCourts("den:valeur\\ avec\\ espace AND cop:valeur +SM_TITLE:rtfqTitre:fdksfqsjmdlk");

      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("CodeOrganismeProprietaire", "cop");
      attendu.getMetaUtilisees().put("Titre", "SM_TITLE");

      parse(attendu);

   }

   @Test
   public void cas7() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("Denomination:\"Test 103-CaptureUnitaire-OK-ToutesMetasSpecifiables\" AND (-(+(Siret:12345678912345)) +NumeroPersonne:123854)");

      attendu
            .setRequeteCodeCourts("den:\"Test 103-CaptureUnitaire-OK-ToutesMetasSpecifiables\" AND (-(+(srt:12345678912345)) +npe:123854)");

      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("Siret", "srt");
      attendu.getMetaUtilisees().put("NumeroPersonne", "npe");

      parse(attendu);

   }

   @Test
   public void cas8() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("Denomination:\"Test 103-CaptureUnitaire-OK-ToutesMetasSpecifiables\" AND ((-(+(-(NOT (-Siret:12345678912345)))) OR +NumeroPersonne:123854))");

      attendu
            .setRequeteCodeCourts("den:\"Test 103-CaptureUnitaire-OK-ToutesMetasSpecifiables\" AND ((-(+(-(NOT (-srt:12345678912345)))) OR +npe:123854))");

      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("Siret", "srt");
      attendu.getMetaUtilisees().put("NumeroPersonne", "npe");

      parse(attendu);

   }

   @Test
   public void cas9() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("Denomination:\"Test 103-CaptureUnitaire-OK-ToutesMetasSpecifiables\" AND ((-(+(-(NOT Siret:12345678912345))) OR +NumeroPersonne:123854))");

      attendu
            .setRequeteCodeCourts("den:\"Test 103-CaptureUnitaire-OK-ToutesMetasSpecifiables\" AND ((-(+(-(NOT srt:12345678912345))) OR +npe:123854))");

      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("Siret", "srt");
      attendu.getMetaUtilisees().put("NumeroPersonne", "npe");

      parse(attendu);

   }

   @Test
   public void cas10() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("Denomination:\"Test 103-CaptureUnitaire-OK-ToutesMetasSpecifiables\" AND ((-(+(-(NOT Siret:12345678912345))) +NumeroPersonne:123854))");

      attendu
            .setRequeteCodeCourts("den:\"Test 103-CaptureUnitaire-OK-ToutesMetasSpecifiables\" AND ((-(+(-(NOT srt:12345678912345))) +npe:123854))");

      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("Siret", "srt");
      attendu.getMetaUtilisees().put("NumeroPersonne", "npe");

      parse(attendu);

   }

   @Test
   public void cas11() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("Denomination:\"Test 103-CaptureUnitaire-OK-ToutesMetasSpecifiables\" AND ((-(+(-(NOT Siret:12345678912345))) -NumeroPersonne:123854))");

      attendu
            .setRequeteCodeCourts("den:\"Test 103-CaptureUnitaire-OK-ToutesMetasSpecifiables\" AND ((-(+(-(NOT srt:12345678912345))) -npe:123854))");

      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("Siret", "srt");
      attendu.getMetaUtilisees().put("NumeroPersonne", "npe");

      parse(attendu);

   }

   @Test
   public void cas12() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("Denomination:\"valeur avec espace\" AND CodeOrganismeProprietaire:valeur AND ((CodeOrganismeGestionnaire:valeur OR Titre:\"rtfqTitre:fdksfqsjmdlk\"))");

      attendu
            .setRequeteCodeCourts("den:\"valeur avec espace\" AND cop:valeur AND ((cog:valeur OR SM_TITLE:\"rtfqTitre:fdksfqsjmdlk\"))");

      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("CodeOrganismeProprietaire", "cop");
      attendu.getMetaUtilisees().put("CodeOrganismeGestionnaire", "cog");
      attendu.getMetaUtilisees().put("Titre", "SM_TITLE");

      parse(attendu);

   }

   @Test
   public void cas13() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("Denomination:\"valeur avec espace\" AND (CodeOrganismeProprietaire:valeur OR CodeOrganismeGestionnaire:valeur) OR Titre:\"rtfqTitre:fdksfqsjmdlk\"");

      attendu
            .setRequeteCodeCourts("den:\"valeur avec espace\" AND (cop:valeur OR cog:valeur) OR SM_TITLE:\"rtfqTitre:fdksfqsjmdlk\"");

      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("CodeOrganismeProprietaire", "cop");
      attendu.getMetaUtilisees().put("CodeOrganismeGestionnaire", "cog");
      attendu.getMetaUtilisees().put("Titre", "SM_TITLE");

      parse(attendu);

   }

   @Test
   public void cas14() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("(RUM:24534Y8465435413Y012312356690123 AND Siret:12345678912345) AND (CodeRND:1.2.2.4.3 AND ApplicationProductrice:CTC AND ApplicationTraitement:CTC)");

      attendu
            .setRequeteCodeCourts("(rum:24534Y8465435413Y012312356690123 AND srt:12345678912345) AND (SM_DOCUMENT_TYPE:1.2.2.4.3 AND apr:CTC AND atr:CTC)");

      attendu.getMetaUtilisees().put("RUM", "rum");
      attendu.getMetaUtilisees().put("Siret", "srt");
      attendu.getMetaUtilisees().put("CodeRND", "SM_DOCUMENT_TYPE");
      attendu.getMetaUtilisees().put("ApplicationProductrice", "apr");
      attendu.getMetaUtilisees().put("ApplicationTraitement", "atr");

      parse(attendu);

   }

   @Test
   public void cas15() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("(RUM:24534Y8465435413Y012312356690123 AND Siret:12345678912345) AND ((CodeRND:1.2.2.4.3 AND ApplicationProductrice:CTC AND ApplicationTraitement:CTC))");

      attendu
            .setRequeteCodeCourts("(rum:24534Y8465435413Y012312356690123 AND srt:12345678912345) AND ((SM_DOCUMENT_TYPE:1.2.2.4.3 AND apr:CTC AND atr:CTC))");

      attendu.getMetaUtilisees().put("RUM", "rum");
      attendu.getMetaUtilisees().put("Siret", "srt");
      attendu.getMetaUtilisees().put("CodeRND", "SM_DOCUMENT_TYPE");
      attendu.getMetaUtilisees().put("ApplicationProductrice", "apr");
      attendu.getMetaUtilisees().put("ApplicationTraitement", "atr");

      parse(attendu);

   }

   @Test
   public void cas17() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("(RUM:\"24534Y8465435413Y012312356690123\" AND Siret:\"12345678912345\") AND (CodeRND:1.2.2.4.3 AND ApplicationProductrice:CTC AND ApplicationTraitement:CTC)");

      attendu
            .setRequeteCodeCourts("(rum:\"24534Y8465435413Y012312356690123\" AND srt:\"12345678912345\") AND (SM_DOCUMENT_TYPE:1.2.2.4.3 AND apr:CTC AND atr:CTC)");

      attendu.getMetaUtilisees().put("RUM", "rum");
      attendu.getMetaUtilisees().put("Siret", "srt");
      attendu.getMetaUtilisees().put("CodeRND", "SM_DOCUMENT_TYPE");
      attendu.getMetaUtilisees().put("ApplicationProductrice", "apr");
      attendu.getMetaUtilisees().put("ApplicationTraitement", "atr");

      parse(attendu);

   }

   @Test
   public void cas18() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("(RUM:\"24534Y8465435413Y012312356690123\" AND Siret:12345678912345) AND (CodeRND:1.2.2.4.3 AND ApplicationProductrice:CTC AND ApplicationTraitement:CTC)");

      attendu
            .setRequeteCodeCourts("(rum:\"24534Y8465435413Y012312356690123\" AND srt:12345678912345) AND (SM_DOCUMENT_TYPE:1.2.2.4.3 AND apr:CTC AND atr:CTC)");

      attendu.getMetaUtilisees().put("RUM", "rum");
      attendu.getMetaUtilisees().put("Siret", "srt");
      attendu.getMetaUtilisees().put("CodeRND", "SM_DOCUMENT_TYPE");
      attendu.getMetaUtilisees().put("ApplicationProductrice", "apr");
      attendu.getMetaUtilisees().put("ApplicationTraitement", "atr");

      parse(attendu);

   }

   @Test
   public void cas19() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("Denomination:\"Test 319-Recherche-OK-Toutes-Metadonnees-Recherchables\" AND Siret:12345678912345");

      attendu
            .setRequeteCodeCourts("den:\"Test 319-Recherche-OK-Toutes-Metadonnees-Recherchables\" AND srt:12345678912345");

      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("Siret", "srt");

      parse(attendu);

   }

   @Test
   public void cas20() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("Denomination:\"Test 319-Recherche-OK-Toutes-Metadonnees-Recherchables\" AND NumeroCompteExterne:30148032541101600");

      attendu
            .setRequeteCodeCourts("den:\"Test 319-Recherche-OK-Toutes-Metadonnees-Recherchables\" AND nce:30148032541101600");

      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("NumeroCompteExterne", "nce");

      parse(attendu);

   }

   @Test
   public void cas21() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("RUM:24534Y8465435413Y012312356690123 AND Siret:12345678912345 AND CodeRND:1.2.2.4.3 AND ApplicationProductrice:CTC AND ApplicationTraitement:CTC");

      attendu
            .setRequeteCodeCourts("rum:24534Y8465435413Y012312356690123 AND srt:12345678912345 AND SM_DOCUMENT_TYPE:1.2.2.4.3 AND apr:CTC AND atr:CTC");

      attendu.getMetaUtilisees().put("RUM", "rum");
      attendu.getMetaUtilisees().put("Siret", "srt");
      attendu.getMetaUtilisees().put("CodeRND", "SM_DOCUMENT_TYPE");
      attendu.getMetaUtilisees().put("ApplicationProductrice", "apr");
      attendu.getMetaUtilisees().put("ApplicationTraitement", "atr");

      parse(attendu);

   }

   @Test
   public void cas22() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("Denomination:\"Test 304-Recherche-OK-Complexe-Dates\" AND (DateCreation:20050618 OR DateCreation:[20050718 TO 20050722] OR DateCreation:{20050818 TO 20050822})");

      attendu
            .setRequeteCodeCourts("den:\"Test 304-Recherche-OK-Complexe-Dates\" AND (SM_CREATION_DATE:20050618 OR SM_CREATION_DATE:[20050718 TO 20050722] OR SM_CREATION_DATE:{20050818 TO 20050822})");

      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("DateCreation", "SM_CREATION_DATE");

      parse(attendu);

   }

   @Test
   public void cas23() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("CodeRND:2.3.1.1.12 AND Siren:3090000001 AND DateCreation:20070401 AND  Denomination:\"Test 309-Recherche-OK-ProjetAttestations\"");

      attendu
            .setRequeteCodeCourts("SM_DOCUMENT_TYPE:2.3.1.1.12 AND srn:3090000001 AND SM_CREATION_DATE:20070401 AND  den:\"Test 309-Recherche-OK-ProjetAttestations\"");

      attendu.getMetaUtilisees().put("CodeRND", "SM_DOCUMENT_TYPE");
      attendu.getMetaUtilisees().put("Siren", "srn");
      attendu.getMetaUtilisees().put("DateCreation", "SM_CREATION_DATE");
      attendu.getMetaUtilisees().put("Denomination", "den");

      parse(attendu);

   }

   @Test
   public void cas24() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("Denomination:\"Test 301-Recherche-OK-Standard\" AND CodeRND:(\"2.3.1.1.12\" AND \"2.3.1.1.8\")");

      attendu
            .setRequeteCodeCourts("den:\"Test 301-Recherche-OK-Standard\" AND SM_DOCUMENT_TYPE:(\"2.3.1.1.12\" AND \"2.3.1.1.8\")");

      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("CodeRND", "SM_DOCUMENT_TYPE");

      parse(attendu);

   }

   @Test
   public void cas25() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("Denomination:\"Test 301-Recherche-OK-Standard\" AND CodeRND:(\"2.3.1.1.12\" OR \"2.3.1.1.8\")");

      attendu
            .setRequeteCodeCourts("den:\"Test 301-Recherche-OK-Standard\" AND SM_DOCUMENT_TYPE:(\"2.3.1.1.12\" OR \"2.3.1.1.8\")");

      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("CodeRND", "SM_DOCUMENT_TYPE");

      parse(attendu);

   }

   @Test
   public void cas26() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("CodeRND:(\"2.3.1.1.12\" OR \"2.3.1.1.8\") AND Denomination:\"Test 301-Recherche-OK-Standard\"");

      attendu
            .setRequeteCodeCourts("SM_DOCUMENT_TYPE:(\"2.3.1.1.12\" OR \"2.3.1.1.8\") AND den:\"Test 301-Recherche-OK-Standard\"");

      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("CodeRND", "SM_DOCUMENT_TYPE");

      parse(attendu);

   }

   @Test
   public void cas27() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("DateCreation:20050618 AND CodeRND:(\"2.3.1.1.12\" OR \"2.3.1.1.8\") AND Denomination:\"Test 301-Recherche-OK-Standard\"");

      attendu
            .setRequeteCodeCourts("SM_CREATION_DATE:20050618 AND SM_DOCUMENT_TYPE:(\"2.3.1.1.12\" OR \"2.3.1.1.8\") AND den:\"Test 301-Recherche-OK-Standard\"");

      attendu.getMetaUtilisees().put("DateCreation", "SM_CREATION_DATE");
      attendu.getMetaUtilisees().put("CodeRND", "SM_DOCUMENT_TYPE");
      attendu.getMetaUtilisees().put("Denomination", "den");

      parse(attendu);

   }

   @Test
   public void cas28() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("Denomination:\"Test 301-Recherche-OK-Standard\" AND CodeRND:(\"2.3.1.1.12\" OR \"2.3.1.1.8\" OR \"2.4.1.1.8\" OR \"2.5.1.1.8\" OR \"2.6.1.1.8\" OR \"2.6.1.1.8\" OR \"2.7.1.1.8\" OR \"2.7.1.1.8\" OR \"2.8.1.1.8\" OR \"2.9.1.1.8\")");

      attendu
            .setRequeteCodeCourts("den:\"Test 301-Recherche-OK-Standard\" AND SM_DOCUMENT_TYPE:(\"2.3.1.1.12\" OR \"2.3.1.1.8\" OR \"2.4.1.1.8\" OR \"2.5.1.1.8\" OR \"2.6.1.1.8\" OR \"2.6.1.1.8\" OR \"2.7.1.1.8\" OR \"2.7.1.1.8\" OR \"2.8.1.1.8\" OR \"2.9.1.1.8\")");

      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("CodeRND", "SM_DOCUMENT_TYPE");

      parse(attendu);

   }

   @Test
   public void cas29() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("CodeRND:(\"2.3.1.1.12\" OR \"2.3.1.1.8\" OR \"2.6.1.1.8\" OR \"2.6.1.1.8\" OR \"2.7.1.1.8\") AND Denomination:\"Test 301-Recherche-OK-Standard\"");

      attendu
            .setRequeteCodeCourts("SM_DOCUMENT_TYPE:(\"2.3.1.1.12\" OR \"2.3.1.1.8\" OR \"2.6.1.1.8\" OR \"2.6.1.1.8\" OR \"2.7.1.1.8\") AND den:\"Test 301-Recherche-OK-Standard\"");

      attendu.getMetaUtilisees().put("CodeRND", "SM_DOCUMENT_TYPE");
      attendu.getMetaUtilisees().put("Denomination", "den");

      parse(attendu);

   }

   @Test
   public void cas30() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("DateCreation:20050618 AND CodeRND:(\"2.3.1.1.12\" OR \"2.3.1.1.8\" OR \"2.6.1.1.8\" OR \"2.6.1.1.8\" OR \"2.7.1.1.8\") AND Denomination:\"Test 301-Recherche-OK-Standard\"");

      attendu
            .setRequeteCodeCourts("SM_CREATION_DATE:20050618 AND SM_DOCUMENT_TYPE:(\"2.3.1.1.12\" OR \"2.3.1.1.8\" OR \"2.6.1.1.8\" OR \"2.6.1.1.8\" OR \"2.7.1.1.8\") AND den:\"Test 301-Recherche-OK-Standard\"");

      attendu.getMetaUtilisees().put("DateCreation", "SM_CREATION_DATE");
      attendu.getMetaUtilisees().put("CodeRND", "SM_DOCUMENT_TYPE");
      attendu.getMetaUtilisees().put("Denomination", "den");

      parse(attendu);

   }

   @Test
   public void cas31() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("DateCreation:20050618 AND CodeRND:(\"2.3.1.1.12\" OR \"2.3.1.1.8\" OR \"2.6.1.1.8\" OR \"2.6.1.1.8\" OR \"2.7.1.1.8\") AND Denomination:(\"Test 301-Recherche-OK-Standard\" OR \"Test 302-Recherche-OK-Standard\" OR \"Test 303-Recherche-OK-Standard\")");

      attendu
            .setRequeteCodeCourts("SM_CREATION_DATE:20050618 AND SM_DOCUMENT_TYPE:(\"2.3.1.1.12\" OR \"2.3.1.1.8\" OR \"2.6.1.1.8\" OR \"2.6.1.1.8\" OR \"2.7.1.1.8\") AND den:(\"Test 301-Recherche-OK-Standard\" OR \"Test 302-Recherche-OK-Standard\" OR \"Test 303-Recherche-OK-Standard\")");

      attendu.getMetaUtilisees().put("DateCreation", "SM_CREATION_DATE");
      attendu.getMetaUtilisees().put("CodeRND", "SM_DOCUMENT_TYPE");
      attendu.getMetaUtilisees().put("Denomination", "den");

      parse(attendu);

   }

   @Test
   public void cas32() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu.setRequeteOrigine("RUM:\"  laRUM  \"");

      attendu.setRequeteCodeCourts("rum:\"  laRUM  \"");

      attendu.getMetaUtilisees().put("RUM", "rum");

      parse(attendu);

   }

   @Test
   public void cas33() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu.setRequeteOrigine("RUM:\"  laRUM\"");

      attendu.setRequeteCodeCourts("rum:\"  laRUM\"");

      attendu.getMetaUtilisees().put("RUM", "rum");

      parse(attendu);

   }

   @Test
   public void cas34() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu.setRequeteOrigine("RUM:\"laRUM  \"");

      attendu.setRequeteCodeCourts("rum:\"laRUM  \"");

      attendu.getMetaUtilisees().put("RUM", "rum");

      parse(attendu);

   }

   @Test
   public void cas35() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu.setRequeteOrigine("RUM:laRUM\\ \\ \\ ");

      attendu.setRequeteCodeCourts("rum:laRUM\\ \\ \\ ");

      attendu.getMetaUtilisees().put("RUM", "rum");

      parse(attendu);

   }

   @Test
   public void cas36() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu.setRequeteOrigine("RUM:\"  rum5\" AND PseudoSiret:Z124867");

      attendu.setRequeteCodeCourts("rum:\"  rum5\" AND psi:Z124867");

      attendu.getMetaUtilisees().put("RUM", "rum");
      attendu.getMetaUtilisees().put("PseudoSiret", "psi");

      parse(attendu);

   }

   @Test
   public void cas37() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("DateCreation:20130115 AND Denomination:\"  toto\" AND CodeRND:1.2.3.4.5");

      attendu
            .setRequeteCodeCourts("SM_CREATION_DATE:20130115 AND den:\"  toto\" AND SM_DOCUMENT_TYPE:1.2.3.4.5");

      attendu.getMetaUtilisees().put("DateCreation", "SM_CREATION_DATE");
      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("CodeRND", "SM_DOCUMENT_TYPE");

      parse(attendu);

   }

   @Test
   public void cas38() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("CodeRND:2.3.1.1.12 AND Siren:3090000001 AND DateCreation:20070401");

      attendu
            .setRequeteCodeCourts("SM_DOCUMENT_TYPE:2.3.1.1.12 AND srn:3090000001 AND SM_CREATION_DATE:20070401");

      attendu.getMetaUtilisees().put("CodeRND", "SM_DOCUMENT_TYPE");
      attendu.getMetaUtilisees().put("Siren", "srn");
      attendu.getMetaUtilisees().put("DateCreation", "SM_CREATION_DATE");

      parse(attendu);

   }

   @Test
   public void cas39() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("Denomination:\"Test 224-CaptureMasse-OK-RUM\" AND RUM:\"    678901234567890123456789012345\"");

      attendu
            .setRequeteCodeCourts("den:\"Test 224-CaptureMasse-OK-RUM\" AND rum:\"    678901234567890123456789012345\"");

      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("RUM", "rum");

      parse(attendu);

   }

   @Test
   public void cas40() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("Denomination:\"Test 224-CaptureMasse-OK-RUM\" AND RUM:\" 2345678901234567890     6789012345\"");

      attendu
            .setRequeteCodeCourts("den:\"Test 224-CaptureMasse-OK-RUM\" AND rum:\" 2345678901234567890     6789012345\"");

      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("RUM", "rum");

      parse(attendu);

   }

   @Test
   public void cas42() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("Denomination:\"Test 224-CaptureMasse-OK-RUM\" AND RUM:\"??abc : d 12 ef\"");

      attendu
            .setRequeteCodeCourts("den:\"Test 224-CaptureMasse-OK-RUM\" AND rum:\"??abc : d 12 ef\"");

      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("RUM", "rum");

      parse(attendu);

   }

   @Test
   public void cas43() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("Denomination:\"Test 224-CaptureMasse-OK-RUM\" AND RUM:\"    ??abc : d 12 ef     \"");

      attendu
            .setRequeteCodeCourts("den:\"Test 224-CaptureMasse-OK-RUM\" AND rum:\"    ??abc : d 12 ef     \"");

      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("RUM", "rum");

      parse(attendu);

   }

   @Test
   public void cas44() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu
            .setRequeteOrigine("Denomination:\"Test 224-CaptureMasse-OK-RUM\" AND RUM: \"                             12345\"");

      attendu
            .setRequeteCodeCourts("den:\"Test 224-CaptureMasse-OK-RUM\" AND rum: \"                             12345\"");

      attendu.getMetaUtilisees().put("Denomination", "den");
      attendu.getMetaUtilisees().put("RUM", "rum");

      parse(attendu);

   }

   /**
    * Cas d'utilisation : une métadonnée dont le code long n'existe pas dans le
    * référentiel des métadonnées<br>
    * Résultat attendu : le code long reste tel quel. Pas d'exception
    */
   @Test
   public void cas45() throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      SAESearchQueryParserResult attendu = new SAESearchQueryParserResult();

      attendu.setRequeteOrigine("MetaInconnue:valeur");

      attendu.setRequeteCodeCourts("MetaInconnue:valeur");

      attendu.getMetaUtilisees().put("MetaInconnue", "MetaInconnue");

      parse(attendu);

   }

}
