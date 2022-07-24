package hooyn.todo.api.request.todo;

import lombok.Getter;

@Getter
public class DeleteTodoRequest {
    private String uuid;
    private Long todo_id;
}
