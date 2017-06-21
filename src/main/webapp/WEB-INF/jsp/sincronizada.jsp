<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<body>
        <h3>Importação Sincronizada Concluída.</h3>
        <p>
                Importado ${quantidade} itens. Resultado dos 100 primeiros abaixo.</h3>
        </p>
        <table class="table table-bordered">
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
                                        <td>${p.tipoPessoa}</td>
                                        <td><fmt:formatDate pattern="dd/MM/yyyy HH:mm:ss" value="${p.dataCriacao}"/></td>
                                </tr>
                        </c:forEach>
                </tbody>
        </table>
</body>
