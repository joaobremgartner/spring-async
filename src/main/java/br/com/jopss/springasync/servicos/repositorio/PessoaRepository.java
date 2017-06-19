package br.com.jopss.springasync.servicos.repositorio;

import br.com.jopss.springasync.modelos.Pessoa;
import org.springframework.data.repository.CrudRepository;

public interface PessoaRepository extends CrudRepository<Pessoa, String> {
        
}
