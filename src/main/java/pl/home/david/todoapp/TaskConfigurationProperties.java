package pl.home.david.todoapp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("task")
public class TaskConfigurationProperties {
    private Template template = new Template();

    public static class Template {

        private boolean allowMultipleTaskFromTemplate;

        public boolean isAllowMultipleTaskFromTemplate() {
            return allowMultipleTaskFromTemplate;
        }

        public void setAllowMultipleTaskFromTemplate(boolean allowMultipleTaskFromTemplate) {
            this.allowMultipleTaskFromTemplate = allowMultipleTaskFromTemplate;
        }
    }

   public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }
}
