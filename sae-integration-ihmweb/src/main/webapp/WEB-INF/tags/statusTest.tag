<%@ tag body-content="empty" %>
<%@ attribute name="objetStatus" required="true" type="fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:choose>
    
    
    
    <c:when test="${objetStatus=='Succes'}">
       <td style="width:100px;text-align:center;background-color: green;color: white; font-weight:bold;">
       OK
       </td>
    </c:when>
    
    <c:when test="${objetStatus=='Echec'}">
       <td style="width:100px;text-align:center;background-color: red;color: white; font-weight:bold;">
       KO
       </td>
    </c:when>
    
    <c:when test="${objetStatus=='NonPasse'}">
       <td style="width:100px;text-align:center;background-color: orange;color: black; font-weight:bold;">
       Non exécuté
       </td>
    </c:when>
    
    <c:when test="${objetStatus=='NonLance'}">
       <td style="width:100px;text-align:center;background-color: gray;color: black; font-weight:bold;">
         Non lancé
       </td>
    </c:when>
    
    <c:when test="${objetStatus=='AControler'}">
       <td style="width:100px;text-align:center;background-color: lightblue;color: black; font-weight:bold;">
         A contrôler
       </td>
    </c:when>
    
    <c:when test="${objetStatus=='SansStatus'}">
       <td>&nbsp;</td>
    </c:when>
    
 </c:choose>
     


