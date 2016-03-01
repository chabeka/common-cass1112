package fr.urssaf.image.sae.services.util;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;

/**
 * Fonctions utilitaires pour le service de recherche
 */
public final class SAESearchUtil {
   
   private static final Logger LOG = LoggerFactory
         .getLogger(SAESearchUtil.class);

   private SAESearchUtil() {
      // constructeur privé
   }

   /**
    * Trim la requête Lucene client.<br>
    * Conserve l'espace de fin de la requête s'il est échappé (ex. de requête:
    * "meta:valeur\ ")
    * 
    * @param requeteClient
    *           la requête client
    * @return la requête trimmée
    */
   public static String trimRequeteClient(String requeteClient) {

      String requeteTrim = StringUtils.trim(requeteClient);

      // Cas particulier des requêtes se terminant par un espace échappé
      // exemple: "meta:valeur\ "
      // pour lesquelles il faut garder l'espace final
      if (StringUtils.isNotBlank(requeteTrim) && requeteTrim.endsWith("\\")
            && requeteClient.endsWith(" ")) {
         requeteTrim += " ";
      }

      return requeteTrim;

   }
   
   /**
    * Vérifie rapidement la syntaxe de la requête LUCENE en utilisant un
    * QueryParser LUCENE
    * 
    * @param requete
    *           : Requête Lucene.
    * @throws SyntaxLuceneEx
    *            : Une exception de type {@link SyntaxLuceneEx}
    */
   public static void verifieSyntaxeLucene(String requete) throws SyntaxLuceneEx {

      // Traces debug - entrée méthode
      String prefixeTrc = "verifieSyntaxeLucene()";
      LOG.debug("{} - Début", prefixeTrc);
      LOG
            .debug(
                  "{} - Début de la vérification SAE: La requête de recherche est syntaxiquement correcte",
                  prefixeTrc);
      // Fin des traces debug - entrée méthode

      // Utilise un QueryParser LUCENE pour analyse la requête
      Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
      QueryParser queryParser = new QueryParser(Version.LUCENE_CURRENT,
            StringUtils.EMPTY, analyzer);
      queryParser.setAllowLeadingWildcard(true);
      try {
         queryParser.parse(requete);
      } catch (ParseException except) {
         LOG.debug("{} - {}", prefixeTrc, ResourceMessagesUtils
               .loadMessage("search.syntax.lucene.error"));
         throw new SyntaxLuceneEx(ResourceMessagesUtils
               .loadMessage("search.syntax.lucene.error"), except);
      }
      LOG
            .debug(
                  "{} - Fin de la vérification SAE: La requête de recherche est syntaxiquement correcte",
                  prefixeTrc);

      LOG.debug("{} - Fin", prefixeTrc);
   }

}
