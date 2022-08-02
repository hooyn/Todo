package hooyn.todo.dto;

import lombok.Getter;

@Getter
public class EventDto {
    private String date;
    private String count;

    public EventDto(String date, String count) {
        this.date = date;
        this.count = count;
    }
}
