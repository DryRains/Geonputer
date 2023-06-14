package Compiler;
public class Parser {
	public String parse(Lexer lex, String filename) {
		Program program = new Program(filename);
		String objectCode = program.parse(lex);
		return objectCode;
	}

}
