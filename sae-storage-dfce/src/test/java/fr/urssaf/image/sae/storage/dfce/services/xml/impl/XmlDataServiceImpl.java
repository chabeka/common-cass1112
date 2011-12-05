package fr.urssaf.image.sae.storage.dfce.services.xml.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;

import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.data.model.DesiredMetaData;
import fr.urssaf.image.sae.storage.dfce.data.model.LuceneQueries;
import fr.urssaf.image.sae.storage.dfce.data.model.SaeDocument;
import fr.urssaf.image.sae.storage.dfce.data.utils.XStreamHelper;
import fr.urssaf.image.sae.storage.dfce.services.xml.XmlDataService;

/**
 * Fournit des méthodes de désérialisation
 * 
 * @author akenore, rhofir
 * 
 */
@Service
@Qualifier("xmlDataService")
public class XmlDataServiceImpl implements XmlDataService {
	/**
	 * {@inheritDoc}
	 */
	public final DesiredMetaData desiredMetaDataReader(final File xmlFile)
			throws FileNotFoundException {
		return XStreamHelper.parse(xmlFile, Constants.ENCODING,
				DesiredMetaData.class,
				buildReadingXStream(DesiredMetaData.class));
	}

	/**
	 * Construit le composant XStream utilisé en lecture.
	 * 
	 * @param clazz
	 *            : classe à séraialiser.
	 * @return le composant xstream
	 */
	private XStream buildReadingXStream(final Class<?> clazz) {

		return XStreamHelper.newXStream(clazz);
	}

	/**
	 * {@inheritDoc}
	 */
	public final List<SaeDocument> saeDocumentsReader(final File... xmlFiles)
			throws FileNotFoundException {
		List<SaeDocument> saeDocuments = new ArrayList<SaeDocument>();
		for (File filePath : xmlFiles) {
			saeDocuments.add(saeDocumentReader(filePath));
		}
		return saeDocuments;
	}

	/**
	 * {@inheritDoc}
	 */
	public final SaeDocument saeDocumentReader(final File xmlFile)
			throws FileNotFoundException {
		return XStreamHelper.parse(xmlFile, Charset.forName("UTF-8"),
				SaeDocument.class, buildReadingXStream(SaeDocument.class));
	}

	/**
	 * {@inheritDoc}
	 */
	public final LuceneQueries queriesReader(File xmlFile)
			throws FileNotFoundException {

		return XStreamHelper.parse(xmlFile, Charset.forName("UTF-8"),
				LuceneQueries.class, buildReadingXStream(LuceneQueries.class));
	}

}
