package fr.urssaf.image.commons.webservice.generate;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("PMD")
public class GenerateSourceCxfTest {

   private final static GenerateUtil TEST;

   static {
      TEST = new GenerateUtil("generateByCxf");
   }

   @BeforeClass
   public static void init() throws IOException {
      TEST.init();

   }

   @AfterClass
   public static void clean() throws IOException {
      TEST.clean();

   }

   @Test
   public void generateDocument() {

      String PACKAGEPATH = "Document";

      GenerateSourceCxf generateSource = new GenerateSourceCxf(PACKAGEPATH,
            "src/test/resources/cxf/document_literal.wsdl", TEST.getPath());
      generateSource.generate();

      TEST.assertFile(PACKAGEPATH, "AllDocuments.java");
      TEST.assertFile(PACKAGEPATH, "AllDocumentsResponse.java");
      TEST.assertFile(PACKAGEPATH, "DocService.java");
      TEST.assertFile(PACKAGEPATH, "DocServiceDocument.java");
      TEST.assertFile(PACKAGEPATH, "Document.java");
      TEST.assertFile(PACKAGEPATH, "Etat.java");
      TEST.assertFile(PACKAGEPATH, "GetDocument.java");
      TEST.assertFile(PACKAGEPATH, "GetDocumentResponse.java");
      TEST.assertFile(PACKAGEPATH, "ObjectFactory.java");
      TEST.assertFile(PACKAGEPATH, "package-info.java");
      TEST.assertFile(PACKAGEPATH, "Save.java");
      TEST.assertFile(PACKAGEPATH, "SaveResponse.java");

      TEST.assertFiles(PACKAGEPATH, 12);

   }

   @Test
   public void generateRpcEncoded() {

      String PACKAGEPATH = "RpcEncoded";

      GenerateSourceCxf generateSource = new GenerateSourceCxf(PACKAGEPATH,
            "src/test/resources/cxf/rpc_encoded.wsdl", TEST.getPath());
      generateSource.generate();

      TEST.assertFile(PACKAGEPATH, "DocService.java");
      TEST.assertFile(PACKAGEPATH, "DocServiceRpcEncoded.java");
      TEST.assertFile(PACKAGEPATH, "Document.java");
      TEST.assertFile(PACKAGEPATH, "DocumentArray.java");
      TEST.assertFile(PACKAGEPATH, "Etat.java");
      TEST.assertFile(PACKAGEPATH, "ObjectFactory.java");
      TEST.assertFile(PACKAGEPATH, "package-info.java");

      TEST.assertFiles(PACKAGEPATH, 7);

   }

   @Test
   public void generateRpcLiteral() {

      String PACKAGEPATH = "RpcLiteral";

      GenerateSourceCxf generateSource = new GenerateSourceCxf(PACKAGEPATH,
            "src/test/resources/cxf/rpc_literal.wsdl", TEST.getPath());
      generateSource.generate();

      TEST.assertFile(PACKAGEPATH, "DocService.java");
      TEST.assertFile(PACKAGEPATH, "DocServiceRpcLiteral.java");
      TEST.assertFile(PACKAGEPATH, "Document.java");
      TEST.assertFile(PACKAGEPATH, "DocumentArray.java");
      TEST.assertFile(PACKAGEPATH, "Etat.java");
      TEST.assertFile(PACKAGEPATH, "ObjectFactory.java");
      TEST.assertFile(PACKAGEPATH, "package-info.java");

      TEST.assertFiles(PACKAGEPATH, 7);

   }

}
