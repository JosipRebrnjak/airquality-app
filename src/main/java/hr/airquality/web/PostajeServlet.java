package hr.airquality.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import hr.airquality.dto.MrezaDTO;
import hr.airquality.service.AirQualityService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/fm/postaje")
public class PostajeServlet extends HttpServlet {

    @EJB
    private AirQualityService airQualityService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String mrezaNaziv = req.getParameter("mrezaNaziv");
        if (mrezaNaziv == null || mrezaNaziv.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nedostaje parametar mrezaNaziv");
            return;
        }

        MrezaDTO mrezaDTO = airQualityService.getMrezaDTOByNaziv(mrezaNaziv);
        if (mrezaDTO == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Mreza nije pronađena");
            return;
        }

        try {
            freemarker.template.Configuration cfg = (freemarker.template.Configuration) req.getServletContext()
                    .getAttribute("freemarkerCfg");

            freemarker.template.Template template = cfg.getTemplate("postaje.ftl");
            Map<String, Object> model = new HashMap<>();
            model.put("postaje", mrezaDTO.getPostaje());
            model.put("mreza", mrezaDTO);
            resp.setContentType("text/html;charset=UTF-8");
            template.process(model, resp.getWriter());
        } catch (Exception e) {
            throw new ServletException("Greška kod renderiranja FreeMarker template-a", e);
        }
    }

}
