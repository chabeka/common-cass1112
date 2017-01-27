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
	<h3 class="text-primary">Resultat des tests de non-regression</h3>
	<div class="pull-right">
		<table width="100%">
		<tr>
				<td colspan="2" align="right"><a href='testRegression.do'>Retour
						&agrave; la liste des tests</a></td>
			</tr>
			<tr>
				<td colspan="2" align="right"><a href='index.do'>Retour
						&agrave; l'accueil</a></td>
			</tr>
		</table>
	</div>
	<hr />
	<table class="table table-bordered">
		<c:forEach var="metadonnee" items="${resRegression.resStub}">
			<tr>
				<td style="font-weight: bold;"><c:out value="${metadonnee.key}" /></td>
				<c:forEach var="meta" items="${metadonnee.value}">
				<tr>
					<td style="text-align: center;"><c:out value="${meta.key}" /></td>
					<c:choose>
						<c:when test="${meta.value=='OK'}">
							<td style="text-align: center; color: #00e64d"><c:out
									value="${meta.value}" /></td>
							<br />
						</c:when>
						<c:otherwise>
							<td style="text-align: center; color: #990000"><c:out
									value="${meta.value}" /></td>
							<br />
						</c:otherwise>
					</c:choose>
					</tr>
				</c:forEach>
			</tr>
		</c:forEach>
	</table>


</body>
</html>