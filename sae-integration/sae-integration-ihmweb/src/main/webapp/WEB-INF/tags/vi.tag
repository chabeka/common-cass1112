<%@ tag body-content="empty" %>
<%@ attribute name="objetFormulaire" required="true" type="fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire" %>
<%@ attribute name="pathFormulaire" required="true" type="java.lang.String" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae" %>


<p style="font-weight:bold;text-decoration:underline;">Paramètres du VI</p>

<table border=0 cellpadding=2>
   <tr valign="top">
      <td>
         <table border=0 cellpadding=2>
            <tr>
               <td>Identifiant du CS (<i>issuer</i>):</td>
               <td><form:input path='${pathFormulaire}.issuer' cssStyle="width:150pt;" /></td>
               <td>(exemple : "SaeIntegration")</td>
            </tr>
            <tr>
               <td>Identifiant de l'organisme fournisseur (<i>recipient</i>):</td>
               <td><form:input path='${pathFormulaire}.recipient' cssStyle="width:150pt;" /></td>
               <td>(exemple : "urn:URSSAF")</td>
            </tr>
            <tr>
               <td>Identifiant du service visé (<i>audience</i>):</td>
               <td><form:input path='${pathFormulaire}.audience' cssStyle="width:150pt;" /></td>
               <td>(exemple : "http://sae.urssaf.fr")</td>
            </tr>
            <tr>
               <td valign="top">Liste des PAGM :</td>
               <td><form:textarea path="${pathFormulaire}.pagms" cssStyle="width:150pt;height:60px;" /></td>
               <td>(exemple : <br />PAGM_A<br />PAGM_B)</td>
            </tr>
         </table>
      </td>
      <td style="width:20pt;">&nbsp;</td>
      <td>
         <table border=0 cellpadding=2>
            <tr>
               <td>VI non valide avant (NotBefore):</td>
               <td>personnalisation non implémentée</td>
               <td>&nbsp;</td>
            </tr>
            <tr>
               <td>VI plus valide après (NotOnOrAfter):</td>
               <td>personnalisation non implémentée</td>
               <td>&nbsp;</td>
            </tr>
            <tr>
               <td>PKCS#12 à utiliser pour signer le VI:</td>
               <td><form:select path="${pathFormulaire}.idCertif">
                  <form:option value="1">IGC AED (val), CN=PNR_Application_Test</form:option>
                  <form:option value="2">IGC AED (val), CN=ApplicationTestSAE</form:option>
                  <form:option value="3">IGC cellule intégration, CN=APPLICATION_TEST_1</form:option>
               </form:select></td>
               <td>&nbsp;</td>
            </tr>
         </table>
      </td>
   </tr>
</table>


<hr />