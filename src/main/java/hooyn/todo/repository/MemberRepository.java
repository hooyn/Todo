package hooyn.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hooyn.todo.domain.Member;
import hooyn.todo.domain.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.UUID;

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
                .selectFrom(QMember.member)
                .where(QMember.member.userID.eq(userID))
                .fetchOne();
    }
}
