package fr.urssaf.image.sae.services.batch.common.support;

import java.util.List;

import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.services.exception.SAESearchQueryParseException;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;

/**
 * Interface commune permettant de verifier les droits d'une requete lucene.
 */
public interface VerifDroitRequeteLuceneSupport {

   /**
    * Methode permettant de verifier les droits d'une requete lucene.
    * 
    * @param requeteLucene
    *           requete lucene initiale
    * @param prmds
    *           liste de prmd
    * @return String : requete lucene final (libelle court)
    * @throws SyntaxLuceneEx
    *            Erreur de syntaxe dans la requete lucene
    * @throws SAESearchQueryParseException
    *            Erreur de parsing de la requete lucene
    * @throws SAESearchServiceEx
    *            Erreur de recherche
    * @throws UnknownLuceneMetadataEx
    *            Metadonnee inconnue dans la requete lucene
    * @throws MetaDataUnauthorizedToSearchEx
    *            Metadonnee non rechercheable dans la requete lucene
    */
   String verifDroitRequeteLucene(String requeteLucene, List<SaePrmd> prmds)
         throws SyntaxLuceneEx, SAESearchQueryParseException,
         SAESearchServiceEx, UnknownLuceneMetadataEx,
         MetaDataUnauthorizedToSearchEx;
}
