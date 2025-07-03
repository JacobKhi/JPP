package core;
import java.util.ArrayList;
import java.util.List;

import language.Token;
import language.TokenType;

/**
 * O Analisador Lexico (Lexer ou Scanner).
 * Responsável por ler o codigo fonte como uma string e dividi-lo
 * em uma lista de unidades logicas chamadas "tokens".
*/

public class Lexer {

    private String source; //codigo a ser tokenizad
    private int position;  //posicao no texto
    private List<Token> tokens = new ArrayList<>(); //todos os tokens vao parar nessa lista
    

    /**
     * Construtor que inicializa o Lexer com o código-fonte.
     * @param source O codigo fonte em formato de String.
     */
    public Lexer(String source){
        this.source = source;
    }


    /**
     * Metodo principal que executa a tokenização.
     * Percorre o codigo fonte caractere por caractere e gera a lista de tokens.
     * @return Uma lista de Tokens.
     */
    public List<Token> tokenize(){
        while (!isAtEnd()) {
            char currentChar = peek();

            // Ignora espaços em branco (espaço, tabulação, nova linha)
            if(Character.isWhitespace(currentChar)){
                advance();
                continue;
            }

            // Identifica palavras chave ou identificadores (nomes de variáveis)
            if(Character.isLetter(currentChar) || currentChar == '_'){
                tokenizeWord();
                continue;
            }

            // Identifica números
            if(Character.isDigit(currentChar)){
                tokenizeNumber();
                continue;
            }

            // Identifica strings literais (delimitadas por aspas duplas)
            if(currentChar == '"'){
                tokenizeString();
                continue;
            }


            // Identifica operadores e símbolos de um único caractere
            switch (currentChar){
                case '=':
                    tokens.add(new Token(TokenType.EQUAL, "="));
                    advance();
                    break;
                case '+':
                    tokens.add(new Token(TokenType.PLUS, "+"));
                    advance();
                    break;
                case '-':
                    tokens.add(new Token(TokenType.MINUS, "-"));
                    advance();
                    break;
                case '*':
                    tokens.add(new Token(TokenType.MULTIPLY, "*"));
                    advance();
                    break;
                case '/':
                    tokens.add(new Token(TokenType.DIVIDE, "/"));
                    advance();
                    break;
                case ';':
                    tokens.add(new Token(TokenType.SEMICOLON, ";"));
                    advance();
                    break;

                default:
                    // Se um caractere desconhecido for encontrado, lança um erro.
                    throw new RuntimeException("Simbolo invalido -> " + currentChar + " na posicao " + position);
            }
        }

        return tokens;
    }


    // --- Métodos Auxiliares de Navegaçao ---
    

    /**
     * Verifica se o analisador alcançou o final do código-fonte.
     * @return true se a posição atual for maior ou igual ao tamanho do código.
    */
    private boolean isAtEnd(){
        return position >= source.length();
    }


    /**
     * Retorna o caractere na posição atual sem consumi-lo (olha para frente).
     * @return O caractere atual.
    */
    private char peek(){
        return source.charAt(position);
    }


    /**
     * Consome o caractere na posição atual e avança para a próxima.
     * @return O caractere que foi consumido.
    */
    private char advance(){
        return source.charAt(position++);
    }
    

    /**
     * Verifica se o caractere atual corresponde ao esperado. Se sim, consome o
     * caractere e retorna true.
     * @param expected O caractere que estamos esperando encontrar.
     * @return true se o caractere atual for o esperado, false caso contrário.
    */
    private boolean match(char expected) {
        if (isAtEnd()) {
            return false;
        }
        if (peek() != expected) {
            return false;
        }

        // Se o caractere correspondeu, consome ele e retorna true
        advance();
        return true;
    }


    // --- Métodos Auxiliares de Tokenizaçao ---


    /**
     * Agrupa uma sequencia de letras e digitos para formar uma palavra.
     * Em seguida, decide se é uma palavra-chave (como "var" ou "print") ou um identificador.
    */
    private void tokenizeWord(){
        StringBuilder sb = new StringBuilder(); // Monta uma string baseado nos char

        // Consome todos os caracteres alfanuméricos sequenciais
        while (!isAtEnd() && Character.isLetterOrDigit(peek()) || peek() == '_'){
            sb.append(advance());
        }

        String word = sb.toString();
        
        // Verifica se a palavra corresponde a uma palavra-chave da linguagem.
        if(word.equals("print")){
            tokens.add(new Token(TokenType.PRINT, word));
        }
        else if(word.equals("var")){
            tokens.add(new Token(TokenType.VAR, word));
        }
        else if(word.equals("true")){
            tokens.add(new Token(TokenType.TRUE, word));
        }
        else if(word.equals("false")){
            tokens.add(new Token(TokenType.FALSE, word));
        }
        else{
            // Caso contrário, é um nome de variável (identificador).
            tokens.add(new Token(TokenType.IDENTIFIER, word));
        }
    }


    /**
     * Agrupa uma sequencia de digitos para formar um número.
    */
    private void tokenizeNumber(){
        StringBuilder sb = new StringBuilder();

        while (!isAtEnd() && Character.isDigit(peek())){
            sb.append(advance());
        }

        String number = sb.toString();

        tokens.add(new Token(TokenType.NUMBER, number));
    }


    /**
     * Agrupa todos os caracteres entre um par de aspas duplas para formar uma string literal.
    */
    private void tokenizeString(){
        advance(); //para pular as aspas iniciais
        StringBuilder sb = new StringBuilder();

        //vai loopar por todos os elementos dentro de um par de aspas
        while (!isAtEnd() && peek() != '"') {
            sb.append(advance());
        }

        advance(); //pula aspas final
        String string = sb.toString();
        
        tokens.add(new Token(TokenType.STRING, string));
    }  

}