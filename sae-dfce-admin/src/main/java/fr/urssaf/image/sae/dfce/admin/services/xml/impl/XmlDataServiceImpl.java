package fr.urssaf.image.sae.dfce.admin.services.xml.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;

import fr.urssaf.image.sae.dfce.admin.model.Applications;
import fr.urssaf.image.sae.dfce.admin.model.Codes;
import fr.urssaf.image.sae.dfce.admin.model.Contrats;
import fr.urssaf.image.sae.dfce.admin.model.DataBaseModel;
import fr.urssaf.image.sae.dfce.admin.model.LifeCycleRule;
import fr.urssaf.image.sae.dfce.admin.model.Objects;
import fr.urssaf.image.sae.dfce.admin.model.Organismes;
import fr.urssaf.image.sae.dfce.admin.services.xml.XmlDataService;
import fr.urssaf.image.sae.dfce.admin.utils.XStreamHelper;

/**
 * Fournit des méthodes de désérialisation
 * 
 * @author akenore
 * 
 */
@Service
@Qualifier("xmlDataService")
public class XmlDataServiceImpl implements XmlDataService {
	/** encoding de lecture **/
	private static final Charset ENCODING = Charset.forName("UTF-8");

	/**
	 * {@inheritDoc}
	 * 
	 * @throws FileNotFoundException
	 *             Lorsque le fichier n'existe pas
	 */
	public final DataBaseModel baseModelReader(final File xmlFile)
			throws FileNotFoundException {
		return XStreamHelper.parse(xmlFile, ENCODING, DataBaseModel.class,
				buildReadingXStream(DataBaseModel.class));
	}

	/**
	 * Construit le composant XStream utilisé en lecture.
	 * 
	 * @param xstrClass
	 *            : La classe à désérialiser
	 * @return le composant xstream
	 */
	private   XStream buildReadingXStream( final Class<?> xstrClass) {

		return XStreamHelper.newXStream(xstrClass);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws FileNotFoundException
	 *             Lorsque le fichier n'existe pas
	 */
	public final Organismes organismesReader(final File xmlFile)
			throws FileNotFoundException {
		return XStreamHelper.parse(xmlFile, ENCODING, Organismes.class,
				buildReadingXStream(Organismes.class));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws FileNotFoundException
	 *             Lorsque le fichier n'existe pas
	 */
	public final Contrats contratReader(final File xmlFile)
			throws FileNotFoundException {
		return XStreamHelper.parse(xmlFile, ENCODING, Contrats.class,
				buildReadingXStream(Contrats.class));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws FileNotFoundException
	 *             Lorsque le fichier n'existe pas
	 */
	public final Applications applicationsReader(final File xmlFile)
			throws FileNotFoundException {
		return XStreamHelper.parse(xmlFile, ENCODING, Applications.class,
				buildReadingXStream(Applications.class));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws FileNotFoundException
	 *             Lorsque le fichier n'existe pas
	 */
	public final Objects objectTypeReader(final File xmlFile)
			throws FileNotFoundException {
		return XStreamHelper.parse(xmlFile, ENCODING, Objects.class,
				buildReadingXStream(Objects.class));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws FileNotFoundException
	 *             Lorsque le fichier n'existe pas
	 */

	public final Codes rndReader(final File xmlFile) throws FileNotFoundException {

		return XStreamHelper.parse(xmlFile, ENCODING, Codes.class,
				buildReadingXStream(Codes.class));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws FileNotFoundException
	 *             Lorsque le fichier n'existe pas
	 */
	public final LifeCycleRule lifeCycleRuleReader(final File xmlFile)
			throws FileNotFoundException {
		
		
		return XStreamHelper.parse(xmlFile, ENCODING, LifeCycleRule.class,
				buildReadingXStream(LifeCycleRule.class));
	}
}
