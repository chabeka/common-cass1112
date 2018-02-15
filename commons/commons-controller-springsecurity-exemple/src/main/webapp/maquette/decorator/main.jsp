<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<title><decorator:title default="TITRE" /></title>
	<decorator:head />
	<c:import url="/view/header.jsp" />
</head>

<body>

	<div id="container">

		<div id="header">
			<%@ include file='/maquette/include/header.jsp'%>
		</div>

		<div>
			<decorator:body />
		</div>

		<div id="footer">
			<%@ include file='/maquette/include/footer.jsp'%>
		</div>
		
	</div>
	
</body>

</html>