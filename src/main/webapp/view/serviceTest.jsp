<%@page import="fr.urssaf.image.sae.anais.portail.util.Base64Utils"%>
<%@page import="org.apache.commons.lang.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
   <title>Test de redirection</title>
</head>

<body>

<div>
   <b style="text-decoration:underline">Jeton d'authentification: </b>
   <pre>
      <%=StringEscapeUtils.escapeHtml(Base64Utils.decode(request.getParameter("SAMLResponse")))%></pre>
   </div>

<div>
   <b style="text-decoration:underline">Service demand&eacute; :</b>
   &nbsp;&nbsp;<%=request.getParameter("RelayState")%>
</div>

</body>

</html>