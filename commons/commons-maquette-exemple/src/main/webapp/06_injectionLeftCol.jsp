<%
HttpSession hs = request.getSession() ;
hs.setAttribute("PageInjectionLeftCol", "1") ;
%>
<h1>Titre de ma JSP : injectionLeftCol.jsp</h1>
<div>
Cette page ne contient pas de balise &lt;html&gt; &lt;head&gt; ou &lt;body&gt;<br /> 
Par contre vous devriez avoir une bo�te contextuelle sp�cifique sur la gauche. Son titre est "Divers".
</div>