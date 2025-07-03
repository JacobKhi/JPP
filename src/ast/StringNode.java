package ast;

/**
 * Nó da AST que representa uma string.
*/
public class StringNode extends Node{
    private String string;

    public StringNode(String string){
        this.string = string;
    }

    @Override
    public String toString() {
        return "String( " + string + " )";
    }

    public String getString() {
        return string;
    }
}
