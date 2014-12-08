<%@ tag body-content="empty" %>
<%@ attribute name="objetFormulaire" required="true" type="fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire" %>
<%@ attribute name="pathFormulaire" required="true" type="java.lang.String" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae" %>


<h4 class="etape text-primary">Parametres du VI</h4>

<table border=0 cellpadding=2>
   <tr valign="top">
      <td>
         <table border=0 cellpadding=2>
            <tr>
               <td><label>Identifiant du CS (<i>issuer</i>):</label></td>
               <td><form:input class="form-control" path='${pathFormulaire}.issuer' cssStyle="width:250px;" /></td>
               <td><h6>(Ex: "SaeIntegration")</h6></td>
            </tr>
            <tr>
               <td><label>Identifiant de l'organisme fournisseur (<i>recipient</i>):</label></td>
               <td><form:input class="form-control" path='${pathFormulaire}.recipient' cssStyle="width:250px;" /></td>
               <td><h6>(Ex: "urn:URSSAF")</h6></td>
            </tr>
            <tr>
               <td><label>Identifiant du service vis&eacute; (<i>audience</i>):</label></td>
               <td><form:input class="form-control" path='${pathFormulaire}.audience' cssStyle="width:250px;" /></td>
               <td><h6>(Ex: "http://sae.urssaf.fr")</h6></td>
            </tr>
            <tr>
               <td valign="top"><label>Liste des PAGM :</label></td>
               <td><form:textarea class="form-control" path="${pathFormulaire}.pagms" cssStyle="width:250px;height:70px;" /></td>
               <td><h6>(Ex: <br />PAGM_A<br />PAGM_B)</h6></td>
            </tr>
         </table>
      </td>
      <td style="width:20pt;">&nbsp;</td>
      <td>
         <table border=0 cellpadding=2>
            <tr>
               <td><label>VI non valide avant (NotBefore):</label></td>
               <td>personnalisation non impl&eacute;ment&eacute;e</td>
               <td>&nbsp;</td>
            </tr>
            <tr>
               <td><label>VI plus valide apr&egrave;s (NotOnOrAfter):</label></td>
               <td>personnalisation non impl&eacute;ment&eacute;e</td>
               <td>&nbsp;</td>
            </tr>
            <tr>
               <td><label>PKCS#12 &agrave; utiliser pour signer le VI:</label></td>
               <td><form:select class="form-control" path="${pathFormulaire}.idCertif">
                  <form:option value="1">IGC AED (val), CN=PNR_Application_Test</form:option>
                  <form:option value="2">IGC AED (val), CN=ApplicationTestSAE</form:option>
                  <form:option value="3">IGC cellule int�gration, CN=APPLICATION_TEST_1</form:option>
               </form:select></td>
               <td>&nbsp;</td>
            </tr>
         </table>
      </td>
   </tr>
</table>
