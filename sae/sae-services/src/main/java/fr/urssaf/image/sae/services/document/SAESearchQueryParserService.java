package fr.urssaf.image.sae.services.document;

import java.util.List;

import fr.urssaf.image.sae.services.exception.SAESearchQueryParseException;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;

/**
 * Interface de service de parsing de requêtes
 * 
 * 
 */
public interface SAESearchQueryParserService {

   /**
    * Conversion de codes longs en codes courts
    * 
    * @param requeteFinal
    *           requete à modifier
    * @param listeCodeLong
    *           liste des codes longs
    * @return la requête dont les codes longs ont été remplacés par des codes
    *         courts
    * @throws SyntaxLuceneEx
    *            exception levée si la syntaxe de la requete est erronée
    * @throws SAESearchServiceEx
    *            exception levée si la recherche de code ne trouve pas de
    *            correspondance
    * @throws SAESearchQueryParseException
    *            exception levée si une erreur quelconque est soulevée lors du
    *            traitement
    */
   String convertFromLongToShortCode(String requeteFinal,
         List<String> listeCodeLong) throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException;

}
