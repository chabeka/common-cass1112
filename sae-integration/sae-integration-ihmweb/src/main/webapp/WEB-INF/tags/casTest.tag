<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript">



window.onload=function(){
	var numEtape =document.getElementById('etape').value;
   if(numEtape!="" && numEtape!=null){
      document.getElementById('etape-'+numEtape).scrollIntoView(true);
      };     
}
</script>

<table border="0" cellpadding="0" style="width: 100%;">
	<tr style="width: 100%;">
		<td style="width: 75%;">
		<p class="titreTest">SAE - Intégration / <c:out
			value="${casTest.categorie}" /> / <c:out value="${casTest.code}" /></p>
		</td>
		<td style="width: 25%; text-align: right;"><a
			href='listeTests.do?action=detail&id=<c:out value="${requestScope['id']}"/>'>Retour
		à la liste des tests</a><input type="hidden" name="id"
			value='<c:out value="${requestScope['id']}" />' /></td>
	</tr>
	<tr>
		<td colspan="2" align="right"><a href='listeTests.do'>Retour
		à la liste des catégories de tests</a></td>
	</tr>
	<tr>
		<td colspan="2" align="right"><a href='index.do'>Retour à
		l'accueil</a></td>
	</tr>
</table>

<hr />

<p class="descCasTest"><c:out value="${casTest.description}" /></p>

<hr />
