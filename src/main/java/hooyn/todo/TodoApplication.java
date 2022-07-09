package hooyn.todo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class TodoApplication {
	static Logger logger = LoggerFactory.getLogger(TodoApplication.class);
	public static void main(String[] args) {
		logger.info("서버 정상 작동...  시작시간 ["
				+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
				+" ]");
		SpringApplication.run(TodoApplication.class, args);
	}

	@Bean
	JPAQueryFactory jpaQueryFactory(EntityManager em){
		return new JPAQueryFactory(em);
	}
}
