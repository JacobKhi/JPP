import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import ast.Node;
import core.Interpreter;
import core.Lexer;
import core.Parser;
import language.Token;


/**
 * Classe principal do interpretador.
 * Orquestra todo o processo: leitura do arquivo de código,
 * análise léxica (tokenização), análise sintática (parsing) e, finalmente,
 * a interpretação da árvore sintática gerada.
*/

public class App {
    public static void main(String[] args) throws Exception {
        try {
            // Caminho para o arquivo de codigo fonte a ser interpretado.
            Path path = Paths.get("codigo.txt");
            String source = Files.readString(path);

            // Passo 1 -> analise lexica 
            // O Lexer escaneia o codigo fonte e o transforma em uma lista de Tokens
            System.out.println("--- 1. Tokens ---");
            Lexer lexer = new Lexer(source);
            List<Token> tokens = lexer.tokenize();
            for(Token token : tokens){
                System.out.println(token);
            }

            // Passo 2: analise sintatica
            // O Parser recebe os tokens e os organiza em uma Árvore Sintática Abstrata (AST),
            // que representa a estrutura lógica do código.
            System.out.println("\n--- 2. AST (Arvore Sintatica) ---");
            Parser parser = new Parser(tokens);
            List<Node> ast = parser.parse();
            for(Node node : ast){
                System.out.println(node.toString());
            }

            // Passo 3: interpretacao 
            // O Interpreter percorre a AST e executa as instruções, calculando os resultados.
            System.out.println("\n--- 3. Resultado da Execucao ---");
            Interpreter interpreter = new Interpreter();
            interpreter.interpret(ast);
            
        } catch (Exception e) {
            System.err.println("Erro ao ler arquivo" + e.getMessage());
        }
    }
}