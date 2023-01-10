<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="../WEB-INF/common/common-header.jsp"/>
    <title>Register</title>
</head>
<body>
<div class="container">
    <form action="../RegisterController" method="post">
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
        <div class="form-group">
            <label for="email">Email</label>
            <input required name="email" type="email" class="form-control" id="email"
                   placeholder="Email"/>
        </div>
        <button id="submit" type="submit" class="btn btn-primary">Submit</button>
    </form>
</div>

</body>
</html>
