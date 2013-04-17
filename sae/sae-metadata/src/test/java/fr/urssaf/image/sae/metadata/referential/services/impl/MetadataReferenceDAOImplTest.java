package fr.urssaf.image.sae.metadata.referential.services.impl;

import java.io.IOException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.utils.Utils;

/**
 * Contient les tests sur les services de manipulation du referentiel des
 * métadonnées.
 * 
 * @author akenore
 */

public class MetadataReferenceDAOImplTest extends AbstractService {
	/**
	 * Permet de tester la récupération des métadonnées du référentiel.
	 * 
	 * @throws IOException
	 *             Exception levée lorsqu'il y'a un dysfonctionnement.
	 * @throws ReferentialException
	 *             Exception levée lorsqu'il y'a un dysfonctionnement.
	 */
	@Test
	public void getAllMetadataReferences() throws IOException,
			ReferentialException {
		Assert.assertTrue(getMetadataReferenceDAO().getAllMetadataReferences()
				.size() == 13);
	}

	/**
	 * Permet de tester la récupération des métadonnées consultables du
	 * référentiel.
	 * 
	 * @throws IOException
	 *             Exception levée lorsqu'il y'a un dysfonctionnement.
	 * @throws ReferentialException
	 *             Exception levée lorsqu'il y'a un dysfonctionnement.
	 */
	@Test
	public void getConsultableMetadataReferences() throws IOException,
			ReferentialException {
		Assert.assertTrue(getMetadataReferenceDAO()
				.getConsultableMetadataReferences().size() == 11);
		for (Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
				getMetadataReferenceDAO().getConsultableMetadataReferences())
				.entrySet()) {
			Assert.assertTrue(metaData.getValue().isConsultable());
		}
	}

	/**
	 * Permet de tester la récupération des métadonnées consultables par défaut
	 * du référentiel.
	 * 
	 * @throws IOException
	 *             Exception levée lorsqu'il y'a un dysfonctionnement.
	 * @throws ReferentialException
	 *             Exception levée lorsqu'il y'a un dysfonctionnement.
	 */
	@Test
	public void getDefaultConsultableMetadataReferences() throws IOException,
			ReferentialException {
		Assert.assertTrue(getMetadataReferenceDAO()
				.getDefaultConsultableMetadataReferences().size() == 2);
		
		for (Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
				getMetadataReferenceDAO()
				.getDefaultConsultableMetadataReferences())
				.entrySet()) {
			Assert.assertTrue(metaData.getValue().isDefaultConsultable());
		}
	}

	/**
	 * Permet de tester la récupération des métadonnées obligatoire pour
	 * l'archivage du référentiel.
	 * 
	 * @throws IOException
	 *             Exception levée lorsqu'il y'a un dysfonctionnement.
	 * @throws ReferentialException
	 *             Exception levée lorsqu'il y'a un dysfonctionnement.
	 */
	@Test
	public void getRequiredForArchivalMetadataReferences() throws IOException,
			ReferentialException {
		Assert.assertTrue(getMetadataReferenceDAO()
				.getRequiredForArchivalMetadataReferences().size() == 2);
		
		for (Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
				getMetadataReferenceDAO()
				.getRequiredForArchivalMetadataReferences())
				.entrySet()) {
			Assert.assertTrue(metaData.getValue().isRequiredForArchival());
		}
	}

	/**
	 * Permet de tester la récupération des métadonnées obligatoire pour le
	 * stockage du référentiel.
	 * 
	 * @throws IOException
	 *             Exception levée lorsqu'il y'a un dysfonctionnement.
	 * @throws ReferentialException
	 *             Exception levée lorsqu'il y'a un dysfonctionnement.
	 */
	@Test
	public void getRequiredForStorageMetadataReferences() throws IOException,
			ReferentialException {
		Assert.assertTrue(getMetadataReferenceDAO()
				.getRequiredForStorageMetadataReferences().size() == 5);
		
		for (Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
				getMetadataReferenceDAO()
				.getRequiredForStorageMetadataReferences())
				.entrySet()) {
			Assert.assertTrue(metaData.getValue().isRequiredForStorage());
		}
	}

	
	/**
	 * Permet de tester la récupération des métadonnées autorisée à la
	 * rechercher du référentiel.
	 * 
	 * @throws IOException
	 *             Exception levée lorsqu'il y'a un dysfonctionnement.
	 * @throws ReferentialException
	 *             Exception levée lorsqu'il y'a un dysfonctionnement.
	 */
	@Test
	public void getSearchableMetadataReferences() throws IOException,
			ReferentialException {
		Assert.assertTrue(getMetadataReferenceDAO()
				.getSearchableMetadataReferences().size() == 8);
		for (Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
				getMetadataReferenceDAO()
				.getSearchableMetadataReferences())
				.entrySet()) {
			Assert.assertTrue(metaData.getValue().isSearchable());
		}
	}

	/**
	 * Permet de tester la récupération des métadonnées archivables du
	 * référentiel.
	 * 
	 * @throws IOException
	 *             Exception levée lorsqu'il y'a un dysfonctionnement.
	 * @throws ReferentialException
	 *             Exception levée lorsqu'il y'a un dysfonctionnement.
	 */
	@Test
	public void getArchivableMetadataReferences() throws IOException,
			ReferentialException {
		Assert.assertTrue(getMetadataReferenceDAO()
				.getArchivableMetadataReferences().size() == 8);
	}



	/**
	 * Permet de tester la récupération d'une métadonnées du référentiel à
	 * partir du code long.
	 * 
	 * @throws IOException
	 *             Exception levée lorsqu'il y'a un dysfonctionnement.
	 * @throws ReferentialException
	 *             Exception levée lorsqu'il y'a un dysfonctionnement.
	 */
	@Test
	public void getByLongCode() throws IOException, ReferentialException {
		Assert.assertTrue(getMetadataReferenceDAO().getByLongCode("CodeRND") != null);
		Assert.assertTrue(getMetadataReferenceDAO().getByLongCode("CodeRND")
				.getLongCode().equalsIgnoreCase("CodeRND"));
	}

	/**
	 * Permet de tester la récupération d'une métadonnées du référentiel à
	 * partir du code court.
	 * 
	 * @throws IOException
	 *             Exception levée lorsqu'il y'a un dysfonctionnement.
	 * @throws ReferentialException
	 *             Exception levée lorsqu'il y'a un dysfonctionnement.
	 */
	@Test
	public void getByShortCode() throws IOException, ReferentialException {
		Assert.assertTrue(getMetadataReferenceDAO().getByShortCode("SM_DOCUMENT_TYPE") != null);
		Assert.assertTrue(getMetadataReferenceDAO().getByShortCode("SM_DOCUMENT_TYPE")
				.getShortCode().equalsIgnoreCase("SM_DOCUMENT_TYPE"));
	}

}
