package ru.nsu.gulteam.prof_standards.backend.dummy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties("dummy.database")
public class DummyDatabaseFillerProperties {
    private boolean fill = false;

    public boolean needFill() {
        return fill;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
    }
}
