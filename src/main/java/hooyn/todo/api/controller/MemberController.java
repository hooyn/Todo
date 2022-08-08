package hooyn.todo.api.controller;

import hooyn.todo.api.request.member.*;
import hooyn.todo.api.response.Response;
import hooyn.todo.domain.Member;
import hooyn.todo.function.PrintDate;
import hooyn.todo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static com.querydsl.core.util.StringUtils.isNullOrEmpty;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    private final PrintDate now = new PrintDate();

    /**
     * 회원가입
     */
    @PostMapping("/join")
    public Response join(@RequestBody JoinRequest request){
        String userID = request.getUserID();
        String userNM = request.getUserNM();
        String userPW = request.getUserPW();
        String userPWCHK = request.getUserPWCHK();

        if(isNullOrEmpty(userID) || isNullOrEmpty(userNM) || isNullOrEmpty(userPW) || isNullOrEmpty(userPWCHK)){
            log.error("필수 입력값 없음 Error Code:400 " + now.getDate());
            return new Response(false, HttpStatus.BAD_REQUEST.value(), null, "필수 입력값을 입력해주세요.");
        }
        boolean checkID = memberService.checkDuplicatedID(userID);
        if(checkID){
            boolean checkPW = memberService.checkPasswordConstraint(userPW);
            if(checkPW){
                boolean checkPWSame = memberService.checkPasswordSame(userPW, userPWCHK);
                if(checkPWSame){
                    String data = String.valueOf(memberService.join(userNM, userID, userPW));
                    log.info(data + " 회원가입 Success Code:200 " + now.getDate());
                    return new Response(true, HttpStatus.OK.value(), data, "회원가입이 정상적으로 처리되었습니다.");
                } else {
                    // 303 에러
                    log.error("비밀번호 불일치 Error Code:303 " + now.getDate());
                    return new Response(false, HttpStatus.SEE_OTHER.value(), null, "비밀번호가 일치하지 않습니다.");
                }
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
        String userID = request.getUserID();
        String userPW = request.getUserPW();

        if(isNullOrEmpty(userID) || isNullOrEmpty(userPW)){
            log.error("필수 입력값 없음 Error Code:400 " + now.getDate());
            return new Response(false, HttpStatus.BAD_REQUEST.value(), null, "필수 입력값을 입력해주세요.");
        }

        Member member = memberService.findUserByUserID(userID);
        if(member!=null){
            if(member.matchPassword(passwordEncoder, userPW)){
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
     * 회원 삭제
     */
    @DeleteMapping("/withdraw")
    public Response withdraw(@RequestBody WithdrawRequest request){
        String uuid = request.getUuid();

        if(isNullOrEmpty(uuid)){
            log.error("필수 입력값 없음 Error Code:400 " + now.getDate());
            return new Response(false, HttpStatus.BAD_REQUEST.value(), null, "필수 입력값을 입력해주세요.");
        }

        Member member = memberService.findUserByUUID(uuid);
        if(member != null){
            memberService.deleteMember(uuid);
            log.info("회원 삭제 Success Code:200 " + now.getDate());
            return new Response(true, HttpStatus.OK.value(), member.getUuid(), "회원이 삭제되었습니다.");
        } else {
            // 301 에러
            log.error("회원 정보 Error Code:301 " + now.getDate());
            return new Response(false, HttpStatus.MOVED_PERMANENTLY.value(), null, "등록되지 않은 회원입니다.");
        }
    }

    /**
     * 비밀번호 변경을 위한 기존 비밀번호 확인
     */
    @PostMapping("/password/origin")
    public Response checkPasswordOrigin(@RequestBody ChangePasswordRequest request){
        String password = request.getPassword();
        String uuid = request.getUuid();

        if(isNullOrEmpty(password) || isNullOrEmpty(uuid)){
            log.error("필수 입력값 없음 Error Code:400 " + now.getDate());
            return new Response(false, HttpStatus.BAD_REQUEST.value(), null, "필수 입력값을 입력해주세요.");
        }

        Member member = memberService.findUserByUUID(uuid);
        if(member!=null){
            if(member.matchPassword(passwordEncoder, password)){
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
        String password = request.getPassword();
        String req_uuid = request.getUuid();

        if(isNullOrEmpty(password) || isNullOrEmpty(req_uuid)){
            log.error("필수 입력값 없음 Error Code:400 " + now.getDate());
            return new Response(false, HttpStatus.BAD_REQUEST.value(), null, "필수 입력값을 입력해주세요.");
        }

        boolean checkPW = memberService.checkPasswordConstraint(password);
        if(checkPW){
            String uuid = String.valueOf(memberService.changePassword(req_uuid, password));
            //UUID로 받으면 UUID는 null이 될 수 없기 때문에 500에러가 나옵니다. 조심조심!

            if(!uuid.equals("null")){
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

    /**
     * 비밀번호 제약조건 확인 및 2차 비밀번호 확인
     */
    @PostMapping("/password/check")
    public Response checkPassword(@RequestBody CheckPasswordRequest request){
        String userPW = request.getUserPW();
        String userPWCHK = request.getUserPWCHK();

        if(isNullOrEmpty(userPWCHK) || isNullOrEmpty(userPW)){
            log.error("필수 입력값 없음 Error Code:400 " + now.getDate());
            return new Response(false, HttpStatus.BAD_REQUEST.value(), null, "필수 입력값을 입력해주세요.");
        }

        boolean checkPasswordConstraint = memberService.checkPasswordConstraint(userPW);
        if(checkPasswordConstraint){
            if(memberService.checkPasswordSame(userPW, userPWCHK)){
                log.info("비밀번호 확인 Success Code:200 " + now.getDate());
                return new Response(true, HttpStatus.OK.value(), null, "비밀번호가 확인되었습니다.");
            } else {
                // 301 에러
                log.error("비밀번호 불일치 Error Code:301 " + now.getDate());
                return new Response(false, HttpStatus.MOVED_PERMANENTLY.value(), null, "비밀번호가 일치하지 않습니다.");
            }
        } else {
            // 302 에러
            log.error("비밀번호 제약조건 Error Code:302 " + now.getDate());
            return new Response(false, HttpStatus.FOUND.value(), null, "비밀번호는 영문과 숫자포함 8-20자리입니다.");
        }
    }

    /**
     * 아이디 중복 확인
     */
    @PostMapping("/duplicate/id")
    public Response checkDuplicateId(@RequestBody CheckDuplicateIdRequest request){
        String id = request.getId();

        if(isNullOrEmpty(id)){
            log.error("필수 입력값 없음 Error Code:400 " + now.getDate());
            return new Response(false, HttpStatus.BAD_REQUEST.value(), null, "필수 입력값을 입력해주세요.");
        }

        boolean check = memberService.checkDuplicatedID(id);

        if(check){
            log.info("아이디 중복 Success Code:200 " + now.getDate());
            return new Response(true, HttpStatus.OK.value(), request.getId(), "사용가능한 아이디 입니다.");
        } else {
            // 301 에러
            log.error("아이디 중복 Error Code:301 " + now.getDate());
            return new Response(false, HttpStatus.MOVED_PERMANENTLY.value(), null, "이미 사용중인 아이디 입니다.");
        }
    }


}
