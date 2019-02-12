package fr.urssaf.image.sae.webservices.service.impl;

import static fr.urssaf.image.sae.webservices.service.factory.ObjectRechercheFactory.createRechercheNbResResponse;
import static fr.urssaf.image.sae.webservices.service.factory.ObjectRechercheFactory.createRechercheParIterateurResponse;
import static fr.urssaf.image.sae.webservices.service.factory.ObjectRechercheFactory.createRechercheResponse;
import static org.apache.commons.lang.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.FiltreType;
import fr.cirtil.www.saeservice.IdentifiantPageType;
import fr.cirtil.www.saeservice.ListeMetadonneeType;
import fr.cirtil.www.saeservice.MetadonneeCodeType;
import fr.cirtil.www.saeservice.MetadonneeType;
import fr.cirtil.www.saeservice.MetadonneeValeurType;
import fr.cirtil.www.saeservice.RangeMetadonneeType;
import fr.cirtil.www.saeservice.Recherche;
import fr.cirtil.www.saeservice.RechercheNbRes;
import fr.cirtil.www.saeservice.RechercheNbResResponse;
import fr.cirtil.www.saeservice.RechercheParIterateur;
import fr.cirtil.www.saeservice.RechercheParIterateurRequestType;
import fr.cirtil.www.saeservice.RechercheParIterateurResponse;
import fr.cirtil.www.saeservice.RechercheResponse;
import fr.cirtil.www.saeservice.RequetePrincipaleType;
import fr.cirtil.www.saeservice.UuidType;
import fr.urssaf.image.sae.bo.model.AbstractMetadata;
import fr.urssaf.image.sae.bo.model.untyped.PaginatedUntypedDocuments;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedRangeMetadata;
import fr.urssaf.image.sae.metadata.exceptions.IndexCompositeException;
import fr.urssaf.image.sae.metadata.referential.services.IndexCompositeService;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.search.DoublonFiltresMetadataEx;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownFiltresMetadataEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;
import fr.urssaf.image.sae.webservices.constantes.RechercheConstantes;
import fr.urssaf.image.sae.webservices.exception.RechercheAxis2Fault;
import fr.urssaf.image.sae.webservices.factory.ObjectTypeFactory;
import fr.urssaf.image.sae.webservices.service.WSRechercheService;
import fr.urssaf.image.sae.webservices.util.CollectionUtils;
import fr.urssaf.image.sae.webservices.util.WsMessageRessourcesUtils;

/**
 * Classe concrète pour le service de recherche.
 */
@Service
public class WSRechercheServiceImpl implements WSRechercheService {
   private static final Logger LOG = LoggerFactory.getLogger(WSRechercheServiceImpl.class);

   private static final String VIDE = "";

   /**
    * Façade de service SAE : Capture, Consultation, et Recherche.
    */
   @Autowired
   private SAEDocumentService documentService;

   @Autowired
   private WsMessageRessourcesUtils wsMessageRessourcesUtils;

   @Autowired
   private IndexCompositeService indexCompositeService;

   /**
    * {@inheritDoc}
    */
   @Override
   public final RechercheResponse search(final Recherche request) throws RechercheAxis2Fault {
      // Traces debug - entrée méthode
      final String prefixeTrc = "search()";
      LOG.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode

      final int maxResult = RechercheConstantes.NB_MAX_RESULTATS_RECHERCHE;
      LOG.debug("{} - Le nombre maximum de documents à renvoyer dans les résultats de "
            + "recherche au niveau de la couche webservice est {}", prefixeTrc, maxResult);
      boolean resultatTronque = false;
      RechercheResponse response;
      String requeteLucene = VIDE;
      try {
         requeteLucene = recupererReqLucene(request);
         checkNotNull(requeteLucene);
         final List<String> listMDDesired = recupererListMDDesired(recupererListMDSearch(request));
         final List<UntypedDocument> untypedDocuments = documentService.search(requeteLucene, listMDDesired);
         if (untypedDocuments.size() > maxResult) {
            resultatTronque = true;
            LOG.debug("{} - Les résultats de recherche sont tronqués à {} résultats", prefixeTrc, maxResult);
         }

         response = createRechercheResponse(untypedDocuments, resultatTronque);

      }
      catch (final SAESearchServiceEx except) {
         throw new RechercheAxis2Fault("ErreurInterneRecherche", except.getMessage(), except);
      }
      catch (final MetaDataUnauthorizedToSearchEx except) {
         throw new RechercheAxis2Fault("RechercheMetadonneesInterdite", except.getMessage(), except);
      }
      catch (final MetaDataUnauthorizedToConsultEx except) {
         throw new RechercheAxis2Fault("ConsultationMetadonneesInterdite", except.getMessage(), except);
      }
      catch (final UnknownDesiredMetadataEx except) {
         throw new RechercheAxis2Fault("ConsultationMetadonneesInconnues", except.getMessage(), except);
      }
      catch (final UnknownLuceneMetadataEx except) {
         throw new RechercheAxis2Fault("RechercheMetadonneesInconnues", except.getMessage(), except);
      }
      catch (final SyntaxLuceneEx except) {
         throw new RechercheAxis2Fault("SyntaxeLuceneNonValide", except.getMessage(), except);
      }
      catch (final RechercheAxis2Fault except) {
         throw new RechercheAxis2Fault("RequeteLuceneVideOuNull", except.getMessage(), except);
      }
      LOG.debug("{} - Sortie", prefixeTrc);
      return response;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public RechercheNbResResponse searchWithNbRes(final RechercheNbRes request) throws RechercheAxis2Fault {

      // -- Traces debug - entrée méthode
      final String prefixeTrc = "searchWithNbRes()";
      LOG.debug("{} - Début", prefixeTrc);

      final int maxResult = RechercheConstantes.NB_MAX_RESULTATS_RECHERCHE;
      final int maxResultRechDfce = RechercheConstantes.NB_MAX_RESULTATS_RECH_DFCE;
      LOG.debug("{} - Le nombre maximum de documents à renvoyer dans les résultats de "
            + "recherche au niveau de la couche webservice est {}", prefixeTrc, maxResult);
      boolean resultatTronque = false;
      RechercheNbResResponse response;
      String requeteLucene = VIDE;
      try {
         requeteLucene = recupererReqLucene(request);
         checkNotNull(requeteLucene);
         final List<String> listMDDesired = recupererListMDDesired(recupererListMDSearch(request));
         final List<UntypedDocument> untypedDocuments = documentService.search(requeteLucene,
                                                                               listMDDesired,
                                                                               maxResultRechDfce);
         if (untypedDocuments.size() > maxResult) {
            resultatTronque = true;
            final String mssg = "{} - Les résultats de recherche sont tronqués à {} résultats";
            LOG.debug(mssg, prefixeTrc, maxResult);
         }
         response = createRechercheNbResResponse(untypedDocuments, resultatTronque, maxResult);

      }
      catch (final SAESearchServiceEx except) {
         throw new RechercheAxis2Fault("ErreurInterneRecherche", except.getMessage(), except);
      }
      catch (final MetaDataUnauthorizedToSearchEx except) {
         throw new RechercheAxis2Fault("RechercheMetadonneesInterdite", except.getMessage(), except);
      }
      catch (final MetaDataUnauthorizedToConsultEx except) {
         throw new RechercheAxis2Fault("ConsultationMetadonneesInterdite", except.getMessage(), except);
      }
      catch (final UnknownDesiredMetadataEx except) {
         throw new RechercheAxis2Fault("ConsultationMetadonneesInconnues", except.getMessage(), except);
      }
      catch (final UnknownLuceneMetadataEx except) {
         throw new RechercheAxis2Fault("RechercheMetadonneesInconnues", except.getMessage(), except);
      }
      catch (final SyntaxLuceneEx except) {
         throw new RechercheAxis2Fault("SyntaxeLuceneNonValide", except.getMessage(), except);
      }
      catch (final RechercheAxis2Fault except) {
         throw new RechercheAxis2Fault("RequeteLuceneVideOuNull", except.getMessage(), except);
      }
      LOG.debug("{} - Sortie", prefixeTrc);
      return response;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public RechercheParIterateurResponse rechercheParIterateur(final RechercheParIterateur request)
         throws RechercheAxis2Fault {

      // Traces debug - entrée méthode
      final String prefixeTrc = "rechercheParIterateur()";
      LOG.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode

      RechercheParIterateurResponse response;

      final RechercheParIterateurRequestType params = request.getRechercheParIterateur();

      // Récupération de la requête principale à partir des paramètres
      // d'entrée
      final RequetePrincipaleType requetePrincipale = params.getRequetePrincipale();
      // Récupération de l'identifiant de la page à partir des paramètres
      // d'entrée
      final IdentifiantPageType identifiantPage = params.getIdentifiantPage();

      // Métadonnées fixes
      final List<UntypedMetadata> listeFixedMeta = recupererFixedMetadatas(requetePrincipale);

      // Métadonnée variable
      final UntypedRangeMetadata untypedRangeMeta = recupererRangeMetadata(requetePrincipale);

      // Si l'identifiant de la page est renseigné (lorsque ce n'est pas le
      // premier appel)
      // on met à jour la valeur min de la métadonnée de type range avec la
      // valeur de l'identifiant
      UUID idDoc = null;
      if (identifiantPage != null) {
         untypedRangeMeta.setValeurMin(identifiantPage.getValeur().getMetadonneeValeurType());
         idDoc = UUID.fromString(identifiantPage.getIdArchive().getUuidType());
      }

      final FiltreType filtres = params.getFiltres();
      // Filtres de type "égal à" ou "contenu dans" (pour les range)
      final List<AbstractMetadata> listeFiltreEgalite = recupererFiltresEgalite(filtres);
      // Filtres de type "différent" ou "non contenu dans" (pour les range)
      final List<AbstractMetadata> listeFiltreDifferent = recupererFiltresDifferent(filtres);

      // Nombre de documents par page
      final int nbDocsParPage = params.getNbDocumentsParPage();

      // Liste des métadonnées souhaitées en retour de recherche
      List<String> listeMetaSouhaitees = recupererListMDDesired(recupererListMDSearch(request));

      // On ajoute la métadonnée variable dans la liste des méta désirés, car
      // on a forcément besoin de la récupérer pour pouvoir mettre la valeur dans
      // l'identifiant de la dernière page
      boolean isMetaVariableAjoute = false;
      if (!listeMetaSouhaitees.contains(untypedRangeMeta.getLongCode())) {
         if (listeMetaSouhaitees.isEmpty()) {
            listeMetaSouhaitees = new ArrayList<>();
            listeMetaSouhaitees.add(untypedRangeMeta.getLongCode());
            isMetaVariableAjoute = true;
         } else {
            listeMetaSouhaitees.add(untypedRangeMeta.getLongCode());
            isMetaVariableAjoute = true;
         }
      }

      try {

         // Identification de l'indexComposite ou simple à utiliser pour la recherche
         final List<String> bestIndex = getBestIndex(listeFixedMeta, untypedRangeMeta);
         if (LOG.isDebugEnabled()) {
            final String indexAsString = String.join("-", bestIndex);
            LOG.debug("{} - Index à utiliser : {}", prefixeTrc, indexAsString);
         }

         // Lancement de la recherche paginée
         final PaginatedUntypedDocuments paginatedUDoc = documentService.searchPaginated(listeFixedMeta,
                                                                                         untypedRangeMeta,
                                                                                         listeFiltreEgalite,
                                                                                         listeFiltreDifferent,
                                                                                         nbDocsParPage,
                                                                                         idDoc,
                                                                                         listeMetaSouhaitees,
                                                                                         bestIndex);

         final List<UntypedDocument> listeUDoc = paginatedUDoc.getDocuments();
         final boolean lastPage = paginatedUDoc.getLastPage();

         // Récupération de l'UUID du dernier document retourné
         UUID lastUuid = null;
         UntypedDocument lastDoc = null;
         if (listeUDoc.size() > 0) {
            lastDoc = listeUDoc.get(listeUDoc.size() - 1);
            lastUuid = lastDoc.getUuid();
         }

         response = createRechercheParIterateurResponse(listeUDoc,
                                                        isMetaVariableAjoute,
                                                        untypedRangeMeta.getLongCode());

         response.getRechercheParIterateurResponse().setDernierePage(lastPage);

         // Identifiant de la derniere page retournee par la recherche
         final IdentifiantPageType idPage = recupererIdPage(paginatedUDoc, lastUuid);

         // recupererIdPage(requetePrincipale, lastDoc, lastUuid);
         // rechParItRespType.setIdentifiantPageSuivante(idPage);
         response.getRechercheParIterateurResponse().setIdentifiantPageSuivante(idPage);

      }
      catch (final MetaDataUnauthorizedToSearchEx e) {
         throw new RechercheAxis2Fault("RechercheMetadonneesInterdite", e.getMessage(), e);
      }
      catch (final MetaDataUnauthorizedToConsultEx e) {
         throw new RechercheAxis2Fault("ConsultationMetadonneesInterdite", e.getMessage(), e);
      }
      catch (final UnknownLuceneMetadataEx e) {
         throw new RechercheAxis2Fault("RechercheMetadonneesInconnues", e.getMessage(), e);
      }
      catch (final SAESearchServiceEx e) {
         throw new RechercheAxis2Fault("ErreurInterneRecherche", e.getMessage(), e);
      }
      catch (final SyntaxLuceneEx e) {
         throw new RechercheAxis2Fault("SyntaxeLuceneNonValide", e.getMessage(), e);
      }
      catch (final UnknownDesiredMetadataEx e) {
         throw new RechercheAxis2Fault("ConsultationMetadonneesInconnues", e.getMessage(), e);
      }
      catch (final UnknownFiltresMetadataEx e) {
         throw new RechercheAxis2Fault("RechercheMetadonneesInconnues", e.getMessage(), e);
      }
      catch (final DoublonFiltresMetadataEx e) {
         throw new RechercheAxis2Fault("RechercheMetadonneesDoublons", e.getMessage(), e);
      }
      catch (final IndexCompositeException e) {
         throw new RechercheAxis2Fault("IndexCompositeInconnue", e.getMessage(), e);
      }
      LOG.debug("{} - Sortie", prefixeTrc);

      return response;

   }

   private IdentifiantPageType recupererIdPage(final PaginatedUntypedDocuments paginatedUDoc, final UUID lastUuid) {
      IdentifiantPageType idPage = null;
      if (lastUuid != null) {
         idPage = new IdentifiantPageType();
         final UuidType lastUuidType = new UuidType();
         lastUuidType.setUuidType(lastUuid.toString());
         idPage.setIdArchive(lastUuidType);

         final MetadonneeValeurType metaLastDoc = new MetadonneeValeurType();
         metaLastDoc.setMetadonneeValeurType(paginatedUDoc.getValeurMetaLastPage());
         idPage.setValeur(metaLastDoc);
      }
      return idPage;
   }

   /**
    * Récuperation de la liste des filtres de type "egal à" ou "contenu dans"
    * et conversion en liste de métadonnées
    * 
    * @param filtres
    * @return la liste des métadonnées
    */
   private List<AbstractMetadata> recupererFiltresEgalite(final FiltreType filtres) {
      final List<AbstractMetadata> listeAbstractMeta = new ArrayList<>();
      if (filtres != null) {
         if (filtres.getEqualFilter() != null) {
            final MetadonneeType[] listeEqualFilter = filtres.getEqualFilter().getMetadonnee();
            if (listeEqualFilter != null) {
               for (final MetadonneeType metadonneeType : listeEqualFilter) {
                  final UntypedMetadata abstractMeta = new UntypedMetadata(
                                                                           metadonneeType.getCode().getMetadonneeCodeType(),
                                                                           metadonneeType.getValeur().getMetadonneeValeurType());

                  listeAbstractMeta.add(abstractMeta);
               }
            }
         }

         if (filtres.getRangeFilter() != null) {
            final RangeMetadonneeType[] listeRangeFilter = filtres.getRangeFilter().getRangeMetadonnee();
            if (listeRangeFilter != null) {
               for (final RangeMetadonneeType rangeMetadonneeType : listeRangeFilter) {
                  final UntypedRangeMetadata abstractMeta = new UntypedRangeMetadata(
                                                                                     rangeMetadonneeType.getCode().getMetadonneeCodeType(),
                                                                                     rangeMetadonneeType.getValeurMin().getMetadonneeValeurType(),
                                                                                     rangeMetadonneeType.getValeurMax().getMetadonneeValeurType());

                  listeAbstractMeta.add(abstractMeta);
               }
            }
         }
      }
      return listeAbstractMeta;
   }

   /**
    * Récuperation de la liste des filtres de type "different de" ou "non
    * contenu dans" et conversion en liste de métadonnées
    * 
    * @param filtres
    * @return la liste des métadonnées
    */
   private List<AbstractMetadata> recupererFiltresDifferent(final FiltreType filtres) {
      final List<AbstractMetadata> listeAbstractMeta = new ArrayList<>();
      if (filtres != null) {

         if (filtres.getNotEqualFilter() != null) {
            final MetadonneeType[] listeNotEqualFilter = filtres.getNotEqualFilter().getMetadonnee();
            if (listeNotEqualFilter != null) {
               for (final MetadonneeType metadonneeType : listeNotEqualFilter) {
                  final UntypedMetadata abstractMeta = new UntypedMetadata(
                                                                           metadonneeType.getCode().getMetadonneeCodeType(),
                                                                           metadonneeType.getValeur().getMetadonneeValeurType());

                  listeAbstractMeta.add(abstractMeta);
               }
            }
         }

         if (filtres.getNotInRangeFilter() != null) {
            final RangeMetadonneeType[] listeNotInRangeFilter = filtres.getNotInRangeFilter().getRangeMetadonnee();
            if (listeNotInRangeFilter != null) {
               for (final RangeMetadonneeType rangeMetadonneeType : listeNotInRangeFilter) {
                  final UntypedRangeMetadata abstractMeta = new UntypedRangeMetadata(
                                                                                     rangeMetadonneeType.getCode().getMetadonneeCodeType(),
                                                                                     rangeMetadonneeType.getValeurMin().getMetadonneeValeurType(),
                                                                                     rangeMetadonneeType.getValeurMax().getMetadonneeValeurType());

                  listeAbstractMeta.add(abstractMeta);
               }
            }
         }

      }
      return listeAbstractMeta;
   }

   /**
    * Récupère la liste des métadonnées fixes
    * 
    * @param requetePrincipale
    * @return La liste des métadonnées
    */
   private List<UntypedMetadata> recupererFixedMetadatas(final RequetePrincipaleType requetePrincipale) {
      final ListeMetadonneeType fixedMeta = requetePrincipale.getFixedMetadatas();
      final List<UntypedMetadata> listeFixedMeta = new ArrayList<>();
      final MetadonneeType[] tabMetaType = fixedMeta.getMetadonnee();
      if (tabMetaType != null) {
         for (final MetadonneeType metadonneType : tabMetaType) {
            final UntypedMetadata untypedMeta = new UntypedMetadata(metadonneType.getCode().getMetadonneeCodeType(),
                                                                    metadonneType.getValeur().getMetadonneeValeurType());
            listeFixedMeta.add(untypedMeta);
         }
      }
      return listeFixedMeta;
   }

   /**
    * Récupère la métadonnée de type range
    * 
    * @param requetePrincipale
    * @return La métadonnée de type range
    */
   private UntypedRangeMetadata recupererRangeMetadata(final RequetePrincipaleType requetePrincipale) {
      final RangeMetadonneeType varyingMeta = requetePrincipale.getVaryingMetadata();
      final UntypedRangeMetadata untypedRangeMeta = new UntypedRangeMetadata(varyingMeta.getCode().getMetadonneeCodeType(),
                                                                             varyingMeta.getValeurMin().getMetadonneeValeurType(),
                                                                             varyingMeta.getValeurMax().getMetadonneeValeurType());
      return untypedRangeMeta;
   }

   /**
    * Verifier que la requête est non null et non vide
    * 
    * @throws RechercheAxis2Fault
    *            Une exception est levée lors de la recherche.
    */
   private void checkNotNull(final String requeteLucene) throws RechercheAxis2Fault {
      // Traces debug - entrée méthode
      final String prefixeTrc = "checkNotNull()";
      LOG.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode
      LOG.debug("{} - Début de la vérification : La requête de recherche est renseignée", prefixeTrc);
      if (isEmpty(requeteLucene.trim())) {
         throw new RechercheAxis2Fault("RequeteLuceneVideOuNull",
                                       wsMessageRessourcesUtils.recupererMessage("search.lucene.videornull", null));
      }
      LOG.debug("{} - Fin de la vérification : La requête de recherche est renseignée", prefixeTrc);
      LOG.debug("{} - Sortie", prefixeTrc);
   }

   /**
    * Récupérer la liste des codes des meta données souhaitées.
    */
   protected static List<String> recupererListMDDesired(final MetadonneeCodeType[] listeMDSearch) {

      return CollectionUtils.loadListNotNull(ObjectTypeFactory.buildMetaCodeFromWS(listeMDSearch));

   }

   /**
    * Récupérer la requête Lucene
    */
   private String recupererReqLucene(final Recherche request) {
      return request.getRecherche().getRequete().getRequeteRechercheType();
   }

   /**
    * Récupérer la requête Lucene
    */
   private String recupererReqLucene(final RechercheNbRes request) {
      return request.getRechercheNbRes().getRequete().getRequeteRechercheNbResType();
   }

   /**
    * Récupérer La liste métadonnées souhaitées.
    */
   private MetadonneeCodeType[] recupererListMDSearch(final Recherche request) {
      return request.getRecherche().getMetadonnees().getMetadonneeCode();
   }

   /**
    * Récupérer La liste métadonnées souhaitées.
    */
   private MetadonneeCodeType[] recupererListMDSearch(final RechercheNbRes request) {
      return request.getRechercheNbRes().getMetadonnees().getMetadonneeCode();
   }

   /**
    * Récupérer La liste métadonnées souhaitées.
    */
   private MetadonneeCodeType[] recupererListMDSearch(final RechercheParIterateur request) {
      return request.getRechercheParIterateur().getMetadonnees().getMetadonneeCode();
   }

   /**
    * @return Le provider pour les services de Capture, recherche et
    *         consultation.
    */
   public final SAEDocumentService getDocumentService() {
      return documentService;
   }

   /**
    * @param documentService
    *           : Le provider pour les services de Capture, recherche et
    *           consultation.
    */
   public final void setDocumentService(final SAEDocumentService documentService) {
      this.documentService = documentService;
   }

   /**
    * Renvoie l'indexComposite ou simple à utiliser pour la recherche
    * 
    * @return
    *         Liste des code courts des métadonnées composant l'index
    * @throws IndexCompositeException
    * @{@link IndexCompositeException}
    */
   private List<String> getBestIndex(final List<UntypedMetadata> listeFixedMeta, final UntypedRangeMetadata untypedRangeMeta)
         throws IndexCompositeException {

      // On souhaite trouver un index dont l'ensemble des métadonnées qui le composent sont valorisées à partir
      // des métadonnées fixes et variables de la requête
      final List<UntypedMetadata> listMetaDataFromRequest = new ArrayList<>();
      for (final UntypedMetadata meta : listeFixedMeta) {
         if (!meta.getValue().isEmpty()) {
            listMetaDataFromRequest.add(meta);
         }
      }
      final UntypedMetadata varyingMetadata = new UntypedMetadata(untypedRangeMeta.getLongCode(),
                                                                  untypedRangeMeta.getValeurMin());
      listMetaDataFromRequest.add(varyingMetadata);

      // Conversion des métadonnées en code court
      final List<String> shortCodeRequiredMetadatas = indexCompositeService.untypedMetadatasToShortCodeMetadatas(listMetaDataFromRequest);
      // On cherche le meilleur index à partit de ces métadonnées
      return indexCompositeService.getBestIndexForQuery(shortCodeRequiredMetadatas);

   }

}
