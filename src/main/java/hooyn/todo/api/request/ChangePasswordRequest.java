package hooyn.todo.api.request;

import lombok.Getter;

@Getter
public class ChangePasswordRequest {
    private String uuid;
    private String password;
}
