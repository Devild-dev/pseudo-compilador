package analisadorlex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LeitorDeArquivosTexto {
    private final static int TAMANHO_BUFFER = 20;//define o tamanho do buffer que vai guardar a string de entrada
    int []bufferDeLeitura;
    int ponteiro;
    int bufferAtual;//indica qual o buffer altual
    int inicioLexema;//marca o inicio do lexema que tento encontrar
    private String lexema;//guarda o lexema numa string
    
    InputStream is;
    
    public LeitorDeArquivosTexto(String arquivo){
        try{
            is = new FileInputStream(new File(arquivo));
            inicializarBuffer();
        }catch(Exception ex){
            ex.printStackTrace();
        }       
    }
    
    private void inicializarBuffer(){//inicializa o buffer
        bufferAtual = 2;
        inicioLexema = 0;
        lexema = "";
        bufferDeLeitura = new int[TAMANHO_BUFFER * 2];
        ponteiro = 0;
        recarregarBuffer1();
        
    }
    
    private void incrementarPonteiro(){
        ponteiro++;
        if(ponteiro == TAMANHO_BUFFER){
            recarregarBuffer2();
        }else if(ponteiro == TAMANHO_BUFFER * 2){
            recarregarBuffer1();
            ponteiro = 0;
        }
    }
    
    private void recarregarBuffer1(){
        if(bufferAtual == 2){
            bufferAtual = 1;
            
            for(int i=0; i<TAMANHO_BUFFER; i++){
                try {
                    bufferDeLeitura[i] = is.read();
                    if(bufferDeLeitura[i] == -1){
                        break;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(LeitorDeArquivosTexto.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private void recarregarBuffer2(){
        if(bufferAtual == 1){
            bufferAtual = 2;
            for(int i=TAMANHO_BUFFER; i<TAMANHO_BUFFER *2; i++){
                try {
                    bufferDeLeitura[i] = is.read();
                    if(bufferDeLeitura[i] == -1){
                        break;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(LeitorDeArquivosTexto.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private int lerCaractereDoBuffer(){
        int ret = bufferDeLeitura[ponteiro];
        //System.out.println(this);//imprime a representação do Buffer
        incrementarPonteiro();
        return ret;
    }

    public String getLexema() {
        return lexema;
    }
    
    public int lerProximoCaractere(){
        int c = lerCaractereDoBuffer();
        lexema += (char)c;
        return c;
    }
    
    public void retroceder(){
        ponteiro--;
        lexema = lexema.substring(0, lexema.length() - 1);
        if(ponteiro < 0){
            ponteiro = TAMANHO_BUFFER * 2 - 1;
        }
    }
    
    public void zerar(){
        ponteiro = inicioLexema;
        lexema = "";
    }
    
    public void confirmar(){
        inicioLexema = ponteiro;
        lexema = "";
    }
    
    @Override
    public String toString(){//retorna a String que é a representação do Buffer
        String ret = "Buffer:[";
        for(int i : bufferDeLeitura){
            char c = (char) i;
            if(Character.isWhitespace(c)){
                ret += ' ';
            }else{
                ret += (char) i;
            }
        }
        ret += "]\n";
        ret += "        ";
        for(int i = 0; i < TAMANHO_BUFFER * 2; i++){
            if(i == inicioLexema && i == ponteiro){
                ret += "^";
            }else if(i == inicioLexema){
                ret += "%";
            }else if(i == ponteiro){
                ret += "*";
            }else{
                ret += " ";
            }
        }
        return ret;
    }
    
}

  