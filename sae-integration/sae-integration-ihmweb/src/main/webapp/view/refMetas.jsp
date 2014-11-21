<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>SAE - Intégration - Référentiel des métadonnées</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- Twitter boootstrap -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css" />
</head>
<body>
	<div class="container">
		<table width="100%">
			<tr>
				<td style="width: 75%">
				<p class="titre1">SAE - Intégration - Référentiel des métadonnées</p>
				</td>
				<td style="width: 25%" align="right"><a href="index.do">&lt;&lt;&nbsp;Retour à l&apos;accueil </a></td>
			</tr>
			<tr>
				<td colspan="2" align="right"><a href="listeTests.do">Liste
				des cas de test&nbsp;&gt;&gt; </a></td>
			</tr>
		</table>

		<table class="table table-bordered">
			<tr style="font-weight: bold;; background-color: #3ebaba;">
				<td>Code long</td>
				<td style="text-align: center">Code court</td>
				<td style="text-align: center;">Spécifiable à l'archivage</td>
				<td style="text-align: center;">Obligatoire à l'archivage</td>
				<td style="text-align: center;">Consultée par défaut</td>
				<td style="text-align: center;">Consultable</td>
				<td style="text-align: center;">Critère de recherche</td>
				<td style="text-align: center;">Exposée client</td>
				<td style="text-align: center;">Obligatoire au stockage</td>
			</tr>
		
			<c:forEach var="metadonnee" items="${refMetas}">
				<tr>
					<td style="font-weight: bold;"><c:out value="${metadonnee.codeLong}" /></td>
					<td style="text-align: center;"><c:out value="${metadonnee.codeCourt}" /></td>
					<td style="text-align: center;">${sae:booleanToOuiNon(metadonnee.archivablePossible)}</td>
					<td style="text-align: center;">${sae:booleanToOuiNon(metadonnee.archivableObligatoire)}</td>
					<td style="text-align: center;">${sae:booleanToOuiNon(metadonnee.consulteeParDefaut)}</td>
					<td style="text-align: center;">${sae:booleanToOuiNon(metadonnee.consultable)}</td>
					<td style="text-align: center;">${sae:booleanToOuiNon(metadonnee.critereRecherche)}</td>
					<td style="text-align: center;">${sae:booleanToOuiNon(metadonnee.client)}</td>
					<td style="text-align: center;">${sae:booleanToOuiNon(metadonnee.obligatoireAuStockage)}</td>
				</tr>
		
			</c:forEach>
		
		</table>
	</div>
</body>
</html>
