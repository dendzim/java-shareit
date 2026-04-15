package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.ErrorHandler;
import ru.practicum.shareit.exceptions.ErrorResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@SpringBootTest
class ShareItTests {

	@Test
	void contextLoads() {
		ShareItApp.main(new String[]{});
		new ErrorHandler();
		new ErrorResponse("test");
		new User();
		new Item();
		new Booking();
	}

}
