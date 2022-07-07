package hooyn.todo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class TodoApplication {
	static Logger logger = LoggerFactory.getLogger(TodoApplication.class);
	public static void main(String[] args) {
		logger.info("서버 정상 작동..." + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

		SpringApplication.run(TodoApplication.class, args);
	}

}
