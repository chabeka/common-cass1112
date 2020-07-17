package fr.urssaf.image.sae.lotinstallmaj.constantes;

import java.util.HashMap;
import java.util.Map;

import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotInexistantUpdateException;
import fr.urssaf.image.sae.lotinstallmaj.modele.InfoLot;

/**
 * Gestion de versionning de la base SAE en mode CQL
 */
public enum LotVersionCQL {

   CQL_VERSION_1("CREATE_CQL_SCHEMA", 1, "Creation du schéma CQL complet de la base"),

   CQL_VERSION_2("RATTRAPAGE_DATA_INIT", 2, "Initialisation de la creation des données (Format, Paramètres, droits)"),

   CQL_VERSION_3("UPDATE_SAE_META_4_4", 3, "Mise à jor du reférentiel des métadonnées en version 4.4 en base SAE"),

   /**
    * Teste de mise à jour manuelle
    */
   CQL_VERSION_4("TESTER_MANUAL_UPDATE", 4, true, "Cette mise à jour se fait en dehors de lotinstallmaj. Il convient d'upgrader le cluster cassandra en version 3.11.28. Reportez-vous à la documation xxx\r\n"
         + "     Puis lancer la commande \"lotinstallmaj --changeVersionTo 4\" une fois l'action effectuée");

   public static final int SAE_LAST_AVAILABLE_VERSION_CQL = 4;

   private String nomVersion;

   private int version;

   private String descriptif;

   private boolean isManual;

   private static Map<Integer, InfoLot> lotVersionMatchers = new HashMap<>();

   private LotVersionCQL(final String nomVersion, final int version, final String descriptif) {
      this(nomVersion, version, false, descriptif);
   }

   private LotVersionCQL(final String nomVersion, final int version, final boolean isManual, final String descriptif) {
      this.nomVersion = nomVersion;
      this.version = version;
      this.isManual = isManual;
      this.descriptif = descriptif;
   }

   /**
    * @return the nomVersion
    */
   public String getNomVersion() {
      return nomVersion;
   }

   /**
    * @return the version
    */
   public int getVersion() {
      return version;
   }

   /**
    * @return the descriptif
    */
   public String getDescriptif() {
      return descriptif;
   }

   /**
    * @return the isManual
    */
   public boolean isManual() {
      return isManual;
   }

   /**
    * @return the lotVersionMatchers
    */
   public static Map<Integer, InfoLot> getLotVersionMatchers() {
      return lotVersionMatchers;
   }

   static {
      for (final LotVersionCQL lot : LotVersionCQL.values()) {
         final InfoLot infoLot = new InfoLot(lot.version, lot.nomVersion, lot.descriptif);
         infoLot.setManuel(lot.isManual);
         lotVersionMatchers.put(lot.version, infoLot);
      }
   }

   public static InfoLot getLotByVersion(final int version) throws MajLotInexistantUpdateException {
      if (!lotVersionMatchers.containsKey(version)) {
         throw new MajLotInexistantUpdateException("Mise à jour en version : " + version + " inexistante!! "
               + "Veuillez crééer cette mise à jour puis réexecutez votre commande.");
      }
      return lotVersionMatchers.get(version);
   }

}
