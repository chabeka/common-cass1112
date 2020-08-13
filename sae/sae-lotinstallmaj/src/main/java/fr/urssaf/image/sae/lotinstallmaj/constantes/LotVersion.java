package fr.urssaf.image.sae.lotinstallmaj.constantes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.urssaf.image.sae.lotinstallmaj.modele.InfoLot;

public enum LotVersion {

   CODE_ACTIVITE("CODEACTIVITENONOBLIGATOIRE"),
   DUREE_CONSERVATION("DUREECONSERVATIONDEMANDEDELAICOTISANT"),

   CASSANDRA_DROITS_GED("CASSANDRA_DROITS_GED"),
   GNS_DISABLE_COMPOSITE_INDEX("GNS_DISABLE_COMPOSITE_INDEX"),
   GNT_DISABLE_COMPOSITE_INDEX("GNT_DISABLE_COMPOSITE_INDEX"),

   META_SEPA("META_SEPA"),
   META_130400("META_130400"),
   META_150100("META_150100"),

   DFCE_130700("DFCE_130700"),
   DFCE_150400("DFCE_150400"),
   DFCE_150400_P5("DFCE_150400_P5"),
   DFCE_151000("DFCE_151000"),
   CASSANDRA_120510("CASSANDRA_120510", 1, "Création du Keyspace SAE\r\n" +
         "\r\n" +
         "Création des CF pour SpringBatch :\r\n" +
         "   - JobInstance\r\n" +
         "   - JobInstancesByName\r\n" +
         "   - JobInstanceToJobExecution\r\n" +
         "   - JobExecution\r\n" +
         "   - JobExecutions\r\n" +
         "   - JobExecutionsRunning\r\n" +
         "   - JobExecutionToJobStep\r\n" +
         "   - JobStep\r\n" +
         "   - JobSteps\r\n" +
         "   - Sequences\r\n" +
         "\r\n" +
         "Création des CF pour la pile des travaux :\r\n" +
         "   - JobRequest\r\n" +
         "   - JobsQueue\r\n" +
         "\r\n" +
         "Création de la CF des paramètres :\r\n" +
         "   - Parameters"),
   CASSANDRA_120512("CASSANDRA_120512", 2, "Met la base Cassandra du SAE en version 2 \r\n" +
         "\r\n" +
         "Création d'1 CF supplémentaire pour la pile des travaux :\r\n" +
         "   - JobHistory"),
   CASSANDRA_121110("CASSANDRA_121110", 3),
   CASSANDRA_130400("CASSANDRA_130400", 4),
   CASSANDRA_130700("CASSANDRA_130700", 5),
   CASSANDRA_131100("CASSANDRA_131100", 6),
   CASSANDRA_140700("CASSANDRA_140700", 7),
   CASSANDRA_150100("CASSANDRA_150100", 8),
   CASSANDRA_DFCE_150400("CASSANDRA_DFCE_150400", 9),
   CASSANDRA_DFCE_150600("CASSANDRA_DFCE_150600", 10),
   CASSANDRA_DFCE_150601("CASSANDRA_DFCE_150601", 11),
   CASSANDRA_151000("CASSANDRA_151000", 12),
   CASSANDRA_DFCE_151001("CASSANDRA_DFCE_151001", 13),
   CASSANDRA_DFCE_151200("CASSANDRA_DFCE_151200", 14),
   CASSANDRA_DFCE_151201("CASSANDRA_DFCE_151201", 15),
   CASSANDRA_DFCE_160300("CASSANDRA_DFCE_160300", 16),
   CASSANDRA_DFCE_160400("CASSANDRA_DFCE_160400", 17),

   GNS_CASSANDRA_DFCE_160600("GNS_CASSANDRA_DFCE_160600", 18),
   GNT_CASSANDRA_DFCE_160600("GNT_CASSANDRA_DFCE_160600", 18),

   GNS_CASSANDRA_DFCE_160601("GNS_CASSANDRA_DFCE_160601", 19),
   GNT_CASSANDRA_DFCE_160601("GNT_CASSANDRA_DFCE_160601", 19),

   CASSANDRA_DFCE_160900("CASSANDRA_DFCE_160900", 20),
   CASSANDRA_DFCE_160901("CASSANDRA_DFCE_160901", 21),
   CASSANDRA_DFCE_161100("CASSANDRA_DFCE_161100", 22),

   GNS_CASSANDRA_DFCE_170200("GNS_CASSANDRA_DFCE_170200", 23),
   GNT_CASSANDRA_DFCE_170200("GNT_CASSANDRA_DFCE_170200", 23),

   CASSANDRA_170201("CASSANDRA_170201", 24),

   GNS_CASSANDRA_DFCE_170202("GNS_CASSANDRA_DFCE_170202", 25),
   GNT_CASSANDRA_DFCE_170202("GNT_CASSANDRA_DFCE_170202", 25),

   CASSANDRA_DFCE_170900("CASSANDRA_DFCE_170900", 26),
   CASSANDRA_DFCE_170901("CASSANDRA_DFCE_170901", 27),

   GNS_CASSANDRA_DFCE_180300("GNS_CASSANDRA_DFCE_180300", 28),
   GNT_CASSANDRA_DFCE_180300("GNT_CASSANDRA_DFCE_180300", 28),

   CASSANDRA_DFCE_180900("CASSANDRA_DFCE_180900", 29),
   CASSANDRA_DFCE_180901("CASSANDRA_DFCE_180901", 30),
   CASSANDRA_DFCE_190700("CASSANDRA_DFCE_190700", 31),
   CASSANDRA_DFCE_200200("CASSANDRA_DFCE_200200", 32),

   CASSANDRA_DFCE_200500("CASSANDRA_DFCE_200500", 33, "Met la base Cassandra du SAE en version 33 :\r\n" +
         "\r\n" +
         "- Ajout des métadonnées NomContact et PrenomContact\r\n" +
         "- Ajout de l'index composite DomaineCotisant-CodeOrganismeProprietaire-CodeProduitV2-CodeTraitementV2-DateArchivage"),

   CASSANDRA_DFCE_201100("CASSANDRA_DFCE_201100", 34, "Met la base Cassandra du SAE en version 34 :\r\n" +
         "\r\n" +
         "- Ajout du Paramètre de trace de Déblocage OK [WS_DEBLOCAGE|OK]\r\n");

   // CASSANDRA_DFCE_201100("GNS_CASSANDRA_DFCE_200500", 34, true, "Cette mise à jour se fait en dehors de lotinstallmaj. Il convient d'upgrader le cluster cassandra en version 3.11.28. Reportez-vous à la documation xxx\r\n"
   // + " Puis lancer la commande \"lotinstallmaj --changeVersionTo 34\" une fois l'action effectuée");

   private String nomLot;

   private int numVersionLot;

   private String descriptif;

   private boolean isManuel = false;

   private static Map<Integer, InfoLot> lotVersionMatchers = new HashMap<>();

   /**
    * Dernière version disponible de la base SAE
    */
   private static int lastAvailableVersion;

   LotVersion(final String nomOp, final int version) {
      nomLot = nomOp;
      numVersionLot = version;
   }

   /**
    * Constructeur d'installation d'un nouveau lot non manuel
    * 
    * @param nomOp
    * @param version
    * @param descriptif
    */
   LotVersion(final String nomOp, final int version, final String descriptif) {
      this(nomOp, version, false, descriptif);
   }

   LotVersion(final String nomOp, final int version, final boolean isManuel, final String descriptif) {
      this(nomOp, version);
      this.isManuel = isManuel;
      this.descriptif = descriptif;
   }

   LotVersion(final String nomOp) {
      nomLot = nomOp;
      numVersionLot = 0;
   }

   static {
      final List<Integer> versions = new ArrayList<>();
      for (final LotVersion version : LotVersion.values()) {
         if (version.getNumVersionLot() != 0) {
            final int numVer = version.getNumVersionLot();
            final String nomL = version.getNomLot();
            final String desc = version.getDescriptif();
            final InfoLot infoLot = new InfoLot(numVer, nomL, desc);
            infoLot.setManuel(version.isManuel);
            if (lotVersionMatchers.containsKey(version.getNumVersionLot())) {
               final StringBuilder nomOp = new StringBuilder(lotVersionMatchers.get(version.getNumVersionLot()).getNomLot());
               nomOp.append(" | ");
               nomOp.append(version.getNomLot());
               infoLot.setDescriptif(nomOp.toString());
            }
            lotVersionMatchers.put(version.getNumVersionLot(), infoLot);
         }

         if (version.getNumVersionLot() != 0) {
            versions.add(version.getNumVersionLot());
         }
      }
      lastAvailableVersion = versions.stream().max((o1, o2) -> o1 < o2 ? -1 : o1 > o2 ? +1 : 0).get();
   }

   /**
    * @return the nomOperation
    */
   public String getNomLot() {
      return nomLot;
   }

   /**
    * @return the numVersion
    */
   public int getNumVersionLot() {
      return numVersionLot;
   }

   /**
    * @return the descriptif
    */
   public String getDescriptif() {
      return descriptif;
   }

   /**
    * Recupère le nom du lot correspondant à une version
    * 
    * @param version
    * @return
    */
   public static InfoLot getInfoLotByVersion(final int version) {
      return lotVersionMatchers.get(version);
   }

   /**
    * Recupère le nom du dernier lot
    * 
    * @return
    */
   public static String getLastLotName() {
      return getInfoLotByVersion(lastAvailableVersion).getNomLot();
   }

   /**
    * @return the isManuel
    */
   public boolean isManuel() {
      return isManuel;
   }

   /**
    * @return the lastAvailableVersion
    */
   public static int getLastAvailableVersion() {
      return lastAvailableVersion;
   }

}
