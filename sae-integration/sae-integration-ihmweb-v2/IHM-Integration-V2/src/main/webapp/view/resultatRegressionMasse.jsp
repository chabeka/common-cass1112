<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>SAE - Intégration - Tests de Regression</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<!-- Twitter boootstrap -->
<link rel="stylesheet" type="text/css"
	href="bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="css/style.css" />
<style>
#inp {
	text-align: center;
	margin: auto;
}

.mesForms {
	float: right;
}
</style>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
	<h3 class="text-primary">Validation des test des traitements de
		masse</h3>
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
		
			<c:forEach var="metadonnee" items="${resMasse}"
				varStatus="status">
				<tr>
				<c:choose>
				<c:when test="${metadonnee.valider=='NON'}">
					<td colspan="3">
						<h4>${metadonnee.name}</h4>
					</td>
						</c:when>
						<c:when test="${metadonnee.valider=='OK'}">
					<td colspan="3">
						<h4 style="color: #2eb82e">${metadonnee.name}</h4>
					</td>
						</c:when>
						<c:otherwise >
					<td colspan="3">
						<h4 style="color: #990000">${metadonnee.name}</h4>
					</td>
						</c:otherwise>
						</c:choose>
						<td style="text-align: center;">
							<form method="post" class="mesForms2">
								<input type="hidden" name="myValue" value="${metadonnee.lienEcde}" />
								<input type="hidden" name="action" id="${metadonnee.name}" />
								<input type="submit" class="btn btn-default btn-md"
									onclick="document.getElementById('${metadonnee.name}').value='detailTestRegressionMasse'"
									value="Afficher les détails du test" />
							</form>
						</td>
						<td style="text-align: center;">
							<form method="post" class="mesForms2"> 
								<input type="hidden" name="myValue" value="${metadonnee.name}" />
								<input type="hidden" name="action" id="${metadonnee.name}" />
								<input type="submit" class="btn btn-default btn-md"
									onclick="document.getElementById('${metadonnee.name}').value='validerTest'"
									value="Valider le test" />
							</form>
						</td>
						<td style="text-align: center;">
						<c:choose>
					<c:when test="${metadonnee.isResultatPresent ne true}">
						<h5 style="color: #990000">Test encore en cour</h5>
					</c:when>
					<c:otherwise>
						<h5 style="color: #2eb82e">Test terminé</h5>
					</c:otherwise>
				</c:choose>
				</td>
				</tr>
			</c:forEach>
		
	</table>
		<form method="post" class="mesForms2">
								<input type="hidden" name="myValue" value="${resMasse}" />
								<input type="hidden" name="action" id="${resMasse}" />
								<input type="submit" class="btn btn-default btn-md"
									onclick="document.getElementById('${resMasse}').value='rafraichir'"
									value="Rafraichir" />
										<form method="post" class="mesForms2">
								<input type="hidden" name="myValue" value="${resMasse}" />
								<input type="hidden" name="action" id="${resMasse}" />
								<input type="submit" class="btn btn-default btn-md"
									onclick="document.getElementById('${resMasse}').value='toutValider'"
									value="Valider tout les tests" />
</body>
</html>