package hooyn.todo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hooyn.todo.service.DateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@Slf4j
public class TodoApplication {
	public static void main(String[] args) {
		log.info("서버 정상 작동 8080..." + new DateService().getDate());
		SpringApplication.run(TodoApplication.class, args);
	}

	@Bean
	JPAQueryFactory jpaQueryFactory(EntityManager em){
		return new JPAQueryFactory(em);
	}
}
