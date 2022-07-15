package hooyn.todo.api.request.member;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String userID;
    private String userPW;
}
