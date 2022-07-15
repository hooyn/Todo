package hooyn.todo.api.request.member;

import lombok.Getter;

@Getter
public class CheckPasswordSameRequest {
    private String password;
    private String password_;
}
