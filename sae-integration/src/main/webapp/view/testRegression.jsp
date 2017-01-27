<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>SAE - Intégration - Tests de Regression</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- Twitter boootstrap -->
<link rel="stylesheet" type="text/css"
	href="bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="css/style.css" />
<style>
#inp {
	text-align: center;
	margin: auto;
}
</style>
</head>
<body>
	<h3 class="text-primary">Resultat des tests de non regression</h3>
	<div class="pull-right">
		<table width="100%">
			<tr>
				<td colspan="2" align="right"><a href='index.do'>Retour
						&agrave; l'accueil</a></td>
			</tr>
		</table>
	</div>


	<hr />

	<table class="table table-bordered">

		<c:forEach var="meta" items="${listeTestXml}">
					<tr>
					<form  method="post">
						<input type="hidden" name="myValue" value="${meta}" />
						<input type="hidden" name="action" id="${meta}" />
						<td><input type="submit" onclick="document.getElementById('${meta}').value='checkboxTest'"
 							value="${meta}" /> </td>
<%--  							<td><a href="">${meta}</a></td> --%>
					</form>
<%-- 					<td><a href="listeTests.do">${meta}</a></td> --%>
<%-- 						<input type="checkbox" name="checkboxName" value="${meta}" /> --%>
<%-- 						<label for="${meta}">${meta}</label> --%>
<!-- 						<br /> -->

<!-- 						<br /> -->
						</tr>
				</c:forEach>
				
	</table> 
	

</body>
</html>
