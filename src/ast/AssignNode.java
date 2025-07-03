package ast;


/**
 * Nó da AST que representa uma instrução de atribuição de variável.
 * Exemplo: var x = 10;
*/
public class AssignNode extends Node{
    private String variable;
    private Node expression;

    public AssignNode(String variable, Node expression){
        this.variable = variable;
        this.expression = expression;
    }
    
    @Override
    public String toString() {
        return "Atribui(" + variable + " = " + expression.toString() + ")";
    }

    public String getVariable() {
        return variable;
    }

    public Node getExpression() {
        return expression;
    }
}
