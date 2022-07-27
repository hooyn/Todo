package hooyn.todo.service;

import hooyn.todo.domain.Member;
import hooyn.todo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Primary //MemberService 의존관계 주입 시 우선권을 가진다.
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 가입
     */
    @Transactional
    public UUID join(String userNM, String userID, String userPW){
        Member member = new Member(userNM, userID, userPW);
        member.encodePassword(passwordEncoder);
        return memberRepository.save(member);
    }

    /**
     * 아이디에 따른 회원 조회
     */
    @Transactional(readOnly = true)
    public Member findUserByUserID(String userID){
        return memberRepository.findByUserId(userID);
    }

    /**
     * UUID에 따른 회원 조회
     */
    @Transactional(readOnly = true)
    public Member findUserByUUID(String uuid){
        return memberRepository.findByUUID(uuid);
    }

    /**
     * 아이디 중복 확인
     */
    @Transactional(readOnly = true)
    public boolean checkDuplicatedID(String userID){
        Member member = memberRepository.findByUserId(userID);
        if(member!=null){
            return false; //아이디로 검색되는 회원이 있다면 중복(false)
        } else return true;
    }

    /**
     * 비밀번호 제약조건 확인
     */
    public boolean checkPasswordConstraint(String userPW){
        Pattern pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d).{8,20}$");
        return pattern.matcher(userPW).matches();
    }

    /**
     * 비밀번호 변경 (변경 감지 이용)
     */
    @Transactional
    public UUID changePassword(String uuid, String userPW){
        Member member = findUserByUUID(uuid);
        if(member==null) return null; //회원 정보 없음

        //비밀번호 암호화
        String encodedPW = passwordEncoder.encode(userPW);

        //변경 감지를 이용한 사용자 비밀번호 업데이트
        member.setUserPW(encodedPW);

        return member.getUuid();
    }
}
