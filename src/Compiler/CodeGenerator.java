package Compiler;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;


public class CodeGenerator {
	
	public enum Opcode{
		HALT("00"),
		LOD("01"),
		STO("03"),
		ADD("05"),
		SUB("06"),
		MUL("07"),
		DIV("08"),
		EQ("0a"),
		NOT("0b"),
		JMP("10"),
		GZJ("11"),
		BZJ("12"),
		EZJ("13"),
		DL("20"),
		PUSHINT("21"),
		STF("22"),
		NEWINT("30"),
		PRT("50"),
		INP("60")
		;
		
		private String machineCode;
		
		private Opcode(String machineCode) {
			this.machineCode = machineCode;
		}
		
		private String getMachineCode() {
			return this.machineCode;
		}
		
	}
	
	private Vector<Statement> statements;
	private SymbolTable symbolTable;
	private String filename;
	private int DSSize;
	private int CSSize;

	public CodeGenerator(String filename, Vector<Statement> statements, SymbolTable symbolTable, int DSSize, int CSSize) {
		this.filename = filename;
		this.statements = statements;
		this.symbolTable = symbolTable;
		this.DSSize = DSSize;
		this.CSSize = CSSize;
	}

	public String compile() {
		String objectCode = "";
		//DS , CS 세그먼트 사이즈 첫줄에 기록
		objectCode = DSSize+" "+CSSize+"\n";
		//Code Segment 컴파일
		int i=0;//프린트용 변수
		for(Statement statement : statements) {
			String mode = "0";
			switch(statement.getLength()) { //Length가 1인 명령어들 STA,LDA의 경우 operand가 00임
			case 1:
				//기계어 변환
				mode = "0";
				String opcode = matchOpcode(statement.getOperator());
				objectCode += "0x"+mode+opcode+"00\n";
				System.out.println(i+": "+statement.getStatement()+"=====>0x0"+opcode+"00\n");
				break;
			case 2://뒤에 Operand가 있는 명령어들
				String operand = statement.getOperand1();
				//Global Variable
				if(operand.startsWith("@")){
					//@면 전역변수 주소 -> mode를 1로
					mode = "1";
				}
				//Local Variable
				else if(operand.startsWith("#")) {
					mode = "2";
				}
				//Member Variable(=Instance Variable)
				else if(operand.contains("*")) {
					mode = "3";
				}
				//기계어 변환
				opcode = matchOpcode(statement.getOperator());
				String AODcode = matchAODcode(operand);
				objectCode += "0x"+mode+opcode+AODcode+"\n";
				System.out.println(i+": "+statement.getStatement()+"=====>0x"+mode+opcode+AODcode+"\n");
				break;
			default:
				break;
			}
			i=i+1;//프린트용 변수
			
		}
		System.out.println("<Object Code>\n"+objectCode);
		return objectCode;
	}
	
	public String matchOpcode(String operator){
		String result = null;
		for(Opcode opcode : Opcode.values()) {
			if(operator.equals(opcode.name())) {
				result = opcode.getMachineCode();
			}
		}
		return result;
		
	}

	private String matchAODcode(String operand) {
		//심볼테이블 탐색 -> JMP start 같은 경우 start는 레이블이름이고, 이는 심볼테이블 안에 정의되어있으니 찾아서 바꿔야함
		for(SymbolEntity symbolEntity : this.symbolTable) {
			//심볼테이블에서 일치하는 operand 발견시
			if(operand.equals(symbolEntity.getVariableName())) {
				operand = ""+symbolEntity.getValue();
				break;
			}
		}
		//16진수의 형태로 변환
		int operandInt = Integer.parseInt(operand);
		String hexOperand = Integer.toHexString(operandInt);
		//ex. 10일경우 a로 변환되는데, 칸을 맞추려면 0a로 변환되어야함
		if(hexOperand.length()==1) hexOperand = "0"+hexOperand;
		
		return hexOperand;
	
	}
	
}
