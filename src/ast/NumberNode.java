package ast;


/**
 * Nó da AST que representa um número literal.
*/
public class NumberNode extends Node{
    private double value;

    public NumberNode(String value){
        this.value = Double.parseDouble(value);
    }

    @Override
    public String toString() {
        return "Numero(" + value + ")";
    }

    public double getValue() {
        return value;
    }
}
