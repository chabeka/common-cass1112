<%@ tag body-content="empty" %>
<%@ attribute name="objetResultats" required="true" type="fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest" %>
<%@ attribute name="pathResultats" required="true" type="java.lang.String" %>
<%@ attribute name="height" required="false" type="java.lang.String" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae" %>


<c:choose>
   <c:when test="${fn:length(height)>0}">
      <c:set var="heightOk" value="${height}" />
   </c:when>
   <c:otherwise>
      <c:set var="heightOk" value="190pt" />
   </c:otherwise>
</c:choose>


<form:hidden path="${pathResultats}.status"/>
<form:hidden path="${pathResultats}.liens"/>

<table border=0 cellpadding=3>
  <tr>
     <td class="titre3">Résultats:</td>
     <td style="width:20px;">&nbsp;</td>
     <sae:statusTest objetStatus="${objetResultats.status}"/>
  </tr>
</table>


<table border=0 cellpadding=4 style="width:100%;">
   <tr>
      <td style="width:75%;">
         <form:textarea path="${pathResultats}.log" cssStyle="width:100%;height:${heightOk};" readonly="true" />
      </td>
      <td style="width:25%; vertical-align:top;">
         <c:forEach var="lien" items="${objetResultats.liens}">
            <a class="lienCas" href="<c:out value="${lien.url}" />">
               <c:out value="${lien.texte}" />
            </a><br />
         </c:forEach>
      </td>
   </tr>
</table>


