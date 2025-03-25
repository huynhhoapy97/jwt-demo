<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
	<base href="${pageContext.request.contextPath}/">
	<meta charset="utf-8">
	<title>Đăng nhập</title>
</head>
<body>
    <div>
        <form:form method="POST" action="account/login" modelAttribute="account">
            <div>
                <form:input type="hidden" class="form-control" path="id" />
            </div>
            <div>
                <form:input type="text" class="form-control"
                placeholder="Tên tài khoản"
                autocomplete="off"
                path="userName" />
            </div>
            <div>
                <form:input type="password" class="form-control"
                placeholder="Mật khẩu"
                autocomplete="off"
                path="password" />
            </div>
            <div style="margin-top: 15px">
                <div>
                    <button id="btn-login">Đăng nhập</button>
                </div>
            </div>
        </form:form>
    </div>
</body>
</html>