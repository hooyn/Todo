package hooyn.todo.api.request;

import lombok.Getter;

@Getter
public class SignInRequest {
    private String userID;
    private String userPW;
}
