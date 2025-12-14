package hr.airquality.web;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import hr.airquality.dto.PostajaDTO;
import hr.airquality.service.AirQualityService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/fm/postaja")
public class PostajaServlet extends HttpServlet {

    @EJB
    private AirQualityService airQualityService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String postajaNaziv = req.getParameter("naziv");
        if (postajaNaziv == null || postajaNaziv.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nedostaje parametar naziv");
            return;
        }

        PostajaDTO postaja = airQualityService.getPostajaDTOByNaziv(postajaNaziv);
        if (postaja == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Postaja nije pronađena");
            return;
        }

        Map<String, Object> model = new HashMap<>();
        model.put("postaja", postaja);

        Configuration cfg = (Configuration) req.getServletContext().getAttribute("freemarkerCfg");
        Template template = cfg.getTemplate("postaja.ftl");

        resp.setContentType("text/html;charset=UTF-8");
        try {
            template.process(model, resp.getWriter());
        } catch (TemplateException e) {
            throw new ServletException("FreeMarker template error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String postajaNaziv = req.getParameter("naziv");
        String nazivEng = req.getParameter("nazivEng");
        boolean aktivna = "on".equals(req.getParameter("aktivna"));

        if (postajaNaziv == null || postajaNaziv.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nedostaje naziv postaje");
            return;
        }

        boolean updated = airQualityService.updatePostaja(postajaNaziv, nazivEng, aktivna);
        if (!updated) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Postaja nije pronađena za update");
            return;
        }

        String mrezaNaziv = req.getParameter("mrezaNaziv");
        String encodedMrezaNaziv = URLEncoder.encode(mrezaNaziv, StandardCharsets.UTF_8.toString());

        resp.sendRedirect(req.getContextPath() + "/fm/postaje?mrezaNaziv=" + encodedMrezaNaziv);
    }
}
