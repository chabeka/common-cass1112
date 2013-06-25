package fr.urssaf.image.sae.rnd.dao.support;

import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.rnd.dao.RndDao;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;

/**
 * Support de manipulation de la CF Rnd
 * 
 * 
 */
@Component
public class RndSupport {

   private final RndDao rndDao;

   private static final String FIN_LOG = "{} - fin";
   private static final String DEBUT_LOG = "{} - début";
   private static final Logger LOGGER = LoggerFactory
         .getLogger(RndSupport.class);

   /**
    * Constructeur
    * 
    * @param rndDao
    *           DAO d'accès à la CF Rnd
    */
   @Autowired
   public RndSupport(RndDao rndDao) {
      this.rndDao = rndDao;
   }

   /**
    * Création d'un RND dans la CF Rnd
    * 
    * @param typeDoc
    *           le type de document à ajouter
    * @param clock
    *           Horloge de la création
    */
   public final void ajouterRnd(TypeDocument typeDoc, long clock) {

      String trcPrefix = "ajouterRnd";
      LOGGER.debug(DEBUT_LOG, trcPrefix);

      LOGGER.debug("{} - Code du type de doc : {}", new String[] { trcPrefix,
            typeDoc.getCode() });

      ColumnFamilyUpdater<String, String> updater = rndDao.getCfTmpl()
            .createUpdater(typeDoc.getCode());

      rndDao.ecritCloture(typeDoc.isCloture(), updater, clock);

      // Le code activité peut être null
      if (typeDoc.getCodeActivite() != null) {
         rndDao.ecritCodeActivite(Integer.valueOf(typeDoc.getCodeActivite()),
               updater, clock);
      }
      // Si le code fonction est null, alors il s'agit d'un code temporaire et
      // on le met à 0
      String codeFonction = "0";
      if (typeDoc.getCodeFonction() == null
            && typeDoc.getType().equals(TypeCode.TEMPORAIRE)) {
         codeFonction = "0";
      } else {
         codeFonction = typeDoc.getCodeFonction();
      }
      rndDao.ecritCodeFonction(Integer.valueOf(codeFonction), updater, clock);

      rndDao.ecritDureeConservation(Integer.valueOf(typeDoc
            .getDureeConservation()), updater, clock);
      rndDao.ecritLibelle(typeDoc.getLibelle(), updater, clock);
      rndDao.ecritType(typeDoc.getType().toString(), updater, clock);

      rndDao.getCfTmpl().update(updater);

      LOGGER.info("{} - Ajout du code : {}", new String[] { trcPrefix,
            typeDoc.getCode() });

      LOGGER.debug(FIN_LOG, trcPrefix);
   }

   /**
    * Récupère le type de document correspondant au code passé en paramètre
    * 
    * @param code
    *           le code RND dont on veut le type de document
    * @return le type de document recherché
    */
   public final TypeDocument getRnd(String code) {
      ColumnFamilyResult<String, String> result = rndDao.getCfTmpl()
            .queryColumns(code);

      TypeDocument typeDoc = getTypeDocFromResult(result);

      return typeDoc;
   }

   /**
    * Construction d'un objet {@link TypeDocument} à  partir du réultat de la
    * requête
    * 
    * @param result
    *           {@link ColumnFamilyResult}
    * @return {@link TypeDocument}
    */
   private TypeDocument getTypeDocFromResult(
         ColumnFamilyResult<String, String> result) {
      TypeDocument typeDoc = null;
      if (result != null && result.hasResults()) {
         typeDoc = new TypeDocument();

         typeDoc.setCode(result.getKey());
         typeDoc.setCloture(result.getBoolean(rndDao.RND_CLOTURE));

         // Code activité et code fonction peuvent être nul (ex code temporaire)
         if (result.getInteger(rndDao.RND_CODE_ACTIVITE) != null) {
            typeDoc.setCodeActivite(result.getInteger(rndDao.RND_CODE_ACTIVITE)
                  .toString());
         }
         if (result.getInteger(rndDao.RND_CODE_FONCTION) != null) {
            typeDoc.setCodeFonction(result.getInteger(rndDao.RND_CODE_FONCTION)
                  .toString());
         }
         typeDoc.setDureeConservation(result
               .getInteger(rndDao.RND_DUREE_CONSERVATION));
         typeDoc.setLibelle(result.getString(rndDao.RND_LIBELLE));
         typeDoc.setType(TypeCode.valueOf(result.getString(rndDao.RND_TYPE)));
      }
      return typeDoc;
   }

}
