package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.sae.lotinstallmaj.constantes.LotVersion;
import fr.urssaf.image.sae.lotinstallmaj.dao.SAECassandraDao;
import fr.urssaf.image.sae.lotinstallmaj.service.MajLotServiceVerificator;
import net.docubase.toolkit.service.ServiceProvider;

@Service
@Qualifier("MajLotServiceVerificatorImpl")
public class MajLotServiceVerificatorImpl implements MajLotServiceVerificator {

   private static final Logger LOG = LoggerFactory.getLogger(MajLotServiceVerificatorImpl.class);

   @Autowired
   private RefMetaInitialisationService refMetaInitService;

   @Autowired
   private SAECassandraDao saeDao;

   private ServiceProvider serviceProvider;

   @Autowired
   private DFCEConnection dfceConfig;

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
      } else {
         LOG.info("...........................Veuillez implementer la verification de la version {} !! .........................", version);
      }
      return isOK;
   }

   /**
    * Verifie que les métas sont bien à jour dans dfce
    * 
    * @return
    */
   private boolean verifyMetaDfce() {
      final boolean isOK = false;

      return isOK;
   }

   /**
    * Verifie que les index composites sont bien à jour dans dfce
    * 
    * @return
    */
   private boolean verifyIndexesComposites() {
      final boolean isOK = false;

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
    * Implémentation de la vérification de la version 33 de la base
    * Verifie que les métadonnées NomContact et PrenomContact ont bien été créées
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
    * Implémentation de la vérification de la version 33 de la base
    * Verifie que les métadonnées CodeCaisseTI et CodeServiceContentieuxTI ont bien été créées
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

      final List<String> indexesComposite = Arrays.asList("DomaineCotisant", 
                                                          "CodeOrganismeProprietaire", 
                                                          "CodeProduitV2",
                                                          "CodeTraitementV2",
            "DateArchivage");

      return isOK;
   }

}
