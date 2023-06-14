package Compiler;
//심볼 테이블에 들어가는 하나의 Record
public class SymbolEntity {
	private String variableName;//keyword
	private int value;
	
	public void setVariableName(String variableName){
		this.variableName = variableName;
		
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	public String getVariableName() {
		return this.variableName;
	}
	
	public int getValue() {
		return this.value;
	}

}
