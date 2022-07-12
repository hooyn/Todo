package hooyn.todo.api.request;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String userID;
    private String userPW;
}
