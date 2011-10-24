package fr.urssaf.image.sae.webservices.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.activation.DataHandler;

import org.apache.axiom.util.base64.Base64Utils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ConsultationResponseType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.MetadonneeType;

public class ConsultationTest {

   public void consultation(ConsultationResponseType response,
         Map<String, Object> expectedMetadatas, File expectedContent)
         throws IOException {

      MetadonneeType[] metadatas = response.getMetadonnees().getMetadonnee();

      assertNotNull("la liste des metadonnées doit être renseignée", metadatas);

      for (MetadonneeType metadata : metadatas) {

         assertMetadata(metadata, expectedMetadatas);
      }

      DataHandler actualContent = response.getObjetNumerique()
            .getObjetNumeriqueConsultationTypeChoice_type0().getContenu();

      assertEquals("le contenu n'est pas attendu en base64", Base64Utils
            .encode(FileUtils.readFileToByteArray(expectedContent)),
            Base64Utils.encode(actualContent));

      assertTrue("le contenu n'est pas attendu", IOUtils.contentEquals(
            FileUtils.openInputStream(expectedContent), actualContent
                  .getInputStream()));

      assertNull(
            "Test de l'archivage unitaire : doit avoir aucune url directe de consultation",
            response.getObjetNumerique()
                  .getObjetNumeriqueConsultationTypeChoice_type0().getUrl());

   }

   private static void assertMetadata(MetadonneeType metadata,
         Map<String, Object> expectedMetadatas) {

      assertTrue("la metadonnée '" + metadata.getCode().getMetadonneeCodeType()
            + "' est inattendue", expectedMetadatas.containsKey(metadata
            .getCode().getMetadonneeCodeType()));

      assertEquals("la valeur de la metadonnée '"
            + metadata.getCode().getMetadonneeCodeType() + "' est inattendue",
            expectedMetadatas.get(metadata.getCode().getMetadonneeCodeType()),
            metadata.getValeur().getMetadonneeValeurType());

      expectedMetadatas.remove(metadata.getCode().getMetadonneeCodeType());
   }

}
