package hooyn.todo.service;

import com.querydsl.core.Tuple;
import hooyn.todo.domain.todo.Deadline;
import hooyn.todo.domain.todo.Todo;
import hooyn.todo.domain.todo.TodoStatus;
import hooyn.todo.dto.FindTodoDto;
import hooyn.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    /**
     * 투두 작성
     */
    @Transactional
    public Long writeTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    /**
     * 투두 조회 (id)
     */
    @Transactional(readOnly = true)
    public Todo findTodoById(Long todo_id) {
        return todoRepository.findById(todo_id);
    }

    /**
     * 투두 조회 (deadline)
     */
    @Transactional(readOnly = true)
    public List<FindTodoDto> findTodoByDeadline(String uuid, Deadline deadline, Integer page) {
        List<Todo> todos = todoRepository.findByDeadline(uuid, deadline, page);
        return todos
                .stream()
                .map(todo -> new FindTodoDto(
                        todo.getId(),
                        todo.getTitle(),
                        todo.getContent(),
                        todo.getDeadline(),
                        todo.getCreate_time(),
                        todo.getStatus()))
                .collect(Collectors.toList());
    }

    /**
     * 투두 조회 (content)
     */
    @Transactional(readOnly = true)
    public List<FindTodoDto> findTodoByContent(String uuid, String content, Integer page) {
        List<Todo> todos = todoRepository.findByContent(uuid, content, page);
        return todos
                .stream()
                .map(todo -> new FindTodoDto(
                        todo.getId(),
                        todo.getTitle(),
                        todo.getContent(),
                        todo.getDeadline(),
                        todo.getCreate_time(),
                        todo.getStatus()))
                .collect(Collectors.toList());
    }

    /**
     * 투두 이벤트 조회
     */
    public List<Tuple> findTodoEventByYearMonth(String uuid, String year, String month){
        return todoRepository.findEventByYearMonth(uuid, year, month);
    }


    /**
     * 투두 삭제
     */
    @Transactional
    public Long deleteTodo(Long todo_id) {
        return todoRepository.delete(todo_id);
    }

    /**
     * 투두 업데이트 (변경감지 사용)
     */
    @Transactional
    public Long updateTodo(Long todo_id, String title, String content, Deadline deadline) {
        Todo todo = todoRepository.findById(todo_id);

        if(!title.isBlank()){
            todo.changeTitle(title);
        }

        if(!content.isBlank()){
            todo.changeContent(content);
        }

        if(deadline!=null || !deadline.getDate().isBlank()){
            todo.changeDeadline(deadline);
        }

        return todo_id;
    }

    /**
     * 투두 권한 확인
     */
    public boolean checkAuthorization(String uuid, Long todo_id) {
        return todoRepository.checkAuthorization(uuid, todo_id);
    }

    /**
     * 투두 상태 변경 (완료 & 완료취소)
     */
    @Transactional
    public Long updateTodoStatus(Long todo_id) {
        Todo todo = todoRepository.findById(todo_id);

        TodoStatus status = todo.getStatus();

        if(status.equals(TodoStatus.NOT_COMPLETE)){
            todo.changeTodoStatus(TodoStatus.COMPLETE);
        } else if(status.equals(TodoStatus.COMPLETE)){
            todo.changeTodoStatus(TodoStatus.NOT_COMPLETE);
        }

        return todo.getId();
    }
}
