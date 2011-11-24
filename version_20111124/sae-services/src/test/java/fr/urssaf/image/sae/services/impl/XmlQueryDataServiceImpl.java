package fr.urssaf.image.sae.services.impl;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;

import fr.urssaf.image.sae.metadata.constants.Constants;
import fr.urssaf.image.sae.metadata.utils.Utils;
import fr.urssaf.image.sae.metadata.utils.XStreamHelper;
import fr.urssaf.image.sae.model.SAEQueriesData;
import fr.urssaf.image.sae.model.SAEQueryData;
import fr.urssaf.image.sae.services.XmlQueryDataService;

/**
 * Fournit des méthodes de désérialisation
 * 
 * @author rhofir
 * 
 */
@Service
@Qualifier("xmlQueryDataService")
public class XmlQueryDataServiceImpl implements XmlQueryDataService{

	/**
	 * {@inheritDoc}
	 */
	public final Map<String, SAEQueryData> saeQueriesReader(
			final InputStream xmlInputStream) throws FileNotFoundException {
		final SAEQueriesData dataFromXml = getQueriesData(xmlInputStream);
		final Map<String, SAEQueryData> referential = new HashMap<String, SAEQueryData>();
		for (SAEQueryData query : Utils.nullSafeIterable(dataFromXml
				.getQueriesData())) {
			referential.put(query.getQueryType(), query);
		}
		return referential;
	}

	/**
	 * @throws FileNotFoundException
	 *             Lorsque le fichier n'existe pas
	 */
	private SAEQueriesData getQueriesData(final InputStream xmlInputStream)
			throws FileNotFoundException {
	   return XStreamHelper.parse(new InputStreamReader(xmlInputStream),
            Constants.ENCODING, SAEQueriesData.class,
            buildReadingXStream(SAEQueriesData.class));
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
