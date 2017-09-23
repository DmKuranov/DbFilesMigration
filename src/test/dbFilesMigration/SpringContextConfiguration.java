package dbFilesMigration;

import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@ActiveProfiles({"test"})
public abstract class SpringContextConfiguration {

    @Configuration
    @ImportResource({"classpath:applicationContext.xml" })
    @ComponentScan(basePackages= "dbFilesMigration")
    public static class ContextConfiguration{

    }
}
