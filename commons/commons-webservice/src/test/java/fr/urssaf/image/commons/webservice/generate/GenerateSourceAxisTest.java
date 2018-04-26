package fr.urssaf.image.commons.webservice.generate;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("PMD")
public class GenerateSourceAxisTest {

   private final static GenerateUtil TEST;

   static {
      TEST = new GenerateUtil("generateByAxis");
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
   public void generateDocumentLiteral() {
      
      String PACKAGEPATH = "DocumentLiteral";

      GenerateSourceAxis generateSource = new GenerateSourceAxis(PACKAGEPATH,
            "src/test/resources/axis/document_literal.wsdl", TEST.getPath());
      generateSource.generate();

      TEST.assertFile(PACKAGEPATH, "BonjourRequestType.java");
      TEST.assertFile(PACKAGEPATH, "BonjourResponseType.java");
      TEST.assertFile(PACKAGEPATH, "HelloService.java");
      TEST.assertFile(PACKAGEPATH, "HelloServiceBindingStub.java");
      TEST.assertFile(PACKAGEPATH, "HelloServiceLocator.java");
      TEST.assertFile(PACKAGEPATH, "HelloServicePortType.java");
      TEST.assertFile(PACKAGEPATH, "MultiplieRequestType.java");
      TEST.assertFile(PACKAGEPATH, "MultiplieResponseType.java");

      TEST.assertFiles(PACKAGEPATH, 8);

   }
   
   @Test
   public void generateRpcEncoded() {
      
      String PACKAGEPATH = "RpcEncoded";

      GenerateSourceAxis generateSource = new GenerateSourceAxis(PACKAGEPATH,
            "src/test/resources/axis/rpc_encoded.wsdl", TEST.getPath());
      generateSource.generate();

      TEST.assertFile(PACKAGEPATH, "HelloService.java");
      TEST.assertFile(PACKAGEPATH, "HelloServiceBindingStub.java");
      TEST.assertFile(PACKAGEPATH, "HelloServiceLocator.java");
      TEST.assertFile(PACKAGEPATH, "HelloServicePortType.java");
      
      TEST.assertFiles(PACKAGEPATH, 4);

   }
   
   @Test
   public void generateRpctLiteral() {
      
      String PACKAGEPATH = "RpcLiteral";

      GenerateSourceAxis generateSource = new GenerateSourceAxis(PACKAGEPATH,
            "src/test/resources/axis/rpc_literal.wsdl", TEST.getPath());
      generateSource.generate();

      TEST.assertFile(PACKAGEPATH, "HelloService.java");
      TEST.assertFile(PACKAGEPATH, "HelloServiceBindingStub.java");
      TEST.assertFile(PACKAGEPATH, "HelloServiceLocator.java");
      TEST.assertFile(PACKAGEPATH, "HelloServicePortType.java");
      
      TEST.assertFiles(PACKAGEPATH, 4);

   }

}
