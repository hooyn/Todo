package hooyn.todo.api.controller;

import hooyn.todo.api.request.todo.*;
import hooyn.todo.api.response.Response;
import hooyn.todo.domain.Deadline;
import hooyn.todo.domain.Member;
import hooyn.todo.domain.Todo;
import hooyn.todo.dto.FindTodoDto;
import hooyn.todo.function.PrintDate;
import hooyn.todo.service.MemberService;
import hooyn.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.querydsl.core.util.StringUtils.isNullOrEmpty;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TodoController {

    private final TodoService todoService;
    private final MemberService memberService;
    private final PrintDate now = new PrintDate();

    /**
     * 투두 작성
     */
    @PostMapping("/todo")
    public Response writeTodo(@RequestBody WriteTodoRequest request){
        String uuid = request.getUuid();
        String title = request.getTitle();
        String content = request.getContent();
        Deadline deadline = request.getDeadline();

        if(isNullOrEmpty(uuid) || isNullOrEmpty(title) || isNullOrEmpty(content) || isNullOrEmpty(deadline.getDate())){
            log.error("필수 입력값 없음 Error Code:400 " + now.getDate());
            return new Response(true, HttpStatus.BAD_REQUEST.value(), null, "필수 입력값을 입력해주세요.");
        }

        Member member = memberService.findUserByUUID(uuid);

        if(member!=null){
            Todo todo = Todo.createTodo(title, content, deadline, member);
            Long todo_id = todoService.writeTodo(todo);

            log.info("투두 작성 Success Code:200 " + now.getDate());
            return new Response(true, HttpStatus.OK.value(), todo_id, "작성이 완료되었습니다.");
        } else {
            // 301 에러
            log.error("아이디 없음 Error Code:301 " + now.getDate());
            return new Response(false, HttpStatus.MOVED_PERMANENTLY.value(), null, "등록되지 않은 회원입니다.");
        }
    }

    /**
     * 기한에 따른 투두 조회
     */
    @PostMapping("/todo/deadline")
    public Response findTodoByDeadline(@RequestBody FindTodoRequest request, @RequestParam Integer page){
        String uuid = request.getUuid();
        Deadline deadline = request.getDeadline();

        if(isNullOrEmpty(uuid) || isNullOrEmpty(deadline.getDate())){
            log.error("필수 입력값 없음 Error Code:400 " + now.getDate());
            return new Response(true, HttpStatus.BAD_REQUEST.value(), null, "필수 입력값을 입력해주세요.");
        }

        Member member = memberService.findUserByUUID(uuid);

        if(member!=null){
            List<FindTodoDto> data = todoService.findTodoByDeadline(uuid, deadline, page);

            log.info("투두 조회 Success Code:200 " + now.getDate());
            return new Response(true, HttpStatus.OK.value(), data, "투두 데이터가 조회되었습니다.");
        } else {
            // 301 에러
            log.error("아이디 없음 Error Code:301 " + now.getDate());
            return new Response(false, HttpStatus.MOVED_PERMANENTLY.value(), null, "등록되지 않은 회원입니다.");
        }
    }

    /**
     * 키워드에 따른 투두 조회
     */
    @PostMapping("/todo/content")
    public Response findTodoByContent(@RequestBody FindTodoRequest request, @RequestParam Integer page){
        String uuid = request.getUuid();
        String content = request.getContent();

        if(isNullOrEmpty(uuid) || isNullOrEmpty(content)){
            log.error("필수 입력값 없음 Error Code:400 " + now.getDate());
            return new Response(true, HttpStatus.BAD_REQUEST.value(), null, "필수 입력값을 입력해주세요.");
        }

        Member member = memberService.findUserByUUID(request.getUuid());

        if(member!=null){
            List<FindTodoDto> data = todoService.findTodoByContent(request.getUuid(), request.getContent(), page);

            log.info("투두 조회 Success Code:200 " + now.getDate());
            return new Response(true, HttpStatus.OK.value(), data, "투두 데이터가 조회되었습니다.");
        } else {
            // 301 에러
            log.error("아이디 없음 Error Code:301 " + now.getDate());
            return new Response(false, HttpStatus.MOVED_PERMANENTLY.value(), null, "등록되지 않은 회원입니다.");
        }
    }

    /**
     * 투두 수정
     */
    @PutMapping("/todo")
    public Response updateTodo(@RequestBody UpdateTodoRequest request){
        Long req_todo_id = request.getTodo_id();
        String uuid = request.getUuid();
        String title = request.getTitle();
        String content = request.getContent();
        Deadline deadline = request.getDeadline();

        if(req_todo_id==null || isNullOrEmpty(uuid) || isNullOrEmpty(title) || isNullOrEmpty(content) ||isNullOrEmpty(deadline.getDate())){
            log.error("필수 입력값 없음 Error Code:400 " + now.getDate());
            return new Response(true, HttpStatus.BAD_REQUEST.value(), null, "필수 입력값을 입력해주세요.");
        }

        Todo todo = todoService.findTodoById(req_todo_id);

        if(todo!=null){

            if(todoService.checkAuthorization(uuid, req_todo_id)){
                Long todo_id = todoService.updateTodo(req_todo_id, title, content, deadline);

                log.info("투두 업데이트 Success Code:200 " + now.getDate());
                return new Response(true, HttpStatus.OK.value(), todo_id, "업데이트가 완료되었습니다.");
            } else {
                // 302 에러
                log.error("수정 권한 없음 Error Code:302 " + now.getDate());
                return new Response(false, HttpStatus.FOUND.value(), null, "수정 권한이 없습니다.");
            }

        } else {
            // 301 에러
            log.error("투두 없음 Error Code:301 " + now.getDate());
            return new Response(false, HttpStatus.MOVED_PERMANENTLY.value(), null, "등록되지 않은 투두입니다.");
        }
    }


    /**
     * 투두 삭제
     */
    @DeleteMapping("/todo")
    public Response deleteTodo(@RequestBody DeleteTodoRequest request){
        Long req_todo_id = request.getTodo_id();
        String uuid = request.getUuid();

        if(req_todo_id==null || isNullOrEmpty(uuid)){
            log.error("필수 입력값 없음 Error Code:400 " + now.getDate());
            return new Response(true, HttpStatus.BAD_REQUEST.value(), null, "필수 입력값을 입력해주세요.");
        }

        Member member = memberService.findUserByUUID(request.getUuid());

        if(member!=null){

            if(todoService.checkAuthorization(request.getUuid(), request.getTodo_id())){
                Long todo_id = todoService.deleteTodo(request.getTodo_id());

                log.info("투두 삭제 Success Code:200 " + now.getDate());
                return new Response(true, HttpStatus.OK.value(), todo_id, "삭제가 완료되었습니다.");
            } else {
                // 302 에러
                log.error("삭제 권한 없음 Error Code:302 " + now.getDate());
                return new Response(false, HttpStatus.FOUND.value(), null, "삭제 권한이 없습니다.");
            }

        } else {
            // 301 에러
            log.error("아이디 없음 Error Code:301 " + now.getDate());
            return new Response(false, HttpStatus.MOVED_PERMANENTLY.value(), null, "등록되지 않은 회원입니다.");
        }
    }

    /**
     * 투두 상태 변경
     */
    @PutMapping("/todo/status")
    public Response updateTodoStatus(@RequestBody UpdateTodoStatusRequest request){
        Long req_todo_id = request.getTodo_id();
        String uuid = request.getUuid();

        if(req_todo_id==null || isNullOrEmpty(uuid)){
            log.error("필수 입력값 없음 Error Code:400 " + now.getDate());
            return new Response(true, HttpStatus.BAD_REQUEST.value(), null, "필수 입력값을 입력해주세요.");
        }

        Member member = memberService.findUserByUUID(request.getUuid());

        if(member!=null){
            Todo todo = todoService.findTodoById(request.getTodo_id());
            if(todo!=null){
                Long todo_id = todoService.updateTodoStatus(request.getTodo_id());

                log.info("투두 상태 변경 Success Code:200 " + now.getDate());
                return new Response(true, HttpStatus.OK.value(), todo_id, "투두 상태가 변경되었습니다.");
            } else {
                // 302 에러
                log.error("투두 없음 Error Code:302 " + now.getDate());
                return new Response(false, HttpStatus.FOUND.value(), null, "등록되지 않은 투두입니다.");
            }

        } else {
            // 301 에러
            log.error("아이디 없음 Error Code:301 " + now.getDate());
            return new Response(false, HttpStatus.MOVED_PERMANENTLY.value(), null, "등록되지 않은 회원입니다.");
        }
    }
}
