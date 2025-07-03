package language;
/**
 * Define todos os tipos de tokens que o Lexer pode reconhecer.
 * Cada variante representa uma categoria de elemento da linguagem.
*/
public enum TokenType {
    // Palavras-chave
    VAR,
    PRINT,

    // Literais e Identificadores
    IDENTIFIER,
    NUMBER,
    STRING,
    
    // Operadores e Símbolos
    EQUAL,
    PLUS,
    MINUS,
    SEMICOLON,

}