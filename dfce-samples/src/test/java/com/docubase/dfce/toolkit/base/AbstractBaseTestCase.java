package com.docubase.dfce.toolkit.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import junit.framework.Assert;
import net.docubase.toolkit.exception.ged.ExceededSearchLimitException;
import net.docubase.toolkit.exception.ged.TagControlException;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.ChainedFilter;
import net.docubase.toolkit.model.search.SearchResult;
import net.docubase.toolkit.service.ServiceProvider;

import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public abstract class AbstractBaseTestCase {
    public static final String ADM_LOGIN = "_ADMIN";
    public static final String ADM_PASSWORD = "DOCUBASE";

    public static final String SERVICE_URL;
    public static final String SERVICE2_URL;
    public static final String SIMPLE_USER_NAME = "SIMPLE_USER_NAME";
    public static final String SIMPLE_USER_PASSWORD = "SIMPLE_USER_PASSWORD";
    public static final String SIMPLE_USER_GROUP = "SIMPLE_USER_GROUP";
    /* Instance de la base GED. Utilis�e pour d�finir / modifier la base GED */
    protected static Base base;

    protected static ServiceProvider serviceProvider = ServiceProvider
	    .newServiceProvider();

    static {
	Properties props = new Properties();
	URL url = ClassLoader.getSystemResource("test.properties");

	try {
	    props.load(url.openStream());
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}

	SERVICE_URL = props.getProperty("test.server.1.url");
	SERVICE2_URL = props.getProperty("test.server.2.url");
    }

    protected static Document storeDoc(Document document, File newDoc,
	    boolean expectStore) {
	Document stored;
	FileInputStream in = null;
	try {
	    in = new FileInputStream(newDoc);

	    stored = serviceProvider.getStoreService().storeDocument(document,
		    FilenameUtils.getBaseName(newDoc.getName()),
		    FilenameUtils.getExtension(newDoc.getName()), in);
	    Assert.assertEquals(expectStore, stored != null);
	    in.close();

	} catch (IOException e) {
	    Assert.assertFalse(expectStore);
	    e.printStackTrace();
	    stored = null;
	} catch (TagControlException e) {
	    Assert.assertFalse(expectStore);
	    throw new RuntimeException(e);
	} finally {
	    if (in != null) {
		try {
		    in.close();
		} catch (IOException e) {
		    throw new RuntimeException(e);
		}
	    }
	}
	return stored;
    }

    protected static void deleteBase(Base base) {
	serviceProvider.getBaseAdministrationService().stopBase(base);
	serviceProvider.getBaseAdministrationService().deleteBase(base);
    }

    protected static File getFile(String fileName, Class<?> clazz) {
	if (fileName.startsWith("/") || fileName.startsWith("\\")) {
	    fileName = fileName.substring(1, fileName.length());
	}
	File file = new File(clazz.getResource(fileName).getPath());
	if (!file.exists()) {
	    file = new File(clazz.getResource("/" + fileName).getPath());
	}

	return file;
    }

    /**
     * G�n�re une date de cr�ation. Date du jour moins 2 heures.
     * 
     * @return the date
     */
    protected static Date generateCreationDate() {
	return new DateTime(new Date()).minusHours(2).toDate();
    }

    protected int searchLucene(String query, int searchLimit)
	    throws ExceededSearchLimitException {
	return searchLucene(query, searchLimit, null);

    }

    protected int searchLucene(String query, int searchLimit,
	    ChainedFilter chainedFilter) throws ExceededSearchLimitException {

	SearchResult search = serviceProvider.getSearchService().search(query,
		searchLimit, base, chainedFilter);
	if (search == null) {
	    return 0;
	}
	List<Document> docs = search.getDocuments();
	return docs == null ? 0 : docs.size();
    }
}
