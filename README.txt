웹서비스설계 실습 README입니다.

Spring Boot기반 REST API입니다.
기본 주소: http://localhost:8080/api/v1/items
요청/응답 형식: JSON (Content-Type: application/json)

데이터 필드
id필드의 타입은 Long입니다. 고유한 번호를 가지고 있으며 서버가 자동으로 생성합니다.
sender의 타입은 String입니다. 보내는 사람을 의미하며 빈값을 가질 수 없습니다.
item의 타입은 String입니다. 물건을 의미하며 빈값을 가질 수 없습니다.
origin의 타입은 String입니다. 원산지를 의미하며 빈값을 가질 수 없습니다.
receiver의 타입은 String입니다. 받는 사람을 의미하며 빈값을 가질 수 없습니다.
quantity의 타입은 Integer입니다. 수량을 의미하며 1이상의 값을 가져야 합니다.

API
GET(/api/v1/items): 데이터 전체를 조회하는 API입니다. 성공코드는 200입니다.
GET(/api/v1/items/{id}): 데이터 하나를 조회하는 API입니다. 성공코드는 200, 실패코드는 404입니다.
POST(/api/v1/items): 데이터를 등록하는 API입니다. 성공코드는 201, 실패코드는 400입니다.
POST(/api/v1/items/bulk): 여러 데이터를 등록하는 API입니다. 성공코드는 201, 실패코드는 400입니다.
PUT(/api/v1/items/{id}): body에 적은 필드만 수정하는 API입니다. 성공코드는 200, 실패코드는 400, 404입니다.
PUT(/api/v1/items/{id}/receiver): receiver필드만 수정하는 API입니다. 성공코드는 200, 실패코드는 400, 404입니다.
DELETE(/api/v1/items): 저장된 데이터를 전부 삭제하는 API입니다. 성공코드는 200입니다.
DELETE(/api/v1/items/{id}): 지정한 아이디를 가진 데이터를 삭제하는 API입니다. 성공코드는 200, 실패코드는 404입니다.
GET(/api/v1/items/error/500): 500응답을 확인하기 위한 API입니다.
GET(/api/v1/items/error/503): 503응답을 확인하기 위한 API입니다.

응답코드
200	조회,수정,삭제에 성공했을시 나오는 코드입니다.
201	등록에 성공했을시 나오는 코드입니다.
400	빈값, quantity 1미만, JSON형식 오류, 잘못된 id형식이 발생했을때 나오는 코드입니다.
404	존재하지 않는 id또는 주소가 입력되었을때 나오는 코드입니다.
405	지원하지 않는 HTTP메서드를 입력했을시 나오는 코드입니다.
415	Content-Type이 JSON이 아닐때 나오는 코드입니다.
500	서버 내부 오류로 나오는 코드입니다.
503	서비스 이용 불가일때 나오는 코드입니다.

응답형식
성공시 {"data": {...}, "status": "success"}를 반환합니다.
실패시 {"message": "관련 에러 메시지", "status": "error"}를 반환합니다.

프로젝트 구조
src/main/java/com/example/jbnu/osj/demo
├── api
│   ├── dto
│   │   ├── ApiResponse.java
│   │   └── ItemDto.java
│   ├── request
│   │   ├── ItemCreateRequest.java
│   │   └── ItemUpdateRequest.java
│   └── v1
│       └── ItemController.java
├── config
│   ├── GlobalExceptionHandler.java
│   ├── LoggingInterceptor.java
│   └── WebConfig.java
└── DemoApplication.java

코드 설명

ItemController
모든 API를 처리하는 컨트롤러입니다. @RestController와 @RequestMapping("/api/v1/items")로 기본 경로를 설정하고, @GetMapping, @PostMapping, @PutMapping, @DeleteMapping으로 각 API를 연결합니다.

ApiResponse
모든 응답을 같은 형식으로 감싸는 클래스입니다. 성공 시 status와 data를, 실패 시 status와 message를 담습니다.

ItemDto
서버가 저장하고 응답으로 내보내는 데이터 형태입니다.

ItemCreateRequest / ItemUpdateRequest
클라이언트가 보내는 요청 Body를 받는 클래스입니다.

LoggingInterceptor
요청이 컨트롤러에 도착하기 전과 응답이 끝난 후에 실행되는 미들웨어입니다.

WebConfig
LoggingInterceptor를 Spring에 등록하는 설정 클래스입니다.

GlobalExceptionHandler
@RestControllerAdvice로 모든 컨트롤러에서 발생하는 예외를 한곳에서 처리합니다.

DemoApplication
애플리케이션 실행 진입점입니다.
