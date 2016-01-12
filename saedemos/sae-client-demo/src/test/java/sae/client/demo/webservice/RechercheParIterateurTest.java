package sae.client.demo.webservice;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.MetadonneeType;
import sae.client.demo.webservice.modele.SaeServiceStub.RechercheParIterateur;
import sae.client.demo.webservice.modele.SaeServiceStub.RechercheParIterateurResponse;
import sae.client.demo.webservice.modele.SaeServiceStub.RechercheParIterateurResponseType;
import sae.client.demo.webservice.modele.SaeServiceStub.ResultatRechercheType;

public class RechercheParIterateurTest {

   /**
    * Exemple de consommation de l'opération recherche par itérateur du service
    * web SaeService<br>
    * <br>
    * Cas sans erreur
    * 
    * @throws RemoteException
    */
   @Test
   public void rechercheParIterateur_success() throws RemoteException {

      // Métadonnées fixes (peut être null)
      HashMap<String, String> listeMetasFixes = null;
      // HashMap<String, String> listeMetasFixes = new HashMap<String,
      // String>();
      // listeMetasFixes.put("CodeOrganismeGestionnaire", "UR917");

      // Métadonnée variable (obligatoire)
      String codeMetaVariable = "DateArchivage";
      String valeurMinMetaVar = "20150909000000000";
      String valeurMaxMetaVar = "20150909050000000";

      // Filtres de type égalité (facultatif)
      // Attention une seule valeur par métadonnée
      HashMap<String, String> equalFilter = null;
      // HashMap<String, String> equalFilter = new HashMap<String, String>();
      // equalFilter.put("nomMeta", "valeurMeta");

      // Filtres de type non égalité (facultatif)
      // Attention une seule valeur par métadonnée
      HashMap<String, String> notEqualFilter = null;
      // HashMap<String, String> notEqualFilter = new HashMap<String, String>();
      // notEqualFilter.put("nomMeta", "valeurMeta");

      // Filtres de type range (facultatif)
      HashMap<String, String[]> rangeFilter = null;
      // HashMap<String, String[]> rangeFilter = new HashMap<String,
      // String[]>();

      // Filtres de type not in range (facultatif)
      HashMap<String, String[]> notInRangeFilter = null;
      // HashMap<String, String[]> notInRangeFilter = new HashMap<String,
      // String[]>();

      // Nombre de document retourné par appel du service
      String nombreDocParPage = "100";

      // Identifiant de la page en cours (à saisir uniquement à partir du 2ème
      // appel, retourné par l'appel précédent)
      String valeurIdentifiantPage = null;
      // UUID du dernier document récupéré (à saisir uniquement à partir du 2ème
      // appel, retourné par l'appel précédent)
      String idArchive = null;

      // Métadonnées souhaitées dans les résultats de recherche
      // Soit on veut les métadonnées dites "consultées par défaut" d'après le
      // référentiel des métadonnées. Dans ce cas, il faut écrire :
      // List<String> codesMetasSouhaitess = null;
      // Si on veut choisir les métadonnées récupérées, il faut écrire :
      // List<String> codesMetasSouhaitess = new ArrayList<String>();
      // codesMetasSouhaitess.add("Titre");
      // codesMetasSouhaitess.add("CodeRND");
      // ...

      List<String> codesMetasSouhaites = null;

      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération recherche,
      // avec les objets modèle générés par Axis2.
      RechercheParIterateur paramsEntree = Axis2ObjectFactory
            .contruitParamsEntreeRechercheParIterateur(listeMetasFixes,
                  codeMetaVariable, valeurMinMetaVar, valeurMaxMetaVar,
                  equalFilter, notEqualFilter, rangeFilter, notInRangeFilter,
                  nombreDocParPage, codesMetasSouhaites, valeurIdentifiantPage,
                  idArchive);

      // Appel du service web de recherche
      RechercheParIterateurResponse reponse = saeService
            .rechercheParIterateur(paramsEntree);

      // Affichage dans la console du résultat de la recherche
      afficheResultatsRechercheParIterateur(reponse);

   }

   private void afficheResultatsRechercheParIterateur(
         RechercheParIterateurResponse reponse) {

      RechercheParIterateurResponseType rechercheResponse = reponse
            .getRechercheParIterateurResponse();

      // Affichage dans la console du flag dernière page
      System.out.println("Dernière page => "
            + rechercheResponse.getDernierePage());

      // Affichage des résultats
      if ((rechercheResponse.getResultats() == null)
            || (rechercheResponse.getResultats().getResultat() == null)
            || (rechercheResponse.getResultats().getResultat().length == 0)) {

         System.out.println("La recherche n'a pas ramené de résultats");

      } else {

         ResultatRechercheType[] tabResRecherche = rechercheResponse
               .getResultats().getResultat();

         // Affiche le nombre de résultats
         System.out.println("La recherche a ramené " + tabResRecherche.length
               + " résultats");
         System.out.println("");

         // Boucle sur les résultats de recherche
         ResultatRechercheType resRecherche;
         MetadonneeType[] tabMetas;
         String codeMeta;
         String valeurMeta;
         for (int i = 0; i < tabResRecherche.length; i++) {

            // Affiche un compteur
            // System.out.println("Résultat de recherche #" + (i + 1));

            // Affiche l'identifiant unique d'archivage
            resRecherche = tabResRecherche[i];
            // System.out.println("Identifiant unique d'archivage : "
            // + resRecherche.getIdArchive().toString());

            // Affiche les métadonnées
            // System.out.println("Métadonnées : ");
            tabMetas = resRecherche.getMetadonnees().getMetadonnee();
            for (MetadonneeType metadonnee : tabMetas) {

               codeMeta = metadonnee.getCode().getMetadonneeCodeType();
               valeurMeta = metadonnee.getValeur().getMetadonneeValeurType();

               // System.out.println(codeMeta + "=" + valeurMeta);
               if (codeMeta.equals("IdTraitementMasseInterne")) {
                  System.out.println(valeurMeta + ";"
                        + resRecherche.getIdArchive().toString());
               }

            }

            // Un saut de ligne
            System.out.println("");

         }

      }

      // Identifiant page suivante
      if (rechercheResponse.getIdentifiantPageSuivante() != null) {
         System.out.println("UUID archive = "
               + rechercheResponse.getIdentifiantPageSuivante().getIdArchive());
         System.out.println("Valeur identifiant page suivante = "
               + rechercheResponse.getIdentifiantPageSuivante().getValeur()
                     .getMetadonneeValeurType());
      }

   }

}
