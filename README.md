# Geonputer

### 컴파일러론, 컴퓨터 구조론, 운영체제론을 학습하고 이 지식을 이용해 나만의 프로그래밍 언어인 GeonSsembly Language로 작성된 프로그램을 실행할 수 있는 컴퓨터 시스템을 구현한 프로젝트 💻
#### CPU Architecture : RISC 기반, I/O Device - CPU 통신 방식 : Memory Mapped I/O
**GeonSsembly Language**에 대한 문법 정의와 소스코드는 [여기](https://github.com/DryRains/Geonputer-Documents)를 참고

프로그램의 흐름은 아래와 같다.
1. 프로그램 실행 후 1번을 눌러 Disk_SourceCode 내에 컴파일할 소스코드 선택 (소스코드는 GeonSsembly Language로 작성되어 있다.)
2. Lexer & Parser가 소스코드로부터 토큰들을 뜯어내고 SymbolTable을 통해 Parse Tree Node에 추가해가며 문장 구조를 만들어낸다.
3. Code Generator가 이 문장들을 16진수의 기계어로 번역한다.
4. 이후 Linker는 이 변환된 기계어를 받아 Disk_Program 폴더에 실행 가능한 파일로 저장한다.
5. 프로그램을 다시 실행 후 2번을 눌러 실행할 파일을 선택
6. Loader는 선택한 파일을 메모리에 적당한 페이지 & 각 세그먼트 사이즈를 할당하여 프로세스의 형태로 올리게 되고 CPU가 instruction Cycle을 돌면서 해당 프로세스의 Code Segment를 한줄씩 읽어 해석 및 실행한다.
<br><br>

*5번에서 Non-Blocking Mode와 Blocking Mode 두가지 방식중 하나를 선택해서 실행할 수 있다.<br><br>
**Non-Blocking Mode** : CPU가 Insturction Cycle을 수행하는 동안 사용자는 이와 별개로 키보드 입력을 통해 I/O Inturrupt를 발생시킬 수 있는 모드이다.<br>
's'를 누르면 메모리에 붙어있는 I/O Buffer에 이 입력이 쌓이게 되고 CPU는 한 사이클을 마치고 I/O Buffer에 인터럽트가 들어와 있는지 체킹한다. 이 경우 's' 라는 사용자의 인터럽트는 Context Switching을 하라는 명령으로 맵핑되어 있기에 CPU는 이 인터럽트를 확인하면 'Context Switching'을 수행한다.<br>
Context Switching이 수행되면 위의 5번과 동일하게 실행할 프로그램을 선택하는 창이 뜨고, 현재 실행중이던 프로그램의 정보들을 임시 저장하고 만일 내가 선택한 파일이 기존에 실행중이던 프로그램이면 임시 저장된 정보들을 가져와 이전에 진행중이던 작업을 이어서 수행, 
기존에 메모리에 올려져 있지 않은 프로그램이면 위의 6번과 같이 적당한 메모리 공간이 할당되어 메모리에 새로 올라오게 된다 -> 이렇게 여러개의 프로그램을 메모리에 올리고 Context Switching을 하며 변경하면서 실행할 수 있다.<br>
(이 모드는 자바의 멀티 스레드를 이용해 구현하여 CPU 클래스의 Intruction Cycle과 IO Device 클래스의 사용자의 입력을 받아 I/O Buffer로 보내는 메소드가 별개로 작동하는데, 시스템이 사용자에게 입력값을 요청할 때 자바의 입출력 메소드를 두 스레드에서 동시에 사용하게 되기 때문에 입력값을 두번 입력해야 하는 상황이 나타나니 참고 -> 그냥 입력할 값 똑같이 두번 입력해주면 된다.)<br><br>
**Bloking Mode** : 사용자의 Inturrupt를 제한하는 모드, 오로지 시스템이 사용자에게 입력을 요청할 때만 사용자는 입력을 할 수 있다. 컨텍스트 스위칭 기능을 사용하지 않고 단순히 프로그램의 작동만 빠르게 확인하려면 이 모드로 실행하면 된다.*
<br><br>
