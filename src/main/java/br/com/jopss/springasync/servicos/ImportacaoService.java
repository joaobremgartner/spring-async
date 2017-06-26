package br.com.jopss.springasync.servicos;

import br.com.jopss.springasync.modelos.Pessoa;
import br.com.jopss.springasync.web.form.AsyncForm;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Classe que faz a importação de dados do CSV.
 * A persistencia foi feita com query nativa e transação manual 
 * por questão de performace, já que o CSV com 1.5mi de dados.
 * 
 * @author jopss
 */
@Service
public class ImportacaoService {

        @PersistenceContext
        private EntityManager em;
        
        /**
         * Transforma o EntityManager com StatelessSession, por performace.
         * @return StatelessSession
         */
        private StatelessSession createSession(){
                return em.getEntityManagerFactory().createEntityManager().unwrap(Session.class).getSessionFactory().openStatelessSession();
        }
        
        /**
         * Metodo sincrono para importacao de pessoas, gravando no banco de dados.
         * Gera log no servidor (System.out).
         * 
         * @return List<Pessoa> com as pessoas gravadas no banco de dados.
         */
        public List<Pessoa> importarSincronizado() {
                StatelessSession session = this.createSession();
                Transaction tx = session.beginTransaction();
                System.out.println("----------------------------");
                System.out.println(" IMPORTACAO SINCRONIZADA :( ");
                System.out.println("----------------------------");
                List<Pessoa> pessoas = this.importar(session);
                
                System.out.println("Gravando lista de pessoas, aguarde...");
                for(Pessoa p : pessoas){
                        Query nat = session.createNativeQuery("INSERT INTO Pessoa(id,dataCriacao,nome,tipoPessoa) VALUES(?,?,?,?);");
                        nat.setParameter(1, p.getId());
                        nat.setParameter(2, p.getDataCriacao(), TemporalType.TIMESTAMP);
                        nat.setParameter(3, p.getNome());
                        nat.setParameter(4, p.getTipoPessoa());
                        nat.executeUpdate();
                }
                System.out.println("--> Gravacao ok!");

                System.out.println("Efetuando commit da transacao BD, aguarde...");
                tx.commit();
                session.close();
                System.out.println("--> Commit ok!");
                System.out.println("-------------------------");
                return pessoas;
        }

        /**
         * Metodo assincrono para importacao de pessoas, gravando no banco de dados.
         * Gera log no StringBuilder, para o controlador poder devolver ao JSP o log.
         * 
         * @param log StringBuilder
         * @return List<Pessoa> com as pessoas gravadas no banco de dados.
         */
        @Async
        public CompletableFuture<AsyncForm> importarAsync(StringBuilder log) {
                StatelessSession session = this.createSession();
                Transaction tx = session.beginTransaction();
                System.out.println("--------------------------");
                System.out.println(" IMPORTACAO ASSINCRONA :) ");
                System.out.println("--------------------------");
                
                log.append("Carregando dados, aguarde...").append("<br>");
                List<Pessoa> pessoas = this.importar(session);
                log.append("TOTAL DE PESSOAS IMPORTADAS: "+pessoas.size()).append("<br>");
                System.out.println("--------------------------");
                
                log.append("Gravando lista de pessoas, aguarde...").append("<br>");
                for(Pessoa p : pessoas){
                        Query nat = session.createNativeQuery("INSERT INTO Pessoa(id,dataCriacao,nome,tipoPessoa) VALUES(?,?,?,?);");
                        nat.setParameter(1, p.getId());
                        nat.setParameter(2, p.getDataCriacao(), TemporalType.TIMESTAMP);
                        nat.setParameter(3, p.getNome());
                        nat.setParameter(4, p.getTipoPessoa());
                        nat.executeUpdate();
                }
                log.append("--> Gravacao ok!").append("<br>");

                log.append("Efetuando commit da transacao BD, aguarde...").append("<br>");
                tx.commit();
                session.close();
                log.append("--> Commit ok!").append("<br>");
                
                AsyncForm form = new AsyncForm();
                form.addLog("Importado "+pessoas.size()+" itens. Resultado dos 100 primeiros abaixo.");
                form.setPessoas(pessoas);
                return CompletableFuture.completedFuture(form);
        }
        
        /**
         * Tenta remover todas as pessoas do banco de dados e reimporta o arquivo CSV.
         * @param session StatelessSession
         * @return List<Pessoa> pessoas importadas no CSV.
         */
        private List<Pessoa> importar(StatelessSession session) {
                System.out.println("Removendo todas as pessoas...");
                session.createNativeQuery("DELETE FROM Pessoa;").executeUpdate();
                System.out.println("--> Remocao ok!");

                System.out.println("Carregando CSV pessoas...");
                List<Pessoa> pessoas = this.carregarPessoas();
                System.out.println("TOTAL DE PESSOAS IMPORTADAS: "+pessoas.size());

                return pessoas;
        }

        /**
         * Importa as pessoas do arquivo CSV, gerando uma lista de objetos. Nao grava no banco de dados ainda.
         * Exemplo de linha dentro do arquivo:
         * <br>
         * <ul>
         * <li>1,2013-11-11 00:00:00,SEBASTIANA M DE CARVALHO,FISICA</li>
         * </ul>
         * 
         * @return List<Pessoa> pessoas importadas no CSV.
         */
        private List<Pessoa> carregarPessoas() {
                List<Pessoa> lista = new ArrayList<>();
                try {
                        Scanner s = new Scanner(this.getClass().getResourceAsStream("/backup_pessoas.csv"));
                        s.useDelimiter(Pattern.compile("(\\n)"));

                        int count = 0;
                        while (s.hasNextLine()) {
                                count++;
                                String linha = s.nextLine();

                                //somente para log.
                                if (count % 10000 == 0) {
                                        System.out.println(count + " linhas importadas.");
                                }
                                
                                if (linha != null && !linha.isEmpty()) {
                                        String[] dados = linha.split(",");
                                        
                                        Pessoa p = new Pessoa();
                                        p.setId(dados[0]);
                                        p.setDataCriacao( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dados[1]) );
                                        p.setNome(dados[2]);
                                        p.setTipoPessoa(dados[3]);

                                        lista.add(p);
                                }
                        }
                } catch (ParseException e) {
                        throw new RuntimeException(e);
                } 
                return lista;
        }
}
