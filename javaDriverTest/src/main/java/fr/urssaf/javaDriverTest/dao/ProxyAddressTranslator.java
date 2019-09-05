/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.javaDriverTest.dao;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.List;

import com.datastax.oss.driver.api.core.addresstranslation.AddressTranslator;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.context.DriverContext;

/**
 * TODO (ac75007394) Description du type
 */
public class ProxyAddressTranslator implements AddressTranslator {

   private final InetSocketAddress proxyAddress;

   public ProxyAddressTranslator(final DriverContext context) {
      final List<String> contactPoints = context.getConfig().getDefaultProfile().getStringList(DefaultDriverOption.CONTACT_POINTS);
      final String contactPoint = contactPoints.get(0).replace(":9042", "").trim();
      InetAddress[] addressList;
      try {
         addressList = InetAddress.getAllByName(contactPoint);
      }
      catch (final UnknownHostException e) {
         throw new RuntimeException(e);
      }
      proxyAddress = new InetSocketAddress(addressList[0], 9042);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public InetSocketAddress translate(final InetSocketAddress address) {
      System.out.println("toto:" + address.getAddress().toString());
      if (address.getAddress().toString().equals("/10.203.34.47")) {
         return proxyAddress;
      }
      return address;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void close() {
      // TODO Auto-generated method stub

   }

}
