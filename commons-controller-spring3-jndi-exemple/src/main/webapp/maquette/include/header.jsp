<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="fr.urssaf.image.commons.controller.spring3.jndi.exemple.service.ParametersComponent"%>
<h1 class="title">
	<c:out value="<%=  ParametersComponent.getTitle() %>"/> Locale = ${pageContext.response.locale}
</h1>
