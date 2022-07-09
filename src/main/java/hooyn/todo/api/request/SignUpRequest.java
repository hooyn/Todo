package hooyn.todo.api.request;

import lombok.Getter;

@Getter
public class SignUpRequest {
    private String userNM;
    private String userID;
    private String userPW;
}
