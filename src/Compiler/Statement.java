package Compiler;

	public class Statement implements ParseTreeNode{
		//하나의 문장은 operator와 0~2개의 operands로 구성
		private String operator;
		private String operand1;
		private String operand2;
		
		private String statement;//print용 변수
		
		public Statement(String operator) {
			this.operator = operator;
			
			//print용
			statement = operator;
		}
			
		public Statement(String operator, String operand1) {
			this.operator = operator;
			this.operand1 = operand1;
			//print
			statement = operator + " " + operand1;
		}
		@Override
		public String parse(Lexer lex) {
			return operator;
		}
		
		public String getOperator() {
			return operator;
		}
		
		public String getOperand1() {
			return operand1;
		}
		
		public String getOperand2() {
			return operand2;
		}
		
		
		public int getLength() {
			if(this.operator!=null&&this.operand1==null&&this.operand2==null) {
				return 1;
			}
			else if(this.operator!=null&&this.operand1!=null&&this.operand2==null) {
				return 2;
			}
			else if(this.operator!=null&&this.operand1!=null&&this.operand2!=null) {
				return 3;
			}
			return 0;
		}
		
		public String getStatement() {
			return statement;
		}
	}