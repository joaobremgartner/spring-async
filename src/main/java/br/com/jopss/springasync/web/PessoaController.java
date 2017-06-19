package br.com.jopss.springasync.web;

import br.com.jopss.springasync.modelos.Pessoa;
import br.com.jopss.springasync.servicos.ImportacaoService;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;

@Controller
@RequestMapping(value = "/pessoa/exec")
public class PessoaController {

        @Autowired
        private ImportacaoService importacaoService;
        
        @RequestMapping(value = "/sincronizada", method = RequestMethod.GET)
        public String sincronizada(Model m){
                Set<Pessoa> pessoas = importacaoService.importarSincronizado();
                m.addAttribute("pessoas",pessoas);
                return "sincronizada";
        }
        
        @RequestMapping(value = "/assinc", method = RequestMethod.GET)
        public String assinc(Model m){
                importacaoService.importarAsync();
                return "assinc";
        }
        
}
