package hooyn.todo.repository;

import hooyn.todo.domain.Deadline;
import hooyn.todo.domain.Todo;

import java.util.List;

public interface TodoRepository {

    //투두 작성
    Long save(Todo todo);

    //투두 엔티티 검색
    Todo findById(Long id);

    //투두 UUID에 따른 엔티티 검색
    List<Todo> findByUUID(String uuid);

    //투두 Deadline에 따른 엔티티 검색
    List<Todo> findByDeadline(String uuid, Deadline deadLine);

    //투두 콘텐츠 키워드에 따른 엔티티 검색
    List<Todo> findByContent(String uuid, String content);

    //투두 수정, 삭제 권한 확인
    boolean checkAuthorization(String uuid, Long id);

    //투두 삭제
    Long delete(Long id);
}
