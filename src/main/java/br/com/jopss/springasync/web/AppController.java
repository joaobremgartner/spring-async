package br.com.jopss.springasync.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/")
public class AppController {
        
        @RequestMapping(value = "/teste", method = RequestMethod.GET)
        @ResponseBody
        public String teste(Model model) {
                return "Acesso de teste com sucesso!";
        }

}
