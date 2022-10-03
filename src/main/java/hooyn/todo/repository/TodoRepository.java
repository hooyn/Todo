package hooyn.todo.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hooyn.todo.domain.todo.Deadline;
import hooyn.todo.domain.todo.QTodo;
import hooyn.todo.domain.todo.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

import static hooyn.todo.domain.todo.QTodo.todo;


@Repository
@RequiredArgsConstructor
public class TodoRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    /**
     * 투두 작성
     */
    public Long save(Todo todo) {
        em.persist(todo);

        return todo.getId();
    }

    /**
     * 투두_아이디에 따른 투두 조회
     */
    public Todo findById(Long id) {
        return em.find(Todo.class, id);
    }

    /**
     * UUID에 따른 투두 조회
     */
    public List<Todo> findByUUID(String uuid) {
        return queryFactory
                .selectFrom(todo)
                .where(todo.member.uuid.eq(UUID.fromString(uuid)))
                .fetch();
    }

    /**
     * 기한에 따른 투두 조회
     */
    public List<Todo> findByDeadline(String uuid, Deadline deadLine, Integer page) {
        return queryFactory
                .selectFrom(todo)
                .where(todo.member.uuid.eq(UUID.fromString(uuid))
                        .and(todo.deadline.date.eq(deadLine.getDate())))
                .orderBy(todo.create_time.desc())
                .offset(0+((page-1)*10))
                .limit(10)
                .fetch();
    }

    /**
     * content가 포함된 투두 조회
     */
    public List<Todo> findByContent(String uuid, String content, Integer page) {
        return queryFactory
                .selectFrom(todo)
                .where(
                        (todo.content.like("%"+content+"%").or(todo.title.like("%"+content+"%")))
                                .and(todo.member.uuid.eq(UUID.fromString(uuid)))
                )
                .orderBy(todo.create_time.desc())
                .offset(0+((page-1)*10))
                .limit(10)
                .fetch();
    }

    /**
     * 홈에서 회원이 작성한 투두 한번에 확인
     */
    public List<Tuple> findEventByYearMonth(String uuid, String year, String month){
        List<Tuple> result = queryFactory
                .select(todo.deadline.date, todo.deadline.date.count())
                .distinct()
                .from(todo)
                .where(todo.deadline.date.like(year + "/" + month + "/%")
                        .and(todo.member.uuid.eq(UUID.fromString(uuid))))
                .groupBy(todo.deadline.date)
                .fetch();

        return result;
    }


    /**
     * 권한 확인
     */
    public boolean checkAuthorization(String uuid, Long id) {
        List<Todo> fetch = queryFactory
                .selectFrom(todo)
                .where(todo.member.uuid.eq(UUID.fromString(uuid))
                        .and(todo.id.eq(id)))
                .fetch();

        if(fetch.size()>0){
            return true;
        } else {
            return false;
        }
    }

    /**
     * 투두 삭제
     */
    public Long delete(Long id) {
        queryFactory
                .delete(todo)
                .where(todo.id.eq(id))
                .execute();

        return id;
    }

}
