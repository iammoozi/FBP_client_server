# FBP_client_server
java를 이용한 소켓 통신

Client에서 보내온 메시지를 접속한 모든 client 또는 특정 client에 전송할 수 있도록 multi-chatting client/server를 만들어 보자.

아래의 요구 사항에 맞는 server를 구현해 보자.

* 실행시 서비스를 위한 port를 지정할 수 있다. 별도의 port 지정이 없는 경우, 1234를 기본으로 한다.

* Server는 실행 후 대기 상태를 유지하고, client가 접속되면 client 정보를 출력한다.

* Server는 하나 이상의 client가 접속되어도 동시에 지원 가능하도록 한다.

* Server는 접속된 client로부터 ID를 받아 별도 관리한다.

* Client 메시지 시작에 대상 client id가 추가되어 있는 경우, 해당 client에만 메시지를 전달한다.
** 대상 client id는 "@[ID] message"로 @ 다음에 붙여서 온다.
** user1에 hello 메시지를 보내기 위해서는 "@user1 hello"로 보내면 된다.
* 제어 명령을 구현한다.
** !list 명령은 접속되어 있는 client의 id list를 반환한다.

아래의 요구 사항에 맞는 client를 구현해 보자.

* 실행시 서비스를 위한 id, host, port를 지정할 수 있다.

* 별도의 지정이 없는 경우,
** id는 임의의 문자열로 생성한다.
** host는 localhost
** port는 1234로 한다.

* Client가 server에 정상적으로 접속하면, 설정된 id를 전송한다.

* 특정 client에만 메시지 전송을 원할 경우, 메시지 앞에 @[대상 id]을 추가한다.
* 제어 명령을 구현한다.
** !exit 명령은 clie은 연결을 끊는다.
