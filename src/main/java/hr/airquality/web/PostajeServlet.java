package hr.airquality.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import hr.airquality.dto.MrezaDTO;
import hr.airquality.service.MrezaService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Putanja za FreeMarker klijenta - postaje
*/

@WebServlet("/fm/postaje")
public class PostajeServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(PostajeServlet.class);

    @EJB
    private MrezaService mrezaService; // samo read operations

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String mrezaNaziv = req.getParameter("mrezaNaziv");
        if (mrezaNaziv == null || mrezaNaziv.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nedostaje parametar mrezaNaziv");
            return;
        }

        MrezaDTO mrezaDTO = mrezaService.getMrezaByNaziv(mrezaNaziv);
        if (mrezaDTO == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Mreza nije pronađena");
            return;
        }

        log.info("Prikaz postaja mreže '{}', broj postaja: {}", mrezaNaziv, mrezaDTO.getPostaje().size());

        Map<String, Object> model = new HashMap<>();
        model.put("postaje", mrezaDTO.getPostaje());
        model.put("mreza", mrezaDTO);

        freemarker.template.Configuration cfg =
                (freemarker.template.Configuration) req.getServletContext().getAttribute("freemarkerCfg");
        freemarker.template.Template template = cfg.getTemplate("postaje.ftl");

        resp.setContentType("text/html;charset=UTF-8");
        try (var out = resp.getWriter()) {
            template.process(model, out);
        } catch (freemarker.template.TemplateException e) {
            log.error("Greška kod renderiranja FreeMarker template-a", e);
            throw new ServletException("Greška kod renderiranja FreeMarker template-a", e);
        }
    }
}

