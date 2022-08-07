package hooyn.todo.api.request.member;

import lombok.Getter;

@Getter
public class JoinRequest {
    private Integer check;
    private String userNM;
    private String userID;
    private String userPW;
    private String userPWCHK;
}
