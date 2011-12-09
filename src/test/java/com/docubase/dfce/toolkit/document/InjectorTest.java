package com.docubase.dfce.toolkit.document;

import static junit.framework.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import net.docubase.toolkit.exception.ged.FrozenDocumentException;
import net.docubase.toolkit.model.document.Document;

import org.junit.Ignore;
import org.junit.Test;

import com.docubase.dfce.toolkit.TestUtils;
import com.docubase.dfce.toolkit.recordmanager.AbstractEventTest;

public class InjectorTest extends AbstractEventTest {

    /**
     * Test d'injection en masse de documents
     * 
     * @throws IOException
     * @throws FrozenDocumentException
     * 
     */
    @Ignore
    @Test
    public void testInjectBatch() throws IOException, FrozenDocumentException {
	File file = createFile();

	// File file = getFile(XML_BATCH, this.getClass());
	List<Document> documens = null;
	String absolutePath = file.getAbsolutePath();
	documens = serviceProvider.getStoreService().injectDocuments(
		absolutePath);

	assertEquals("Tous les documents n'ont pas été injectés.", 3,
		documens.size());

	for (Document doc : documens) {
	    serviceProvider.getStoreService().deleteDocument(doc.getUuid());
	}

    }

    public File createFile() throws IOException {
	File file1 = TestUtils.getFile("doc1.pdf");
	File file2 = TestUtils.getFile("doc2.pdf");
	File file3 = TestUtils.getFile("doc3.pdf");

	String doc1path = file1.getAbsolutePath().replace("\\", "/");
	String doc2path = file2.getAbsolutePath().replace("\\", "/");
	String doc3path = file3.getAbsolutePath().replace("\\", "/");

	StringBuilder xml = new StringBuilder();
	xml.append("<?xml version = '1.0' encoding = 'ISO-8859-1'?> \n");
	xml.append("<batch> \n");
	xml.append("    <documents> \n");
	xml.append(" 		<document> \n");
	xml.append(" 			<title>document 1</title>    \n");
	xml.append(" 			<createdDate>2010-02-11</createdDate>   \n");
	xml.append(" 			<typeFile>PDF</typeFile>                           \n");
	xml.append(" 			<href>" + doc1path + "</href> \n");
	xml.append(" 			<storage>            \n");
	xml.append(" 				<base>RICHGED</base>        \n");
	xml.append(" 			</storage>			    \n");
	xml.append(" 			<metas>                \n");
	xml.append(" 				<meta>           \n");
	xml.append(" 					<fieldId>Catégorie zéro</fieldId>       \n");
	xml.append(" 					<fieldValue>test meta 1</fieldValue>     \n");
	xml.append(" 				</meta>                           \n");
	xml.append(" 				<meta>                                \n");
	xml.append(" 					<fieldId>Catégorie un</fieldId>       \n");
	xml.append(" 					<fieldValue>test meta 11</fieldValue>  \n");
	xml.append(" 				</meta>                            \n");
	xml.append(" 			</metas>			                  \n");
	xml.append(" 		</document>                  \n");
	xml.append(" 		<document>              \n");
	xml.append(" 			<title>document 2</title>      \n");
	xml.append(" 			<createdDate>2010-02-11</createdDate>   \n");
	xml.append(" 			<typeFile>PDF</typeFile>                     \n");
	xml.append(" 			<href>" + doc2path + "</href>	 \n");
	xml.append(" 			<storage>                      \n");
	xml.append(" 				<base>RICHGED</base>         \n");
	xml.append(" 			</storage>			      \n");
	xml.append(" 		<metas>                 \n");
	xml.append(" 				<meta>           \n");
	xml.append(" 					<fieldId>Catégorie zéro</fieldId>    \n");
	xml.append(" 					<fieldValue>test meta 20</fieldValue>   \n");
	xml.append(" 				</meta>                          \n");
	xml.append(" 				<meta>                             \n");
	xml.append(" 					<fieldId>Catégorie un</fieldId>   \n");
	xml.append(" 					<fieldValue>test meta 21</fieldValue>    \n");
	xml.append(" 				</meta>                             \n");
	xml.append(" 				<meta>                              \n");
	xml.append(" 					<fieldId>Catégorie deux</fieldId>   \n");
	xml.append(" 					<fieldValue>test meta 22</fieldValue>  \n");
	xml.append(" 				</meta>                                \n");
	xml.append(" 			</metas>		                             \n");
	xml.append(" 		</document>	                            \n");
	xml.append(" 		<document>                             \n");
	xml.append(" 			<title>document 3</title>            \n");
	xml.append(" 			<createdDate>2010-02-11</createdDate>  \n");
	xml.append(" 		<typeFile>PDF</typeFile>                    \n");
	xml.append(" 			<href>" + doc3path + "</href>     \n");
	xml.append(" 			<storage>                                   \n");
	xml.append(" 				<base>RICHGED</base>                 \n");
	xml.append(" 			</storage>		   \n");
	xml.append(" 			<metas>         \n");
	xml.append(" 				<meta>         ");
	xml.append(" 					<fieldId>Catégorie zéro</fieldId>      \n");
	xml.append(" 					<fieldValue>test meta 3</fieldValue>        \n");
	xml.append(" 				</meta>                             \n");
	xml.append(" 				<meta>                           \n");
	xml.append(" 					<fieldId>Catégorie un</fieldId>    \n");
	xml.append(" 					<fieldValue>test meta 31</fieldValue>    \n");
	xml.append(" 				</meta>                                   \n");
	xml.append(" 				<meta>                                  \n");
	xml.append(" 					<fieldId>Catégorie deux</fieldId>    \n");
	xml.append(" 					<fieldValue>test meta 32</fieldValue>    \n");
	xml.append(" 				</meta>                      \n");
	xml.append(" 			</metas>			             \n");
	xml.append(" 		</document>              \n");
	xml.append(" 	</documents>           \n");
	xml.append(" </batch>          \n");

	File tempFile = File.createTempFile("lot_valide", ".xml");

	Writer writer = new FileWriter(tempFile);
	String string = xml.toString();
	// System.out.println(string);
	writer.write(string);
	writer.flush();
	writer.close();

	BufferedReader bufferedReader = new BufferedReader(new FileReader(
		tempFile));
	String readLine = bufferedReader.readLine();
	while (readLine != null) {
	    readLine = bufferedReader.readLine();
	}

	return tempFile;
    }
}
