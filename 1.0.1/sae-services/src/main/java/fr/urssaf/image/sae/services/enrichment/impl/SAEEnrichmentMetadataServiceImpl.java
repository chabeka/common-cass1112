package fr.urssaf.image.sae.services.enrichment.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.services.enrichment.SAEEnrichmentMetadataService;
import fr.urssaf.image.sae.services.enrichment.dao.RNDReferenceDAO;
import fr.urssaf.image.sae.services.enrichment.dao.impl.SAEMetatadaFinderUtils;
import fr.urssaf.image.sae.services.enrichment.xml.model.SAEArchivalMetadatas;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.SAEEnrichmentEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;

/**
 * Classe concrète pour l’enrichissement des métadonnées.
 * 
 * @author rhofir.
 */
@Service
@Qualifier("saeEnrichmentMetadataService")
@SuppressWarnings({ "PMD.CyclomaticComplexity", "PMD.LongVariable" })
public class SAEEnrichmentMetadataServiceImpl implements
		SAEEnrichmentMetadataService {
	@Autowired
	@Qualifier("rndReferenceDAO")
	private RNDReferenceDAO rndReferenceDAO;
	@Autowired
	@Qualifier("metadataReferenceDAO")
	private MetadataReferenceDAO metadataReferenceDAO;

	/**
	 * @return Le service RND reference.
	 */
	public final RNDReferenceDAO getRndReferenceDAO() {
		return rndReferenceDAO;
	}

	/**
	 * @param rndReferenceDAO
	 *            : Le service RND reference.
	 */
	public final void setRndReferenceDAO(RNDReferenceDAO rndReferenceDAO) {
		this.rndReferenceDAO = rndReferenceDAO;
	}

	/**
	 * @return Le service metadataReferenceDAO.
	 */
	public final MetadataReferenceDAO getMetadataReferenceDAO() {
		return metadataReferenceDAO;
	}

	/**
	 * @param metadataReferenceDAO
	 *            : Le service metadataReferenceDAO.
	 */
	public final void setMetadataReferenceDAO(
			MetadataReferenceDAO metadataReferenceDAO) {
		this.metadataReferenceDAO = metadataReferenceDAO;
	}

	@Override
	public final void enrichmentMetadata(SAEDocument saeDoc)
			throws SAEEnrichmentEx, ReferentialRndException, UnknownCodeRndEx {
		List<SAEMetadata> saeMetadatas = saeDoc.getMetadatas();

		String rndValue = SAEMetatadaFinderUtils.codeMetadataFinder(
				saeMetadatas, SAEArchivalMetadatas.CODE_RND.getLongCode());
		try {
			if (!StringUtils.isEmpty(rndValue)) {
				completedMetadatas(saeDoc, rndValue);
			}
		} catch (ReferentialRndException e) {
			throw new ReferentialRndException(e.getMessage(), e);
		} catch (UnknownCodeRndEx e) {
			throw new UnknownCodeRndEx(e.getMessage(), e);
		} catch (ParseException e) {
			throw new SAEEnrichmentEx(e.getMessage(), e);
		} catch (ReferentialException e) {
			throw new SAEEnrichmentEx(e.getMessage(), e);
		}
	}

	/**
	 * Permet de compléter les métadonnées non specifiable qui sont :
	 * <ul>
	 * <li>VersionRND</li>
	 * <li>CodeFonction</li>
	 * <li>CodeActivite</li>
	 * <li>DateDebutConservation</li>
	 * <li>DateFinConservation</li>
	 * <li>NomFichier</li>
	 * <li>DocumentVirtuel</li>
	 * <li>ContratDeService</li>
	 * <li>DateArchivage</li>
	 * </ul>
	 * 
	 * 
	 * @param document
	 *            : le document retourné par DFCE.
	 * @param metadata
	 *            : La métadonnée désirés.
	 * @throws UnknownCodeRndEx
	 * @throws ReferentialRndException
	 * @throws ParseException
	 * @throws ReferentialException
	 */
	// CHECKSTYLE:OFF
	@SuppressWarnings({ "PMD.AvoidInstantiatingObjectsInLoops",
			"PMD.ExcessiveMethodLength", "PMD.CollapsibleIfStatements" })
	private void completedMetadatas(SAEDocument saeDocument, String rndCode)
			throws ReferentialRndException, UnknownCodeRndEx, ParseException,
			ReferentialException {
		SAEMetadata saeMetadata = null;
		for (SAEArchivalMetadatas metadata : SAEArchivalMetadatas.values()) {
			saeMetadata = new SAEMetadata();
			saeMetadata.setLongCode(metadata.getLongCode());
			metadata = SAEMetatadaFinderUtils.metadtaFinder(metadata
					.getLongCode());
			if (metadata.getLongCode().equals(
					SAEArchivalMetadatas.CODE_ACTIVITE.getLongCode())) {
				saeMetadata.setShortCode(metadataReferenceDAO.getByLongCode(
						SAEArchivalMetadatas.CODE_ACTIVITE.getLongCode())
						.getShortCode());
				saeMetadata.setValue(rndReferenceDAO
						.getActivityCodeByRnd(rndCode));
				saeDocument.getMetadatas().add(saeMetadata);
			} else if (metadata.getLongCode().equals(
					SAEArchivalMetadatas.CODE_FONCTION.getLongCode())) {
				saeMetadata.setShortCode(metadataReferenceDAO.getByLongCode(
						SAEArchivalMetadatas.CODE_FONCTION.getLongCode())
						.getShortCode());
				saeMetadata.setValue(rndReferenceDAO
						.getFonctionCodeByRnd(rndCode));
				saeDocument.getMetadatas().add(saeMetadata);
			} else if (metadata.getLongCode().equals(
					SAEArchivalMetadatas.DATE_FIN_CONSERVATION.getLongCode())) {
				if (SAEMetatadaFinderUtils.dateMetadataFinder(saeDocument
						.getMetadatas(),
						SAEArchivalMetadatas.DATE_DEBUT_CONSERVATION
								.getLongCode()) == null) {
					
					saeMetadata.setValue(DateUtils.addDays(new Date(),
							rndReferenceDAO.getStorageDurationByRnd(rndCode)));
				} else {
					saeMetadata
							.setValue(DateUtils.addDays(
									SAEMetatadaFinderUtils.dateMetadataFinder(
											saeDocument.getMetadatas(),
											SAEArchivalMetadatas.DATE_DEBUT_CONSERVATION
													.getLongCode()),
									rndReferenceDAO
											.getStorageDurationByRnd(rndCode)));
				}
				saeMetadata.setShortCode(metadataReferenceDAO
						.getByLongCode(
								SAEArchivalMetadatas.DATE_FIN_CONSERVATION
										.getLongCode()).getShortCode());
				saeDocument.getMetadatas().add(saeMetadata);
			} else if (metadata.getLongCode().equals(
					SAEArchivalMetadatas.NOM_FICHIER.getLongCode())) {
				saeMetadata.setShortCode(metadataReferenceDAO.getByLongCode(
						SAEArchivalMetadatas.NOM_FICHIER.getLongCode())
						.getShortCode());
				saeMetadata.setValue(FilenameUtils.getName(FilenameUtils
						.separatorsToSystem(saeDocument.getFilePath())));
				saeDocument.getMetadatas().add(saeMetadata);
			} else if (metadata.getLongCode().equals(
					SAEArchivalMetadatas.DATE_ARCHIVAGE.getLongCode())) {
				saeMetadata.setShortCode(metadataReferenceDAO.getByLongCode(
						SAEArchivalMetadatas.DATE_ARCHIVAGE.getLongCode())
						.getShortCode());
				saeMetadata.setValue(new Date());
				saeDocument.getMetadatas().add(saeMetadata);
			} else if (metadata.getLongCode().equals(
					SAEArchivalMetadatas.DATE_DEBUT_CONSERVATION.getLongCode())) {
				if (SAEMetatadaFinderUtils.dateMetadataFinder(saeDocument
						.getMetadatas(),
						SAEArchivalMetadatas.DATE_DEBUT_CONSERVATION
								.getLongCode()) == null) {
					saeMetadata
							.setShortCode(metadataReferenceDAO
									.getByLongCode(
											SAEArchivalMetadatas.DATE_DEBUT_CONSERVATION
													.getLongCode())
									.getShortCode());
					// La date DATEDEBUTCONSERVATION est égale à la date
					// d'archivage.
					saeMetadata.setValue(new Date());
					saeDocument.getMetadatas().add(saeMetadata);
				}
			} else if (metadata.getLongCode().equals(
					SAEArchivalMetadatas.DOCUMENT_VIRTUEL.getLongCode())) {
				saeMetadata.setShortCode(metadataReferenceDAO.getByLongCode(
						SAEArchivalMetadatas.DOCUMENT_VIRTUEL.getLongCode())
						.getShortCode());
				saeMetadata.setValue(false);
				saeDocument.getMetadatas().add(saeMetadata);
			} else if (metadata.getLongCode().equals(
					SAEArchivalMetadatas.CONTRAT_DE_SERVICE.getLongCode())) {
				saeMetadata.setShortCode(metadataReferenceDAO.getByLongCode(
						SAEArchivalMetadatas.CONTRAT_DE_SERVICE.getLongCode())
						.getShortCode());
				// FIXME attente de spécification.
				saeMetadata.setValue("ATT_PROD_001");
				saeDocument.getMetadatas().add(saeMetadata);
			} else if (metadata.getLongCode().equals(
					SAEArchivalMetadatas.VERSION_RND.getLongCode())) {
				if (SAEMetatadaFinderUtils.codeMetadataFinder(
						saeDocument.getMetadatas(),
						SAEArchivalMetadatas.VERSION_RND.getLongCode()) == null) {
					saeMetadata.setShortCode(metadataReferenceDAO
							.getByLongCode(
									SAEArchivalMetadatas.VERSION_RND
											.getLongCode()).getShortCode());
					saeMetadata.setValue(rndReferenceDAO.getTypeDocument(
							rndCode).getVersionRnd());
					saeDocument.getMetadatas().add(saeMetadata);
				}
			}
		}
	}
	// CHECKSTYLE:ON
}
