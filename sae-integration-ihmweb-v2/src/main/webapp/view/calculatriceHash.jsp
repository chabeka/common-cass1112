<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Calculatrice de Hash</title>
</head>
<body>
	<h3 class="text-primary">Calculer le hash d'un fichier sommaire
		contenu sur l'ECDE</h3>
	<div class="pull-right">
		<table width="100%">
			<tr>
				<td colspan="2" align="right"><a href='index.do'>Retour
						&agrave; l'accueil</a></td>
			</tr>

		</table>
	</div>
	<form method="post">
		<input type="hidden" name="action" id="action" /> <input type="text"
			name="cheminFichier"> <input type="submit"
			onclick="document.getElementById('action').value='lancerCalcul'"
			value="Calculer le hash" />
	</form>
	<div><p>${hashCode}</p></div>
</body>
</html>