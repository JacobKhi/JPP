package ast;

/**
 * Nó da AST que representa uma funcao de imprimir algo no console.
*/
public class PrintNode extends Node{
    private Node expression;

    public PrintNode(Node expression){
        this.expression = expression;
    }

    public Node getExpression() {
        return expression;
    }

}