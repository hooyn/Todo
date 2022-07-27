package hooyn.todo.dto;

import hooyn.todo.domain.Deadline;
import hooyn.todo.domain.TodoStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class FindTodoDto {

    private Long todo_id;

    private String title;
    private String content;

    private Deadline deadline;

    private LocalDateTime create_time;

    private TodoStatus status;

    public FindTodoDto(Long todo_id, String title, String content, Deadline deadline, LocalDateTime create_time, TodoStatus status) {
        this.todo_id = todo_id;
        this.title = title;
        this.content = content;
        this.deadline = deadline;
        this.create_time = create_time;
        this.status = status;
    }
}
