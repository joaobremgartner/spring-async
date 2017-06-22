package br.com.jopss.springasync.web;

import br.com.jopss.springasync.modelos.Pessoa;
import br.com.jopss.springasync.servicos.ImportacaoService;
import br.com.jopss.springasync.web.form.AsyncForm;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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
        
        /**
         * Executa a importacao pela chamada sincrona normal.
         * @param m Model
         * @return String com a pagina.
         */
        @RequestMapping(value = "/sincronizada", method = RequestMethod.GET)
        public String sincronizada(Model m){
                List<Pessoa> pessoas = importacaoService.importarSincronizado();
                m.addAttribute("quantidade",pessoas.size());
                m.addAttribute("pessoas",pessoas.subList(0, 100));
                return "sincronizada";
        }
        
        /**
         * Executa a importacao pela chamada a um metodo assicrono.
         * @param m Model
         * @param session HttpSession
         * @return String com a pagina.
         */
        @RequestMapping(value = "/assinc", method = RequestMethod.GET)
        public String assinc(Model m, HttpSession session){
                StringBuilder log = new StringBuilder();
                this.addSessaoLog(session, log);
                CompletableFuture<AsyncForm>  promisse = importacaoService.importarAsync(log);
                this.addSessaoPromisse(session, promisse);
                return "assinc";
        }
        
        /**
         * Metodo chamado pelo Javascript para verificar conclusao do Async.
         * Caso ainda nao tenha concluido, retorna o log de execucao.
         * 
         * @param session HttpSession
         * @return AsyncForm
         * @throws InterruptedException
         * @throws ExecutionException 
         */
	@ResponseBody
	@RequestMapping(value = "/assinc/verificar", method = RequestMethod.GET)
	public AsyncForm verificarAssinc(HttpSession session) throws InterruptedException, ExecutionException {
		CompletableFuture<AsyncForm> promisse = this.getSessaoPromisse(session);
                if (promisse == null) {
			return null;
		} else {
                        //caso o Async ainda nao tenha concluido, este metodo cria como retorno o parametro passado.
			return promisse.getNow(new AsyncForm(this.getSessaoLog(session)));
		}
	}
        
        private void addSessaoLog(HttpSession session, StringBuilder log) {
		session.setAttribute("LOG_IMPORTACAO", log);
	}
        
        private StringBuilder getSessaoLog(HttpSession session) {
		return (StringBuilder) session.getAttribute("LOG_IMPORTACAO");
	}
        
        private void addSessaoPromisse(HttpSession session, CompletableFuture<AsyncForm> promisse) {
		session.setAttribute("FUTURE_IMPORTACAO", promisse);
	}

	private CompletableFuture<AsyncForm> getSessaoPromisse(HttpSession session) {
		return (CompletableFuture<AsyncForm>) session.getAttribute("FUTURE_IMPORTACAO");
	}
        
}
