package hooyn.todo.service;

import hooyn.todo.domain.Deadline;
import hooyn.todo.domain.Todo;

import java.util.List;

public interface TodoService {

    //투두 작성
    Long writeTodo(Todo todo);

    //투두 조회 (todo_id)
    Todo FindTodoById(Long todo_id);

    //투두 조회 (Deadline)
    List<Todo> findTodoByDeadline(String uuid, Deadline deadline);

    //투두 조회 (Content)
    List<Todo> findTodoByContent(String uuid, String content);

    //투두 수정
    Long updateTodo(Long todo_id, String title, String content, Deadline deadline);

    //투두 삭제
    Long deleteTodo(Long todo_id);

    //권한 확인
    boolean checkAuthorization(String uuid, Long todo_id);
}
