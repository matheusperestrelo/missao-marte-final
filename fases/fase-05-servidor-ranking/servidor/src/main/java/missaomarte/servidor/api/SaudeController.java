package missaomarte.servidor.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SaudeController {

    public record Saude(String status) {
    }

    @GetMapping("/api/saude")
    public Saude saude() {
        return new Saude("ok");
    }
}
