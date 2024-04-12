<%--
  Created by IntelliJ IDEA.
  User: valeryia
  Date: 22.03.2024
  Time: 15:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div>
  <span>CONTENT русский</span>
  <p>Size: ${requestScope.flights.size()}</p>
    <p>Description: ${requestScope.flights.get(0).description()}</p>
    <p>Id: ${requestScope.flights[1].id()}</p>
    <p>JSESSIONID: ${cookie.get("JSESSIONID")}</p>
    <p>PARAM id: ${param.id}</p>
    <p>HEADER id: ${header["cookie"]}</p>
    <p>NOT EMPTY: ${not empty flights}</p>
</div>

</body>
</html>
