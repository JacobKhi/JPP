package language;
/**
 * Representa um Token, a menor unidade de código com significado.
 * Um token possui um tipo e um valor (o texto original).
*/
public class Token {
    
    public TokenType type;
    public String value;

    public Token(TokenType type, String value){
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString(){
        return type + "('" + value +"')";
    }

}