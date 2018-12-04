<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript">
	window.onload=function(){
		var numEtape =document.getElementById('etape').value;
		if(numEtape!="" && numEtape!=null){
			document.getElementById('etape-'+numEtape).scrollIntoView(true);
		}     
	};
</script>

<table border="0" cellpadding="0" style="width: 100%;">
	<tr style="width: 100%;">
		<td style="width: 75%;">
			<h3 class="text-primary">SAE - Int&eacute;gration / <c:out
				value="${casTest.categorie}" /> / <c:out value="${casTest.code}" />
			</h3>
		</td>
		<td style="width: 25%; text-align: right;"><a
			href='testsLibres.do'>Retour &agrave; la liste des tests</a><input type="hidden" name="id"
			value='<c:out value="${requestScope['id']}" />' />
		</td>
	</tr>
	<tr>
		<td colspan="2" align="right"><a href='listeTests.do'>Retour &agrave; l'&eacute;cran r&eacute;aliser des tests</a></td>
	</tr>
	<tr>
		<td colspan="2" align="right"><a href='index.do'>Retour &agrave; l'accueil</a></td>
	</tr>
</table>

<hr />

<h5 class="bg-success pad15"><c:out value="${casTest.description}" /></h5>

<hr />
