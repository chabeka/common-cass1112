/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.image.sae.dfcetools;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import com.datastax.oss.driver.api.core.CqlSession;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.commons.dfce.service.impl.DFCEServicesImpl;
import fr.urssaf.image.sae.dfcetools.dao.CassandraSessionFactory;
import net.docubase.toolkit.model.reference.LifeCycleRule;

public class Main {

   /**
    * @param args
    * @throws Exception
    */
   public static void main(final String[] args) throws Exception {
      final DFCEServices dfceServices = getDfceServices("localhost", "SAE-PROD", 8080);

      final Set<LifeCycleRule> rules = dfceServices.getAllLifeCycleRules();
      for (final Iterator iterator = rules.iterator(); iterator.hasNext();) {
         final LifeCycleRule lifeCycleRule = (LifeCycleRule) iterator.next();
         System.out.println(lifeCycleRule.getDocumentType());
      }

   }

   public static void main_old(final String[] args) throws Exception {
      final DFCEServices dfceServices = getDfceServices("hwi7gntcveappli1.cve.recouv", "GNT-PROD", 8080);
      final String servers = "cnp7gntcvecas1.cve.recouv,cnp7gntcvecas2.cve.recouv"; // Charge GNT
      final String cassandraLocalDC = "DC7";
      final CqlSession session = CassandraSessionFactory.getSession(servers, "root", "regina4932", cassandraLocalDC);

      if (args.length == 0) {
         System.out.println("Attendu sur la ligne de commande : date : exemple : 201801251710");
         return;
      }
      final String date = args[0];
      System.out.println("Date : " + date);
      if (!date.startsWith("20")) {
         System.out.println("La date semble invalide");
         return;
      }
      final DocumentsDeleter deleter = new DocumentsDeleter(session);
      deleter.deleteDocumentsAfterDate(dfceServices, date);

   }

   private DFCEServices getDfceServices(final String server, final String base) throws MalformedURLException {
      return getDfceServices(server, base, 80);
   }

   private static DFCEServices getDfceServices(final String server, final String base, final int port) throws MalformedURLException {
      final DFCEConnection params = new DFCEConnection();
      params.setHostName(server);
      params.setHostPort(port);
      params.setBaseName(base);
      params.setContextRoot("/dfce-webapp/");
      params.setSecure(true);
      params.setLogin("_ADMIN");
      params.setPassword("DOCUBASE");
      params.setTimeout(10000);
      final String url = "http://" + params.getHostName() + ":" + params.getHostPort() + params.getContextRoot();
      params.setUrlToolkit(url);
      params.setServerUrl(new URL(url));
      params.setCheckHash(true);
      params.setDigestAlgo("SHA-1");
      params.setNbtentativecnx(3);

      final DFCEServices services = new DFCEServicesImpl(params);
      return services;
   }

}
