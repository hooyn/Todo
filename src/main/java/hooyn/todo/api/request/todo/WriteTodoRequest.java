package hooyn.todo.api.request.todo;

import hooyn.todo.domain.todo.Deadline;
import lombok.Getter;

@Getter
public class WriteTodoRequest {
    private String uuid;
    private String title;
    private String content;
    private Deadline deadline;
}
