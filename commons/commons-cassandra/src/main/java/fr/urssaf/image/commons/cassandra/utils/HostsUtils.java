package fr.urssaf.image.commons.cassandra.utils;

public class HostsUtils {

  public final static String PORT_THRIFT = "9160";

  public final static String PORT_CQL = "9042";

  /**
   * Construction de host avec port specifique Thrift ou Cql suivant la valeur du booleen
   * 
   * @param hosts
   * @param Cql
   * @return
   */
  public static String buildHost(final String hosts, final boolean Cql) {

    String hostsModifiedFinal = "";
    if (hosts != null && !hosts.isEmpty()) {
      // On split si plusieurs hosts
      final String[] tabHosts1 = hosts.split(",");
      // On boucle sur les hosts
      for (final String hostTemp : tabHosts1) {
        // Obtention du host sans port
        final String[] tabHost2 = hostTemp.split(":");
        hostsModifiedFinal += tabHost2[0].trim() + ":";
        // Affectation du port dédié
        if (Cql) {
          hostsModifiedFinal += PORT_CQL;
        } else {
          hostsModifiedFinal += PORT_THRIFT;
        }
        // On ajoute la virgule pour séparer les hosts
        hostsModifiedFinal += ",";
      }
      // Suppression de la virgule en fin de chaîne
      final char lastChar = hostsModifiedFinal.charAt(hostsModifiedFinal.length() - 1);
      if (lastChar == ',') {
        hostsModifiedFinal = hostsModifiedFinal.substring(0, hostsModifiedFinal.length() - 1);
      }
    }

    return hostsModifiedFinal;
  }

}
