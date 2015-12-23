package sae.client.demo.webservice;

import static org.junit.Assert.fail;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.junit.Test;

import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.MetadonneeType;
import sae.client.demo.webservice.modele.SaeServiceStub.RechercheNbRes;
import sae.client.demo.webservice.modele.SaeServiceStub.RechercheNbResResponse;
import sae.client.demo.webservice.modele.SaeServiceStub.RechercheNbResResponseType;
import sae.client.demo.webservice.modele.SaeServiceStub.ResultatRechercheType;

public class RechercheNbResTest {

   
   /**
    * Exemple de consommation de l'opération rechercheNbRes du service web SaeService<br>
    * <br>
    * Cas sans erreur
    * 
    * @throws RemoteException 
    */
   @Test
   public void rechercheNbRes_success() throws RemoteException {
      
      // Requête de recherche
      String requeteRecherche = "Siren:123456789"; 
      
      // Métadonnées souhaitées dans les résultats de recherche
      // Soit on veut les métadonnées dites "consultées par défaut" d'après le
      //  référentiel des métadonnées. Dans ce cas, il faut écrire :
      //  List<String> codesMetasSouhaitess = null;
      // Si on veut choisir les métadonnées récupérées, il faut écrire :
      //  List<String> codesMetasSouhaitess = new ArrayList<String>();
      //  codesMetasSouhaitess.add("Titre");
      //  codesMetasSouhaitess.add("CodeRND");
      //  ...
      
//      List<String> codesMetasSouhaitess = null;
      
      List<String> codesMetasSouhaitess = new ArrayList<String>();
      codesMetasSouhaitess.add("CodeRND");
      codesMetasSouhaitess.add("Siren");
      codesMetasSouhaitess.add("Denomination");
      
      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();
      
      // Construction du paramètre d'entrée de l'opération recherche, 
      //  avec les objets modèle générés par Axis2.
      RechercheNbRes paramsEntree = Axis2ObjectFactory.contruitParamsEntreeRechercheNbRes(
            requeteRecherche,codesMetasSouhaitess);
      
      // Appel du service web de recherche
      RechercheNbResResponse reponse = saeService.rechercheNbRes(paramsEntree);
      
      // Affichage dans la console du résultat de la recherche
      afficheResultatsRecherche(reponse);
      
   }
   
   
   private void afficheResultatsRecherche(
         RechercheNbResResponse reponse)  {
    

      RechercheNbResResponseType rechercheNbResResponse = 
         reponse.getRechercheNbResResponse();
      
      // Affichage dans la console du flag de resultats tronques
      System.out.println(
            "Résultats tronqués => " + 
                  rechercheNbResResponse.getResultatTronque());
      
      // Regarde si on a eu au moins 1 résultat de recherche
      if (
            (rechercheNbResResponse.getResultats()==null) || 
            (rechercheNbResResponse.getResultats().getResultat()==null) || 
            (rechercheNbResResponse.getResultats().getResultat().length==0)) {
         
         System.out.println("La recherche n'a pas ramené de résultats");
         
      } else {
         
         ResultatRechercheType[] tabResRecherche = 
               rechercheNbResResponse.getResultats().getResultat();
         
         // Affiche le nombre de résultats 
         // (le nombre de résultat est tronqué à 200 documents)
         // La taille de la liste sera donc au maximum de 200 documents
         System.out.println(
               "La recherche a ramené " + 
               tabResRecherche.length + 
               " résultats");
         System.out.println("");
         
         // Affiche le nombre "réel" de résultats
         // C'est a dire le nombre total de document correspond à cette recherche
         System.out.println(
               "La recherche a ramené " + 
               rechercheNbResResponse.getNbResultats() + 
               " résultats");
         System.out.println("");
         
         // Boucle sur les résultats de recherche
         ResultatRechercheType resRecherche;
         MetadonneeType[] tabMetas;
         String codeMeta;
         String valeurMeta;
         for(int i=0;i<tabResRecherche.length;i++) {
            
            // Affiche un compteur
            System.out.println("Résultat de recherche #" + (i+1));
            
            // Affiche l'identifiant unique d'archivage
            resRecherche = tabResRecherche[i];
            System.out.println(
                  "Identifiant unique d'archivage : " + 
                  resRecherche.getIdArchive().toString());
            
            // Affiche les métadonnées
            System.out.println("Métadonnées : ");
            tabMetas = resRecherche.getMetadonnees().getMetadonnee();
            for(MetadonneeType metadonnee: tabMetas) {
               
               codeMeta = metadonnee.getCode().getMetadonneeCodeType();
               valeurMeta = metadonnee.getValeur().getMetadonneeValeurType();
               
               System.out.println(codeMeta + "=" + valeurMeta);
               
            }
            
            // Un saut de ligne
            System.out.println("");
            
         }
         
      }
      
   }
   
   
   /**
    * Exemple de consommation de l'opération rechercheNbRes du service web SaeService<br>
    * <br>
    * Cas avec erreur : On utilise une métadonnée inconnue du SAE dans la requête de recherche<br>
    * <br>
    * Le SAE renvoie la SoapFault suivante :<br>
    * <ul>
    *    <li>Code : sae:RechercheMetadonneesInconnues</li>
    *    <li>Message : La ou les métadonnées suivantes, utilisées dans la requête de recherche, n'existent pas dans le référentiel des métadonnées : Toto</li>
    * </ul>
    * 
    */
   @Test
   public void rechercheNbRes_failure() {
      
      // Requête de recherche
      String requeteRecherche = "Toto:123456789"; 
      
      // Métadonnées souhaitées dans les résultats de recherche
      List<String> codesMetasSouhaitess = null;
      
      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();
      
      // Construction du paramètre d'entrée de l'opération recherche, 
      //  avec les objets modèle générés par Axis2.
      RechercheNbRes paramsEntree = Axis2ObjectFactory.contruitParamsEntreeRechercheNbRes(
            requeteRecherche,codesMetasSouhaitess);
      
      // Appel de l'opération recherche
      try {
      
         // Appel de l'opération rechercheNbRes
         // On ne récupère pas la réponse de l'opération, puisqu'on est censé obtenir une SoapFault
         saeService.rechercheNbRes(paramsEntree);
         
         // Si on a passé l'appel, le test est en échec
         fail("La SoapFault attendue n'a pas été renvoyée");
      
      } catch (AxisFault fault) {
         
         // sysout
         TestUtils.sysoutAxisFault(fault);
         
         // Vérification de la SoapFault
         TestUtils.assertSoapFault(
               fault,
               "urn:sae:faultcodes",
               "sae",
               "RechercheMetadonneesInconnues",
               "La ou les métadonnées suivantes, utilisées dans la requête de recherche, n'existent pas dans le référentiel des métadonnées : Toto");
       
      } catch (RemoteException exception) {
         
         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault\r\n" + exception);
         
      }
      
   }
   
}
