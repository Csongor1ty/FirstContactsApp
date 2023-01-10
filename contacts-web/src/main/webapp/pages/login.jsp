<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<html>
<head>
    <jsp:include page="../WEB-INF/common/common-header.jsp"/>
    <title>Login</title>
</head>
<body>
<div class="container">
    <form action="../LoginController" method="post">
        <div class="form-group">
            <label for="username">Username</label>
            <input required name="username" type="text" class="form-control" id="username"
                   placeholder="Username"/>
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input required name="password" type="password" class="form-control" id="password"
                   placeholder="Password"/>
        </div>
        <button id="submit" type="submit" class="btn btn-primary">Submit</button>
        <span><a href="register.jsp">Register</a></span>
    </form>
</div>
</body>
</html>
