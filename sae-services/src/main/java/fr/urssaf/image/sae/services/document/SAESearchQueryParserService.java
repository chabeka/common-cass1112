package fr.urssaf.image.sae.services.document;

import fr.urssaf.image.sae.services.document.model.SAESearchQueryParserResult;
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
    * @return le résultat du parser
    * @throws SyntaxLuceneEx
    *            exception levée si la syntaxe de la requete est erronée
    * @throws SAESearchServiceEx
    *            exception levée si la recherche de code ne trouve pas de
    *            correspondance
    * @throws SAESearchQueryParseException
    *            exception levée si une erreur quelconque est soulevée lors du
    *            traitement
    */
   SAESearchQueryParserResult convertFromLongToShortCode(String requeteFinal)
         throws SyntaxLuceneEx, SAESearchServiceEx,
         SAESearchQueryParseException;

}
