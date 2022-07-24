package hooyn.todo.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response<T> {
    private boolean isSuccess; //통신 성공 여부
    private int statusCode; //통신 상태 코드
    private T data; //응답 데이터
    private String message; //응답 메시지
}