package Compiler;
import java.util.Vector;


public class CodeSegment implements ParseTreeNode{
	private	int CSSize; //Code Segment 사이즈 = 코드가 몇줄인지
	private int StackLast;//지역변수를 숫자로 심볼테이블에 맵핑하기 위한 수치
	private int HeapLast;//멤버변수를 숫자로 심볼테이블에 맵핑하기 위한 수치
	
	//코드 세그먼트는 문장의 집합임
	private Vector<Statement> statements;
	private SymbolTable symbolTable;
	
	public CodeSegment(SymbolTable symbolTable,Vector<Statement> statements) {
		this.statements = statements;
		this.symbolTable = symbolTable;
		this.CSSize = 0;
		this.StackLast = 0;
		this.HeapLast = 0;
	}
	
	
	@Override
	public String parse(Lexer lex) {
		String[] tokens = lex.getTokens();
		String operator = tokens[0];
		while(operator.compareTo(".end") != 0) {//.header ~ .end까지가 코드 세그먼트 영
			if(operator.contains(":")) {//label일 경우
				//syboltable에 집어넣고
				SymbolEntity entity = new SymbolEntity();
				entity.setVariableName(operator.replace(":",""));
				entity.setValue(this.statements.size()-1);
				this.symbolTable.add(entity); //현재 문장의 위치를 기입 심볼테이블 을 Program에서 만들어서 내릴것
				System.out.println("======Symbol Table=======\nKeyword: "+this.symbolTable.lastElement().getVariableName()+"  |  Value: "+this.symbolTable.lastElement().getValue()+"\n=========================");
			} else {//빈 줄이나 레이블이 아닌 의미있는 문장일경우
				Statement statement = null;
				switch(tokens.length) {
				case 1:
					statement = new Statement(tokens[0]);
					System.out.println("token: "+tokens[0]);
					break;
				case 2:
					statement = new Statement(tokens[0],tokens[1]);
					//스택에 들어갈 지역변수 심볼 정의
					if(tokens[1].startsWith("#")) {
						//기존에 정의되어있는지 검사
						for(SymbolEntity entityToCompare : this.symbolTable) {
							//이미 정의되어 있다면 나오기
							if(entityToCompare.getVariableName().equals(tokens[1])) {
								break;								
							}
							SymbolEntity entity = new SymbolEntity();
							//'이 지역변수의 주소값은 ~다' 정의
							entity.setVariableName(tokens[1]);
							entity.setValue(StackLast);
							this.symbolTable.add(entity);
							switch(tokens[0]) { 
								//PUSHINT #num 이면 int 선언이므로 CPU쪽 StackPointer에서 4만큼의 공간을 할당해주고
								//StackPointer를 4 늘려야 함
								//만약 스택의 맨 처음에 선언되었다면 #num은 0으로 치환 -> 실제주소는 스택주소+0;
								//이후 PUSHINT #num2로 새로운 지역변수 선언되었다면
								//이 #num2는 4로 치환되고 주소는 실제주소는 스택주소+4가된다.
								case "PUSHINT":
									StackLast += 4;
									break;
								case "PUSHFLOAT":
									//이번 프로젝트에서는 정수형 값만 다루지만 만들어 놓기
									StackLast += 4;
									break;
								case "PUSHCHAR":
									StackLast += 1;
									break;
								case "DL":
									StackLast += 4;
									break;
							}
						break;
						}
					}
					//힙에 들어갈 멤버변수 심볼 정의
					if(tokens[1].contains("*")) {
						//student라는 객체가 DS에 선언된 상태에서 NEWINT @student.*score 선언하면
						//심볼테이블에서 student의 치환값을 찾아서
						//그 치환값 + 4
						//1. 우선 . 앞에서 자른다.
						int objectNameIndex = tokens[1].indexOf(".");
						String objectName = tokens[1].substring(0,objectNameIndex);
						System.out.println(objectName);
						//2. 객체이름을 뽑아내서 심볼테이블에서 찾는다(DS에 이미 선언되어 있을테니)
						for(SymbolEntity entityToFind : this.symbolTable) {
							System.out.println(entityToFind.getVariableName());
							if (entityToFind.getVariableName().equals("@"+objectName)) {
								//3. 그 객체의 주소값에 HEAPLAST를 더해서 주소 정의
								switch(tokens[0]) {
								case "NEWINT":
									System.out.println("in");
									SymbolEntity entity = new SymbolEntity();
									entity.setVariableName(tokens[1]);
									//객체의 주소값+4byte(int형 선언이니)
									entity.setValue(entityToFind.getValue()+HeapLast);
									this.symbolTable.add(entity);
									HeapLast+=4;
									break;
								}
							break;
							}
						}
						
					}
					System.out.println("token: "+tokens[0]);
					System.out.println("token: "+tokens[1]);
					break;
				default:
					break;
				}
				this.CSSize = CSSize+1; //한줄 읽을때마다 Code Segment 사이즈에 +1 기록
				this.statements.add(statement);
				//print
				System.out.println("--------statement--------\n"+this.statements.lastElement().getStatement()+"\n-------------------------");
			}
			tokens = lex.getTokens();
			operator = tokens[0];
		}
		return operator;
	}
	
	public int getCSSize() {
		return this.CSSize;
	}
	
	
	
}