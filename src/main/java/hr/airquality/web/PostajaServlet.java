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
import hr.airquality.exception.NotFoundException;
import hr.airquality.service.PostajaService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Putanja za FreeMarker klijenta - postaja
*/
@WebServlet("/fm/postaja")
public class PostajaServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(PostajaServlet.class);

    @Inject
    private PostajaService postajaService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String postajaNaziv = req.getParameter("naziv");
        if (postajaNaziv == null || postajaNaziv.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nedostaje parametar naziv");
            return;
        }

        log.info("Dohvat postaje: {}", postajaNaziv);
        PostajaDTO postaja = postajaService.getPostajaByNaziv(postajaNaziv);

        if (postaja == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Postaja nije pronađena");
            return;
        }

        Map<String, Object> model = new HashMap<>();
        model.put("postaja", postaja);

        Configuration cfg = (Configuration) req.getServletContext().getAttribute("freemarkerCfg");
        Template template = cfg.getTemplate("postaja.ftl");

        resp.setContentType("text/html;charset=UTF-8");
        try (var out = resp.getWriter()) {
            template.process(model, out);
        } catch (TemplateException e) {
            log.error("Greška pri obradi FreeMarker template-a", e);
            throw new ServletException("FreeMarker template error", e);
        }
    }

    @Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String postajaNaziv = req.getParameter("naziv");
    String nazivEng = req.getParameter("nazivEng");
    boolean aktivna = "on".equals(req.getParameter("aktivna"));
    String mrezaNaziv = req.getParameter("mrezaNaziv");

    if (postajaNaziv == null || postajaNaziv.isEmpty() ||
        mrezaNaziv == null || mrezaNaziv.isEmpty()) {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nedostaje naziv postaje ili mreže");
        return;
    }

    try {
        PostajaDTO dto = new PostajaDTO(postajaNaziv, nazivEng, aktivna, mrezaNaziv);
        postajaService.updatePostaja(postajaNaziv, dto);
        log.info("Postaja '{}' uspješno ažurirana", postajaNaziv);
    } catch (NotFoundException e) {
        log.warn(e.getMessage());
        resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        return;
    }

    String encodedMrezaNaziv = URLEncoder.encode(mrezaNaziv, StandardCharsets.UTF_8);
    resp.sendRedirect(req.getContextPath() + "/fm/postaje?mrezaNaziv=" + encodedMrezaNaziv);
}
}

