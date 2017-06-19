package br.com.jopss.springasync.modelos;

import br.com.jopss.springasync.util.Modelos;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Pessoa extends Modelos {
        
	@Id
	private String id;
        
        @NotEmpty
        private String nome;
        
        @NotEmpty
        private String tipoPessoa;
        
        public Pessoa() {
        }

        public Pessoa(String id) {
                this.id = id;
        }

        @Override
        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
        }

        public String getNome() {
                return nome;
        }

        public void setNome(String nome) {
                this.nome = nome;
        }

        public String getTipoPessoa() {
                return tipoPessoa;
        }

        public void setTipoPessoa(String tipoPessoa) {
                this.tipoPessoa = tipoPessoa;
        }

}
