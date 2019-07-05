
package sae.integration.webservice.factory;

/**
 * Les environnements SAE sur lesquels les tests peuvent être lancés
 */
public enum Environments {
                          GNT_INT_CLIENT("http://hwi31intgntv6boweb1.gidn.recouv/ged/services/SaeService/"),
                          GNS_INT_CLIENT("http://hwi31intgnsboweb1.gidn.recouv/ged/services/SaeService/"),
                          GNT_INT_INTERNE("http://hwi31devgntv6boweb1.gidn.recouv/ged/services/SaeService/"),
                          GNS_INT_INTERNE("hwi31devgnsv6boweb1.gidn.recouv/ged/services/SaeService/"),
                          GNT_PIC("http://hwi31picgntboweb1.gidn.recouv/ged/services/SaeService/"),
                          GNT_INT_NAT_C1("http://hwi31ginc1gntv6boweb1.cer31.recouv/ged/services/SaeService/"),
                          GNT_DEV2("http://hwi31dev2gntboweb1.gidn.recouv/ged/services/SaeService/"),
                          LOCALHOST("http://localhost:8080/sae-webservices/services/SaeService/");
   private final String url;

   Environments(final String envUrl) {
      this.url = envUrl;
   }

   public String getUrl() {
      return url;
   }
}
