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

public class PronomUid354Test {

   @Test
   public void getPronomUidFromAFileFmt354() throws IOException {
      
      BinarySignatureIdentifier droid = new BinarySignatureIdentifier();
      droid.setSignatureFile("src/test/resources/test_sig_files/SignatureFile_VTest354.xml");
      droid.init();
      
      File file = new File("src/test/resources/test_sig_files/fmt354/Refus-ATT-264879.pdf");
      assertTrue(file.exists());
      URI resourceUri = file.toURI();

      InputStream in = new FileInputStream(file);
      RequestMetaData metaData = new RequestMetaData(file.length(), file.lastModified(), "Refus-ATT-264879.pdf");
      RequestIdentifier identifier = new RequestIdentifier(resourceUri);
      identifier.setParentId(1L);
      
      IdentificationRequest request =  new FileSystemIdentificationRequest(metaData, identifier);
      request.open(in);
       
      IdentificationResultCollection results = droid.matchBinarySignatures(request);
      //request.close();
      
      IdentificationResult result = results.getResults().iterator().next();
      String pronomId = result.getPuid();
      
      assertEquals("Le format du fichier n'est pas correct", "fmt/354", pronomId);
      request.close();
   }
   
   
}
