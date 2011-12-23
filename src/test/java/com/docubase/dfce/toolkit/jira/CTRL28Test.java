package com.docubase.dfce.toolkit.jira;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchResult;
import net.docubase.toolkit.service.ServiceProvider;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.linkedin.util.concurrent.ThreadPerTaskExecutor;

import com.docubase.dfce.toolkit.TestUtils;
import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

/**
 * Teste l'extraction d'un document en contexte mono et multi thread.
 * 
 */
@RunWith(Parameterized.class)
public class CTRL28Test extends AbstractTestCaseCreateAndPrepareBase {

    private final int nbThread;

    @Parameters
    public static List<Integer[]> numbersOfThreads() {
	return Arrays.asList(new Integer[][] { { 1, 0 }, // mono thread
		{ 10, 0 }, { 50, 0 }, { 200, 0 }, });
    }

    public CTRL28Test(int input, int expected) {
	this.nbThread = input;
	// pas besoin d'expected dans cette classe
    }

    /**
     * Teste que l'extraction des documents est bien thread safe.
     * <p>
     * On crée une tâche d'extraction par thread puis on teste que l'on a bien
     * autant de documents que de threads.
     * </p>
     * 
     * @throws Exception
     */
    @Test
    public void multiSearchExtraction() throws Exception {
	serviceProvider = ServiceProvider.newServiceProvider();
	serviceProvider.connect(ADM_LOGIN, ADM_PASSWORD, SERVICE_URL);
	final String appliSource = "test_multiExtraction" + System.nanoTime();

	Document document = ToolkitFactory.getInstance()
		.createDocumentTag(base);
	BaseCategory baseCategory = base.getBaseCategory(catNames[0]);
	document.addCriterion(baseCategory, appliSource);
	File file = TestUtils.getFile("doc1.pdf");

	Document storeDocument = serviceProvider.getStoreService()
		.storeDocument(document,
			FilenameUtils.getBaseName(file.getName()),
			FilenameUtils.getExtension(file.getName()),
			new FileInputStream(file));

	assertNotNull(storeDocument);

	List<Future<File>> futures = new ArrayList<Future<File>>();
	Set<String> files = new HashSet<String>();

	for (int i = 0; i < nbThread; i++) {
	    Future<File> future = ThreadPerTaskExecutor
		    .execute(new SearchThenExtractTask(appliSource));
	    futures.add(future);
	}

	for (Future<File> future : futures) {
	    File extractedDoc = future.get();
	    assertNotNull(extractedDoc);
	    files.add(extractedDoc.getAbsolutePath());
	}
	assertEquals("L'extraction n'est pas thread safe.", nbThread,
		files.size());
    }

    class SearchThenExtractTask implements Callable<File> {
	private final String docAppliSource;
	private final ServiceProvider localServiceProvider = ServiceProvider
		.newServiceProvider();

	public SearchThenExtractTask(String docAppliSource) {
	    this.docAppliSource = docAppliSource;
	}

	@Override
	public File call() throws Exception {
	    try {
		localServiceProvider.connect(ADM_LOGIN, ADM_PASSWORD,
			SERVICE_URL);

	    } catch (Throwable e) {
		e.printStackTrace();
	    }
	    InputStream extractedDoc = null;
	    BaseCategory baseCategory = base.getBaseCategory(catNames[0]);
	    String fname = baseCategory.getFormattedName();
	    String luceneQuery = String.format("%s:%s", fname, docAppliSource);
	    SearchResult result = localServiceProvider.getSearchService()
		    .search(luceneQuery, 100, base, null);

	    if (result != null && result.getDocuments() != null) {
		assertEquals("Un seul document devrait être trouvé", 1,
			result.getDocuments().size());

		for (Document document : result.getDocuments()) {
		    extractedDoc = localServiceProvider.getStoreService()
			    .getDocumentFile(document);
		}
	    } else {
		fail("Impossible de trouver le document dont l'appli source est "
			+ docAppliSource);
	    }

	    // System.out.printf("%s -> %s\n", Thread.currentThread().getName(),
	    // extractedDoc.getAbsolutePath());
	    File tempFile = File.createTempFile("extractionTest", null);
	    OutputStream outputStream = new FileOutputStream(tempFile);

	    int read = 0;
	    byte[] bytes = new byte[1024];
	    while ((read = extractedDoc.read(bytes)) != -1) {
		outputStream.write(bytes, 0, read);
	    }

	    extractedDoc.close();
	    localServiceProvider.disconnect();
	    return tempFile;
	}
    }
}
