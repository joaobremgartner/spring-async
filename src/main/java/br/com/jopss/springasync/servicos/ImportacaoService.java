package br.com.jopss.springasync.servicos;

import br.com.jopss.springasync.modelos.Pessoa;
import br.com.jopss.springasync.servicos.repositorio.PessoaRepository;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImportacaoService {

        @Autowired
        private PessoaRepository pessoaRepository;

        @Transactional
        public Set<Pessoa> importarSincronizado() {
                System.out.println("-------------------------");
                System.out.println(" IMPORTACAO SINCRONIZADA ");
                System.out.println("-------------------------");

                System.out.print("Removendo todas as pessoas...");
                pessoaRepository.deleteAll();
                System.out.println(" ok!");

                System.out.println("Carregando CSV pessoas...");
                Set<Pessoa> pessoas = this.carregarPessoas();
                System.out.println("TOTAL DE PESSOAS IMPORTADAS: "+pessoas.size());

                System.out.print("Gravando lista de pessoas...");
                pessoaRepository.save(pessoas);
                System.out.println(" ok!");

                System.out.println("-------------------------");
                return pessoas;
        }

        public void importarAsync() {

        }

        //1,2013-11-11 00:00:00,SEBASTIANA M DE CARVALHO,FISICA
        private Set<Pessoa> carregarPessoas() {
                Set<Pessoa> lista = new HashSet<>();
                try {

                        Scanner s = new Scanner(this.getClass().getResourceAsStream("/backup_pessoas.csv"));
                        s.useDelimiter(Pattern.compile("(\\n)"));

                        int count = 0;
                        while (s.hasNextLine()) {
                                count++;
                                String linha = s.next();

                                if (count % 10000 == 0) {
                                        System.out.println(count + " linhas importadas.");
                                }
                                if (linha != null && !linha.isEmpty()) {
                                        String[] dados = linha.split(",");

                                        Date data = null;
                                        try {
                                                data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dados[1]);
                                        } catch (ParseException e) {
                                                data = new SimpleDateFormat("yyyy-MM-dd").parse(dados[1]);
                                        }

                                        Pessoa p = new Pessoa();
                                        p.setId(dados[0]);
                                        p.setDataCriacao(data);
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
