package Compiler;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Lexer {
	private Scanner scanner;
	String token;
	String[] tokens;
	
	public Lexer() {	
	}
	
	public void initialize(String fileName) {
		try {
			scanner = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void finalize() {
		scanner.close();
	}
	
	public String getToken() {
		token = scanner.next();
		while(token.contains("//")) {
			token = scanner.nextLine();
			token = scanner.next();
		}
		System.out.println("token: "+token);	
		return token;
	}
	
	
	public String[] getTokens() {
		token = scanner.nextLine();
    	while (token.contains("//") || token.trim().length() == 0) { // 주석 패스 및 빈 줄 패스
			token = scanner.nextLine();
		}
		tokens = token.trim().split("[\\s]+");
		return tokens;
	}
}
