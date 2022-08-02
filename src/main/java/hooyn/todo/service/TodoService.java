package hooyn.todo.service;

import com.querydsl.core.Tuple;
import hooyn.todo.domain.Deadline;
import hooyn.todo.domain.Todo;
import hooyn.todo.dto.FindTodoDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TodoService {

    //투두 작성
    Long writeTodo(Todo todo);

    //투두 조회 (todo_id)
    Todo findTodoById(Long todo_id);

    //투두 조회 (Deadline)
    List<FindTodoDto> findTodoByDeadline(String uuid, Deadline deadline, Integer page);

    //투두 조회 (Content)
    List<FindTodoDto> findTodoByContent(String uuid, String content, Integer page);

    //투두 이벤트 조회
    List<Tuple> findTodoEventByYearMonth(String uuid, String year, String month);

    //투두 수정
    Long updateTodo(Long todo_id, String title, String content, Deadline deadline);

    //투두 삭제
    Long deleteTodo(Long todo_id);

    //권한 확인
    boolean checkAuthorization(String uuid, Long todo_id);

    //투두 상태 변경 (완료 & 완료취소)
    Long updateTodoStatus(Long todo_id);
}
