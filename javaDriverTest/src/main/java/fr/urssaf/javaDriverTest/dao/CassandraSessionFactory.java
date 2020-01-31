
package fr.urssaf.javaDriverTest.dao;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.DefaultProtocolVersion;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;

/**
 * Permet de créer la session cassandra
 */
public class CassandraSessionFactory {

   public CassandraSessionFactory() {
      // Classe statique
   }

   public static CqlSession getSession(final String cassandraServers, final String cassandraUsername, final String cassandraPassword,
         final String cassandraLocalDC) {
      final String[] servers = StringUtils.split(cassandraServers, ",");
      final List<String> contactPoints = new ArrayList<>(servers.length);
      for (String server : servers) {
         server = server.replace(":9160", "").replace(":9042", "").trim();
         contactPoints.add(server + ":9042");
      }
      final DriverConfigLoader loader = DriverConfigLoader.programmaticBuilder()
            .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(10))
            .withString(DefaultDriverOption.AUTH_PROVIDER_CLASS,
                  "PlainTextAuthProvider")
            .withString(DefaultDriverOption.AUTH_PROVIDER_USER_NAME,
                  cassandraUsername)
            .withString(DefaultDriverOption.AUTH_PROVIDER_PASSWORD,
                  cassandraPassword)
            .withString(DefaultDriverOption.REQUEST_CONSISTENCY,
                  DefaultConsistencyLevel.QUORUM.name())
            .withString(DefaultDriverOption.PROTOCOL_VERSION,
                  DefaultProtocolVersion.V3.name())
            // .withString(DefaultDriverOption.LOAD_BALANCING_POLICY_CLASS,
            // "fr.urssaf.javaDriverTest.dao.NoLoadBalancingPolicy")
            // .withString(DefaultDriverOption.ADDRESS_TRANSLATOR_CLASS,
            // "fr.urssaf.javaDriverTest.dao.ProxyAddressTranslator")
            .withStringList(DefaultDriverOption.CONTACT_POINTS, contactPoints)
            .startProfile("slow")
            .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(30))
            .endProfile()
            .build();

      /*
      final ArrayList<InetSocketAddress> contactPoints = new ArrayList<>();
      for (String server : servers) {
         server = server.replace(":9160", "").replace(":9042", "").trim();
         InetAddress[] addressList;
         try {
            addressList = InetAddress.getAllByName(server);
         }
         catch (final UnknownHostException e) {
            throw new RuntimeException(e);
         }
         for (final InetAddress address : addressList) {
            contactPoints.add(new InetSocketAddress(address, 9042));
         }
      }
       */

      return CqlSession.builder()
            .withConfigLoader(loader)
            // .addContactPoints(contactPoints)
            .withLocalDatacenter(cassandraLocalDC)
            .build();
   }

}
