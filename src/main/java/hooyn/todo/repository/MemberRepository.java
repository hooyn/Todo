package hooyn.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hooyn.todo.domain.Member;
import hooyn.todo.domain.QMember;
import hooyn.todo.domain.todo.QTodo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.UUID;

import static hooyn.todo.domain.QMember.member;
import static hooyn.todo.domain.todo.QTodo.todo;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    /**
     * 회원 정보 저장
     */
    public UUID save(Member member){
        em.persist(member);
        return member.getUuid();
    }

    /**
     * 회원 정보 삭제
     */
    public void delete(String uuid){
        queryFactory
                .delete(todo)
                .where(todo.member.uuid.eq(UUID.fromString(uuid)))
                .execute();

        queryFactory
                .delete(member)
                .where(member.uuid.eq(UUID.fromString(uuid)))
                .execute();

        //추후에 메모 기능 추가되면 관련된 메모도 삭제
    }

    /**
     * 회원 엔티티 조회
     */
    public Member findByUUID(String uuid){
        return em.find(Member.class, UUID.fromString(uuid));
    }

    /**
     * UserID에 따른 회원 엔티티 조회
     */
    public Member findByUserId(String userID){
        return queryFactory
                .selectFrom(member)
                .where(member.userID.eq(userID))
                .fetchOne();
    }
}
