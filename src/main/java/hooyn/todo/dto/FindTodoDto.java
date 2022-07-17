package hooyn.todo.dto;

import hooyn.todo.domain.Deadline;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class FindTodoDto {

    private Long id;

    private String title;
    private String content;

    private Deadline deadline;

    private LocalDateTime create_time;

    public FindTodoDto(Long id, String title, String content, Deadline deadline, LocalDateTime create_time) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.deadline = deadline;
        this.create_time = create_time;
    }
}
