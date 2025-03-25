<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
	<base href="${pageContext.request.contextPath}/">
	<meta charset="utf-8">
	<title>Dashboard</title>
</head>
<body>
    <div>
        <h3>Welcome</h3><br><br>
        <input type="button" id="btn-access-data" value="Truy cập thông tin" />
    </div>

    <!-- jQuery -->
    <script src="resources/js/jquery.min.js"></script>

    <script type="text/javascript">
        $(document).ready(function(){
            let cookies = document.cookie.split('; ');
            let tokenValue = '';

            for (let cookie of cookies) {
                let [key, value] = cookie.split('=');
                if (key === 'token')
                    tokenValue = value;
            }

            $("#btn-access-data").click(function(){
                $.ajax({
                    url: 'account/access-data',
                    type: 'POST',
                    headers: {
                        'Authorization': 'Bearer '.concat(tokenValue)
                    },
                    contentType: 'application/json',
                    data: JSON.stringify({
                        id: '1',
                        userName: 'hoa',
                        password: '***'
                    }),
                    success: function (data) {
                        alert(data);
                    },
                    error: function (xhr, status, error) {
                        console.error('Lỗi:', error);
                    }
                });
            })
        })
    </script>
</body>
</html>