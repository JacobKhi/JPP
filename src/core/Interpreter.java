package core;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ast.AssignNode;
import ast.BinaryOpNode;
import ast.Node;
import ast.NumberNode;
import ast.PrintNode;
import ast.StringNode;
import ast.VariableNode;

/**
 * O Interpretador.
 * Responsável por executar o código representado pela AST.
 * Ele "caminha" pela árvore (usando um padrão de projeto chamado Visitor) e
 * executa as ações definidas por cada nó.
*/

public class Interpreter {
    
    /**
     * A "tabela de símbolos" ou ambiente.
     * Armazena as variáveis e seus respectivos valores durante a execução.
    */
    private final Map<String, Object> environment = new HashMap<>();

    /**
     * Método público que inicia a interpretação de uma lista de instruções (a AST).
     * @param statements A lista de nós raiz da AST.
    */
    public void interpret(List<Node> statements){
        try{
            for (Node statement : statements){
                execute(statement);
            }
        }catch (RuntimeException e){
            System.err.println("Erro de execussao " + e.getMessage());
        }
    }

    /**
     * Avalia uma expressão (expression) e retorna seu valor.
     * Expressões, como `10 + 5` ou `x`, sempre produzem um valor.
     * @param expression O nó da expressão a ser avaliada.
     * @return O valor resultante da expressão (pode ser um Double, String, etc.).
    */
    private Object evaluate(Node expression){
        // --- Casos Base da Recursão ---
        if (expression instanceof NumberNode){
            return (double) ((NumberNode) expression).getValue();
        }

        if (expression instanceof StringNode){
            return ((StringNode) expression).getString();
        }

        if (expression instanceof VariableNode) {
            String name = ((VariableNode) expression).getName();
            if (environment.containsKey(name)) {
                return environment.get(name);
            }
            throw new RuntimeException("Variavel indefinida '" + name + "'.");
        }

        // --- Caso Recursivo ---
        if (expression instanceof BinaryOpNode) {
            BinaryOpNode node = (BinaryOpNode) expression;
            // Avalia recursivamente os lados esquerdo e direito da operação.
            Object left = evaluate(node.getLeft());
            Object right = evaluate(node.getRight());

            // Logica do operador '+'
            if (node.getOperator().equals("+")){
                // Caso os 2 sao strings faz a concatenacao
                if (left instanceof String || right instanceof String){
                    return String.valueOf(left) + String.valueOf(right);
                }
                // Caso sejam 2 doubles faz a soma
                if (left instanceof Double && right instanceof Double){
                    return (Double) left + (Double) right;
                }
            }

            // Garante que estamos fazendo operações com números
            if (left instanceof Double && right instanceof Double) {
                switch (node.getOperator()) {
                    case "-":
                        return (Double) left - (Double) right;
                    case "*":
                        return (Double) left * (Double) right;
                    case "/":
                        if ((double) right == 0) {
                            throw new RuntimeException("Erro: Divisao por zero.");
                        }
                        return (double) left / (double) right;
                }
            }

            throw new RuntimeException("Operacao invalida '" + node.getOperator() + "' para os tipos de dados fornecidos.");
        }

        return null; // ou lançar um erro se o tipo de nó for desconhecido
    }

    /**
     * Executa uma instrução (statement).
     * Instruções, como atribuição de variável, geralmente não retornam um valor.
     * @param statement O nó da instrução a ser executada.
    */
    private void execute(Node statement) {
        if (statement instanceof AssignNode) {
            // Verifica o tipo de nó para saber qual ação tomar.
            AssignNode assignNode = (AssignNode) statement;
            // Avalia a expressão à direita do '='
            Object value = evaluate(assignNode.getExpression());
            // Armazena o resultado no nosso ambiente com o nome da variável
            environment.put(assignNode.getVariable(), value);

            // Apenas para depuração, vamos imprimir o valor
            System.out.println(assignNode.getVariable() + " = " + value);
            return;
        }

        if(statement instanceof PrintNode){
            Object value = evaluate(((PrintNode) statement).getExpression());
            System.out.println(String.valueOf(value));
            return;
        }

        // Se for uma instrução que não conhecemos, podemos avaliá-la
        // (ex: uma linha de código com apenas "10 + 5;").
        evaluate(statement);
    }
}