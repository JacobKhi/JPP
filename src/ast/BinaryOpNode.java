package ast;


/**
 * Nó da AST que representa uma operação binária (com dois operandos).
 * Exemplo: 10 + 5
*/
public class BinaryOpNode extends Node{
    private Node left;
    private Node right;
    private String operator;

    public BinaryOpNode(Node left, String operator, Node right){
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
    
    @Override
    public String toString() {
        return "OpBinaria(" + left.toString() + " " + operator + " " + right.toString() + ")";
    }

        public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public String getOperator() {
        return operator;
    }
}
