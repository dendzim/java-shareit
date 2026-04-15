package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.ErrorHandler;
import ru.practicum.shareit.exceptions.ErrorResponse;
import ru.practicum.shareit.exceptions.InternalServerException;
import ru.practicum.shareit.exceptions.ValidationException;

@SpringBootTest
class ShareItTests {

	@Test
	void contextLoads() {
		ShareItApp.main(new String[]{});
		new ErrorHandler();
		new ErrorResponse("test");
		new ValidationException("test");
		new InternalServerException("test");
	}

}
