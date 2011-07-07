package com.docubase.dfce.toolkit.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;
import net.docubase.toolkit.exception.ged.ExceededSearchLimitException;
import net.docubase.toolkit.exception.ged.TagControlException;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.ChainedFilter;
import net.docubase.toolkit.model.search.SearchResult;
import net.docubase.toolkit.service.ServiceProvider;

import org.joda.time.DateTime;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public abstract class AbstractBaseTestCase {
    public static final String ADM_LOGIN = "_ADMIN";
    public static final String ADM_PASSWORD = "DOCUBASE";
    public static final String SERVICE_URL = "http://cer69-ds4int.cer69.recouv:8080/dfce-webapp/toolkit/";
    public static final String SIMPLE_USER_NAME = "SIMPLE_USER_NAME";
    public static final String SIMPLE_USER_PASSWORD = "SIMPLE_USER_PASSWORD";
    /* Instance de la base GED. Utilisée pour d�finir / modifier la base GED */
    protected static Base base;

    protected static Document storeDoc(Document document, File newDoc,
	    boolean expectStore) {
	Document stored;
	FileInputStream in = null;
	try {
	    in = new FileInputStream(newDoc);
	    String type = newDoc.getName()
		    .substring(newDoc.getName().lastIndexOf(".") + 1)
		    .toUpperCase();
	    document.setType(type);

	    stored = ServiceProvider.getStoreService().storeDocument(document,
		    in);
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
	ServiceProvider.getBaseAdministrationService().stopBase(base);
	ServiceProvider.getBaseAdministrationService().deleteBase(base);
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
     * G�n�re une date de cr�tation. Date du jour moins 2 heures.
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

	SearchResult search = ServiceProvider.getSearchService().search(query,
		searchLimit, base, chainedFilter);
	if (search == null) {
	    return 0;
	}
	List<Document> docs = search.getDocuments();
	return docs == null ? 0 : docs.size();
    }
}
