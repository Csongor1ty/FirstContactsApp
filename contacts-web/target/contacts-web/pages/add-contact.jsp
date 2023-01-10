<%@ page import="hu.alkfejl.model.Phone" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<html>
<head>
    <jsp:include page="../WEB-INF/common/common-header.jsp"/>
    <script src="${pageContext.request.contextPath}/js/add-contact.js"></script>
    <title>Add Contact</title>
</head>
<body>
<jsp:include page="../WEB-INF/common/menu.jsp"/>
<jsp:useBean id="contact" class="hu.alkfejl.model.Contact" scope="request" />

<div class="container">
    <form action="${pageContext.request.contextPath}/ContactController" method="post">
        <input type="hidden" name="id" value="${contact.id}" />
        <div class="form-group">
            <label for="name">Name</label>
            <input required name="name" type="text" class="form-control" id="name"
                   placeholder="Enter name" value="${contact.name}"/>
        </div>
        <div class="form-group">
            <label for="email">Email</label>
            <input required name="email" type="email" class="form-control" id="email"
                   placeholder="Email" value="${contact.email}"/>
        </div>
        <div class="form-group">
            <label for="dateOfBirth">Date of Birth</label>
            <input required id="dateOfBirth" name="dateOfBirth" type="date" class="form-control"
                   placeholder="Date of Birth" value="${contact.dateOfBirth}"/>
        </div>
        <div class="form-group">
            <label>Phones</label>
            <c:if test="${contact.phones.size() > 0}">
                <c:forEach var="phone" items="${contact.phones}">
                    <div class="row">
                        <input type="hidden" name="phoneIds" value="${phone.id}" />
                        <div class="col">
                            <input name="phoneValues" type="text" class="form-control mb-3"
                                   placeholder="Enter phone number" value="${phone.number}"/>
                        </div>
                        <div class="col">
                            <select name="phoneTypes" class="custom-select">
                                <c:forEach var="phoneType" items="<%=Phone.PhoneType.values()%>">
                                    <c:if test="${phone.phoneType.value.equals(phoneType.value)}">
                                        <option selected="true" value="${phoneType.value}">${phoneType.value}</option>
                                    </c:if>
                                    <c:if test="${!phone.phoneType.value.equals(phoneType.value)}">
                                        <option value="${phoneType.value}">${phoneType.value}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col md-2">
                            <c:if test="${phone.equals(contact.phones.get(contact.phones.size() - 1))}">
                                <button type="button" class="btn btn-secondary" onclick="newRow(this)">New</button>
                            </c:if>
                            <c:if test="${!phone.equals(contact.phones.get(contact.phones.size() - 1))}">
                                <button type="button" class="btn btn-secondary" onclick="deleteRow(this)">Delete</button>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
            </c:if>
            <c:if test="${contact.phones == null or contact.phones.size() == 0}">
                <div class="row">
                    <div class="col">
                        <input name="phoneValues" type="text" class="form-control mb-3"
                               placeholder="Enter phone number"/>
                    </div>
                    <div class="col">
                        <select name="phoneTypes" class="custom-select">
                            <c:forEach var="phoneType" items="<%=Phone.PhoneType.values()%>">
                                <option value="${phoneType.value}">${phoneType.value}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col md-2">
                        <button onclick="newRow(this)" type="button" class="btn btn-secondary">New</button>
                    </div>
                </div>
            </c:if>
        </div>
        <div class="form-group">
            <label for="address">Address</label>
            <input id="address" name="address" type="text" class="form-control" id="address"
                   placeholder="Address" value="${contact.address}"/>
        </div>
        <div class="form-group">
            <label for="company">Company</label>
            <input id="company" name="company" type="text" class="form-control" id="company"
                   placeholder="Company" value="${contact.company}"/>
        </div>
        <div class="form-group">
            <label for="address">Position</label>
            <input id="position" name="position" type="text" class="form-control" id="position"
                   placeholder="Position" value="${contact.position}"/>
        </div>
        <button id="submit" type="submit" class="btn btn-primary">Submit</button>
    </form>
</div>
</body>
</html>