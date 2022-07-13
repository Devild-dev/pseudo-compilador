package analisadorlex;

public class AnalisadorLex {

    public static void main(String[] args) {
        SomethingLexico lex = new SomethingLexico(args[0]);
        Token t = null;
        
        while((t = lex.proximoToken()).nome != TipoToken.Fim){    
            System.out.print(t);
        }
    }   
}
