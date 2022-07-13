package analisadorsint;

import analisadorlex.SomethingLexico;
import analisadorlex.TipoToken;
import analisadorlex.Token;
import java.util.ArrayList;
import java.util.List;

public class AnalisadorSint {
    private final static int TAMANHO_BUFFER = 10; //guarda o tamanho do buffer
    List<Token> bufferTokens;
    SomethingLexico lex;
    boolean chegouNoFim = false;
    
    public AnalisadorSint(SomethingLexico lex){
        this.lex = lex;
        bufferTokens = new ArrayList<>();
        lerToken();
    }
    
    private void lerToken(){//busca o token e preenche o buffer
        if(bufferTokens.size()>0){
            bufferTokens.remove(0);
        }
        while(bufferTokens.size() < TAMANHO_BUFFER && !chegouNoFim){
            Token proximo = lex.proximoToken();
            bufferTokens.add(proximo);
            if(proximo.nome == TipoToken.Fim){
                chegouNoFim = true;
            }
        }
        System.out.println("Lido: "+ lookahead(1));
    }
    
    Token lookahead(int k){
        if(bufferTokens.isEmpty()){
            return null;
        }if(k - 1 >= bufferTokens.size()){
            return bufferTokens.get(bufferTokens.size() - 1);
        }
        return bufferTokens.get(k - 1);
    }
    
    void match(TipoToken tipo){//pega um tipo token e compara com lookahead de 1
        if(lookahead(1).nome == tipo){
            System.out.println("Match: "+ lookahead(1));
            lerToken();
        }else{
            erroSintatico(tipo.toString());
        }
    }
    
    void erroSintatico(String... tokensEsperados){
        String mensagem = "Erro Sintático! esperando um dos seguintes (";
        for(int i=0; i<tokensEsperados.length;i++){
            mensagem += tokensEsperados[i];
            if(i<tokensEsperados.length-1){
                mensagem += ",";
            }
        }
        mensagem += "), mas foi encontrado "+lookahead(1);
        throw new RuntimeException(mensagem);
    }
    
    //S : X O X '=';
  public void S(){
      X();
      O();
      X();
      match(TipoToken.SinalIgual);
  }  
//X : R | '(' X ')';
  public void X(){
      if(lookahead(1).nome == TipoToken.PcNumeroRomanoI || lookahead(1).nome == TipoToken.PcNumeroRomanoV || lookahead(1).nome == TipoToken.PcNumeroRomanoL || lookahead(1).nome == TipoToken.PcNumeroRomanoX){// || lookahead(2).nome == TipoToken.OpAritSoma || lookahead(2).nome == TipoToken.OpAritSub || lookahead(2).nome == TipoToken.OpAritdiv){
          R();
      }else if(lookahead(1).nome == TipoToken.AbrePar){
        match(TipoToken.AbrePar);
          X();
        match(TipoToken.FechaPar);
      }
      
      
  }
 //O : '+' | '-' | '*' | '/';
  public void O(){
      if(lookahead(1).nome == TipoToken.OpAritMult){
          match(TipoToken.OpAritMult);
      }else if(lookahead(1).nome == TipoToken.OpAritSoma){
          match(TipoToken.OpAritSoma);
      }else if(lookahead(1).nome == TipoToken.OpAritSub){
           match(TipoToken.OpAritSub);  
      }else{
          match(TipoToken.OpAritdiv);
      }     
  }
  
  //R : A | B | C | Q | 'c';
  public void R(){
      if(lookahead(1).nome == TipoToken.PcNumeroRomanoI){
          A();
      }else if(lookahead(1).nome == TipoToken.PcNumeroRomanoV){
          B();
      }else if(lookahead(1).nome == TipoToken.PcNumeroRomanoX){
          C();
      }else if(lookahead(1).nome == TipoToken.PcNumeroRomanoL){
          Q();
      }else{
           match(TipoToken.PcNumeroRomanoC);
      }   
  }
 
//A : 'i' D;
  public void A(){
      match(TipoToken.PcNumeroRomanoI);
      D();
  }
  
//D : 'i' E | 'v' |'x' | null;
  public void D(){
      if(lookahead(1).nome == TipoToken.PcNumeroRomanoI){
        match(TipoToken.PcNumeroRomanoI);
        E();
      }else if(lookahead(1).nome == TipoToken.PcNumeroRomanoV){
          match(TipoToken.PcNumeroRomanoV);
      }else if(lookahead(1).nome == TipoToken.PcNumeroRomanoX){
           match(TipoToken.PcNumeroRomanoX);
      }else{
           //null;
      }    
  }
  
//E : 'i' | null;
  public void E(){
      if(lookahead(1).nome == TipoToken.PcNumeroRomanoI){
          match(TipoToken.PcNumeroRomanoI);
      }else{
        //null
      }   
  }
  
//B : 'v' F;
  public void B(){
      match(TipoToken.PcNumeroRomanoV);
      F();
  }
  
//F : 'i' G | null;
  public void F(){
      if(lookahead(1).nome == TipoToken.PcNumeroRomanoI){
          match(TipoToken.PcNumeroRomanoI);
          G();
      }else{
         //null  
      }
      
     
     
  }
  //G : 'i' E| null;
  public void G(){
      if(lookahead(1).nome == TipoToken.PcNumeroRomanoI){
          match(TipoToken.PcNumeroRomanoI);
          E();
      }else{
          //null
      }
      
  }
  //C : 'x' K;
  public void C(){
      match(TipoToken.PcNumeroRomanoX);
      K();
  }

//K : 'x' M | A | B | 'l' N | 'c' N | null;
  public void K(){
      if(lookahead(1).nome == TipoToken.PcNumeroRomanoX){
        match(TipoToken.PcNumeroRomanoX);
        M();
      }else if(lookahead(1).nome == TipoToken.PcNumeroRomanoI){
          A();
      }else if(lookahead(1).nome == TipoToken.PcNumeroRomanoV){
          B();
      }else if(lookahead(1).nome == TipoToken.PcNumeroRomanoL){
          match(TipoToken.PcNumeroRomanoL);
          N();
      }else if(lookahead(1).nome == TipoToken.PcNumeroRomanoC){
          match(TipoToken.PcNumeroRomanoC);
          N();
      }else{
        //null
      }  
  }
  
//M : 'x' N | A | B | null;
public void M(){
    if(lookahead(1).nome == TipoToken.PcNumeroRomanoX){
        match(TipoToken.PcNumeroRomanoX);
        N();
    }else if(lookahead(1).nome == TipoToken.PcNumeroRomanoI){
        A();
    }else if(lookahead(1).nome == TipoToken.PcNumeroRomanoV){
        B();
    }else{
        //null
    }    
}

//N : A | B | null;
public void N(){
    if(lookahead(1).nome == TipoToken.PcNumeroRomanoI){
        A();
    }else if(lookahead(1).nome == TipoToken.PcNumeroRomanoV){
        B();
    }else{
       //null 
    } 
}
//Q : 'l' P;
  public void Q(){
      match(TipoToken.PcNumeroRomanoL);
      P();
  }
//P : A | B | 'x' T | null;
  public void P(){
      if(lookahead(1).nome == TipoToken.PcNumeroRomanoI){
          A();
      }else if(lookahead(1).nome == TipoToken.PcNumeroRomanoV){
          B();
      }else if(lookahead(1).nome == TipoToken.PcNumeroRomanoX){
          match(TipoToken.PcNumeroRomanoX);
          T();
      }else{
          //null
      }    
  }
//T : 'x' M | A | B | null;
  public void T(){
      if(lookahead(1).nome == TipoToken.PcNumeroRomanoX){
        match(TipoToken.PcNumeroRomanoX);
        M(); 
      }else if(lookahead(1).nome == TipoToken.PcNumeroRomanoI){
        A();
      }else if(lookahead(1).nome == TipoToken.PcNumeroRomanoV){
        B(); 
      }else{
          //null     
      }    
  }
}
