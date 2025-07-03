package core;
import java.util.ArrayList;
import java.util.List;

import ast.AssignNode;
import ast.BinaryOpNode;
import ast.Node;
import ast.NumberNode;
import ast.PrintNode;
import ast.StringNode;
import ast.VariableNode;
import language.Token;
import language.TokenType;



/**
 * O Analisador Sintático (Parser).
 * Recebe uma lista de tokens do Lexer e a transforma em uma
 * Árvore Sintática Abstrata (AST), que é uma estrutura de dados em árvore
 * que representa a gramática do código-fonte.
*/

public class Parser {
    private List<Token> tokens;
    private int current = 0;
    
    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }


    /**
     * Método principal que inicia a análise sintática.
     * @return Uma lista de nós (statements) que compõem a AST.
    */
    public List<Node> parse(){
        List<Node> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(parseStatement());
        }

        return statements;
    }

    // --- Métodos de Parsing para cada tipo de regra da gramática ---


    /**
     * Analisa uma única instrução (statement).
     * No momento, apenas declarações de variáveis são suportadas.
     * @return O nó da AST para a instrução analisada.
    */
    private Node parseStatement(){
        // Regra 1: Se começar com "var", é uma declaração.
        if (match(TokenType.VAR)) {
            return parseVariableDeclaration();
        }

        // Regra 2: Se for um IDENTIFICADOR seguido por um "=", é uma atribuição.
        if (check(TokenType.IDENTIFIER) && peekNext() != null && peekNext().type == TokenType.EQUAL) {
            return parseAssignmentStatement();
        }

        // Regra 3
        if (match(TokenType.PRINT)){
            return parsePrintStatement();
        }

        // Se não corresponder a nenhuma regra conhecida, lança um erro.
        throw new RuntimeException("Instrucao desconhecida. Esperado 'var' ou uma atribuicao, mas encontrado: " + peek().type);
    }


    /**
     * Analisa uma declaração de variável no formato: "var IDENTIFICADOR = expressao;"
     * @return Um nó do tipo AssignNode.
    */
    private Node parseVariableDeclaration(){
        Token identifier = consume(TokenType.IDENTIFIER, "Esperando o nome da variavel");
        consume(TokenType.EQUAL, "Esperado '=' apos o nome da variavel");
        Node expression = parseExpression();
        consume(TokenType.SEMICOLON, "Esperando ';' no final da declaracao");
        return new AssignNode(identifier.value, expression);
    }


    /**
     * Analisa uma instrução de atribuição a uma variável existente.
     * Ex: z = 100;
     * @return Um nó do tipo AssignNode.
    */
    private Node parseAssignmentStatement() {
        // O token do identificador já foi parcialmente verificado, agora o consumimos.
        Token identifier = consume(TokenType.IDENTIFIER, "Esperando nome da variavel na atribuicao.");
        consume(TokenType.EQUAL, "Esperando '=' apos o nome da variavel.");
        Node expression = parseExpression();
        consume(TokenType.SEMICOLON, "Esperando ';' no final da atribuicao.");
        return new AssignNode(identifier.value, expression);
    }


    // Adicionar o comentario depois
    private Node parsePrintStatement(){
        Node value = parseExpression();
        consume(TokenType.SEMICOLON, "Esperando ';' apos o valor do print");
        return new PrintNode(value);
    }
    

    /**
     * Analisa uma expressão aritmética (soma e subtração).
     * Lida com a associatividade à esquerda (ex: 10 - 2 + 3 é lido como (10-2)+3).
     * @return Um nó de expressão, que pode ser um BinaryOpNode ou um nó primário.
    */
    private Node parseExpression(){
        Node left = parseTerm();
        
        // Loop para tratar múltiplos operadores (associatividade à esquerda)
        while(match(TokenType.PLUS) || match(TokenType.MINUS)){
            String operator = previous().value; // Pega o operador (+ ou -)
            Node right = parseTerm();
            left = new BinaryOpNode(left, operator, right); // Atualiza o nó 'left'
        }

        return left;
    }

    /**
     * Analisa uma expressão de MULTIPLICAÇÃO e DIVISÃO (alta precedência).
    */
    private Node parseTerm() {
        Node left = parsePrimary(); // Chama o nível mais alto de precedência.

        while (match(TokenType.MULTIPLY) || match(TokenType.DIVIDE)) {
            String operator = previous().value;
            Node right = parsePrimary();
            left = new BinaryOpNode(left, operator, right);
        }

        return left;
    }

    /**
     * Analisa as unidades mais básicas de uma expressão.
     * @return Um nó NumberNode, StringNode ou VariableNode.
    */
    private Node parsePrimary(){
        if (match(TokenType.NUMBER)){
            return new NumberNode(previous().value);
        }
        if (match(TokenType.STRING)){
            return new StringNode(previous().value);
        }
        if (match(TokenType.IDENTIFIER)){
            return new VariableNode(previous().value);
        }

        throw new RuntimeException("Esperado numero, string ou identificador na expressao");
    }


    // --- Métodos Auxiliares de Navegação e Verificação ---

    /**
     * Verifica se o token atual corresponde ao tipo esperado. Se sim, consome o token e retorna true.
     * @param type O tipo de token a ser verificado.
     * @return true se o token corresponder e for consumido.
    */
    private boolean match(TokenType type) {
        if (check(type)) {
            advance();
            return true;
        }
        return false;
    }

    /**
     * Consome o token atual se ele for do tipo esperado. Caso contrário, lança um erro com uma mensagem.
     * @param type O tipo de token esperado.
     * @param message A mensagem de erro a ser exibida se o tipo não corresponder.
     * @return O token consumido.
    */
    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw new RuntimeException(message + " Encontrado: " + peek().type + " → '" + peek().value + "'");
    }

    /**
     * Verifica se o token atual é de um determinado tipo, sem consumi-lo.
     * @param type O tipo de token a ser verificado.
     * @return true se o tipo do token atual corresponder.
    */
    private boolean check(TokenType type) {
        return !isAtEnd() && peek().type == type;
    }

    /**
     * Avança para o próximo token.
     * @return O token que foi consumido (o anterior).
    */
    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    /**
     * Verifica se todos os tokens foram consumidos.
    */
    private boolean isAtEnd() {
        return current >= tokens.size();
    }

    /**
     * Retorna o token atual sem consumi-lo.
    */
    private Token peek() {
        return tokens.get(current);
    }

    /**
     * Retorna o próximo token (um à frente do atual) sem consumi-lo.
     * @return O próximo token, ou null se estiver no fim.
    */
    private Token peekNext() {
        if (current + 1 >= tokens.size()) {
            return null;
        }
        return tokens.get(current + 1);
    }

    /**
     * Retorna o token que acabamos de consumir.
    */
    private Token previous() {
        return tokens.get(current - 1);
    }

}