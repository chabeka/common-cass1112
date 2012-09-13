package fr.urssaf.image.sae.integration.ihmweb.service.tests.utils;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeDefinition;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTestLog;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ListeMetadonneeType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.MetadonneeType;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceTypeUtils;
import fr.urssaf.image.sae.integration.ihmweb.service.MetadonneeValeurListService;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielMetadonneesService;
import fr.urssaf.image.sae.integration.ihmweb.utils.TestUtils;

/**
 * Service de tests des métadonnées
 */
@Service
public final class TestsMetadonneesService {

   @Autowired
   private ReferentielMetadonneesService refMetasService;

   /**
    * Vérifie que la liste de métadonnées fournies en paramètre ne comprend que
    * et toutes les métadonnées dites "consultable par défaut"
    * 
    * @param metadonnees
    *           la liste des métadonnées à vérifier
    * 
    * @return un éventuel message d'erreur, ou une chaîne vide si aucune erreur
    *         rencontrée
    */
   public String verifieMetadonneesConsulteeParDefaut(
         ListeMetadonneeType metadonnees) {

      // Initialise le résultat
      String msgErreurs = StringUtils.EMPTY;
      String msgErreurUnTest;

      // Récupère la liste des métadonnées consultées par défaut, depuis le
      // référentiel des métadonnées
      List<MetadonneeDefinition> metasConsult = refMetasService
            .listeMetadonneesConsulteeParDefaut();

      // On traite uniquement si on a au moins 1 métadonnées dans le référentiel
      if (!CollectionUtils.isEmpty(metasConsult)) {

         // Vérifie que les métadonnées renvoyées sont bien dans la liste
         // des métadonnées dites "consultées par défaut"
         msgErreurUnTest = verifieMetasUniquementDansListeAutorisee(
               metadonnees, metasConsult);
         msgErreurs = TestUtils.concatMessagesErreurs(msgErreurs,
               msgErreurUnTest);

         // Vérifie que TOUTES les métadonnées consultée par défaut
         // sont renvoyées.
         msgErreurUnTest = verifieMetasToutesPresentes(metadonnees,
               metasConsult);
         msgErreurs = TestUtils.concatMessagesErreurs(msgErreurs,
               msgErreurUnTest);

      }

      // Renvoie du résultat
      return msgErreurs;

   }

   /**
    * Vérifie que la liste des métadonnées metaFournies ne contient que des
    * métadonnées présentes dans la liste metasDispos
    * 
    * @param metasFournies
    *           la liste des métadonnées dans laquelle regarder
    * @param metasDispos
    *           la liste des métadonnées de référence
    * @return un éventuel message d'erreur, ou une chaîne vide si aucune erreur
    *         rencontrée
    */
   public String verifieMetasUniquementDansListeAutorisee(
         ListeMetadonneeType metasFournies,
         List<MetadonneeDefinition> metasDispos) {

      // Initialise le résultat
      String msgErreurs = StringUtils.EMPTY;
      String msgErreurUnTest;

      // Boucle sur la liste des métadonnées fournies
      MetadonneeType[] metasFournies2 = metasFournies.getMetadonnee();
      String codeMetaFournie;
      for (MetadonneeType metaFournie : metasFournies2) {

         // Regarde si la métadonnée existe dans metasDispo
         codeMetaFournie = metaFournie.getCode().getMetadonneeCodeType();

         if (refMetasService.findMeta(codeMetaFournie, metasDispos) == null) {
            msgErreurUnTest = "La métadonnée \""
                  + codeMetaFournie
                  + "\" ne fait pas partie de la liste des métadonnées attendues.";
            msgErreurs = TestUtils.concatMessagesErreurs(msgErreurs,
                  msgErreurUnTest);
         }

      }

      // Renvoie du résultat
      return msgErreurs;

   }

   /**
    * Vérifie que toutes les métadonnées de la liste
    * metasQuiDevraientEtrePresentes sont présentes dans la liste metasFournies
    * 
    * @param metasFournies
    *           la liste des métadonnées dans laquelle regarder
    * @param metasQuiDevraientEtrePresentes
    *           la liste des métadonnées qui devraient être présentes dans
    *           metasFournis
    * @return un éventuel message d'erreur, ou une chaîne vide si aucune erreur
    *         rencontrée
    */
   public String verifieMetasToutesPresentes(ListeMetadonneeType metasFournies,
         List<MetadonneeDefinition> metasQuiDevraientEtrePresentes) {

      // Initialise le résultat
      String msgErreurs = StringUtils.EMPTY;
      String msgErreurUnTest;

      // Boucle sur la liste des métadonnées qui devraient être présentes
      String codeLongAttendu;
      MetadonneeType meta;
      for (MetadonneeDefinition metaQuiDevraitEtrePresente : metasQuiDevraientEtrePresentes) {

         codeLongAttendu = metaQuiDevraitEtrePresente.getCodeLong();

         meta = SaeServiceTypeUtils.chercheMeta(codeLongAttendu, metasFournies);

         if (meta == null) {
            msgErreurUnTest = "La métadonnée \"" + codeLongAttendu
                  + "\" est absente";
            msgErreurs = TestUtils.concatMessagesErreurs(msgErreurs,
                  msgErreurUnTest);
         }

      }

      // Renvoie du résultat
      return msgErreurs;

   }

   /**
    * Vérifie la présence d'une métadonnée, ainsi que sa valeur par rapport à la
    * valeur attendue <br>
    * Si la vérification échoue, ajoute le message d'erreur dans le log du
    * résultat du test
    * 
    * @param resultatTest
    *           l'objet contenant le résultat du test en cours
    * @param liste
    *           la liste des métadonnées source
    * @param code
    *           le code de la métadonnée à vérifier
    * @param valeurAttendue
    *           la valeur attendue de la métadonnée
    * @return true si au moins 1 erreur a été détectée
    */
   public boolean verifiePresenceEtValeurAvecLog(ResultatTest resultatTest,
         MetadonneeValeurList liste, String code, String valeurAttendue) {

      boolean erreur = false;

      String messageErreur = verifiePresenceEtValeur(resultatTest, liste, code,
            valeurAttendue);

      if (StringUtils.isNotBlank(messageErreur)) {
         erreur = true;
         resultatTest.getLog().appendLogNewLine();
         resultatTest.getLog().appendLogLn(messageErreur);
      }

      return erreur;

   }

   /**
    * Vérifie la présence d'une métadonnée, ainsi que sa valeur par rapport à la
    * valeur attendue<br>
    * <br>
    * Si la vérification échoue, renvoie le message d'erreur, sans l'ajouter
    * dans le log du résultat du test
    * 
    * @param resultatTest
    *           l'objet contenant le résultat du test en cours
    * @param liste
    *           la liste des métadonnées source
    * @param code
    *           le code de la métadonnée à vérifier
    * @param valeurAttendue
    *           la valeur attendue de la métadonnée
    * @return un message d'erreur si la vérification a échoué, ou une chaîne
    *         vide dans le cas contraire
    */
   @SuppressWarnings("PMD.InsufficientStringBufferDeclaration")
   public String verifiePresenceEtValeur(ResultatTest resultatTest,
         MetadonneeValeurList liste, String code, String valeurAttendue) {

      // Initialise le résultat
      String messageErreur = StringUtils.EMPTY;

      // Initialise l'éventuel message d'erreur
      StringBuffer sBuffer = new StringBuffer();
      sBuffer.append("Erreur sur la métadonnée ");
      sBuffer.append(code);
      sBuffer.append(" : on attendait la valeur \"");
      if(valeurAttendue.length()>50){
         sBuffer.append(valeurAttendue.substring(0, 50) +"...");
      }else{
         sBuffer.append(valeurAttendue);
      }
      sBuffer.append('\"');

      // Recherche la métadonnée dans la liste
      MetadonneeValeur meta = MetadonneeValeurListService.find(liste, code);
      if (meta == null) {

         resultatTest.setStatus(TestStatusEnum.Echec);

         sBuffer.append(" alors que la métadonnée est absente.");

         messageErreur = sBuffer.toString();

      } else {

         if (!valeurAttendue.equals(meta.getValeur())) {

            resultatTest.setStatus(TestStatusEnum.Echec);

            sBuffer.append(" alors que la valeur obtenue est \"");
            if(meta.getValeur().length()>50){
               sBuffer.append(meta.getValeur().substring(0, 50) +"...");
            }else{
               sBuffer.append(meta.getValeur());
            }
            sBuffer.append("\".");

            messageErreur = sBuffer.toString();

         }

      }

      // Renvoie du résultat
      return messageErreur;

   }

   /**
    * Vérifie que les codes de métadonnées passés en paramètre d'entrée
    * corresponde à des métadonnées dites "consultables" dans le référentiel des
    * métadonnées.
    * 
    * @param codes
    *           la liste des codes de métadonnées à vérifier
    * @param resultatTest
    *           le résultat des tests à mettre à jour
    */
   public void areMetadonneesConsultables(CodeMetadonneeList codes,
         ResultatTest resultatTest) {

      if (CollectionUtils.isNotEmpty(codes)) {

         ResultatTestLog log = resultatTest.getLog();

         MetadonneeDefinition metadonnee;
         for (String code : codes) {

            metadonnee = refMetasService.findMeta(code);
            if (!metadonnee.isConsultable()) {

               resultatTest.setStatus(TestStatusEnum.Echec);

               log.appendLog("Erreur : la métadonnée \"");
               log.appendLog(code);
               log.appendLogLn("\" n'est pas consultable");

            }

         }

      }

   }

}
