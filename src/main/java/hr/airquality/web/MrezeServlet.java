package hr.airquality.web;

import hr.airquality.dto.MrezaDTO;
import hr.airquality.service.MrezaService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/fm/mreze")
public class MrezeServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(MrezeServlet.class);

    @EJB
    private MrezaService mrezaService;  // samo read operations

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<MrezaDTO> mreze = mrezaService.getAllMreze();
        log.info("Prikaz mreža preko FreeMarker-a, broj mreža: {}", mreze.size());

        Map<String, Object> model = new HashMap<>();
        model.put("mreze", mreze);

        Configuration cfg = (Configuration) req.getServletContext().getAttribute("freemarkerCfg");
        Template template = cfg.getTemplate("mreze.ftl");

        resp.setContentType("text/html;charset=UTF-8");

        try (var out = resp.getWriter()) {
            template.process(model, out);
        } catch (TemplateException e) {
            log.error("Greška pri obradi FreeMarker template-a", e);
            throw new ServletException("FreeMarker template error", e);
        }
    }
}
