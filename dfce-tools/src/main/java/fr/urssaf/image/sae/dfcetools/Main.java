/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.image.sae.dfcetools;

import java.util.Iterator;
import java.util.Set;

import com.datastax.oss.driver.api.core.CqlSession;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.dfcetools.dao.CassandraSessionFactory;
import fr.urssaf.image.sae.dfcetools.helper.DFCEServicesHelper;
import net.docubase.toolkit.model.reference.LifeCycleRule;

public class Main {

   /**
    * @param args
    * @throws Exception
    */
   public static void main(final String[] args) {
      final DFCEServices dfceServices = DFCEServicesHelper.getDfceServices("localhost", "SAE-PROD", 8080);

      final Set<LifeCycleRule> rules = dfceServices.getAllLifeCycleRules();
      for (final Iterator iterator = rules.iterator(); iterator.hasNext();) {
         final LifeCycleRule lifeCycleRule = (LifeCycleRule) iterator.next();
         System.out.println(lifeCycleRule.getDocumentType());
      }

   }

   public static void main_old(final String[] args) throws Exception {
      final DFCEServices dfceServices = DFCEServicesHelper.getDfceServices("hwi7gntcveappli1.cve.recouv", "GNT-PROD", 8080);
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


}
