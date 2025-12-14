package hr.airquality.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import hr.airquality.dto.MrezaDTO;
import hr.airquality.service.AirQualityService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/fm/mreze")
public class MrezeServlet extends HttpServlet {

    @EJB
    private AirQualityService airQualityService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<MrezaDTO> mreze = airQualityService.getAllMrezeDTO();
        Map<String, Object> model = new HashMap<>();
        model.put("mreze", mreze);

        Configuration cfg = (Configuration) req.getServletContext().getAttribute("freemarkerCfg");
        Template template = cfg.getTemplate("mreze.ftl");

        resp.setContentType("text/html;charset=UTF-8");
        try {
            template.process(model, resp.getWriter());
        } catch (TemplateException e) {
            throw new ServletException("FreeMarker template error", e);
        }
    }

}
