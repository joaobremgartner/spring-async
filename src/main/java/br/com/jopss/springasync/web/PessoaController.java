package br.com.jopss.springasync.web;

import br.com.jopss.springasync.modelos.Pessoa;
import br.com.jopss.springasync.servicos.ImportacaoService;
import br.com.jopss.springasync.web.form.AsyncForm;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/pessoa/exec")
public class PessoaController {
        
        @Autowired
        private ImportacaoService importacaoService;
        
        @RequestMapping(value = "/sincronizada", method = RequestMethod.GET)
        public String sincronizada(Model m){
                List<Pessoa> pessoas = importacaoService.importarSincronizado();
                m.addAttribute("quantidade",pessoas.size());
                m.addAttribute("pessoas",pessoas.subList(0, 100));
                return "sincronizada";
        }
        
        @RequestMapping(value = "/assinc", method = RequestMethod.GET)
        public String assinc(Model m, HttpSession session){
                CompletableFuture<AsyncForm>  promisse = importacaoService.importarAsync();
                this.addSessaoPromisse(session, promisse);
                return "assinc";
        }
        
	@ResponseBody
	@RequestMapping(value = "/assinc/verificar", method = RequestMethod.GET)
	public AsyncForm verificarAssinc(HttpSession session) throws InterruptedException, ExecutionException {
		CompletableFuture<AsyncForm> promisse = this.getSessaoPromisse(session);
                if (promisse == null) {
			return null;
		} else {
			return promisse.getNow(new AsyncForm(importacaoService.getLog()));
		}
	}
        
        private void addSessaoPromisse(HttpSession session, CompletableFuture<AsyncForm> promisse) {
		session.setAttribute("FUTURE_IMPORTACAO_USUARIO", promisse);
	}

	private CompletableFuture<AsyncForm> getSessaoPromisse(HttpSession session) {
		return (CompletableFuture<AsyncForm>) session.getAttribute("FUTURE_IMPORTACAO_USUARIO");
	}
        
}
