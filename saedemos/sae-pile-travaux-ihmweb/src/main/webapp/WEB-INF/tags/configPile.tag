<%@ tag body-content="empty" %>
<%@ attribute name="objetFormulaire" required="true" type="fr.urssaf.image.sae.pile.travaux.ihmweb.modele.CassandraEtZookeeperConfig" %>
<%@ attribute name="pathFormulaire" required="true" type="java.lang.String" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/tld/sae.tld" prefix="sae" %>


<script type="text/javascript">

   function majConf(select) {

      var idxModele = select.selectedIndex;

      if (idxModele == 0) {
         // Ne rien faire : saisie libre.
      } else if (idxModele == 1) {
         setValues("cer69-ds4int.cer69.recouv:2181", "SAE",
               "cer69imageint9.cer69.recouv:9160", "root", "regina4932",
               "SAE");
      } else if (idxModele == 2) {
         setValues("cer69-saeint1.cer69.recouv", "SAE",
               "cer69-saeint1.cer69.recouv:9160", "root", "regina4932",
               "SAE");
      } else if (idxModele == 3) {
         setValues(
               "hwi69devsaeapp1.cer69.recouv,hwi69devsaeapp2.cer69.recouv",
               "SAE",
               "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160",
               "root", "regina4932", "SAE");
      } else if (idxModele == 4) {
         setValues(
               "hwi69ginsaeappli1.cer69.recouv,hwi69ginsaeappli2.cer69.recouv",
               "SAE",
               "hwi69ginsaecas1.cer69.recouv:9160,hwi69ginsaecas2.cer69.recouv:9160",
               "root", "regina4932", "SAE");
      } else if (idxModele == 5) {
         setValues(
               "hwi69givnsaeappli.cer69.recouv",
               "SAE",
               "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160",
               "root", "regina4932", "SAE");
      } else {
         alert('Valeur inconnue');
      }

   }

   function setValues(zooKeeperHost, zookeeperNs, cassandraHost,
         cassandraUserName, cassandraPassword, cassandraKeyspace) {
      document.getElementById("connexionConfig.zookeeperHosts").value = zooKeeperHost;
      document.getElementById("connexionConfig.zookeeperNamespace").value = zookeeperNs;
      document.getElementById("connexionConfig.cassandraHosts").value = cassandraHost;
      document.getElementById("connexionConfig.cassandraUserName").value = cassandraUserName;
      document.getElementById("connexionConfig.cassandraPassword").value = cassandraPassword;
      document.getElementById("connexionConfig.cassandraKeySpace").value = cassandraKeyspace;
   }
</script>


<table class="tabPile" style="width: 100%;">
   <tr>
      <td colspan="2"><b>Configuration Zookeeper/Cassandra</b></td>
   </tr>
   <tr>
      <td style="width:25%;">Charger un modèle de configuration :</td>
      <td style="width:75%;">
         <select onchange="javascript:majConf(this);">
            <option value="libre">Saisie libre</option>
            <option value="dev1">Développement #1 (ds4/int9)</option>
            <option value="dev2">Développement #2 (saeint1)</option>
            <option value="integ">Intégration interne (hwi69devsae*)</option>
            <option value="integNat">Intégration nationale (GIN)</option>
            <option value="valid">Validation nationale (GIVN)</option>
         </select>
      </td>
   </tr>
   <tr>
      <td>Zookeeper Hosts :</td>
      <td><form:input path="${pathFormulaire}.zookeeperHosts" cssStyle="width:100%;" /></td>
   </tr>
   <tr>
      <td>Zookeeper Namespace :</td>
      <td><form:input path="${pathFormulaire}.zookeeperNamespace" cssStyle="width:100%;" /></td>
   </tr>
   <tr>
      <td>Cassandra Hosts :</td>
      <td><form:input path="${pathFormulaire}.cassandraHosts" cssStyle="width:100%;" /></td>
   </tr>
   <tr>
      <td>Cassandra Username :</td>
      <td><form:input path="${pathFormulaire}.cassandraUserName" cssStyle="width:100%;" /></td>
   </tr>
   <tr>
      <td>Cassandra Password :</td>
      <td><form:input path="${pathFormulaire}.cassandraPassword" cssStyle="width:100%;" /></td>
   </tr>
   <tr>
      <td>Cassandra Keyspace :</td>
      <td><form:input path="${pathFormulaire}.cassandraKeySpace" cssStyle="width:100%;" /></td>
   </tr>
</table>

<br />
