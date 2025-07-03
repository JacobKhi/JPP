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
    EQUAL_EQUAL,   // ==
    BANG_EQUAL,    // !=
    GREATER,        // >
    GREATER_EQUAL, // >=
    LESS,          // <
    LESS_EQUAL,    // <=
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    SEMICOLON,

    TRUE,
    FALSE,

}