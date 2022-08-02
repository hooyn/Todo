package hooyn.todo.api.request.todo;

import lombok.Getter;

@Getter
public class FindTodoEventRequest {
    private String uuid;
    private String year;
    private String month;
}
