package tienda.puntos.app.web.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import tienda.puntos.app.model.dto.StoreDTO;
import tienda.puntos.app.services.store.StoreService;
import tienda.puntos.app.web.webservices.StoreController;

@Controller
public class StoreModelAndViewController {

    private static final Logger log = LoggerFactory.getLogger(StoreController.class);

    @Autowired
    private StoreService storeService;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${nombre.equipo}")
    private String nombresEquipo;

    @GetMapping("/tiendas")
    public ModelAndView findAllStores() {
        log.info(this.getClass().getSimpleName() + " - findAllStores() - Obtenemos todo el listado de tiendas");

        List<StoreDTO> listaTiendasDTO = this.storeService.findAllStores();
        return new ModelAndView("stores").addObject("listaTiendasDTO", listaTiendasDTO)
                .addObject("appName", appName)
                .addObject("nombresEquipo", nombresEquipo);
    }

    @GetMapping("/tiendas/{id}")
    public ModelAndView findStoreByID(@PathVariable Long id) {
        log.info(this.getClass().getSimpleName() + " - findStoreByID() - Obtenemos una tienda por id" + id);

        return new ModelAndView("viewStore").addObject("store", this.storeService.findStoreByID(id))
                .addObject("appName", appName)
                .addObject("nombresEquipo", nombresEquipo);
    }

}
