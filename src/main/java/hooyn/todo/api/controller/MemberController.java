package hooyn.todo.api.controller;

import hooyn.todo.api.request.member.*;
import hooyn.todo.api.response.Response;
import hooyn.todo.domain.Member;
import hooyn.todo.service.DateService;
import hooyn.todo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    private final DateService now = new DateService();

    /**
     * 회원가입
     */
    @PostMapping("/join")
    public Response join(@RequestBody JoinRequest request){
        boolean checkID = memberService.checkDuplicatedID(request.getUserID());
        if(checkID){
            boolean checkPW = memberService.checkPasswordConstraint(request.getUserPW());
            if(checkPW){
                String data = String.valueOf(memberService.join(request.getUserNM(), request.getUserID(), request.getUserPW()));
                log.info(data + " 회원가입 Success Code:200 " + now.getDate());
                return new Response(true, HttpStatus.OK.value(), data, "회원가입이 정상적으로 처리되었습니다.");
            } else {
                // 302 에러
                log.error("비밀번호 제약조건 Error Code:302 " + now.getDate());
                return new Response(false, HttpStatus.FOUND.value(), null, "비밀번호는 영문과 숫자포함 8-20자리입니다.");
            }
        } else {
            // 301 에러
            log.error("아이디 중복 Error Code:301 " + now.getDate());
            return new Response(false, HttpStatus.MOVED_PERMANENTLY.value(), null, "이미 사용중인 아이디 입니다.");
        }
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public Response login(@RequestBody LoginRequest request){
        Member member = memberService.findUserByUserID(request.getUserID());
        if(member!=null){
            if(member.matchPassword(passwordEncoder, request.getUserPW())){
                log.info(member.getUuid() + " 로그인 Success Code:200 " + now.getDate());
                return new Response(true, HttpStatus.OK.value(), member.getUuid(), "로그인 되었습니다.");
            } else {
                // 302 에러
                log.error("비밀번호 불일치 Error Code:302 " + now.getDate());
                return new Response(false, HttpStatus.FOUND.value(), null, "비밀번호가 일치하지 않습니다.");
            }
        } else {
            // 301 에러
            log.error("아이디 없음 Error Code:301 " + now.getDate());
            return new Response(false, HttpStatus.MOVED_PERMANENTLY.value(), null, "등록되지 않은 아이디입니다.");
        }
    }

    /**
     * 비밀번호 제약조건 확인
     */
    @PostMapping("/password/constraint")
    public Response checkPasswordConstraint(@RequestBody CheckPasswordConstraintRequest request){
        boolean checkPW = memberService.checkPasswordConstraint(request.getPassword());

        if(checkPW){
            log.info("비밀번호 제약조건 Success Code:200 " + now.getDate());
            return new Response(true, HttpStatus.OK.value(), null, "비밀번호를 사용 가능합니다.");
        } else {
            // 302 에러
            log.error("비밀번호 제약조건 Error Code:302 " + now.getDate());
            return new Response(false, HttpStatus.FOUND.value(), null, "비밀번호는 영문과 숫자포함 8-20자리입니다.");
        }
    }

    /**
     * 2차 비밀번호 확인
     */
    @PostMapping("/password/same")
    public Response checkPasswordSame(@RequestBody CheckPasswordSameRequest request){
        if(request.getPassword().equals(request.getPassword_())){
            log.info("2차 비밀번호 일치 Success Code:200 " + now.getDate());
            return new Response(true, HttpStatus.OK.value(), null, "2차 비밀번호가 일치합니다.");
        } else {
            // 302 에러
            log.error("2차 비밀번호 불일치 Error Code:302 " + now.getDate());
            return new Response(false, HttpStatus.FOUND.value(), null, "2차 비밀번호가 일치하지 않습니다.");
        }
    }

    /**
     * 비밀번호 변경을 위한 기존 비밀번호 확인
     */
    @PostMapping("/password/origin")
    public Response checkPasswordOrigin(@RequestBody ChangePasswordRequest request){
        Member member = memberService.findUserByUUID(request.getUuid());
        if(member!=null){
            if(member.matchPassword(passwordEncoder, request.getPassword())){
                log.info("회원 인증 Success Code:200 " + now.getDate());
                return new Response(true, HttpStatus.OK.value(), member.getUuid(), "회원 인증이 되었습니다.");
            } else {
                // 302 에러
                log.error("비밀번호 불일치 Error Code:302 " + now.getDate());
                return new Response(false, HttpStatus.FOUND.value(), null, "비밀번호가 일치하지 않습니다.");
            }
        } else {
            // 301 에러
            log.error("회원 정보 Error Code:301 " + now.getDate());
            return new Response(false, HttpStatus.MOVED_PERMANENTLY.value(), null, "등록되지 않은 회원입니다.");
        }
    }

    /**
     * 비밀번호 변경
     */
    @PutMapping("/password")
    public Response changePassword(@RequestBody ChangePasswordRequest request){
        boolean checkPW = memberService.checkPasswordConstraint(request.getPassword());
        if(checkPW){
            String uuid = String.valueOf(memberService.changePassword(request.getUuid(), request.getPassword()));

            if(uuid!=null){
                log.info("비밀번호 변경 Success Code:200 " + now.getDate());
                return new Response(true, HttpStatus.OK.value(), uuid, "비밀번호가 변경되었습니다.");
            } else {
                // 301 에러
                log.error("회원 정보 Error Code:301 " + now.getDate());
                return new Response(false, HttpStatus.MOVED_PERMANENTLY.value(), null, "등록되지 않은 회원입니다.");
            }
        } else {
            // 302 에러
            log.error("비밀번호 제약조건 Error Code:302 " + now.getDate());
            return new Response(false, HttpStatus.FOUND.value(), null, "비밀번호는 영문과 숫자포함 8-20자리입니다.");
        }
    }

}
