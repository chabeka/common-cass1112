<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>SAE - Intégration</title>
<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="description" content="">
	<meta name="author" content="">
		<!-- Twitter boootstrap -->
<link rel="stylesheet" type="text/css"
	href="bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="css/style.css" />
<link
	href="//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css"
	rel="stylesheet">
	<script type="text/javascript" src="js/extjs/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="js/extjs/ext-all-debug.js"></script>
	<script type="text/javascript" src="js/extjs/src/locale/ext-lang-fr.js"></script>
	<style>
.form-group input[type="checkbox"] {
	display: none;
}

.form-group input[type="checkbox"]+.btn-group>label span {
	width: 20px;
}

.form-group input[type="checkbox"]+.btn-group>label span:first-child {
	display: none;
}

.form-group input[type="checkbox"]+.btn-group>label span:last-child {
	display: inline-block;
}

.form-group input[type="checkbox"]:checked+.btn-group>label span:first-child
	{
	display: inline-block;
}

.form-group input[type="checkbox"]:checked+.btn-group>label span:last-child
	{
	display: none;
}
</style>
</head>
<body>
	<h3 class="text-primary">Liste des cas de test pour
		${checkboxValue}</h3>
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

	<div class="[ col-xs-12 col-sm-6 ]">
		<h4>Choisissez les tests que vous desirez lancer</h4>
		<hr />
		<form method="post">
			<input type="hidden" name="action" id="action" />
			<c:forEach var="meta" items="${checkboxRegression}">
				<div class="[ form-group ]">
					<input type="checkbox" name="checkboxName" id="${meta}"
						value="${meta}" checked />
					<div class="[ btn-group ]">
						<label for="${meta}" class="[ btn btn-primary ]"> <span
							class="[ glyphicon glyphicon-ok ]"></span> <span> </span>
						</label> <label for="${meta}" class="[ btn btn-default active ]">
							${meta} </label>
					</div>
				</div>
			</c:forEach>
			<hr />
			<!-- 			<input type="submit" onclick="loadTest()" -->
			<!-- 				value="Lancer les tests sélectionnés" /> -->
			<button class="btn btn-primary btn-lg" id="btnSubmit"
				onclick="loadTest()"
				data-loading-text="<i class='fa fa-spinner fa-spin'></i> Lancement des tests">
				Lancer les tests sélectionnés</button>
			<div style="margin: 3em;">
				<br>
			</div>
		</form>
	</div>
	<script
		src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
	<script
		src='http://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.2.0/js/bootstrap.min.js'></script>
	<script>
      $('#btnSubmit').on('click', function() {
         var $this = $(this);
         $this.button('loading');
         loadTest();
      });

      function loadTest() {
         document.getElementById('action').value = 'lancerTest'
      }
   </script>
</body>
</html>
