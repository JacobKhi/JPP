package ast;

/**
 * Nó da AST que representa o uso de uma variável em uma expressão.
*/
public class VariableNode extends Node{
    private String name;

    public VariableNode(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return "Variavel(" + name + ")";
    }

    public String getName() {
        return name;
    }
}
