package analisadorsint;

import analisadorlex.SomethingLexico;

public class Principal {
   
    public static void main(String[] args){
      SomethingLexico lex = new SomethingLexico(args[0]);
      AnalisadorSint parser = new AnalisadorSint(lex);
       parser.S();
   }
}
