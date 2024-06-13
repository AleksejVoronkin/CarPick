package com.CarPick1.controller;

import com.CarPick1.service.ToPartsService;
import com.CarPick1.service.VinDecoderService;
import com.CarPick1.MailService.EmailService;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
public class VinDecoderController {

    private final VinDecoderService vinDecoderService;
    private final ToPartsService toPartsService;
    private final EmailService emailService;

    private  final Counter addDecodeVin = Metrics.counter("add_vin_count");
    @Autowired
    public VinDecoderController(VinDecoderService vinDecoderService, ToPartsService toPartsService, EmailService emailService) {
        this.vinDecoderService = vinDecoderService;
        this.toPartsService = toPartsService;
        this.emailService = emailService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/decodeKia")
    public String decodeVin(@RequestParam("vin") String vin, Model model) {
        String decodedInformation = vinDecoderService.decodeVIN(vin);
        String[] decodedParts = decodedInformation.split(", ");

        for (String part : decodedParts) {
            String[] keyValue = part.split(": ");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                model.addAttribute(key, value);
            }
        }
        addDecodeVin.increment();
        return "decodeKia";
    }

    @GetMapping("/ToPartsKia")
    public String getToParts(@RequestParam("modelSeries") String modelSeries,
                             @RequestParam("engineType") String engineType,
                             @RequestParam(value = "email", required = false) String email,
                             Model model) {
        try {
            List<String> toParts = toPartsService.getToParts(modelSeries, engineType);
            model.addAttribute("toParts", toParts);
            model.addAttribute("modelSeries", modelSeries);
            model.addAttribute("engineType", engineType);

            if (email != null && !email.isEmpty()) {
                StringBuilder partsText = new StringBuilder();
                for (String part : toParts) {
                    partsText.append(part).append("\n");
                }
                emailService.sendMail(email, "To Parts Information", partsText.toString());
                model.addAttribute("message", "Детали ТО отправлены на " + email);
            }

        } catch (IOException e) {
            model.addAttribute("errorMessage", "Не удалось загрузить детали ТО. Пожалуйста, проверьте правильность данных.");
        }
        return "toPartsKia";
    }
}
