import org.junit.Assert;
import org.junit.Test;

import fr.urssaf.image.commons.cassandra.utils.HostsUtils;

/**
 *  TODO (AC75095351) Description du fichier
 */

/**
 * TODO (AC75095351) Description du type
 *
 */

public class HostsUtilsTest {

  @Test
  public void testSimpleHostThrift() {
    final String host = "cnp.test.er542";
    final String hostActual = HostsUtils.buildHost(host, false);
    final String hostExpected = host + ":" + HostsUtils.PORT_THRIFT;
    Assert.assertEquals(hostActual, hostExpected);

  }

  @Test
  public void testSimpleHostThriftWithPort() {
    final String host = "cnp.test.er542:9042";
    final String hostActual = HostsUtils.buildHost(host, false);
    final String hostExpected = "cnp.test.er542:" + HostsUtils.PORT_THRIFT;
    Assert.assertEquals(hostActual, hostExpected);
  }

  @Test
  public void testSimpleHostCql() {
    final String host = "cnp.test.er542";
    final String hostActual = HostsUtils.buildHost(host, true);
    final String hostExpected = host + ":" + HostsUtils.PORT_CQL;
    Assert.assertEquals(hostActual, hostExpected);
  }

  @Test
  public void testSimpleHostCqlWithPort() {
    final String host = "cnp.test.er542:9160";
    final String hostActual = HostsUtils.buildHost(host, true);
    final String hostExpected = "cnp.test.er542:" + HostsUtils.PORT_CQL;
    Assert.assertEquals(hostActual, hostExpected);
  }

  @Test
  public void testListHostThrift() {
    final String host = "cnp.test.er542,cnp.test.er543";
    final String hostActual = HostsUtils.buildHost(host, false);
    final String hostExpected = "cnp.test.er542:" + HostsUtils.PORT_THRIFT + ",cnp.test.er543:" + HostsUtils.PORT_THRIFT;
    Assert.assertEquals(hostActual, hostExpected);

  }

  @Test
  public void testListHostCql() {
    final String host = "cnp.test.er542,cnp.test.er543";
    final String hostActual = HostsUtils.buildHost(host, true);
    final String hostExpected = "cnp.test.er542:" + HostsUtils.PORT_CQL + ",cnp.test.er543:" + HostsUtils.PORT_CQL;
    Assert.assertEquals(hostActual, hostExpected);
  }

  @Test
  public void testListHostThriftWithPort() {
    final String host = "cnp.test.er542:9142,cnp.test.er543:9160";
    final String hostActual = HostsUtils.buildHost(host, false);
    final String hostExpected = "cnp.test.er542:" + HostsUtils.PORT_THRIFT + ",cnp.test.er543:" + HostsUtils.PORT_THRIFT;
    Assert.assertEquals(hostActual, hostExpected);

  }

  @Test
  public void testListHostEmpty() {
    final String host = "";
    final String hostActual = HostsUtils.buildHost(host, false);
    final String hostExpected = "";
    Assert.assertEquals(hostActual, hostExpected);

  }
}
