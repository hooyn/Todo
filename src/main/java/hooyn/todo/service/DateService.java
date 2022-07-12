package hooyn.todo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateService {

    public DateService() {
    }

    //로깅을 할 때 API 호출 시간을 표현하기 위해 클래스 생성
    public String getDate(){
        return "[ " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " ]";
    }
}
