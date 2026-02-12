package tienda.puntos.app.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    @Value("${spring.application.name}")
    private String appName;

    @Value("${nombre.equipo}")
    private String nombresEquipo;

    @GetMapping("/")
    public ModelAndView landing() {
        log.info(this.getClass().getName() + " - landing() - Devolvemos vista principal");
        return new ModelAndView("index").addObject("appName", appName).addObject("nombresEquipo", nombresEquipo);
    }

}
