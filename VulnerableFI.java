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
			isFiChallengeCompleted(page, 2);
			model.addAttribute("pageTitle",
					"Vulnerability: File Inclusion :: Web Application with Vulnerable (WEBVUL)");
			model.addAttribute("pageId", "fi");
			model.addAttribute("fileName", page);
			return "fi";
		} catch (Exception e) {
			e.printStackTrace();
			return "index";
		}
	}

	public String[] isFiChallengeCompleted(String fileName, Integer vulnerableId) {
		if (fileName.contentEquals("file3.html"))
			return updateChallengeStatus(vulnerableId);
		return new String[] { "false", "Please try again" };
	}

	public String[] updateChallengeStatus(Integer vulnerableId) {
		try {
			Vulnerabilities vul = vulnerabilitiesRepo.findById(vulnerableId).get();
			vul.setFoundStatus(true);
			vulnerabilitiesRepo.save(vul);
			return new String[] { "true", "Success" };
		} catch (Exception e) {
			return new String[] { "false", e.getMessage() };
		}
	}
}
