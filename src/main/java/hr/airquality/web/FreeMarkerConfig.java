package hr.airquality.web;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

@WebListener
public class FreeMarkerConfig implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_33);
        cfg.setServletContextForTemplateLoading(sce.getServletContext(), "/WEB-INF/templates");
        cfg.setDefaultEncoding("UTF-8"); // VAŽNO
        cfg.setOutputEncoding("UTF-8"); // VAŽNO
        cfg.setURLEscapingCharset("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);

        sce.getServletContext().setAttribute("freemarkerCfg", cfg);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
       
    }
}
