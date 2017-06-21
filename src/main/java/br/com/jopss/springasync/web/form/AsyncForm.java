package br.com.jopss.springasync.web.form;

import br.com.jopss.springasync.modelos.Pessoa;
import java.util.ArrayList;
import java.util.List;

public class AsyncForm {
        
        private StringBuilder log = new StringBuilder();
        private List<Pessoa> pessoas = new ArrayList<>();

        public AsyncForm() {
        }
        
        public AsyncForm(StringBuilder log) {
                this.log = log;
        }

        public void addLog(String s){
                this.log.append(s).append("<br>");
        }

        public void setPessoas(List<Pessoa> pessoas) {
                this.pessoas = pessoas.subList(0, 100);
        }

        public String getLog() {
                return log.toString();
        }

        public List<Pessoa> getPessoas() {
                return pessoas;
        }
        
}
