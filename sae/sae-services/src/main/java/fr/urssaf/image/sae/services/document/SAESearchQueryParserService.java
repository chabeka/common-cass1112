package fr.urssaf.image.sae.services.document;

import java.util.List;

import fr.urssaf.image.sae.services.exception.SAESearchQueryParseException;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;

public interface SAESearchQueryParserService {

   
   public String convertFromLongToShortCode(String requeteFinal, List<String> listeCodeLong) throws SyntaxLuceneEx, SAESearchServiceEx, SAESearchQueryParseException ;
   
}
