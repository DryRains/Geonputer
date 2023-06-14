package Compiler;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class Program implements ParseTreeNode{
		//컴파일된 하나의 프로그램은 헤더(데이터 세그먼트)와 코드 세그먼트 영역으로 나뉘어져있다.
		private Header header;
		private CodeSegment codeSegment;
		
		private Vector<Statement> statements;
		private SymbolTable symbolTable;
		private String filename;
		
		private int DSSize;
		private int CSSize;
	
		public Program(String filename) {
			this.filename = filename;
			this.statements = new Vector<Statement>();
			this.symbolTable = new SymbolTable();
			
			//소스코드는 Header(Data Segment)와 CodeSegment 영역으로 나뉜다. -> Stack,Heap Segment는 프로그램을 실행하면 할당
			//symbolTable은 header에 존재하는 변수를 기계어로, Code 영역에 존재하는 레이블을 숫자로 변환하는데 쓰임
			//따라서 Header와 CodeSegment 모두 SymbolTable에 변환할 대상의 이름과 변환할 값을 기록할 수 있어야 한다.
			//Code Generator는 이 작성된 심볼테이블을 참고하여 변수나 레이블을 숫자값으로 변환
			this.header = new Header(symbolTable);
			this.codeSegment = new CodeSegment(symbolTable,statements);
			
			this.DSSize = 0;
			this.CSSize = 0;
		}

		@Override
		public String parse(Lexer lex) {
			String token = lex.getToken();
			if(token.compareTo(".header") == 0) {
				token = this.header.parse(lex);//파싱해서 본인의 노드에 붙여나감
				this.DSSize = header.getDSSize();
				System.out.println("\n*Data Segment size: "+this.DSSize+"\n");
			}
			if(token.compareTo(".code") == 0) {
				token = this.codeSegment.parse(lex);//파싱해서 본인의 노드에 붙여나감
				this.CSSize = codeSegment.getCSSize();//Code Segment에 몇줄이 들어가야할지
				System.out.println("\n*Code Segment size: "+this.CSSize+"\n");
			}
			
			//parsing이 끝나면 CodeGenerator가 컴파일
			CodeGenerator codeGenerator = new CodeGenerator(filename,statements,symbolTable,DSSize,CSSize);
			String objectCode = codeGenerator.compile();
			return objectCode;
		}
		
}