package br.com.jopss.springasync.excecoes;

public class SpringAsyncException extends Exception {

        public SpringAsyncException() {
                super();
        }
        
        public SpringAsyncException(String message) {
                super(message);
        }

        public SpringAsyncException(Throwable cause) {
                super(cause);
        }
        
}
