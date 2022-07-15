package hooyn.todo.repository;

import hooyn.todo.domain.Member;

import java.util.UUID;

public interface MemberRepository {

    //회원 정보 저장
    UUID save(Member member);

    //회원 엔티티 조회
    Member findOne(String uuid);

    //사용자 아이디에 따른 회원 조회
    Member findByUserId(String userID);
}
