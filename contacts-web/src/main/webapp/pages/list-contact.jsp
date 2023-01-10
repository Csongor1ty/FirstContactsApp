<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8" %>

<html>
<head>
    <jsp:include page="../WEB-INF/common/common-header.jsp"/>
    <title>List Person</title>
</head>
<body>
<jsp:include page="../WEB-INF/common/menu.jsp"/>
<jsp:include page="/ContactController"/>
<div class="container">
    <table class="table">
        <thead class="thead-dark">
        <tr>
            <th scope="col">Name</th>
            <th scope="col">Email</th>
            <th scope="col">Phones</th>
            <th scope="col">Birth Date</th>
            <th scope="col">Address</th>
            <th scope="col">Company</th>
            <th scope="col">Position</th>
            <th scope="col">Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${requestScope.contactList}">
            <tr>
                <td>${item.name}</td>
                <td>${item.email}</td>
                <td>
                    <c:forEach var="phone" items="${item.phones}">
                        <div>${phone}</div>
                    </c:forEach>
                </td>
                <td>${item.dateOfBirth}</td>
                <td>${item.address}</td>
                <td>${item.company}</td>
                <td>${item.position}</td>
                <td>
                    <a href="../UpdateContactController?contactId=${item.id}"><i class="fas fa-edit"></i></a>
                    <a href="../DeleteContactController?contactId=${item.id}"><i class="fas fa-trash"></i></a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>


</body>
</html>

