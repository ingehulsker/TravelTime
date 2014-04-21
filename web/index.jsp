<%-- 
    Document   : index
    Created on : 18-apr-2014, 23:11:26
    Author     : Inge
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Actuele route informatie</title>
    </head>
    <body>
        <h1>Actuele route informatie</h1>
        <%
            response.sendRedirect("/routeInfo.do?useraction=displayRouteInfo");
        %>
    </body>
</html>
