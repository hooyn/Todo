package hooyn.todo.api.request;

import lombok.Getter;

@Getter
public class JoinRequest {
    private String userNM;
    private String userID;
    private String userPW;
}
