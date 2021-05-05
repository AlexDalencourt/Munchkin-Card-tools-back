package munchkin.integrator.infrastructure.rest;

import munchkin.integrator.domain.Type;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cards")
public class CardController {

    @GetMapping("types")
    public Type[] getAllCardTypes() {
        return Type.values();
    }
}
