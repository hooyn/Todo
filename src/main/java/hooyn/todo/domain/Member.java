package hooyn.todo.domain;

import hooyn.todo.domain.todo.Todo;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID uuid;

    private String userNM;
    private String userID;
    private String userPW;

    @OneToMany(mappedBy = "member")
    private List<Todo> todos = new ArrayList<>();

    /*
     Memo mapping
    */

    public Member(String userNM, String userID, String userPW) {
        this.userNM = userNM;
        this.userID = userID;
        this.userPW = userPW;
    }

    /**
     * 비밀번호 암호화
     */
    public void encodePassword(PasswordEncoder passwordEncoder){
        this.userPW = passwordEncoder.encode(userPW);
    }

    /**
     * 비밀번호 확인 (복호화)
     */
    public boolean matchPassword(PasswordEncoder passwordEncoder, String password){
        return passwordEncoder.matches(password, getUserPW());
    }

    /**
     * 비밀번호 변경 (변경감지)
     */
    public void setUserPW(String userPW) {
        this.userPW = userPW;
    }
}
