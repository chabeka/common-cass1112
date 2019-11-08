package sae.integration.environment;

/**
 * Liste des environnements utilisables en cible des tests
 */
public class Environments {

   public static final Environment GNT_INT_CLIENT;

   public static final Environment GNS_INT_CLIENT;

   public static final Environment GNT_INT_PAJE;

   public static final Environment GNS_INT_PAJE;

   public static final Environment GNT_INT_CESU;

   public static final Environment GNS_INT_CESU;

   public static final Environment GNT_INT_INTERNE;

   public static final Environment GNS_INT_INTERNE;

   public static final Environment GNT_PIC;

   public static final Environment GNS_PIC;

   public static final Environment GNT_INT_NAT_C1;

   public static final Environment GNT_DEV2;


   public static final Environment LOCALHOST;

   private Environments() {
      // Classe statique
   }

   static {
      GNT_INT_CLIENT = new EnvironmentBuilder()
            .setUrl("http://hwi31intgntv6boweb1.gidn.recouv/ged/services/SaeService/")
            .setEnvCode("INTEGRATION_CLIENTE_GNT")
            .setAppliServer("hwi31intgntv6boappli1.gidn.recouv")
            .setEcdeMountPoint("/hawai/data/ecde_cnp69-evsgidn")
            .setEcdeName("cnp69-evsgidn.cer69.recouv")
            .setCassandraServers("cnp69intgntcas1.gidn.recouv")
            .build();

      GNS_INT_CLIENT = new EnvironmentBuilder()
            .setUrl("http://hwi31intgnsboweb1.gidn.recouv/ged/services/SaeService/")
            .setEnvCode("INTEGRATION_CLIENTE_GNS")
            .setAppliServer("hwi31intgnsboappli1.gidn.recouv")
            .setEcdeMountPoint("/hawai/data/ecde_cnp69-evsgidn")
            .setEcdeName("cnp69-evsgidn.cer69.recouv")
            .setCassandraServers("cnp69intgnscas1.gidn.recouv")
            .build();

      GNT_INT_PAJE = new EnvironmentBuilder()
            .setUrl("http://hwi31intgntpajeboappli1.gidn.recouv:8080/ged/services/SaeService/")
            .setEnvCode("TODO")
            .setAppliServer("hwi31intgntpajeboappli1.gidn.recouv")
            .setEcdeMountPoint("/hawai/data/ecde_cnp69-evsgidn")
            .setEcdeName("cnp69-evsgidn.cer69.recouv")
            .setCassandraServers("cnp69intgntp1cas1.gidn.recouv")
            .build();

      GNS_INT_PAJE = new EnvironmentBuilder()
            .setUrl("http://hwi31intgnspajeboappli1.gidn.recouv:8080/ged/services/SaeService/")
            .setEnvCode("TODO")
            .setAppliServer("hwi31intgnspajeboappli1.gidn.recouv")
            .setEcdeMountPoint("/hawai/data/ecde_cnp69-evsgidn")
            .setEcdeName("cnp69-evsgidn.cer69.recouv")
            .setCassandraServers("cnp69intgnsp1cas1.gidn.recouv")
            .build();

      GNT_INT_CESU = new EnvironmentBuilder()
            .setUrl("http://hwi31intgntcesuboappli1.gidn.recouv:8080/ged/services/SaeService/")
            .setEnvCode("TODO")
            .setAppliServer("hwi31intgntcesuboappli1.gidn.recouv")
            .setEcdeMountPoint("/hawai/data/ecde_cnp69-evsgidn")
            .setEcdeName("cnp69-evsgidn.cer69.recouv")
            .setCassandraServers("cnp69intgntc1cas1.cer69.recouv")
            .build();

      GNS_INT_CESU = new EnvironmentBuilder()
            .setUrl("http://hwi31intgnscesuboappli1.gidn.recouv:8080/ged/services/SaeService/")
            .setAppliServer("hwi31intgnscesuboappli1.gidn.recouv")
            .setEcdeMountPoint("/hawai/data/ecde_cnp69-evsgidn")
            .setEcdeName("cnp69-evsgidn.cer69.recouv")
            .setCassandraServers("cnp69intgnsc1cas1.cer69.recouv")
            .build();

      GNT_INT_INTERNE = new EnvironmentBuilder()
            .setUrl("http://hwi31devgntv6boweb1.gidn.recouv/ged/services/SaeService/")
            .setEnvCode("TODO")
            .setAppliServer("hwi31devgntv6boappli1.gidn.recouv")
            .setEcdeMountPoint("/hawai/data/ecde_cnp69-evsgidn")
            .setEcdeName("cnp69-evsgidn.cer69.recouv")
            .setCassandraServers("cnp69devgntcas1.gidn.recouv")
            .build();

      GNS_INT_INTERNE = new EnvironmentBuilder()
            .setUrl("http://hwi31devgnsv6boweb1.gidn.recouv/ged/services/SaeService/")
            .setEnvCode("TODO")
            .setAppliServer("hwi31devgnsv6boappli1.gidn.recouv")
            .setEcdeMountPoint("/hawai/data/ecde_cnp69-evsgidn")
            .setEcdeName("cnp69-evsgidn.cer69.recouv")
            .setCassandraServers("hwi69devsaecas1.cer69.recouv")
            .build();
      GNT_PIC = new EnvironmentBuilder()
            .setUrl("http://hwi31picgntboweb1.gidn.recouv/ged/services/SaeService/")
            .setEnvCode("PIC_GNT")
            .setAppliServer("hwi31picgntboappli1.gidn.recouv")
            .setEcdeMountPoint("/hawai/data/ecde_cnp69-evsgidn")
            .setEcdeName("cnp69-evsgidn.cer69.recouv")
            .setCassandraServers("cnp31devpicgntcas1.gidn.recouv")
            .build();
      GNS_PIC = new EnvironmentBuilder()
            // .setUrl("http://hwi31picgnsboweb1.gidn.recouv/ged/services/SaeService/")
            .setUrl("http://hwi31picgnsboappli1.gidn.recouv:8080/ged/services/SaeService/")
            .setEnvCode("PIC_GNS")
            .setAppliServer("hwi31picgnsboappli1.gidn.recouv")
            .setEcdeMountPoint("TODO")
            .setEcdeName("TODO")
            .setCassandraServers("cnp31devpicgnscas1.gidn.recouv")
            .build();

      GNT_INT_NAT_C1 = new EnvironmentBuilder()
            .setUrl("http://hwi31ginc1gntv6boweb1.cer31.recouv/ged/services/SaeService/")
            .setEnvCode("TODO")
            .setAppliServer("hwi31ginc1gntv6boappli1.cer31.recouv")
            .setEcdeMountPoint("/hawai/data/nas_cnp_cer69")
            .setEcdeName("cnp31-evsgiin.cer31.recouv")
            .setCassandraServers("cnp69gingntcas1.cer69.recouv")
            .build();
      GNT_DEV2 = new EnvironmentBuilder()
            .setUrl("http://hwi31dev2gntboweb1.gidn.recouv/ged/services/SaeService/")
            .setEnvCode("TODO")
            .setAppliServer("hwi31dev2gntboappli1.gidn.recouv")
            .setEcdeMountPoint("/hawai/data/ecde_cnp69-evsgidn")
            .setEcdeName("cnp69-evsgidn.cer69.recouv")
            .setCassandraServers("cnp69dev2gntcas1.gidn.recouv")
            .build();
      LOCALHOST = new EnvironmentBuilder()
            .setUrl("http://localhost:8080/sae-webservices/services/SaeService/")
            .setEnvCode("TODO")
            .setAppliServer("localhost")
            .setEcdeMountPoint("TODO")
            .setEcdeName("TODO")
            .setCassandraServers("TODO")
            .build();

   }

}
