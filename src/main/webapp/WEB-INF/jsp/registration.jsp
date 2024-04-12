<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: valeryia
  Date: 24.03.2024
  Time: 17:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/registration" method="post">
    <label for="name">Name:
        <input type="text" name="name" id="name">
    </label><br/>
    <label for="birthday">Birthday:
        <input type="date" name="birthday" id="birthday">
    </label><br/>
    <label for="email">Email:
        <input type="text" name="email" id="email">
    </label><br/>
    <label for="pwd">Password:
        <input type="password" name="pwd" id="pwd">
    </label><br/>
    <select name="role">
        <c:forEach var="role" items="${roles}">
            <option value="${role}">${role}</option>
        </c:forEach>
    </select>
    <br/>
    <c:forEach var="gender" items="${genders}">
        <input type="radio" name="gender" value="${gender}">${gender}
        <br/>
    </c:forEach>
    <input type="submit" value="Send">
</form>

<c:if test="${not empty errors}">
    <div style="color: red">
        <c:forEach var="error" items="${errors}">
            <span>${error.message}</span>
            <br>
        </c:forEach>
    </div>
</c:if>
</body>
</html>
