<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<nav class="navbar fixed-top navbar-expand-lg navbar-light bg-light">
    <button class="navbar-toggler" type="button" data-toggle="collapse"
            data-target="#navbarNavDropdown" aria-controls="navbarNavDropdown"
            aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNavDropdown">
        <ul class="navbar-nav w-100">
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/pages/add-contact.jsp">Add Contact</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/pages/list-contact.jsp">List Contacts</a></li>

            <c:if test="${sessionScope.currentUser.username != null}">
                <li class="nav-item dropdown ml-auto">
                    <a class='nav-link dropdown-toggle' href='#' id='navbarDropdownMenuLink' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>
                            ${sessionScope.currentUser.username}
                    </a>
                    <div class='dropdown-menu dropdown-menu-right' aria-labelledby='navbarDropdownMenuLink'>
                        <a class='dropdown-item' href='${pageContext.request.contextPath}/pages/profile.jsp'>Profile</a>
                        <a class='dropdown-item' href='${pageContext.request.contextPath}/LogoutController'>Logout</a>
                    </div>
                </li>
            </c:if>
        </ul>
    </div>
</nav>