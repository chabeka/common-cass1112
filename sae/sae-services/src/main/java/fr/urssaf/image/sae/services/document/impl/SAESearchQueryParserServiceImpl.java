package fr.urssaf.image.sae.services.document.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.metadata.exceptions.LongCodeNotFoundException;
import fr.urssaf.image.sae.metadata.referential.services.SAEConvertMetadataService;
import fr.urssaf.image.sae.services.document.SAESearchQueryParserService;
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
   @Autowired
   private SAEConvertMetadataService convertService;

   private static final Logger LOG = LoggerFactory
         .getLogger(SAESearchQueryParserServiceImpl.class);

   /**
    * {@inheritDoc}
    */
   @Override
   public final String convertFromLongToShortCode(String requeteFinal,
         List<String> listeCodeLong) throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException {
         
      String requeteAvecCodeCourt = StringUtils.EMPTY;
      
      // définition de la liste des opérateurs qu'on peut trouver dans une
      // requête.
      List<String> operateurList = new ArrayList<String>();
      operateurList.add("+");
      operateurList.add("-");
      operateurList.add("NOT");
      operateurList.add(" ");
      operateurList.add("(");
      operateurList.add(")");

      // récupération de la valeur du code court
      // on prend un sous ensemble de nomMeta puisqu'il est possible
      // d'avoir des opérateurs tels que + - ( et NOT au début de la
      // chaîne de caractère mais pas à la fin.
      Map<String, String> map;
      try {
         map = convertService.longCodeToShortCode(listeCodeLong);
      } catch (LongCodeNotFoundException e) {
         throw new SAESearchServiceEx(ResourceMessagesUtils
               .loadMessage("search.referentiel.error"), e);
      }
      String requeteATraiter = requeteFinal;

      // on remplace tous les " AND " et " OR " par $%#A#%$ et $%#O#%$
      // respectivement
      requeteATraiter = requeteATraiter.replace(" AND ", "§%#&#%§£");
      requeteATraiter = requeteATraiter.replace(" OR ", "§%#|#%§£");
      // on remplace tous les espaces qui suivi par un + ou un -
      // on utilise $1 et $2 poure reconstruire la châine. l'expression
      // régulière (\\w{1})\\s+((\\+|\\-)[A-Z]{1}) remplace la chaine "a +A"
      // ou "b -C" par ¤¤£. le $1 et $2 permettent de récupère le "a" et le
      // "A" pour reconstituer la chaîne originale
      requeteATraiter = requeteATraiter.replaceAll("(\\w{1})\\s+((\\+|\\-)[A-Z]{1})",
            "$1¤¤£$2");
      // on remplace tous les espaces précédé par un "
      requeteATraiter = requeteATraiter.replaceAll("(\")\\s+|(\\))\\s+", "$1$2@@£");

      // on décompose la chaime entière pour avoir un tableau de métadonnées
      // String[] meta = requeteFinal.split("§%#.#%§£");
      String[] meta = requeteATraiter.split("£");

      // on supprime tout sauf les sépartateurs afin de garder la séquence
      // des AND et des OR pour la reconstruction de la requête
      String copieRequeteFinale = requeteATraiter;
      // on remplace tous sauf les tokens afin d'enregistrer la position de
      // chaque opérateurs ou espace
      copieRequeteFinale = copieRequeteFinale.replaceAll(
            "[^§%#&#%§£|§%#\\|#%§£|@@£|¤¤£]", StringUtils.EMPTY);
      String[] position = copieRequeteFinale.split("£");

      // parcour de la liste des méta données pour remplacer les codes longs
      // par les codes courts
      ArrayList<String> metaCourt = new ArrayList<String>();
      for (String m : meta) {
         // la décomposition s'est fait à partir du £ il faut donc enlever
         // les reste de token
         m = m.replace("§%#&#%§", StringUtils.EMPTY);
         m = m.replace("§%#|#%§", StringUtils.EMPTY);
         m = m.replace("@@", StringUtils.EMPTY);
         m = m.replace("¤¤", StringUtils.EMPTY);

         String nomMeta = m.substring(0, m.indexOf(":"));
         String codeCourt = StringUtils.EMPTY;

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

         for (Map.Entry<String, String> e : map.entrySet()) {
            if (e.getValue().equals(
                  nomMeta.substring(startPosition, nomMeta.length()))) {
               codeCourt = e.getKey();
            }
         }

         // replacer les codes longs par les codes courts
         if (codeCourt != null && codeCourt != StringUtils.EMPTY) {
            metaCourt.add(m.replaceFirst(nomMeta.substring(startPosition,
                  nomMeta.length()), codeCourt));
         }

      }

      // on doit avoir le même nombre de code court que de code long
      if (meta.length == metaCourt.size()) {
         requeteAvecCodeCourt = rebuidQuery(metaCourt, position);
      } else {
         LOG
               .error("Erreur le nombre de code court est inférieure à celui attendu");
         LOG.error("Liste des métadonnées longues : {}", meta);
         LOG.error("Liste des métadonnées courtes : {}", ArrayUtils
               .toString(metaCourt));
         throw new SAESearchQueryParseException(ResourceMessagesUtils
               .loadMessage("search.parse.error"));

      }


      return requeteAvecCodeCourt;
   }
   
   private String rebuidQuery(List<String> metaCourt, String[] position){
      String requete = StringUtils.EMPTY;
      // construction de la requête avec le code court
      for (int i = 0; i < metaCourt.size(); i++) {
         // on a plusieurs cas possibles, une combinaison de AND OR et
         // d'espaces. Si on a m1:v1 +m2:v2 AND m3:v3 on aura un tablea de
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
                  requete =requete.concat(metaCourt.get(i));
                  requete =requete.concat(" ");
               }
            } else {
               // dernière méta donnée
               requete = requete.concat(metaCourt.get(i));
            }
         } else {
            // cas ou on a pas d'opérateur OR NOT ou AND mais que des
            // espaces et ou ce n'est pas la dernière métadonnée
            if (i < metaCourt.size() - 1) {
               requete =requete.concat( metaCourt.get(i));
               requete =requete.concat(" ");
            } else {
               requete =requete.concat(metaCourt.get(i));
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

}
