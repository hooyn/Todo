package hooyn.todo.domain.todo;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Deadline {
    private String date;
    private String time;
}
