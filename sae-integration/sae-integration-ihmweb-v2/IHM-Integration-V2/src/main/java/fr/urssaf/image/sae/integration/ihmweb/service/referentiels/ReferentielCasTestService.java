package fr.urssaf.image.sae.integration.ihmweb.service.referentiels;

import java.util.Iterator;

import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.modele.CasTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.listetests.CasTestType;
import fr.urssaf.image.sae.integration.ihmweb.modele.listetests.CategorieType;
import fr.urssaf.image.sae.integration.ihmweb.modele.listetests.ListeCategoriesType;
import fr.urssaf.image.sae.integration.ihmweb.utils.JAXBUtils;


/**
 * Service de manipulation de la liste des cas de test<br>
 * <br>
 * Les cas de test sont organisés comme ceci :<br>
 * La liste des cas de tests est constituée dans un premier niveau d'une liste
 * de catégorie de cas de tests.<br>
 * Dans un deuxième niveau, chaque catégorie contient un ensemble de cas de test.<br>
 * <br>
 * Voici un exemple de la vue schématique de liste des cas de test :<br>
 * <ul>
 *    <li>
 *       Catégorie 1
 *       <ul>
 *          <li>Cas 001</li>
 *          <li>Cas 002</li>
 *       </ul>
 *    </li>
 *    <li>
 *       Catégorie 2
 *       <ul>
 *          <li>Cas 003</li>
 *          <li>Cas 004</li>
 *       </ul>
 *    </li>
 *    <li>
 *       Catégorie 3
 *       <ul>
 *          <li>Cas 005</li>
 *          <li>Cas 006</li>
 *       </ul>
 *    </li>
 * </ul>
 */
@Service
public class ReferentielCasTestService {

   
   private ListeCategoriesType listeTests;
   
   
   /**
    * Constructeur
    */
   public ReferentielCasTestService() {
      
      try {
         
         // Lecture du fichier ListeCasTests.xml, et mise en cache
         listeTests = JAXBUtils.unmarshalResource(
               ListeCategoriesType.class, 
               "/ListeCasTests/ListeCasTests.xml", 
               "/ListeCasTests/ListeCasTests.xsd");
         
      } catch (Exception e) {
         throw new IntegrationRuntimeException(e);
      }
      
   }
   
   
   /**
    * Renvoie l'objet décrivant le cas de test dont l'identifiant est passé en paramètre
    * 
    * @param idCasTest l'identifiant du cas de test
    * @return l'objet décrivant le cas de test
    */
   public final CasTest getCasTest(String idCasTest) {
      
      CategorieType categorie;
      CasTestType casTest;
      CasTest casTestTrouve = null;
      
      // Itère sur chaque catégorie
      Iterator<CategorieType> iteratorCategorie = listeTests.getCategorie().iterator();
      while (iteratorCategorie.hasNext())
      {
         
         // Récupère l'objet catégorie
         categorie = iteratorCategorie.next();
         
         // Au sein de chaque catégorie, on itère sur chaque cas de test
         Iterator<CasTestType> iteratorCasTest = categorie.getCasTests().getCasTest().iterator();
         while (iteratorCasTest.hasNext())
         {
            
            // Récupère l'objet casTest
            casTest = iteratorCasTest.next();
            
            // Regarde si l'identifiant du cas de test correspond
            if (casTest.getId().equals(idCasTest)) {
               
               casTestTrouve = new CasTest(); //NOPMD
               casTestTrouve.setIdentifiant(casTest.getId());
               casTestTrouve.setCode(casTest.getCode());
               casTestTrouve.setCategorie(categorie.getNom());
               casTestTrouve.setDescription(casTest.getDescription());
               casTestTrouve.setLuceneExemple(casTest.getLuceneExemple());
               casTestTrouve.setLuceneExempleList(casTest.getLuceneExempleList());
               
               break;
            }
            
         }
         
         if (casTestTrouve!=null) {
            break;
         }
         
         
      }
      
      if (casTestTrouve==null) {
         throw new IntegrationRuntimeException("Le cas de test " + idCasTest + " n'a pas été retrouvé");
      }
      
      return casTestTrouve;
      
   }
   
   
   /**
    * Renvoie la liste des cas de tests
    * 
    * @return la liste des cas de test
    */
   public final ListeCategoriesType getListeTests() {
      return listeTests;
   }
   
   
}
