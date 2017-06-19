<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
        <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <title>Spring Async Exemplo</title>

                <!-- Latest compiled and minified CSS -->
                <link rel="stylesheet" href="resources/css/bootstrap.min.css">
                <link rel="stylesheet" href="resources/css/bootstrap-theme.min.css">

                <script src="resources/js/jquery-3.2.1.min.js"></script>
                <script src="resources/js/bootstrap.min.js"></script>

        </head>
        <body>
                <h3>Importação Sincronizada Concluída. Resultado abaixo.</h3>
                <table>
                        <thead>
                                <tr>
                                        <th>ID</th>
                                        <th>Nome</th>
                                        <th>Tipo</th>
                                        <th>Data Criacao</th>
                                </tr>
                        </thead>
                        <tbody>
                                <c:forEach var="p" items="${pessoas}">
                                        <tr>
                                                <td>${p.id}</td>
                                                <td>${p.nome}</td>
                                                <td>${p.tipo}</td>
                                                <td>${p.dataCriacao}</td>
                                        </tr>
                                </c:forEach>
                        </tbody>
                </table>
        </body>
</html>
