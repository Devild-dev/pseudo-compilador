package analisadorlex;

public class SomethingLexico {
    LeitorDeArquivosTexto ldat;
    
    public SomethingLexico(String arquivo){
        ldat = new LeitorDeArquivosTexto(arquivo);
    }
    
    public Token proximoToken(){//retorna os padroes criados
        Token proximo = null;
        
        espacosEComentarios();
        ldat.confirmar();
        
        proximo = fim();
        if(proximo == null){
            ldat.zerar();
        }else{
            ldat.confirmar();
            return proximo;
        }
        
        proximo = parenteses();
        if(proximo == null){
            ldat.zerar();
        }else{
            ldat.confirmar();
            return proximo;
        }
        
        proximo = sinalIgual();
        if(proximo == null){
            ldat.zerar();
        }else{
            ldat.confirmar();
            return proximo;
        }
        
        
        proximo = numerosRomanos();
        if(proximo == null){
            ldat.zerar();
        }else{
            ldat.confirmar();
            return proximo;
        }
        
        proximo = operadorAritmetico();
        if(proximo == null){
            ldat.zerar();
        }else{
            ldat.confirmar();
            return proximo;
        }
        System.out.println("Erro Léxico");
        System.out.println(ldat.toString());
        
        return null;
    }
    private void espacosEComentarios(){
        int estado = 1;
        while(true){
            char c = (char) ldat.lerProximoCaractere();
            if(estado == 1){
                if(Character.isWhitespace(c) || c == ' '){
                    estado = 2;
                }else if(c == '%'){
                    estado = 3;
                }else{
                    ldat.retroceder();
                    return;
                }
            }else if (estado == 2){
                if(c == '%'){
                    estado = 3;
                }else if(!(Character.isWhitespace(c) || c == ' ')){
                    ldat.retroceder();
                    return;
                }
            }else if(estado == 3){
                if(c == '\n'){
                    return;
                }
            }
        }
    }
    
    private Token parenteses(){
        int caractereLido = ldat.lerProximoCaractere();
        char c = (char) caractereLido;
        if(c == '('){
             return new Token(TipoToken.AbrePar, ldat.getLexema());
         }else if(c == ')'){
             return new Token(TipoToken.FechaPar, ldat.getLexema());
         }else{
             return null;
         }
    }
    
    private Token operadorAritmetico(){
        int caractereLido = ldat.lerProximoCaractere();
        char c = (char) caractereLido;
        if(c == '+'){
            return new Token(TipoToken.OpAritSoma, ldat.getLexema());
         }else if(c == '-'){
            return new Token(TipoToken.OpAritSub, ldat.getLexema());
         }else if(c == '*'){
            return new Token(TipoToken.OpAritMult, ldat.getLexema());
         }else if(c== '/'){
            return new Token(TipoToken.OpAritdiv, ldat.getLexema());
        }else{
             return null;
         }
    }
    
    private Token numerosRomanos(){
        int caractereLido = ldat.lerProximoCaractere();
        char c = (char) caractereLido;
        if(c == 'i'){
            return new Token(TipoToken.PcNumeroRomanoI, ldat.getLexema());
        }else if(c == 'v'){
            return new Token(TipoToken.PcNumeroRomanoV, ldat.getLexema());
        }else if(c == 'x'){
            return new Token(TipoToken.PcNumeroRomanoX, ldat.getLexema());
        }else if (c == 'l'){
             return new Token(TipoToken.PcNumeroRomanoL, ldat.getLexema());
        }else if(c == 'c'){
            return new Token(TipoToken.PcNumeroRomanoC, ldat.getLexema());
        } else{
            return null;
        }
    }
    
    private Token sinalIgual(){
        int caractereLido = ldat.lerProximoCaractere();
        char c = (char) caractereLido;
        if(c == '='){
             return new Token(TipoToken.SinalIgual, ldat.getLexema());
         }else{
             return null;
         }
    }
    
    private Token fim(){
        int caractereLido = ldat.lerProximoCaractere();
        if(caractereLido == -1){
            return new Token(TipoToken.Fim, "Fim");
        }
        return null;
    }
}
