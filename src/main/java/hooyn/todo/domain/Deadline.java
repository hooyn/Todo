package hooyn.todo.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Deadline {
    private String date;
    private String time;
}
