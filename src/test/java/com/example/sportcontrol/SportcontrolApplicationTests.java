package com.example.sportcontrol;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password="
})
@ActiveProfiles("test")
class SportcontrolApplicationTests {

    @Test
	void contextLoads() {}

    @Test
    void constructorCanBeCalled() {
        assertNotNull(new SportcontrolApplication());
    }

    @Test
    void mainDelegatesToSpringApplicationRun() {
        String[] args = {"--spring.main.web-application-type=none"};
        SportcontrolApplication.ApplicationRunner originalRunner = SportcontrolApplication.applicationRunner;
        CapturingRunner runner = new CapturingRunner();
        SportcontrolApplication.applicationRunner = runner;
        try {
            SportcontrolApplication.main(args);
            assertEquals(SportcontrolApplication.class, runner.applicationClass);
            assertArrayEquals(args, runner.args);
        } finally {
            SportcontrolApplication.applicationRunner = originalRunner;
        }
    }

    private static final class CapturingRunner implements SportcontrolApplication.ApplicationRunner {
        private Class<?> applicationClass;
        private String[] args;

        @Override
        public void run(Class<?> applicationClass, String... args) {
            this.applicationClass = applicationClass;
            this.args = args;
        }
    }
}
