<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<body>
        <h3>Acesso Assinc Concluído. Acompanhe a execução.</h3>
        <p id="log">
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
                <tbody id="tbody">
                </tbody>
        </table>
        
        <script>
                $(document).ready(function() {
                        $("#bntAsync").addClass('disabled')
                        setInterval(verificarResultado, 1500);
                });

                function verificarResultado(){
                        $.ajax({
                                type: "GET",
                                url: "${pageContext.request.contextPath}/api/pessoa/exec/assinc/verificar",
                                removeBloqueio: true,
                                success: function (data) {
                                        if(data && data.log && data.log!=''){
                                                if(data.log && data.log!=''){
                                                        $("#log").html(data.log);
                                                }
                                                
                                                if(data.pessoas && data.pessoas!=''){
                                                        $("#bntAsync").removeClass('disabled')
                                                        $("#tbody").html("");

                                                        data.pessoas.forEach(function(p, index){
                                                                var tr = $("<tr>");
                                                                var tdId = $("<td>");
                                                                var tdNome = $("<td>");
                                                                var tdTipo = $("<td>");
                                                                var tdData = $("<td>");

                                                                tdId.html(p.id);
                                                                tdNome.html(p.nome);
                                                                tdTipo.html(p.tipoPessoa);
                                                                tdData.html(p.dataCriacao);

                                                                tr.append(tdId);
                                                                tr.append(tdNome);
                                                                tr.append(tdTipo);
                                                                tr.append(tdData);

                                                                $("#tbody").append(tr);
                                                        });
                                                }
                                        }
                                }
                        });
                }
        </script>
</body>
