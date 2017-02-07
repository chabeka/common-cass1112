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
    <meta http-equiv="X-UA-Compatible" content="IE=edge" >
    <meta name="viewport" content="width=device-width, initial-scale=1" >
    <meta name="description" content="" >
    <meta name="author" content="" >
<!-- Twitter boootstrap -->
<link rel="stylesheet" type="text/css"
	href="bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="css/style.css" />
<style>
#inp {
	text-align: center;
	margin: auto;
}
.mesForms
{
float:right;
}
</style>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
	<h3 class="text-primary">Résultats des tests de non-regression</h3>
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

	<c:forEach var="metadonnee" items="${resRegression.resStub}"
		varStatus="status">
		<div class="container">
			<div class="panel-group">
				<c:choose>
					<c:when test="${resRegression.testOkKo[metadonnee.key]=='OK'}">
						<div class="panel panel-success">
					</c:when>
					<c:otherwise>
						<div class="panel panel-danger">
					</c:otherwise>
				</c:choose>
				<div class="panel-heading">
					<h4 class="panel-title">
						<c:choose>
							<c:when test="${resRegression.testOkKo[metadonnee.key]=='OK'}">
								<a data-toggle="collapse" href="#${status.count}">${metadonnee.key}</a>

							</c:when>
							<c:otherwise>

								<a data-toggle="collapse" href="#${status.count}">${metadonnee.key}</a>

							</c:otherwise>
						</c:choose>
					</h4>
				</div>
				<div id="${status.count}" class="panel-collapse collapse">
					<c:forEach var="meta" items="${metadonnee.value}"
						varStatus="status2">
						<c:choose>
							<c:when test="${meta.value=='OK'}">
								<div class="panel-body" style="color: #2eb82e">${meta.key}
									<form method="post" class="mesForms">
										<input type="hidden" name="myValue" value="${meta.key}" />
										<input type="hidden" name="action" id="${meta.key}" />
										<input type="submit" class="btn btn-default btn-md"
											onclick="document.getElementById('${meta.key}').value='detailTest'"
											value="Afficher les détails du test" />
									</form>
								</div>

								<!--         <div class="panel-footer">Panel Footer</div> -->
							</c:when>
							<c:otherwise>
								<div class="panel-body" style="color: #990000">${meta.key}
									<form method="post" class="mesForms">
										<input type="hidden" name="myValue" value="${meta.key}" />
										<input type="hidden" name="action" id="${meta.key}" />
										<input type="submit" class="btn btn-default btn-md"
											onclick="document.getElementById('${meta.key}').value='detailTest'"
											value="Afficher les détails du test" />
									</form>
								</div>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</div>
			</div>
		</div>
		</div>
	</c:forEach>

</body>
</html>