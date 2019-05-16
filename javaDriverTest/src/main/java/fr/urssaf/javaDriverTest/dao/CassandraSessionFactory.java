
package fr.urssaf.javaDriverTest.dao;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.DefaultProtocolVersion;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.internal.core.config.typesafe.DefaultDriverConfigLoader;
import com.datastax.oss.driver.internal.core.config.typesafe.DefaultDriverConfigLoaderBuilder;

/**
 * Permet de créer la session cassandra
 */
public class CassandraSessionFactory {

   public CassandraSessionFactory() {
      // Classe statique
   }

   public static CqlSession getSession(final String cassandraServers, final String cassandraUsername, final String cassandraPassword,
                                       final String cassandraLocalDC) {
      final DefaultDriverConfigLoaderBuilder configBuilder = DefaultDriverConfigLoader.builder()
                                                                                      .withDuration(DefaultDriverOption.REQUEST_TIMEOUT,
                                                                                                    Duration.ofMillis(10000))
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
                                                                                      .withProfile(
                                                                                                   "profile1",
                                                                                                   DefaultDriverConfigLoaderBuilder.profileBuilder()
                                                                                                                                   .build());

      final String[] servers = StringUtils.split(cassandraServers, ",");
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
      return CqlSession.builder()
                       .withConfigLoader(configBuilder.build())
                       .addContactPoints(contactPoints)
                       .withLocalDatacenter(cassandraLocalDC)
                       .build();
   }
}
