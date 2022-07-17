package hooyn.todo.service;

import hooyn.todo.domain.Deadline;
import hooyn.todo.domain.Todo;
import hooyn.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Primary
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    /**
     * 투두 작성
     */
    @Override
    @Transactional
    public Long writeTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    /**
     * 투두 조회 (id)
     */
    @Override
    @Transactional(readOnly = true)
    public Todo FindTodoById(Long todo_id) {
        return todoRepository.findById(todo_id);
    }

    /**
     * 투두 조회 (deadline)
     */
    @Override
    @Transactional(readOnly = true)
    public List<Todo> findTodoByDeadline(String uuid, Deadline deadline) {
        return todoRepository.findByDeadline(uuid, deadline);
    }

    /**
     * 투두 조회 (content)
     */
    @Override
    @Transactional(readOnly = true)
    public List<Todo> findTodoByContent(String uuid, String content) {
        return todoRepository.findByContent(uuid, content);
    }

    /**
     * 투두 삭제
     */
    @Override
    @Transactional
    public Long deleteTodo(Long todo_id) {
        return todoRepository.delete(todo_id);
    }

    /**
     * 투두 업데이트 (변경감지 사용)
     */
    @Override
    @Transactional
    public Long updateTodo(Long todo_id, String title, String content, Deadline deadline) {
        Todo todo = todoRepository.findById(todo_id);

        if(!title.isBlank()){
            todo.setTitle(title);
        }

        if(!content.isBlank()){
            todo.setContent(content);
        }

        if(deadline!=null || !deadline.getDate().isBlank()){
            todo.setTitle(title);
        }

        return todo_id;
    }

    /**
     * 투두 권한 확인
     */
    @Override
    public boolean checkAuthorization(String uuid, Long todo_id) {
        return todoRepository.checkAuthorization(uuid, todo_id);
    }
}
