package hooyn.todo.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo {

    @Id @GeneratedValue
    @Column(name = "todo_id")
    private Long id;

    private String title;
    private String content;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "date", column = @Column(name = "deadline_date")),
            @AttributeOverride(name = "time", column = @Column(name = "deadline_time"))
    })
    private Deadline deadline;

    private LocalDateTime create_time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uuid")
    private Member member;

    //연간관계 편의 메서드
    void setMember(Member member){
        this.member = member;
        member.getTodos().add(this);
    }

    //변경감지를 위한 set 메서드
    public void setTitle(String title){
        this.title = title;
    }

    public void setDeadline(Deadline deadline){
        this.deadline = deadline;
    }

    public void setContent(String content) {
        this.content = content;
    }

    //생성 매서드

}

