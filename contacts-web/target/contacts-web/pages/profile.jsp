<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="../WEB-INF/common/common-header.jsp"/>
    <script src="../js/profile.js" ></script>
    <title>Profile</title>
</head>
<body>
<jsp:include page="../WEB-INF/common/menu.jsp"/>


<div class="container bootstrap snippet">
    <div class="row">
        <div class="col-sm-10"><h1>${sessionScope.currentUser.username}</h1></div>
    </div>
    <div class="row">
        <div class="col-sm-3"><!--left col-->
            <div class="text-center">
                <c:choose>
                    <c:when test="${sessionScope.currentUser.profilePic.length() > 0}">
                        <img src="${sessionScope.currentUser.profilePic}" class="avatar img-circle img-thumbnail"
                             alt="avatar">
                    </c:when>
                    <c:otherwise>
                        <img src="http://ssl.gstatic.com/accounts/ui/avatar_2x.png" class="avatar img-circle img-thumbnail"
                             alt="avatar">
                    </c:otherwise>
                </c:choose>

                <h6>Upload a different photo...</h6>
                <input type="file" class="text-center center-block file-upload">
            </div>
            <br>

        </div><!--/col-3-->
        <div class="col-sm-9">
            <div class="tab-content">
                <div class="tab-pane active" id="home">
                    <hr>
                    <form class="form" action="../UserController" method="post">
                        <div class="form-group">

                            <div class="col-xs-6">
                                <label for="name"><h4>Name</h4></label>
                                <input type="text" class="form-control" name="name" id="name"
                                       placeholder="Name" value="${sessionScope.currentUser.username}">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-xs-6">
                                <label for="email"><h4>Email</h4></label>
                                <input type="email" class="form-control" name="email" id="email"
                                       placeholder="your@email.com" value="${sessionScope.currentUser.email}">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-xs-6">
                                <label for="description"><h4>Description</h4></label>
                                <input type="text" class="form-control" name="description" id="description"
                                       placeholder="a short description" value="${sessionScope.currentUser.description}">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-xs-12">
                                <br>
                                <button class="btn btn-primary" type="submit"><i
                                        class="glyphicon glyphicon-ok-sign"></i> Save
                                </button>
                            </div>
                        </div>
                    </form>

                </div><!--/tab-pane-->
            </div><!--/tab-content-->

        </div><!--/col-9-->
    </div><!--/row-->

</div>
</body>
</html>
