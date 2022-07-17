package hooyn.todo.api.controller;

import hooyn.todo.api.request.todo.DeleteTodoRequest;
import hooyn.todo.api.request.todo.FindTodoRequest;
import hooyn.todo.api.request.todo.UpdateTodoRequest;
import hooyn.todo.api.request.todo.WriteTodoRequest;
import hooyn.todo.api.response.Response;
import hooyn.todo.domain.Member;
import hooyn.todo.domain.Todo;
import hooyn.todo.dto.FindTodoDto;
import hooyn.todo.service.DateService;
import hooyn.todo.service.MemberService;
import hooyn.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TodoController {

    private final TodoService todoService;
    private final MemberService memberService;
    private final DateService now = new DateService();

    /**
     * 투두 작성
     */
    @PostMapping("/todo")
    public Response writeTodo(@RequestBody WriteTodoRequest request){
        Member member = memberService.findUserByUUID(request.getUuid());

        if(member!=null){
            Todo todo = Todo.createTodo(request.getTitle(), request.getContent(), request.getDeadline(), member);
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
        Member member = memberService.findUserByUUID(request.getUuid());

        if(member!=null){
            List<FindTodoDto> data = todoService.findTodoByDeadline(request.getUuid(), request.getDeadline(), page);

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
        Todo todo = todoService.findTodoById(request.getTodo_id());

        if(todo!=null){

            if(todoService.checkAuthorization(request.getUuid(), request.getTodo_id())){
                Long todo_id = todoService.updateTodo(request.getTodo_id(), request.getTitle(), request.getContent(), request.getDeadline());

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

}
