package hooyn.todo.api.request.todo;

import lombok.Getter;

@Getter
public class UpdateTodoStatusRequest {
    private String uuid;
    private Long todo_id;
}
