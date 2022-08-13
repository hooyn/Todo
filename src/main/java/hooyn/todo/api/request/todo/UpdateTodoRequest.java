package hooyn.todo.api.request.todo;

import hooyn.todo.domain.todo.Deadline;
import lombok.Getter;

@Getter
public class UpdateTodoRequest {
    private String uuid;
    private Long todo_id;
    private String title;
    private String content;
    private Deadline deadline;

}
