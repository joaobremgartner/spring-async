package br.com.jopss.springasync.servicos;

import br.com.jopss.springasync.modelos.Pessoa;
import br.com.jopss.springasync.web.form.AsyncForm;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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

@Service
public class ImportacaoService {

        @PersistenceContext
        private EntityManager em;
        
        private StatelessSession session;
        private StringBuilder log = new StringBuilder();

        public StringBuilder getLog() {
                return log;
        }
        
        private StatelessSession getSession(){
                if(session == null || !session.isOpen()){
                        session = em.getEntityManagerFactory().createEntityManager().unwrap(Session.class).getSessionFactory().openStatelessSession();
                }
                return session;
        }
        
        public List<Pessoa> importarSincronizado() {
                Transaction tx = getSession().beginTransaction();
                System.out.println("----------------------------");
                System.out.println(" IMPORTACAO SINCRONIZADA :( ");
                System.out.println("----------------------------");
                List<Pessoa> pessoas = this.importar();
                
                System.out.println("Gravando lista de pessoas, aguarde...");
                for(Pessoa p : pessoas){
                        Query nat = getSession().createNativeQuery("INSERT INTO Pessoa(id,dataCriacao,nome,tipoPessoa) VALUES(?,?,?,?);");
                        nat.setParameter(1, p.getId());
                        nat.setParameter(2, p.getDataCriacao(), TemporalType.TIMESTAMP);
                        nat.setParameter(3, p.getNome());
                        nat.setParameter(4, p.getTipoPessoa());
                        nat.executeUpdate();
                }
                System.out.println("--> Gravacao ok!");

                System.out.println("Efetuando commit da transacao BD, aguarde...");
                tx.commit();
                getSession().close();
                System.out.println("--> Commit ok!");
                System.out.println("-------------------------");
                return pessoas;
        }

        @Async
        public CompletableFuture<AsyncForm> importarAsync() {
                log = new StringBuilder();
                
                Transaction tx = getSession().beginTransaction();
                System.out.println("--------------------------");
                System.out.println(" IMPORTACAO ASSINCRONA :) ");
                System.out.println("--------------------------");
                List<Pessoa> pessoas = this.importar();
                System.out.println("--------------------------");
                
                log.append("Gravando lista de pessoas, aguarde...").append("<br>");
                for(Pessoa p : pessoas){
                        Query nat = getSession().createNativeQuery("INSERT INTO Pessoa(id,dataCriacao,nome,tipoPessoa) VALUES(?,?,?,?);");
                        nat.setParameter(1, p.getId());
                        nat.setParameter(2, p.getDataCriacao(), TemporalType.TIMESTAMP);
                        nat.setParameter(3, p.getNome());
                        nat.setParameter(4, p.getTipoPessoa());
                        nat.executeUpdate();
                }
                log.append("--> Gravacao ok!").append("<br>");

                log.append("Efetuando commit da transacao BD, aguarde...").append("<br>");
                tx.commit();
                getSession().close();
                log.append("--> Commit ok!").append("<br>");
                
                AsyncForm form = new AsyncForm();
                form.addLog("Importado "+pessoas.size()+" itens. Resultado dos 100 primeiros abaixo.");
                form.setPessoas(pessoas);
                return CompletableFuture.completedFuture(form);
        }
        
        private List<Pessoa> importar() {
                System.out.println("Removendo todas as pessoas...");
                log.append("Removendo todas as pessoas...").append("<br>");
                getSession().createNativeQuery("DELETE FROM Pessoa;").executeUpdate();
                System.out.println("--> Remocao ok!");
                log.append("--> Remocao ok!").append("<br>");

                System.out.println("Carregando CSV pessoas...");
                log.append("Carregando CSV pessoas...").append("<br>");
                List<Pessoa> pessoas = this.carregarPessoas();
                System.out.println("TOTAL DE PESSOAS IMPORTADAS: "+pessoas.size());
                log.append("TOTAL DE PESSOAS IMPORTADAS: "+pessoas.size()).append("<br>");;

                return pessoas;
        }

        //1,2013-11-11 00:00:00,SEBASTIANA M DE CARVALHO,FISICA
        private List<Pessoa> carregarPessoas() {
                List<Pessoa> lista = new ArrayList<>();
                try {
                        Scanner s = new Scanner(this.getClass().getResourceAsStream("/backup_pessoas.csv"));
                        s.useDelimiter(Pattern.compile("(\\n)"));

                        int count = 0;
                        while (s.hasNextLine()) {
                                count++;
                                String linha = s.next();

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
                } catch (NoSuchElementException e) {
                        //nope
                }
                return lista;
        }
}
