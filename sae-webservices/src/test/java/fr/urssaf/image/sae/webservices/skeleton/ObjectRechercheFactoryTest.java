package fr.urssaf.image.sae.webservices.skeleton;

import static fr.urssaf.image.sae.webservices.service.factory.ObjectRechercheFactory.createRechercheResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.ListeResultatRechercheType;
import fr.cirtil.www.saeservice.RechercheResponse;
import fr.cirtil.www.saeservice.RechercheResponseType;
import fr.cirtil.www.saeservice.ResultatRechercheType;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = { "/applicationContext-service-test.xml"
                                  })
@SuppressWarnings({"PMD.ExcessiveImports"})
public class ObjectRechercheFactoryTest {
   
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
