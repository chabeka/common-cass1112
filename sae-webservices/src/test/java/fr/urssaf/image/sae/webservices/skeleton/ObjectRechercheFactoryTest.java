package fr.urssaf.image.sae.webservices.skeleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static fr.urssaf.image.sae.webservices.service.factory.ObjectRechercheFactory.createRechercheResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import fr.cirtil.www.saeservice.ListeResultatRechercheType;
import fr.cirtil.www.saeservice.Recherche;
import fr.cirtil.www.saeservice.RechercheResponse;
import fr.cirtil.www.saeservice.RechercheResponseType;
import fr.cirtil.www.saeservice.ResultatRechercheType;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;
import fr.urssaf.image.sae.webservices.util.XMLStreamUtils;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = { "/applicationContext-service-test.xml"
                                  })
@SuppressWarnings({"PMD.ExcessiveImports"})
public class ObjectRechercheFactoryTest {
   
   private static final String NB_MD_INATTENDU = "nombre de metadatas inattendu";
   private static final String MD_ATTENDU = "Des métadonnées sont attendues";
   
   @Autowired
   private SaeServiceSkeleton skeleton;
   @Autowired
   private SAEDocumentService documentService;
   
   private Recherche createSearchType(String filePath) {
      try {
         XMLStreamReader reader = XMLStreamUtils.createXMLStreamReader(filePath);
         return Recherche.Factory.parse(reader);
      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }
   }
   
   /**
    * Test avec resultats de la réponse vide
    * 
    * @throws IOException
    * @throws SAESearchServiceEx
    * @throws MetaDataUnauthorizedToSearchEx
    * @throws MetaDataUnauthorizedToConsultEx
    * @throws UnknownDesiredMetadataEx
    * @throws UnknownLuceneMetadataEx
    * @throws SyntaxLuceneEx
    */
   
   @Test
   public void createRechercheResponseEmpty() {

            
      List<UntypedDocument> untypedDocuments = new ArrayList<UntypedDocument>();
      boolean resultatTronque = false;
      
      RechercheResponse recherche = createRechercheResponse(untypedDocuments, resultatTronque);
      assertNotNull(
            "L'objet de résultat de recherche est null",
            recherche);
      
      RechercheResponseType responseType = recherche.getRechercheResponse();
      assertNotNull(
            "L'objet de résultat de recherche est null",
            responseType);
      
      // Vérifie le flag resultat tronqué
      assertEquals(
            "Valeur incorrect de resultat tronque", 
            responseType.getResultatTronque(), 
            false);
      
      ListeResultatRechercheType resultatsRecherche = responseType.getResultats();
      assertNotNull(
            "L'objet de résultat de recherche est null",
            resultatsRecherche);
      
      
      ResultatRechercheType[] arrResultatRecherche = 
         resultatsRecherche.getResultat();
      assertNotNull(
            "L'objet de résultat de recherche est null",
            arrResultatRecherche);
      
      assertEquals("Le nombre de résultats n'est pas égal à 0",
            0,
            arrResultatRecherche.length);

   }
   
   

}
