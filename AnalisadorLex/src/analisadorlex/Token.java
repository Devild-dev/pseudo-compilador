package analisadorlex;

public class Token {
    public TipoToken nome;
    public String lexema;

    public Token(TipoToken nome, String lexama) {
        this.nome = nome;
        this.lexema = lexama;
    }
    @Override
    public String toString(){//para poder imprimir os tokens no formato <nome,lexema>
        return "<"+nome+","+lexema+">";
    }
}
