package org.demo.todolist.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.StringJoiner;

@ConfigurationProperties(prefix = "org.demo.todolist")
public class TodoListProperties {
    private String appVersion;
    private Cors cors = new Cors();

    public Cors getCors() {
        return cors;
    }

    public void setCors(Cors cors) {
        this.cors = cors;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TodoListProperties.class.getSimpleName() + "[", "]")
                .add("appVersion='" + appVersion + "'")
                .add("cors=" + cors)
                .toString();
    }

    public static class Cors {
        private String allowedOrigins = "*";
        private boolean allowCredentials = false;
        private String allowedMethods = "*";
        private int maxAge = 3600;
        private String allowedHeaders = "*";
        private String exposedHeader = "*";

        public String getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(String allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }

        public boolean isAllowCredentials() {
            return allowCredentials;
        }

        public void setAllowCredentials(boolean allowCredentials) {
            this.allowCredentials = allowCredentials;
        }

        public String getAllowedMethods() {
            return allowedMethods;
        }

        public void setAllowedMethods(String allowedMethods) {
            this.allowedMethods = allowedMethods;
        }

        public int getMaxAge() {
            return maxAge;
        }

        public void setMaxAge(int maxAge) {
            this.maxAge = maxAge;
        }

        public String getAllowedHeaders() {
            return allowedHeaders;
        }

        public void setAllowedHeaders(String allowedHeaders) {
            this.allowedHeaders = allowedHeaders;
        }

        public String getExposedHeader() {
            return exposedHeader;
        }

        public void setExposedHeader(String exposedHeader) {
            this.exposedHeader = exposedHeader;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Cors.class.getSimpleName() + "[", "]")
                    .add("allowedOrigins='" + allowedOrigins + "'")
                    .add("allowCredentials=" + allowCredentials)
                    .add("allowedMethods='" + allowedMethods + "'")
                    .add("maxAge=" + maxAge)
                    .add("allowedHeaders='" + allowedHeaders + "'")
                    .add("exposedHeader='" + exposedHeader + "'")
                    .toString();
        }
    }
}
