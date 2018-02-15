<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"  %>

<html>
<head>
<title>Page 1</title>
</head>
<body>

<p>&nbsp;page 1&nbsp;</p>
<p>Cette page est accessible &agrave; tous</p>


	<div style="margin-left: 45%;
   margin-right: auto;">
	<span>hi&eacute;rarchie des r&ocirc;les</span>

		<ul>

			<sec:authorize access="hasAuthority('ROLE_ADMIN')">
				<li>admin</li>
			</sec:authorize>

			<sec:authorize access="hasAuthority('ROLE_USER')">
				<li>user</li>
			</sec:authorize>

			<sec:authorize access="hasAuthority('ROLE_USER2')">
				<li>user2</li>
			</sec:authorize>

			<sec:authorize access="hasAuthority('ROLE_AUTH')">
				<li>auth</li>
			</sec:authorize>

		</ul>

	</div>

</body>
</html>



