package fr.urssaf.image.sae.metadata.referential.services.impl;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;

import fr.urssaf.image.sae.metadata.constants.Constants;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.model.Referentiel;
import fr.urssaf.image.sae.metadata.referential.services.XmlDataService;
import fr.urssaf.image.sae.metadata.utils.Utils;
import fr.urssaf.image.sae.metadata.utils.XStreamHelper;

/**
 * Fournit des méthodes de désérialisation
 */
@Service
@Qualifier("xmlDataService")
public class XmlDataServiceImpl implements XmlDataService {
	

	/**
	 * {@inheritDoc}
	 */
	public final Map<String, MetadataReference> referentialReader(
			final InputStream xmlInputStream) throws FileNotFoundException {
		final Referentiel dataFromXml = getReferential(xmlInputStream);
		final Map<String, MetadataReference> referential = new HashMap<String, MetadataReference>();
		for (MetadataReference metaData : Utils.nullSafeIterable(dataFromXml
				.getMetadatas())) {
			referential.put(metaData.getLongCode(), metaData);
		}
		return referential;
	}


	/**
	 * 
	 * 
	 * @throws FileNotFoundException
	 *             Lorsque le fichier n'existe pas
	 */
	private Referentiel getReferential(final InputStream xmlInputStream)
			throws FileNotFoundException {
	   return XStreamHelper.parse(new InputStreamReader(xmlInputStream),
            Constants.ENCODING, Referentiel.class,
            buildReadingXStream(Referentiel.class));
	}

	/**
	 * Construit le composant XStream utilisé en lecture.
	 * 
	 * @param xstrClass
	 *            : La classe à désérialiser
	 * @return le composant xstream
	 */
	private XStream buildReadingXStream(final Class<?> xstrClass) {

		return XStreamHelper.newXStream(xstrClass);
	}
}
