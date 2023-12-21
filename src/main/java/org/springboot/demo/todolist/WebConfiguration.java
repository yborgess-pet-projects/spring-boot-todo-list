package org.springboot.demo.todolist;

import org.springboot.demo.todolist.config.TodoListProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
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

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Jackson adds the default Json Converter using application/+json
        // We place our Json Patch converter before Jackson one, otherwise Jackson will try to serialize
        // our application/merge-patch+json PATCH request
        int i = 0;
        for (; i < converters.size(); i++) {
            if (converters.get(i) instanceof MappingJackson2HttpMessageConverter) {
                break;
            }
        }
        HttpMessageConverter<?> mergePatchConverter = new JsonMergePatchHttpMessageConverter();
        converters.add(i, mergePatchConverter);
    }
}
