/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.javaDriverTest.dao;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.UUID;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.context.DriverContext;
import com.datastax.oss.driver.api.core.loadbalancing.LoadBalancingPolicy;
import com.datastax.oss.driver.api.core.metadata.Node;
import com.datastax.oss.driver.api.core.session.Request;
import com.datastax.oss.driver.api.core.session.Session;
import com.datastax.oss.driver.internal.core.util.collection.QueryPlan;

/**
 * TODO (ac75007394) Description du type
 */
public class NoLoadBalancingPolicy implements LoadBalancingPolicy {

   private final String contactPoint;

   private Node nodeToUse;

   public NoLoadBalancingPolicy(final DriverContext context, final String profile) {
      final List<String> contactPoints = context.getConfig().getDefaultProfile().getStringList(DefaultDriverOption.CONTACT_POINTS);
      contactPoint = contactPoints.get(0).replace(":9042", "");
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void init(final Map<UUID, Node> nodes, final DistanceReporter distanceReporter) {
      for (final Entry<UUID, Node> entry : nodes.entrySet()) {
         final Node node = entry.getValue();
         final InetSocketAddress host = node.getBroadcastRpcAddress().get();
         final String hostname = host.getHostName();
         // if (hostname.equals(contactPoint)) {
         if (true) {
            System.out.println("Address:" + host.getAddress().toString());
            nodeToUse = node;
            // break;
         }
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Queue<Node> newQueryPlan(final Request request, final Session session) {
      return new QueryPlan(nodeToUse);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void onAdd(final Node node) {
      // TODO Auto-generated method stub

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void onUp(final Node node) {
      // TODO Auto-generated method stub

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void onDown(final Node node) {
      // TODO Auto-generated method stub

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void onRemove(final Node node) {
      // TODO Auto-generated method stub

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void close() {
      // TODO Auto-generated method stub

   }

}
