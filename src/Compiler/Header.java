package Compiler;
public class Header implements ParseTreeNode{
	private SymbolTable declarations;
	private int DSSize;
	
	public Header(SymbolTable symbolTable) {
		this.declarations = symbolTable;
		this.DSSize = 0;
	}
		@Override
	public String parse(Lexer lex) {
	//let보고 토큰단위로 찢어 반환하도록
			String token = lex.getToken();
			/*make SymbolTable*/
			while(token.compareTo(".code") != 0) {//.header ~ .code 전까지가 헤더이므로
				SymbolEntity declaration = new SymbolEntity();
				switch(token) {
				case "int":
					//int형일 경우 사이즈는 4byte ex)int i가 헤더 첫줄에 선언되어있으면
					declaration.setVariableName("@"+lex.getToken());//@i는
					declaration.setValue(DSSize);//0
					DSSize += 4;
					break;
				case "float":	
					//float형일 경우 사이즈는 4byte -> 이번 프로젝트는 int형만 다루지만 만들어놓기
					declaration.setVariableName("@"+lex.getToken());
					declaration.setValue(DSSize);
					DSSize += 4;
					break;
				case "char":
					//char형일 경우 사이즈는 1byte
					declaration.setVariableName("@"+lex.getToken());
					declaration.setValue(DSSize);
					DSSize +=1;
					break;
				case "object":
					//객체형일 경우 사이즈 4byte
					declaration.setVariableName("@"+lex.getToken());
					declaration.setValue(DSSize);
					DSSize +=4;
					break;
				}
				this.declarations.add(declaration);
				
				//print Header
				System.out.println("========header========\n"+this.declarations.lastElement().getVariableName()+" "+this.declarations.lastElement().getValue()
						+"\n======================");
				
				token = lex.getToken();
			}
			return token;			
	}
	
	public int getDSSize() {
		return this.DSSize;
	}	
		
}