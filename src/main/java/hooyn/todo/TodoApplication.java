package hooyn.todo;

import hooyn.todo.function.PrintDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class TodoApplication {
	public static void main(String[] args) {
		log.info("서버 정상 작동 8080..." + new PrintDate().getDate());
		SpringApplication.run(TodoApplication.class, args);
	}
}
