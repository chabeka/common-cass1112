package fr.urssaf.image.sae.webservices.service.impl;

import static fr.urssaf.image.sae.webservices.service.factory.ObjectRechercheFactory.createRechercheNbResResponse;
import static fr.urssaf.image.sae.webservices.service.factory.ObjectRechercheFactory.createRechercheParIterateurResponse;
import static fr.urssaf.image.sae.webservices.service.factory.ObjectRechercheFactory.createRechercheResponse;
import static org.apache.commons.lang.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.Arrays;
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
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.PaginatedUntypedDocuments;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedRangeMetadata;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.SaeIndexComposite;
import fr.urssaf.image.sae.service.indexComposite.IndexCompositeService;
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
	 * 
	 */
	@Override
	public final RechercheResponse search(Recherche request) throws RechercheAxis2Fault {
		// Traces debug - entrée méthode
		String prefixeTrc = "search()";
		LOG.debug("{} - Début", prefixeTrc);
		// Fin des traces debug - entrée méthode

		int maxResult = RechercheConstantes.NB_MAX_RESULTATS_RECHERCHE;
		LOG.debug("{} - Le nombre maximum de documents à renvoyer dans les résultats de "
				+ "recherche au niveau de la couche webservice est {}", prefixeTrc, maxResult);
		boolean resultatTronque = false;
		RechercheResponse response;
		String requeteLucene = VIDE;
		try {
			requeteLucene = recupererReqLucene(request);
			checkNotNull(requeteLucene);
			List<String> listMDDesired = recupererListMDDesired(recupererListMDSearch(request));
			List<UntypedDocument> untypedDocuments = documentService.search(requeteLucene, listMDDesired);
			if (untypedDocuments.size() > maxResult) {
				resultatTronque = true;
				LOG.debug("{} - Les résultats de recherche sont tronqués à {} résultats", prefixeTrc, maxResult);
			}

			response = createRechercheResponse(untypedDocuments, resultatTronque);

		} catch (SAESearchServiceEx except) {
			throw new RechercheAxis2Fault("ErreurInterneRecherche", except.getMessage(), except);
		} catch (MetaDataUnauthorizedToSearchEx except) {
			throw new RechercheAxis2Fault("RechercheMetadonneesInterdite", except.getMessage(), except);
		} catch (MetaDataUnauthorizedToConsultEx except) {
			throw new RechercheAxis2Fault("ConsultationMetadonneesInterdite", except.getMessage(), except);
		} catch (UnknownDesiredMetadataEx except) {
			throw new RechercheAxis2Fault("ConsultationMetadonneesInconnues", except.getMessage(), except);
		} catch (UnknownLuceneMetadataEx except) {
			throw new RechercheAxis2Fault("RechercheMetadonneesInconnues", except.getMessage(), except);
		} catch (SyntaxLuceneEx except) {
			throw new RechercheAxis2Fault("SyntaxeLuceneNonValide", except.getMessage(), except);
		} catch (RechercheAxis2Fault except) {
			throw new RechercheAxis2Fault("RequeteLuceneVideOuNull", except.getMessage(), except);
		}
		LOG.debug("{} - Sortie", prefixeTrc);
		return response;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public RechercheNbResResponse searchWithNbRes(RechercheNbRes request) throws RechercheAxis2Fault {

		// -- Traces debug - entrée méthode
		String prefixeTrc = "searchWithNbRes()";
		LOG.debug("{} - Début", prefixeTrc);

		int maxResult = RechercheConstantes.NB_MAX_RESULTATS_RECHERCHE;
		int maxResultRechDfce = RechercheConstantes.NB_MAX_RESULTATS_RECH_DFCE;
		LOG.debug("{} - Le nombre maximum de documents à renvoyer dans les résultats de "
				+ "recherche au niveau de la couche webservice est {}", prefixeTrc, maxResult);
		boolean resultatTronque = false;
		RechercheNbResResponse response;
		String requeteLucene = VIDE;
		try {
			requeteLucene = recupererReqLucene(request);
			checkNotNull(requeteLucene);
			List<String> listMDDesired = recupererListMDDesired(recupererListMDSearch(request));
			List<UntypedDocument> untypedDocuments = documentService.search(requeteLucene, listMDDesired,
					maxResultRechDfce);
			if (untypedDocuments.size() > maxResult) {
				resultatTronque = true;
				String mssg = "{} - Les résultats de recherche sont tronqués à {} résultats";
				LOG.debug(mssg, prefixeTrc, maxResult);
			}
			response = createRechercheNbResResponse(untypedDocuments, resultatTronque, maxResult);

		} catch (SAESearchServiceEx except) {
			throw new RechercheAxis2Fault("ErreurInterneRecherche", except.getMessage(), except);
		} catch (MetaDataUnauthorizedToSearchEx except) {
			throw new RechercheAxis2Fault("RechercheMetadonneesInterdite", except.getMessage(), except);
		} catch (MetaDataUnauthorizedToConsultEx except) {
			throw new RechercheAxis2Fault("ConsultationMetadonneesInterdite", except.getMessage(), except);
		} catch (UnknownDesiredMetadataEx except) {
			throw new RechercheAxis2Fault("ConsultationMetadonneesInconnues", except.getMessage(), except);
		} catch (UnknownLuceneMetadataEx except) {
			throw new RechercheAxis2Fault("RechercheMetadonneesInconnues", except.getMessage(), except);
		} catch (SyntaxLuceneEx except) {
			throw new RechercheAxis2Fault("SyntaxeLuceneNonValide", except.getMessage(), except);
		} catch (RechercheAxis2Fault except) {
			throw new RechercheAxis2Fault("RequeteLuceneVideOuNull", except.getMessage(), except);
		}
		LOG.debug("{} - Sortie", prefixeTrc);
		return response;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public RechercheParIterateurResponse rechercheParIterateur(RechercheParIterateur request)
			throws RechercheAxis2Fault {

		// Traces debug - entrée méthode
		String prefixeTrc = "rechercheParIterateur()";		
		LOG.debug("{} - Début", prefixeTrc);
		// Fin des traces debug - entrée méthode
		
		long debut = System.currentTimeMillis();

		RechercheParIterateurResponse response;

		RechercheParIterateurRequestType params = request.getRechercheParIterateur();

		// Récupération de la requête principale à partir des paramètres
		// d'entrée
		RequetePrincipaleType requetePrincipale = params.getRequetePrincipale();
		// Récupération de l'identifiant de la page à partir des paramètres
		// d'entrée
		IdentifiantPageType identifiantPage = params.getIdentifiantPage();

		// Métadonnées fixes
		List<UntypedMetadata> listeFixedMeta = recupererFixedMetadatas(requetePrincipale);

		// Métadonnée variable
		UntypedRangeMetadata untypedRangeMeta = recupererRangeMetadata(requetePrincipale);

		// Si l'identifiant de la page est renseigné (lorsque ce n'est pas le
		// premier appel)
		// on met à jour la valeur min de la métadonnée de type range avec la
		// valeur de l'identifiant
		UUID idDoc = null;
		if (identifiantPage != null) {
			untypedRangeMeta.setValeurMin(identifiantPage.getValeur().getMetadonneeValeurType());
			idDoc = UUID.fromString(identifiantPage.getIdArchive().getUuidType());
		}

		FiltreType filtres = params.getFiltres();
		// Filtres de type "égal à" ou "contenu dans" (pour les range)
		List<AbstractMetadata> listeFiltreEgalite = recupererFiltresEgalite(filtres);
		// Filtres de type "différent" ou "non contenu dans" (pour les range)
		List<AbstractMetadata> listeFiltreDifferent = recupererFiltresDifferent(filtres);

		// Nombre de documents par page
		int nbDocsParPage = params.getNbDocumentsParPage();

		// Liste des métadonnées souhaitées en retour de recherche
		List<String> listeMetaSouhaitees = recupererListMDDesired(recupererListMDSearch(request));

		// On ajoute la métadonnée variable dans la liste des méta désirés, car
		// on a forcément besoin de la récupérer pour pouvoir mettre la valeur dans
		// l'identifiant de la dernière page
		boolean isMetaVariableAjoute = false;
		if (!listeMetaSouhaitees.contains(untypedRangeMeta.getLongCode())) {
			if (listeMetaSouhaitees.isEmpty()) {
				listeMetaSouhaitees = new ArrayList<String>();
				listeMetaSouhaitees.add(untypedRangeMeta.getLongCode());
				isMetaVariableAjoute = true;
			} else {
				listeMetaSouhaitees.add(untypedRangeMeta.getLongCode());
				isMetaVariableAjoute = true;
			}
		}

		try {

			long debutCalculIndexC = System.currentTimeMillis();
			
			// Identification de l'indexComposite à utiliser pour la recherche
			List<SaeIndexComposite> listIndexComposites = indexCompositeService.getAllComputedIndexComposite();

			List<String> indexOrderPreferenceList = null;
			SaeIndexComposite indexCompositeCandidat = null;

			// 1- Récupérer les metadatas critères à partir de la requête
			if (listIndexComposites != null) {
				
				List<SaeIndexComposite> indexCompositeCandidats = new ArrayList<SaeIndexComposite>();
				List<SAEMetadata> listSaeMetadatasFromRequest = null;

				// Sert strictement que pour identifier l'indexComposite à
				// utiliser pour la recherche
				UntypedMetadata varyingMetadata = new UntypedMetadata(untypedRangeMeta.getLongCode(),
						untypedRangeMeta.getValeurMin());

				List<UntypedMetadata> listMetaDataFromRequest = new ArrayList<UntypedMetadata>();
				listMetaDataFromRequest.addAll(listeFixedMeta);
				listMetaDataFromRequest.add(varyingMetadata);

				// Maping des métadonnées
				if (!listMetaDataFromRequest.isEmpty()) {
					listSaeMetadatasFromRequest = indexCompositeService.untypedMetadatasToSaeMetadatas(listMetaDataFromRequest);
				}

				// Recuperer la liste des indexComposite candidats
				for (SaeIndexComposite saeIndexComposite : listIndexComposites) {
					if (indexCompositeService.isIndexCompositeValid(saeIndexComposite, listSaeMetadatasFromRequest)) {
						indexCompositeCandidats.add(saeIndexComposite);
					}
				}

				// Si un ou plusieurs indexComposites identifies
				if (!indexCompositeCandidats.isEmpty()) {
					if (indexCompositeCandidats.size() == 1) {
						indexCompositeCandidat = indexCompositeCandidats.get(0);
					} else if (indexCompositeCandidats.size() > 1) {
						// recuperer l'indexComposite le plus pertinent ou celui
						// qui contient le max de criteres
						indexCompositeCandidat = indexCompositeService.getBestIndexComposite(indexCompositeCandidats);
					}
					indexOrderPreferenceList = new ArrayList<String>(
							Arrays.asList(indexCompositeCandidat.getName().split("&")));
				}
				// - Sinon, si aucun indexComposite identifié, on utilise un index simple
				else if (indexCompositeCandidats.isEmpty()) {
					for (UntypedMetadata metadata : listMetaDataFromRequest) {
						if (indexCompositeService.isIndexedMetadata(metadata)) {
							int indexItem = listMetaDataFromRequest.indexOf(metadata);
							indexOrderPreferenceList = new ArrayList<String>();
							indexOrderPreferenceList.add(listSaeMetadatasFromRequest.get(indexItem).getShortCode());
							break;
						}
					}
				}
			}
			
			long finCalculIndexC = System.currentTimeMillis();
			
			// Lancement de la recherche paginée
			PaginatedUntypedDocuments paginatedUDoc = documentService.searchPaginated(listeFixedMeta, untypedRangeMeta,
					listeFiltreEgalite, listeFiltreDifferent, nbDocsParPage, idDoc, listeMetaSouhaitees,
					indexOrderPreferenceList);
			
			List<UntypedDocument> listeUDoc = paginatedUDoc.getDocuments();
			boolean lastPage = paginatedUDoc.getLastPage();

			// Récupération de l'UUID du dernier document retourné
			UUID lastUuid = null;
			UntypedDocument lastDoc = null;
			if (listeUDoc.size() > 0) {
				lastDoc = listeUDoc.get(listeUDoc.size() - 1);
				lastUuid = lastDoc.getUuid();
			}

			response = createRechercheParIterateurResponse(listeUDoc, isMetaVariableAjoute,
					untypedRangeMeta.getLongCode());


			response.getRechercheParIterateurResponse().setDernierePage(lastPage);

			// Identifiant de la derniere page retournee par la recherche
			IdentifiantPageType idPage = recupererIdPage(paginatedUDoc, lastUuid);

			// recupererIdPage(requetePrincipale, lastDoc, lastUuid);
			// rechParItRespType.setIdentifiantPageSuivante(idPage);
			response.getRechercheParIterateurResponse().setIdentifiantPageSuivante(idPage);
			
//			System.out.println("Temps de Recherche en MS: "+ (System.currentTimeMillis()-debut));
//			System.out.println("Temps de calcul de l'index en MS: "+ (finCalculIndexC - debutCalculIndexC));
//			System.out.println("IndexOrderPreferenceList: "+indexOrderPreferenceList.toString());

		} catch (MetaDataUnauthorizedToSearchEx e) {
			throw new RechercheAxis2Fault("RechercheMetadonneesInterdite", e.getMessage(), e);
		} catch (MetaDataUnauthorizedToConsultEx e) {
			throw new RechercheAxis2Fault("ConsultationMetadonneesInterdite", e.getMessage(), e);
		} catch (UnknownLuceneMetadataEx e) {
			throw new RechercheAxis2Fault("RechercheMetadonneesInconnues", e.getMessage(), e);
		} catch (SAESearchServiceEx e) {
			throw new RechercheAxis2Fault("ErreurInterneRecherche", e.getMessage(), e);
		} catch (SyntaxLuceneEx e) {
			throw new RechercheAxis2Fault("SyntaxeLuceneNonValide", e.getMessage(), e);
		} catch (UnknownDesiredMetadataEx e) {
			throw new RechercheAxis2Fault("ConsultationMetadonneesInconnues", e.getMessage(), e);
		} catch (UnknownFiltresMetadataEx e) {
			throw new RechercheAxis2Fault("RechercheMetadonneesInconnues", e.getMessage(), e);
		} catch (DoublonFiltresMetadataEx e) {
			throw new RechercheAxis2Fault("RechercheMetadonneesDoublons", e.getMessage(), e);
		} catch (InvalidSAETypeException e) {
			throw new RechercheAxis2Fault("InvalidSAETypeException", e.getMessage(), e);
		} catch (MappingFromReferentialException e) {
			throw new RechercheAxis2Fault("MappingFromReferentialException", e.getMessage(), e);
		}
		LOG.debug("{} - Sortie", prefixeTrc);
		
		
		return response;

	}

	private IdentifiantPageType recupererIdPage(PaginatedUntypedDocuments paginatedUDoc, UUID lastUuid) {
		IdentifiantPageType idPage = null;
		if (lastUuid != null) {
			idPage = new IdentifiantPageType();
			UuidType lastUuidType = new UuidType();
			lastUuidType.setUuidType(lastUuid.toString());
			idPage.setIdArchive(lastUuidType);

			MetadonneeValeurType metaLastDoc = new MetadonneeValeurType();
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
	private List<AbstractMetadata> recupererFiltresEgalite(FiltreType filtres) {
		List<AbstractMetadata> listeAbstractMeta = new ArrayList<AbstractMetadata>();
		if (filtres != null) {
			if (filtres.getEqualFilter() != null) {
				MetadonneeType[] listeEqualFilter = filtres.getEqualFilter().getMetadonnee();
				if (listeEqualFilter != null) {
					for (MetadonneeType metadonneeType : listeEqualFilter) {
						UntypedMetadata abstractMeta = new UntypedMetadata(
								metadonneeType.getCode().getMetadonneeCodeType(),
								metadonneeType.getValeur().getMetadonneeValeurType());

						listeAbstractMeta.add(abstractMeta);
					}
				}
			}

			if (filtres.getRangeFilter() != null) {
				RangeMetadonneeType[] listeRangeFilter = filtres.getRangeFilter().getRangeMetadonnee();
				if (listeRangeFilter != null) {
					for (RangeMetadonneeType rangeMetadonneeType : listeRangeFilter) {
						UntypedRangeMetadata abstractMeta = new UntypedRangeMetadata(
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
	private List<AbstractMetadata> recupererFiltresDifferent(FiltreType filtres) {
		List<AbstractMetadata> listeAbstractMeta = new ArrayList<AbstractMetadata>();
		if (filtres != null) {

			if (filtres.getNotEqualFilter() != null) {
				MetadonneeType[] listeNotEqualFilter = filtres.getNotEqualFilter().getMetadonnee();
				if (listeNotEqualFilter != null) {
					for (MetadonneeType metadonneeType : listeNotEqualFilter) {
						UntypedMetadata abstractMeta = new UntypedMetadata(
								metadonneeType.getCode().getMetadonneeCodeType(),
								metadonneeType.getValeur().getMetadonneeValeurType());

						listeAbstractMeta.add(abstractMeta);
					}
				}
			}

			if (filtres.getNotInRangeFilter() != null) {
				RangeMetadonneeType[] listeNotInRangeFilter = filtres.getNotInRangeFilter().getRangeMetadonnee();
				if (listeNotInRangeFilter != null) {
					for (RangeMetadonneeType rangeMetadonneeType : listeNotInRangeFilter) {
						UntypedRangeMetadata abstractMeta = new UntypedRangeMetadata(
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
	private List<UntypedMetadata> recupererFixedMetadatas(RequetePrincipaleType requetePrincipale) {
		ListeMetadonneeType fixedMeta = requetePrincipale.getFixedMetadatas();
		List<UntypedMetadata> listeFixedMeta = new ArrayList<UntypedMetadata>();
		MetadonneeType[] tabMetaType = fixedMeta.getMetadonnee();
		if (tabMetaType != null) {
			for (MetadonneeType metadonneType : tabMetaType) {
				UntypedMetadata untypedMeta = new UntypedMetadata(metadonneType.getCode().getMetadonneeCodeType(),
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
	private UntypedRangeMetadata recupererRangeMetadata(RequetePrincipaleType requetePrincipale) {
		RangeMetadonneeType varyingMeta = requetePrincipale.getVaryingMetadata();
		UntypedRangeMetadata untypedRangeMeta = new UntypedRangeMetadata(varyingMeta.getCode().getMetadonneeCodeType(),
				varyingMeta.getValeurMin().getMetadonneeValeurType(),
				varyingMeta.getValeurMax().getMetadonneeValeurType());
		return untypedRangeMeta;
	}

	/**
	 * Verifier que la requête est non null et non vide
	 * 
	 * @throws RechercheAxis2Fault
	 *             Une exception est levée lors de la recherche.
	 */
	private void checkNotNull(String requeteLucene) throws RechercheAxis2Fault {
		// Traces debug - entrée méthode
		String prefixeTrc = "checkNotNull()";
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
	protected static List<String> recupererListMDDesired(MetadonneeCodeType[] listeMDSearch) {

		return CollectionUtils.loadListNotNull(ObjectTypeFactory.buildMetaCodeFromWS(listeMDSearch));

	}

	/**
	 * Récupérer la requête Lucene
	 */
	private String recupererReqLucene(Recherche request) {
		return request.getRecherche().getRequete().getRequeteRechercheType();
	}

	/**
	 * Récupérer la requête Lucene
	 */
	private String recupererReqLucene(RechercheNbRes request) {
		return request.getRechercheNbRes().getRequete().getRequeteRechercheNbResType();
	}

	/**
	 * Récupérer La liste métadonnées souhaitées.
	 */
	private MetadonneeCodeType[] recupererListMDSearch(Recherche request) {
		return request.getRecherche().getMetadonnees().getMetadonneeCode();
	}

	/**
	 * Récupérer La liste métadonnées souhaitées.
	 */
	private MetadonneeCodeType[] recupererListMDSearch(RechercheNbRes request) {
		return request.getRechercheNbRes().getMetadonnees().getMetadonneeCode();
	}

	/**
	 * Récupérer La liste métadonnées souhaitées.
	 */
	private MetadonneeCodeType[] recupererListMDSearch(RechercheParIterateur request) {
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
	 *            : Le provider pour les services de Capture, recherche et
	 *            consultation.
	 */
	public final void setDocumentService(SAEDocumentService documentService) {
		this.documentService = documentService;
	}

}
