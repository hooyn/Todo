package hooyn.todo.api.controller;

import hooyn.todo.api.request.SignInRequest;
import hooyn.todo.api.request.SignUpRequest;
import hooyn.todo.api.response.Response;
import hooyn.todo.domain.Member;
import hooyn.todo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/member/signup")
    public Response signUp(@RequestBody SignUpRequest request){
        boolean checkID = memberService.checkDuplicatedID(request.getUserID());
        if(checkID){
            if(memberService.checkPasswordConstraint(request.getUserPW())){
                UUID data = memberService.join(request.getUserNM(), request.getUserID(), request.getUserPW());
                return new Response(true, HttpStatus.OK.value(), data, "회원가입이 정상적으로 처리되었습니다.");
            } else {
                // 302 에러
                return new Response(false, HttpStatus.FOUND.value(), null, "비밀번호는 영문과 숫자포함 8-20자리입니다.");
            }
        } else {
            // 301 에러
            return new Response(false, HttpStatus.MOVED_PERMANENTLY.value(), null, "이미 사용중인 아이디 입니다.");
        }
    }

    @PostMapping("/member/signin")
    public Response signUp(@RequestBody SignInRequest request){
        Member member = memberService.findUser(request.getUserID());
        if(member!=null){
            if(member.matchPassword(passwordEncoder, request.getUserPW())){
                return new Response(true, HttpStatus.OK.value(), member.getUuid(), "로그인 되었습니다.");
            } else {
                // 302 에러
                return new Response(false, HttpStatus.FOUND.value(), null, "비밀번호가 일치하지 않습니다.");
            }
        } else {
            // 301 에러
            return new Response(false, HttpStatus.MOVED_PERMANENTLY.value(), null, "등록되지 않은 아이디입니다.");
        }
    }
}
