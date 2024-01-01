package org.myapp.demo;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyappApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainTest(){
		assertEquals(1, 1);
	}

}
