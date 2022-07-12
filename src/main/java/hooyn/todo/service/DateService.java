package hooyn.todo.service;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class DateService {
    private String date;

    //로깅을 할 때 API 호출 시간을 표현하기 위해 클래스 생성
    public DateService() {
        this.date = "[ " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " ]";
    }
}
