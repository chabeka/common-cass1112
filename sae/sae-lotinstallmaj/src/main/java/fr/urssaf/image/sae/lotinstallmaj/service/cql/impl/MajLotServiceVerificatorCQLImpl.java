package fr.urssaf.image.sae.lotinstallmaj.service.cql.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.lotinstallmaj.constantes.LotVersionCQL;
import fr.urssaf.image.sae.lotinstallmaj.service.MajLotServiceVerificator;

@Service
@Qualifier("MajLotServiceVerificatorCQLImpl")
public class MajLotServiceVerificatorCQLImpl implements MajLotServiceVerificator {

   private static final Logger LOG = LoggerFactory.getLogger(MajLotServiceVerificatorCQLImpl.class);

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean verify(final int version) {
      boolean isOK = false;

      if (version == LotVersionCQL.CQL_VERSION_1.getVersion()) {
         isOK = verifyVersion1();
      } else if (version == LotVersionCQL.CQL_VERSION_2.getVersion()) {
         isOK = verifyVersion2();
      } else {
         LOG.info("...........................Veuillez implementer la verification de la version {} !! .........................", version);
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
    * Implémentation de la vérification de la version 33 de la base
    * Verifie que les métadonnées NomContact et PrenomContact ont bien été créées
    */
   private boolean verifyVersion1() {
      LOG.info("...........................Veuillez implementer la verification de la version 1 !! .........................");
      return false;
   }

   /**
    * Implémentation de la vérification de la version 33 de la base
    * Verifie que les métadonnées CodeCaisseTI et CodeServiceContentieuxTI ont bien été créées
    */
   private boolean verifyVersion2() {
      LOG.info("...........................Veuillez implementer la verification de la version 2 !! .........................");
      return false;
   }

}
