package com.fresco.t7challenge.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fresco.t7challenge.models.Vulnerabilities;
import com.fresco.t7challenge.repo.VulnerabilitiesRepo;

@Controller
public class FiController {
    @Autowired
    VulnerabilitiesRepo vulnerabilitiesRepo;

    @GetMapping("/fi")
    public String vulnerableFi(@RequestParam(defaultValue = "", required = false) String page, Model model) {
        try {
            if (isFiChallengeCompleted(page, 2)) {
                model.addAttribute("pageTitle", "Vulnerability: File Inclusion :: Web Application with Vulnerable (WEBVUL)");
                model.addAttribute("pageId", "fi");
                model.addAttribute("fileName", page);
                return "fi";
            } else {
                // Handle invalid or unauthorized page request
                return "index";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "index";
        }
    }

    public boolean isFiChallengeCompleted(String fileName, Integer vulnerableId) {
        return fileName.equals("file3.html") && updateChallengeStatus(vulnerableId);
    }

    public boolean updateChallengeStatus(Integer vulnerableId) {
        try {
            Vulnerabilities vul = vulnerabilitiesRepo.findById(vulnerableId).orElse(null);
            if (vul != null) {
                vul.setFoundStatus(true);
                vulnerabilitiesRepo.save(vul);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
