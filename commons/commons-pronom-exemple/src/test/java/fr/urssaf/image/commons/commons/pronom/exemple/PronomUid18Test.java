package fr.urssaf.image.commons.commons.pronom.exemple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.junit.Test;

import fr.urssaf.image.commons.commons.pronom.exemple.interfaces.IdentificationResult;
import fr.urssaf.image.commons.commons.pronom.exemple.interfaces.IdentificationResultCollection;
import fr.urssaf.image.commons.commons.pronom.exemple.interfaces.RequestIdentifier;
import fr.urssaf.image.commons.commons.pronom.exemple.interfaces.RequestMetaData;
import fr.urssaf.image.commons.commons.pronom.exemple.modele.signature.xml.BinarySignatureIdentifier;
import fr.urssaf.image.commons.commons.pronom.exemple.modele.signature.xml.IdentificationRequest;
import fr.urssaf.image.commons.commons.pronom.exemple.resources.FileSystemIdentificationRequest;

public class PronomUid18Test {

   @Test
   public void getPronomUidFromAFileFmt18() throws IOException {
      
      BinarySignatureIdentifier binarySig = new BinarySignatureIdentifier();
      binarySig.setSignatureFile("src/test/resources/test_sig_files/SignatureFile_VTest18.xml");

      binarySig.init();
      File file = new File("src/test/resources/test_sig_files/fmt18/attestation.pdf");
      
      assertTrue(file.exists());
      URI resourceUri = file.toURI();

      InputStream in = new FileInputStream(file);
      RequestMetaData metaData = new RequestMetaData(file.length(), file.lastModified(), "attestation.pdf");
      RequestIdentifier identifier = new RequestIdentifier(resourceUri);
      identifier.setParentId(1L);
      
      IdentificationRequest request =  new FileSystemIdentificationRequest(metaData, identifier);
      request.open(in);
 
      IdentificationResultCollection results = binarySig.matchBinarySignatures(request);
      
      IdentificationResult result = results.getResults().iterator().next();
      String pronomId = result.getPuid();
 //     request.close();
      assertEquals("Le format du fichier n'est pas correct", "fmt/18", pronomId);
      request.close();
   }
   
}
