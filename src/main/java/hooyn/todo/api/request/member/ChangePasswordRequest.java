package hooyn.todo.api.request.member;

import lombok.Getter;

@Getter
public class ChangePasswordRequest {
    private String uuid;
    private String password;
}
