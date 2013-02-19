package fr.urssaf.image.sae.services.document.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.services.document.SAESearchQueryParserService;
import fr.urssaf.image.sae.services.document.model.SAESearchQueryParserResult;
import fr.urssaf.image.sae.services.exception.SAESearchQueryParseException;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Classe d'implémentation de l'interface {@link SAESearchQueryParserService}
 * 
 * 
 */
@Component
public class SAESearchQueryParserServiceImpl implements
      SAESearchQueryParserService {

   private static final Logger LOG = LoggerFactory
         .getLogger(SAESearchQueryParserServiceImpl.class);

   @Autowired
   private MetadataReferenceDAO metaRefDAO;

   /**
    * {@inheritDoc}
    */
   @Override
   public final SAESearchQueryParserResult convertFromLongToShortCode(
         String requeteFinal) throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {

      // Traces
      String prefixeTrc = "convertFromLongToShortCode()";
      LOG.debug("{} - Début", prefixeTrc);
      LOG.debug("{} - Requête fournie : {}", prefixeTrc, requeteFinal);

      // Initialise
      SAESearchQueryParserResult parserResult = new SAESearchQueryParserResult(
            requeteFinal);
      String requeteATraiter = requeteFinal;
      String requeteAvecCodeCourt = StringUtils.EMPTY;

      // définition de la liste des opérateurs qu'on peut trouver dans une
      // requête.
      List<String> operateurList = Arrays
            .asList("+", "-", "NOT", " ", "(", ")");

      // Détecte toutes les expressions entre quotes
      List<String> listeValeursQuotees = extraitValeursEntreQuotes(requeteATraiter);
      LOG.debug("{} - Valeurs quotées : {}", prefixeTrc, listeValeursQuotees);

      // Remplace les expressions entre quotes par des marqueurs
      Map<String, String> mapRemplacements = new HashMap<String, String>();
      if (!listeValeursQuotees.isEmpty()) {
         String valeurRemplacement;
         int compteurReplace = 1;
         for (String valeurEntreQuote : listeValeursQuotees) {
            valeurRemplacement = String.format("XYZ_%s", StringUtils.leftPad(
                  String.format("%s", compteurReplace), 3, '0'));
            requeteATraiter = StringUtils.replace(requeteATraiter,
                  valeurEntreQuote, valeurRemplacement);
            mapRemplacements.put(valeurEntreQuote, valeurRemplacement);
            compteurReplace++;
         }
         LOG.debug("{} - Requête après traitement des valeurs quotées : {}",
               prefixeTrc, requeteATraiter);
      }

      // on remplace tous les " AND " et " OR " par $%#&#%$ et $%#|#%$
      // respectivement
      requeteATraiter = requeteATraiter.replace(" AND ", "§%#&#%§£");
      LOG.debug("{} - Requête après traitement des opérateurs AND : {}",
            prefixeTrc, requeteATraiter);
      requeteATraiter = requeteATraiter.replace(" OR ", "§%#|#%§£");
      LOG.debug("{} - Requête après traitement des opérateurs OR : {}",
            prefixeTrc, requeteATraiter);
      // on remplace tous les espaces qui sont suivi par un + ou un -
      // on utilise $1 et $2 poure reconstruire la châine. l'expression
      // régulière (\\w{1})\\s+((\\+|\\-)[A-Z]{1}) remplace la chaine "a +A"
      // ou "b -C" par ¤¤£. le $1 et $2 permettent de récupère le "a" et le
      // "A" pour reconstituer la chaîne originale
      requeteATraiter = requeteATraiter.replaceAll(
            "(\\w{1})\\s+((\\+|\\-)[A-Z]{1})", "$1¤¤£$2");
      LOG
            .debug(
                  "{} - Requête après traitement des espaces après un + ou un - : {}",
                  prefixeTrc, requeteATraiter);
      // on remplace tous les espaces précédé par un "
      requeteATraiter = requeteATraiter.replaceAll("(\")\\s+|(\\))\\s+",
            "$1$2@@£");
      LOG
            .debug(
                  "{} - Requête après traitement des espaces précédé par un double-quotes : {}",
                  prefixeTrc, requeteATraiter);

      // on décompose la chaine entière pour avoir un tableau de métadonnées
      // String[] meta = requeteFinal.split("§%#.#%§£");
      String[] meta = requeteATraiter.split("£");
      LOG.debug(
            "{} - Split de la requête pour la recherche des métadonnées : {}",
            prefixeTrc, meta);

      // on supprime tout sauf les sépartateurs afin de garder la séquence
      // des AND et des OR pour la reconstruction de la requête
      String copieRequeteFinale = requeteATraiter;
      // on remplace tous sauf les tokens afin d'enregistrer la position de
      // chaque opérateurs ou espace
      copieRequeteFinale = copieRequeteFinale.replaceAll(
            "[^§%#&#%§£|§%#\\|#%§£|@@£|¤¤£]", StringUtils.EMPTY);
      String[] position;
      if (StringUtils.isEmpty(copieRequeteFinale)) {
         position = new String[] {};
      } else {
         position = copieRequeteFinale.split("£");
      }

      // parcour de la liste des méta données pour remplacer les codes longs
      // par les codes courts
      LOG.debug("{} - Traitements des parties splittés", prefixeTrc);
      ArrayList<String> metaCourt = new ArrayList<String>();
      int index = 0;
      for (String m : meta) {

         LOG.debug("{} - Traitement de : {}", prefixeTrc, m);

         // la décomposition s'est fait à partir du £ il faut donc enlever
         // les reste de token
         m = m.replace("§%#&#%§", StringUtils.EMPTY);
         m = m.replace("§%#|#%§", StringUtils.EMPTY);
         m = m.replace("@@", StringUtils.EMPTY);
         m = m.replace("¤¤", StringUtils.EMPTY);
         LOG.debug("{} - Bloc après remplacement des tokens : {}", prefixeTrc,
               m);

         // on recherche les codes court pour les codes longs présents dans la
         // requête
         if (m.contains(":")) {
            LOG
                  .debug(
                        "{} - On a trouvé un : dans le bloc. On extrait la métadonnée",
                        prefixeTrc);
            // seul les éléments contenant un ":" sont considérés comme
            // metadonnées ex CodeRND:("2.3.1.1.12" AND "2.3.1.1.8") sera
            // décomposé en [CodeRND:("2.3.1.1.12", "2.3.1.1.8")] la taille de
            // la liste est de deux mais elle ne contient q'une seule métadonnée
            try {
               findCodeCourtForCodeLong(operateurList, m, metaCourt,
                     parserResult.getMetaUtilisees());
            } catch (ReferentialException e) {
               throw new SAESearchQueryParseException(
                     "Erreur lors de la lecture du référentiel des métadonnées",
                     e);
            }
         } else {
            LOG
                  .debug(
                        "{} - Pas de : dans le bloc. On récupère le bloc [{}] tel quel",
                        prefixeTrc, m);
            // cette liste va contenir les valuers pour lesquelles il n'y a pas
            // de métadonnées par ex la valeu "2.3.1.1.8" si on reprend le cas
            // de CodeRND:("2.3.1.1.12" AND "2.3.1.1.8")
            metaCourt.add(index, m);
         }

         index++;
      }

      // on doit avoir le même nombre de code court que de code long
      if (meta.length == metaCourt.size()) {

         requeteAvecCodeCourt = rebuidQuery(metaCourt, position);

         LOG.debug("{} - Requête après reconstruction : {}", prefixeTrc,
               requeteAvecCodeCourt);

      } else {
         LOG
               .error("Erreur le nombre de code court est inférieure à celui attendu");
         LOG.error("Liste des métadonnées longues : {}", meta);
         LOG.error("Liste des métadonnées courtes : {}", ArrayUtils
               .toString(metaCourt));
         throw new SAESearchQueryParseException(ResourceMessagesUtils
               .loadMessage("search.parse.error"));

      }

      // Replace les valeurs entre quote
      if (!mapRemplacements.isEmpty()) {
         for (Map.Entry<String, String> entry : mapRemplacements.entrySet()) {
            requeteAvecCodeCourt = StringUtils.replace(requeteAvecCodeCourt,
                  entry.getValue(), entry.getKey());
         }
         LOG.debug("{} - Requête après replacement des valeurs quotées : {}",
               prefixeTrc, requeteAvecCodeCourt);
      }

      // Termine
      parserResult.setRequeteCodeCourts(requeteAvecCodeCourt);
      LOG.debug("{} - Analyse de la requête terminée", prefixeTrc);
      LOG.debug("{} - Requête d'origine : {}", prefixeTrc, parserResult
            .getRequeteOrigine());
      LOG.debug("{} - Requête avec les codes courts : {}", prefixeTrc,
            parserResult.getRequeteCodeCourts());
      LOG.debug("{} - Liste des métadonnées trouvées dans la requête : {}",
            prefixeTrc, parserResult.getMetaUtilisees());
      LOG.debug("{} - Fin", prefixeTrc);
      return parserResult;

   }

   private String rebuidQuery(List<String> metaCourt, String[] position) {
      String requete = StringUtils.EMPTY;
      // construction de la requête avec le code court
      for (int i = 0; i < metaCourt.size(); i++) {
         // on a plusieurs cas possibles, une combinaison de AND OR et
         // d'espaces. Si on a m1:v1 +m2:v2 AND m3:v3 on aura un tableau de
         // position de taille 2 avec [vide,AND] on va donc devoir tester
         // la longeur de la chaîne pour savoir s'il faut mettre un espace
         if (position.length > 0) {
            if (i < position.length) {
               // on vérifie la longeur de la chaîne si elle est > 0 c'est
               // un token sinon c'est un espace
               if (position[i].length() > 0) {
                  requete = requete.concat(metaCourt.get(i));
                  requete = requete.concat(position[i]);
               } else {
                  requete = requete.concat(metaCourt.get(i));
                  requete = requete.concat(" ");
               }
            } else {
               // dernière méta donnée
               requete = requete.concat(metaCourt.get(i));
            }
         } else {
            // cas ou on a pas d'opérateur OR NOT ou AND mais que des
            // espaces et ou ce n'est pas la dernière métadonnée
            if (i < metaCourt.size() - 1) {
               requete = requete.concat(metaCourt.get(i));
               requete = requete.concat(" ");
            } else {
               requete = requete.concat(metaCourt.get(i));
            }
         }
      }
      // remplacer les tokens par les filtres de départ. Le £ le figure plus
      // dans les tokens puisqu'il a été utilisé pour splitter la requête.
      requete = requete.replace("§%#&#%§", " AND ");
      requete = requete.replace("§%#|#%§", " OR ");
      requete = requete.replace("¤¤", " ");
      requete = requete.replace("@@", " ");

      return requete;
   }

   private void findCodeCourtForCodeLong(List<String> operateurList,
         String blocAanalyser, List<String> metaCourt,
         Map<String, String> metasTrouvees) throws ReferentialException {

      // Traces
      String prefixeTrc = "findCodeCourtForCodeLong()";
      LOG.debug("{} - Début", prefixeTrc);
      LOG.debug("{} - Bloc à analyser : {}", prefixeTrc, blocAanalyser);

      String nomMeta = blocAanalyser.substring(0, blocAanalyser.indexOf(':'));
      nomMeta = StringUtils.trim(nomMeta);
      LOG.debug("{} - Métadonnée trouvée en 1ère analyse : {}", prefixeTrc,
            nomMeta);

      // on vérifie qu'il n'existe pas d'autres opérateurs + - ( NOT ou
      // une combinaison d'opérateurs
      // si on détecte la présence d'un ou de plusieurs de ces opérateurs
      // on ajuste la position de départ
      // ces opérateurs seront présents uniquement en début de chaîne et
      // pas en fin de chaîne car il ne peut pas y avoir d'opérateurs
      // entre la métadonnée et ":"
      int startPosition = 0;
      for (String op : operateurList) {
         int nbOccurence = StringUtils.countMatches(nomMeta, op);
         if (nbOccurence > 0) {
            // on doit prendre en compte la longueur de l'opérateur. en
            // effet "+" est de longueur 1 donc le nombre d'occurence
            // correspond au nombre de caractères à prendre en compte. Par
            // contre "NOT" est de longueur 3, si on considère uniquement le
            // nombre d'occurence, le nombre de caractère à prendre en
            // compte sera faux. il faut donc multiplier le nombre
            // d'occurence par la longueur. pour 1 occurence le nombre de
            // caractère a prendre en compte est de 3.
            startPosition += (nbOccurence * op.length());
         }

      }

      String codeLongMeta = nomMeta.substring(startPosition, nomMeta.length());
      LOG.debug("{} - Code long de la métadonnée trouvée en 2ème analyse : {}",
            prefixeTrc, codeLongMeta);

      MetadataReference objMetadata = metaRefDAO.getByLongCode(codeLongMeta);

      String codeCourt;
      if (objMetadata == null) {
         codeCourt = codeLongMeta;
      } else {
         codeCourt = objMetadata.getShortCode();
      }

      LOG
            .debug(
                  "{} - Code court de la métadonnée récupérée du référentiel des métadonnées : {}",
                  prefixeTrc, codeCourt);

      // replacer les codes longs par les codes courts
      // et mémoriser la métadonnées que l'on vient de détecter
      if (codeCourt != null && codeCourt != StringUtils.EMPTY) {

         LOG.debug("{} - Remplacement du code long par le code court.",
               prefixeTrc);

         metaCourt.add(blocAanalyser.replaceFirst(nomMeta.substring(
               startPosition, nomMeta.length()), codeCourt));

         LOG
               .debug(
                     "{} - Mémorise la métadonnée détectée. Code long = {}, Code court = {}.",
                     prefixeTrc, new Object[] { codeLongMeta, codeCourt });

         metasTrouvees.put(codeLongMeta, codeCourt);

      } else {
         LOG
               .debug(
                     "{} - La métadonnée {} n'a pas été trouvée dans le référentiel des métadonnées. On conserver le code long.",
                     prefixeTrc, codeLongMeta);
      }

      // Fin
      LOG.debug("{} - Fin", prefixeTrc);

   }

   private List<String> extraitValeursEntreQuotes(String requete) {

      List<String> valeursQuotees = new ArrayList<String>();

      // (?:"[^"]*\\(?:.[^"]*\\)*.[^"]*")|(?:"[^"]*")
      Pattern regex = Pattern
            .compile("(?:\"[^\"]*\\\\(?:.[^\"]*\\\\)*.[^\"]*\")|(?:\"[^\"]*\")");
      Matcher regexMatcher = regex.matcher(requete);

      String exprTrouvee;
      while (regexMatcher.find()) {

         exprTrouvee = regexMatcher.group();

         if (!valeursQuotees.contains(exprTrouvee)) {
            valeursQuotees.add(exprTrouvee);
         }

      }

      return valeursQuotees;

   }

}
