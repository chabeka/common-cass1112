package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.format.referentiel.service.ReferentielFormatService;
import fr.urssaf.image.sae.lotinstallmaj.component.DFCEConnexionComponent;
import fr.urssaf.image.sae.lotinstallmaj.constantes.LotVersion;
import fr.urssaf.image.sae.lotinstallmaj.dao.SAECassandraDao;
import fr.urssaf.image.sae.lotinstallmaj.service.MajLotServiceVerificator;
import fr.urssaf.image.sae.metadata.referential.model.SaeIndexComposite;
import fr.urssaf.image.sae.metadata.referential.support.SaeIndexCompositeSupport;

@Service
@Qualifier("MajLotServiceVerificatorImpl")
public class MajLotServiceVerificatorImpl implements MajLotServiceVerificator {

   private static final Logger LOG = LoggerFactory.getLogger(MajLotServiceVerificatorImpl.class);

   @Autowired
   private RefMetaInitialisationService refMetaInitService;

   @Autowired
   private SAECassandraDao saeDao;

   @Autowired
   private DFCEConnection dfceConfig;

   @Autowired
   private DFCEConnexionComponent dfceConnexionComponent;

   @Autowired
   ReferentielFormatService referentielFormatService;
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean verify(final int version) {
      boolean isOK = false;

      if (version == LotVersion.CASSANDRA_DFCE_200500.getNumVersionLot()) {
         isOK = verifyVersion33();
         displayInstallationStatus(isOK, version, LotVersion.CASSANDRA_DFCE_200500.getNomLot());
      } else if (version == LotVersion.CASSANDRA_DFCE_200200.getNumVersionLot()) {
         isOK = verifyVersion32();
         displayInstallationStatus(isOK, version, LotVersion.CASSANDRA_DFCE_200200.getNomLot());
      }else if (version == LotVersion.CASSANDRA_DFCE_201100.getNumVersionLot()) {
         isOK = verifyVersion34();
         displayInstallationStatus(isOK, version, LotVersion.CASSANDRA_DFCE_201100.getNomLot());
      } else {
         LOG.info("...........................Veuillez implementer la verification de la version {} !! .........................", version);
      }
      return isOK;
   }


   /**
    * Vérifie que les formats de fichiers sont listés sont bien présents dans le référentiel des formats
    * 
    * @return
    */
   private boolean verifyFormatFichier(final List<String> formatsToCheck) {

      final List<FormatFichier> listFormat = referentielFormatService.getAllFormat();
      final List<String> extensions = new ArrayList<>();
      for (final FormatFichier format : listFormat) {
         extensions.add(format.getExtension());
      }

      return extensions.containsAll(formatsToCheck);
   }

   /**
    * Vérifie que les index composites sont bien à jour dans dfce
    * 
    * @return
    */
   private boolean verifyIndexesComposites(final String indexCodeCourt) {

      boolean isOK = false;

      // -- dcfe connect
      try {
         dfceConnexionComponent.setCnxParameter(dfceConfig);
         dfceConnexionComponent.openConnection();
      }
      catch (final Exception e) {
         throw new RuntimeException(e);
      }

      final SaeIndexCompositeSupport serviceSupport = new SaeIndexCompositeSupport(dfceConnexionComponent.getDFCEServices());
      final List<SaeIndexComposite> listIndex = serviceSupport.getListeCompositeIndex();

      for (final SaeIndexComposite index : listIndex) {
         if (index.getName().equals(indexCodeCourt)) {
            isOK = true;
         }
      }
      return isOK;
   }

   /**
    * Affiche le status d'installation d'une version
    * 
    * @param state
    * @param version
    * @param lot
    */
   private void displayInstallationStatus(final boolean state, final int version, final String lot) {
      if (state) {
         LOG.info("La version {} correspondant au lot {} a bien été installée", version, lot);
      } else {
         LOG.info("La version {} correspondant au lot {} n'est pas encore installée", version, lot);
      }
   }


   /**
    * Implémentation de la vérification de la version 34 de la base
    * Vérifie que le code pour la trace de déblocage, la nouvelle méta
    * DateNaissanceCotisant et l'index composite associé, et les formats rtf et eml
    * on bien été créé
    */
   private boolean verifyVersion34() {
      LOG.info("Démarrage de la vérification du Lot : {}, Version : {}",
            LotVersion.CASSANDRA_DFCE_201100,
            LotVersion.CASSANDRA_DFCE_201100.getNumVersionLot());
      boolean isOK = false;

      saeDao.connectToKeySpace();

      if (saeDao.getDatabaseVersion() < LotVersion.CASSANDRA_DFCE_200500.getNumVersionLot()) {
         return false;
      }
      // vérifier la creation de la méta DateNaissanceCotisant
      final List<String> codesLong = Arrays.asList("DateNaissanceCotisant");
      isOK = refMetaInitService.findListMeta(saeDao.getKeyspace(), codesLong);

      // vérifier la création des formats
      final List<String> formats = Arrays.asList("rtf", "eml");
      isOK = isOK && verifyFormatFichier(formats);

      // vérifier la creation de l'indexComposite
      final String indexCodeCourt = "cot&cop&dnc&SM_ARCHIVAGE_DATE&";
      isOK = isOK && verifyIndexesComposites(indexCodeCourt);

      return isOK;
   }

   /**
    * Implémentation de la vérification de la version 33 de la base
    * Vérifie que les métadonnées NomContact et PrenomContact ont bien été créées
    */
   private boolean verifyVersion33() {
      LOG.info("Demarrage de la verification du Lot : {}, Version : {}",
            LotVersion.CASSANDRA_DFCE_200500,
            LotVersion.CASSANDRA_DFCE_200500.getNumVersionLot());
      boolean isOK = false;

      saeDao.connectToKeySpace();

      if (saeDao.getDatabaseVersion() < LotVersion.CASSANDRA_DFCE_200500.getNumVersionLot()) {
         return false;
      }

      final List<String> codesLong = Arrays.asList("NomContact", "PrenomContact");
      isOK = refMetaInitService.findListMeta(saeDao.getKeyspace(), codesLong);

      return isOK;
   }

   /**
    * Implémentation de la vérification de la version 32 de la base
    * Vérifie que les métadonnées CodeCaisseTI et CodeServiceContentieuxTI ont bien été créées
    */
   private boolean verifyVersion32() {
      LOG.info("Demarrage de la verification du Lot : {}, Version : {}",
            LotVersion.CASSANDRA_DFCE_200200,
            LotVersion.CASSANDRA_DFCE_200200.getNumVersionLot());
      boolean isOK = false;

      saeDao.connectToKeySpace();

      if (saeDao.getDatabaseVersion() < LotVersion.CASSANDRA_DFCE_200200.getNumVersionLot()) {
         return false;
      }

      final List<String> codesLong = Arrays.asList("CodeCaisseTI", "CodeCaisseTI");
      isOK = refMetaInitService.findListMeta(saeDao.getKeyspace(), codesLong);

      // vérifier la création de l'indexComposite
      final String indexCodeCourt = "cot&cop&cpr&ctr&SM_ARCHIVAGE_DATE&";
      isOK = isOK && verifyIndexesComposites(indexCodeCourt);

      return isOK;
   }

}
