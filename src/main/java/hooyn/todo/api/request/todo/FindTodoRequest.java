package hooyn.todo.api.request.todo;

import hooyn.todo.domain.todo.Deadline;
import lombok.Getter;

@Getter
public class FindTodoRequest {
    private String uuid;
    private Deadline deadline;
    private String content;
}
