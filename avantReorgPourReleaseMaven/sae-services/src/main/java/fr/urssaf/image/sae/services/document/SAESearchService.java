package fr.urssaf.image.sae.services.document;

import java.util.List;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;

/**
 * Fournit l’ensemble des services pour la recherche.<BR />
 * 
 * @author lbaadj.
 */
public interface SAESearchService {

   /**
    * Service pour l'opération <b>Recherche</b>
    * 
    * @param requete
    *           : requete LuceneCriteria
    * @param listMetaDesired
    *           liste des metaDonnees desirée
    * @return Une liste de document de type {@link UntypedDocument}.
    * @throws SAESearchServiceEx
    *            Exception levée lorsqu'une erreur s'est produite lors de la
    *            recherche dans DFCE.
    * @throws MetaDataUnauthorizedToSearchEx
    *            Exception levée lorsqu'une erreur s'est produite sur des
    *            metadonnees<br>
    *            non autorisées pour la recherche dans DFCE.
    * @throws MetaDataUnauthorizedToConsultEx
    *            Exception levée lorsqu'une erreur s'est produite sur des
    *            metadonnees<br>
    *            non autorisées pour la consultation dans DFCE.
    * @throws UnknownDesiredMetadataEx {@link UnknownDesiredMetadataEx}
    * @throws UnknownLuceneMetadataEx  {@link UnknownLuceneMetadataEx}
    * @throws SyntaxLuceneEx  {@link SyntaxLuceneEx}
    */
   List<UntypedDocument> search(String requete, List<String> listMetaDesired)
         throws MetaDataUnauthorizedToSearchEx,
         MetaDataUnauthorizedToConsultEx, UnknownDesiredMetadataEx,
         UnknownLuceneMetadataEx, SyntaxLuceneEx, SAESearchServiceEx;

}