package hooyn.todo.service;

import hooyn.todo.domain.Member;

import java.util.UUID;

public interface MemberService {

    //회원가입
    UUID join(String userNM, String userID, String userPW);

    //UserID를 통한 회원(엔티티) 검색
    Member findUserByUserID(String userID);

    //UUID를 통한 회원(엔티티) 검색
    Member findUserByUUID(String uuid);

    //ID 중복확인
    boolean checkDuplicatedID(String userID);

    //비밀번호 제약조건 확인
    boolean checkPasswordConstraint(String userPW);

    //비밀번호 변경
    UUID changePassword(String uuid, String userPW);

    boolean checkPasswordSame(String password, String password_check);
}
