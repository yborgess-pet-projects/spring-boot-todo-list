package org.demo.todolist;

import org.demo.todolist.config.TodoListProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {
    private static final Logger logger = Logger.getLogger(WebConfiguration.class.getName());
    private final TodoListProperties conf;

    public WebConfiguration(TodoListProperties conf) {
        this.conf = conf;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        logger.log(Level.FINE, conf.toString());

        registry.addMapping("/**")
                .allowedOrigins(conf.getCors().getAllowedOrigins())
                .allowedMethods(conf.getCors().getAllowedMethods())
                .maxAge(conf.getCors().getMaxAge())
                .allowedHeaders(conf.getCors().getAllowedHeaders())
                .exposedHeaders(conf.getCors().getExposedHeader())
                .allowCredentials(conf.getCors().isAllowCredentials());
    }
}
