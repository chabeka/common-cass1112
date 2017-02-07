<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>SAE - Intégration - Tests de non-Regression</title>
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
</style>
</head>
<body>
	<h3 class="text-primary">Tests de non-regression</h3>
	<div class="pull-right">
		<table width="100%">
			<tr>
				<td colspan="2" align="right"><a href='index.do'>Retour
						&agrave; l'accueil</a></td>
			</tr>
		</table>
	</div>


	<hr />

	<div class="row marginBottom20">
		<div class="col-md-6">
			<label>MESSAGE ENVOYE</label>
			<textarea class="form-control" rows="15" cols="50"
				readonly="readonly"><c:out value="${messageOut}" /></textarea>
		</div>
		<div class="col-md-6">
			<label>MESSAGE RECU</label>
			<textarea class="form-control" rows="15" cols="50"
				readonly="readonly"><c:out value="${messageIn}" /></textarea>
		</div>
	</div>
	<form method="post">
		<input type="hidden" name="myValue" value="retourTest" />
		<input type="hidden" name="action" id="retourTest" />
		<td><input type="submit" class="btn btn-primary btn-md"
			onclick="document.getElementById('retourTest').value='retourTest'"
			value="Retourner au résultat des tests" /></td>
	</form>

</body>
</html>
