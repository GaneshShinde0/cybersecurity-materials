https://dpaste.org/L2OvJ 
https://dpaste.org/L2OvJ
About History New snippet
Python Expires in: 4 weeks, 1 day
Delete Now
Raw
Slim
package com.fresco.t7challenge.controllers;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fresco.t7challenge.models.Users;
import com.fresco.t7challenge.models.Vulnerabilities;
import com.fresco.t7challenge.repo.SqlService;
import com.fresco.t7challenge.repo.UsersRepo;
import com.fresco.t7challenge.repo.VulnerabilitiesRepo;

@Controller
public class GeneralController {
	@Autowired
	VulnerabilitiesRepo vulnerabilitiesRepo;
	@Autowired
	SqlService sqlService;
	@Autowired
	UsersRepo usersRepo;

	public List<Vulnerabilities> getAllVulnerablesChallengeDetails() {
		return vulnerabilitiesRepo.findAll();
	}

	public String[] isSqliChallengeCompleted(List<Users> sqliOutput, Integer vulnerableId) {
		if (usersRepo.findAll().size() == sqliOutput.size())
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

	public List<Users> getSqliOutput(String formUserid) {
		List<Users> sqliOutput = sqlService.getUser(formUserid);
		return sqliOutput;
	}

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("pageTitle", "Welcome :: Web Application with Vulnerable (WEBVUL)");
		model.addAttribute("pageId", "home");
		return "index";
	}

	@GetMapping("/instructions")
	public String instructions(Model model) {
		model.addAttribute("pageTitle", "Instructions :: Web Application with Vulnerable (WEBVUL)");
		model.addAttribute("pageId", "instructions");
		getAllVulnerablesChallengeDetails();
		return "instruction.html";
	}

	@GetMapping("/challenge")
	public String challenge(Model model) {
		model.addAttribute("pageTitle", "Challenge :: Web Application with Vulnerable (WEBVUL)");
		model.addAttribute("pageId", "challenge");
		model.addAttribute("vulnerablesChallengeDetails", getAllVulnerablesChallengeDetails());
		return "challenge";
	}

	@GetMapping("/sqli")
	public String getVulnerableSqli(Model model) {
		model.addAttribute("pageTitle", "Vulnerability: SQL Injection :: Web Application with Vulnerable (WEBVUL)");
		model.addAttribute("pageId", "sqli");
		return "sqli";
	}

	@PostMapping("/sqli")
	public String postVulnerableSqli(@RequestParam(defaultValue = "", required = false) String user_id, Model model) {
		model.addAttribute("pageTitle", "Vulnerability: SQL Injection :: Web Application with Vulnerable (WEBVUL)");
		model.addAttribute("pageId", "sqli");
		List<Users> sqliOutput = getSqliOutput(user_id);
		isSqliChallengeCompleted(sqliOutput, 1);
		model.addAttribute("sqliOutput", sqliOutput);
		return "sqli";
	}

	// Below mentioned routes used for code editor

	@GetMapping("/editor")
	public String loadEditor() {
		return "editor";
	}

	@PostMapping("/save")
	@ResponseBody
	public JSONObject saveFile(@RequestBody JSONObject body) {
		JSONObject json = new JSONObject();
		try {
			String filename = (String) body.get("filename");
			String code = (String) body.get("code");
			if (filename.contains("controller"))
				filename = "src/main/java/com/fresco/t7challenge/repo/SqlService.java";
			else if (filename.contains("route"))
				filename = "src/main/java/com/fresco/t7challenge/controllers/FiController.java";
			else if (filename.contains("model"))
				filename = "src/main/java/com/fresco/t7challenge/models/Users.java";
			Files.write(Paths.get(filename), code.getBytes());
			json.put("save_status", true);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			json.put("save_status", false);
			return json;
		}

	}

	@PostMapping("/load")
	@ResponseBody
	public JSONObject loadFile(@RequestBody String filename) {
		JSONObject json = new JSONObject();
		try {
			if (filename.contains("controller"))
				filename = "src/main/java/com/fresco/t7challenge/repo/SqlService.java";
			else if (filename.contains("route"))
				filename = "src/main/java/com/fresco/t7challenge/controllers/FiController.java";
			else if (filename.contains("model"))
				filename = "src/main/java/com/fresco/t7challenge/models/Users.java";
			String fileContent = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
			json.put("load_status", true);
			json.put("code", fileContent);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			json.put("load_status", false);
			json.put("code", "");
			return json;
		}
	}

	@PostMapping("/test")
	public String testSqlApp(@RequestParam String filename) {


		Boolean testStatus = false;
		int score = 0;
		try {
			if (filename.contentEquals("routes_app.py")) {
				score = 100;
				testStatus = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "{'test_status': '" + testStatus + "', 'score': '" + score + "'}";
	}
}




package com.fresco.t7challenge.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class Users {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Integer userId;
	private String firstName;	
	private String lastName;	
	private String user;
	private String password;
	private String avatar;
	
	public Users() {
		super();
	}

	public Users(Integer userId, @NotBlank(message = "firstName is mandatory") String firstName,
			@NotBlank(message = "lastName is mandatory") String lastName,
			@NotBlank(message = "user is mandatory") String user,
			@NotBlank(message = "password is mandatory") String password,
			@NotBlank(message = "avatar is mandatory") String avatar) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.user = user;
		this.password = password;
		this.avatar = avatar;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public String toString() {
		return "Users [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", user=" + user
				+ ", password=" + password + ", avatar=" + avatar + "]";
	}	
}

package com.fresco.t7challenge.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class Vulnerabilities {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Integer vulnerableId;
	@NotBlank(message = "Name is mandatory")
	@Column(unique=true)
	private String vulnerableName;	
	@NotBlank(message = "Description is mandatory")
	private String vulnerableDesc;	
	@NotBlank(message = "Status is mandatory")
	private Boolean foundStatus;
	
	public Vulnerabilities() {
		super();
	}

	public Vulnerabilities(Integer vulnerableId, @NotBlank(message = "Name is mandatory") String vulnerableName,
			@NotBlank(message = "Description is mandatory") String vulnerableDesc,
			@NotBlank(message = "Status is mandatory") Boolean foundStatus) {
		super();
		this.vulnerableId = vulnerableId;
		this.vulnerableName = vulnerableName;
		this.vulnerableDesc = vulnerableDesc;
		this.foundStatus = foundStatus;
	}

	public int getVulnerableId() {
		return vulnerableId;
	}

	public void setVulnerableId(Integer vulnerableId) {
		this.vulnerableId = vulnerableId;
	}

	public String getVulnerableName() {
		return vulnerableName;
	}

	public void setVulnerableName(String vulnerableName) {
		this.vulnerableName = vulnerableName;
	}

	public String getVulnerableDesc() {
		return vulnerableDesc;
	}

	public void setVulnerableDesc(String vulnerableDesc) {
		this.vulnerableDesc = vulnerableDesc;
	}

	public Boolean getFoundStatus() {
		return foundStatus;
	}

	public void setFoundStatus(Boolean foundStatus) {
		this.foundStatus = foundStatus;
	}

	@Override
	public String toString() {
		return "Vulnerabilities [vulnerableId=" + vulnerableId + ", vulnerableName=" + vulnerableName
				+ ", vulnerableDesc=" + vulnerableDesc + ", foundStatus=" + foundStatus + "]";
	}
	
}
package com.fresco.t7challenge.repo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fresco.t7challenge.models.Users;

@Service
public class SqlService {
	public List<Users> getUser(String userId) {
		List<Users> users = new ArrayList<Users>();
		try (Connection sqlConnection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "")) {
			Statement st = sqlConnection.createStatement();
			ResultSet rs = st.executeQuery(
					"SELECT user_id, first_name, last_name, user, password, avatar FROM users WHERE user_id = "
							+ userId);
			while (rs.next()) {
				users.add(new Users(rs.getInt(1), rs.getString(1), rs.getString(1), rs.getString(1), rs.getString(1),
						rs.getString(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users;
	}
}
package com.fresco.t7challenge.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fresco.t7challenge.models.Users;

@Repository
public interface UsersRepo extends JpaRepository<Users, Integer> {
}

package com.fresco.t7challenge.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fresco.t7challenge.models.Vulnerabilities;

@Repository
public interface VulnerabilitiesRepo extends JpaRepository<Vulnerabilities, Integer>{
}

package com.fresco.t7challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class T7ChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(T7ChallengeApplication.class, args);
	}

}







Copy Snippet
Edit Snippet
 Wordwrap
package com.fresco.t7challenge.controllers;
​
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
​
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
​
import com.fresco.t7challenge.models.Users;
import com.fresco.t7challenge.models.Vulnerabilities;
import com.fresco.t7challenge.repo.SqlService;
import com.fresco.t7challenge.repo.UsersRepo;
import com.fresco.t7challenge.repo.VulnerabilitiesRepo;
​
@Controller
public class GeneralController {
	@Autowired
	VulnerabilitiesRepo vulnerabilitiesRepo;
	@Autowired
	SqlService sqlService;
	@Autowired
	UsersRepo usersRepo;
​
	public List<Vulnerabilities> getAllVulnerablesChallengeDetails() {
		return vulnerabilitiesRepo.findAll();
	}
​
	public String[] isSqliChallengeCompleted(List<Users> sqliOutput, Integer vulnerableId) {
		if (usersRepo.findAll().size() == sqliOutput.size())
			return updateChallengeStatus(vulnerableId);
		return new String[] { "false", "Please try again" };
	}
​
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
​
	public List<Users> getSqliOutput(String formUserid) {
		List<Users> sqliOutput = sqlService.getUser(formUserid);
		return sqliOutput;
	}
​
	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("pageTitle", "Welcome :: Web Application with Vulnerable (WEBVUL)");
		model.addAttribute("pageId", "home");
		return "index";
	}
​
	@GetMapping("/instructions")
	public String instructions(Model model) {
		model.addAttribute("pageTitle", "Instructions :: Web Application with Vulnerable (WEBVUL)");
		model.addAttribute("pageId", "instructions");
		getAllVulnerablesChallengeDetails();
		return "instruction.html";
	}
​
	@GetMapping("/challenge")
	public String challenge(Model model) {
		model.addAttribute("pageTitle", "Challenge :: Web Application with Vulnerable (WEBVUL)");
		model.addAttribute("pageId", "challenge");
		model.addAttribute("vulnerablesChallengeDetails", getAllVulnerablesChallengeDetails());
		return "challenge";
	}
​
	@GetMapping("/sqli")
	public String getVulnerableSqli(Model model) {
		model.addAttribute("pageTitle", "Vulnerability: SQL Injection :: Web Application with Vulnerable (WEBVUL)");
		model.addAttribute("pageId", "sqli");
		return "sqli";
	}
​
	@PostMapping("/sqli")
	public String postVulnerableSqli(@RequestParam(defaultValue = "", required = false) String user_id, Model model) {
		model.addAttribute("pageTitle", "Vulnerability: SQL Injection :: Web Application with Vulnerable (WEBVUL)");
		model.addAttribute("pageId", "sqli");
		List<Users> sqliOutput = getSqliOutput(user_id);
		isSqliChallengeCompleted(sqliOutput, 1);
		model.addAttribute("sqliOutput", sqliOutput);
		return "sqli";
	}
​
	// Below mentioned routes used for code editor
​
	@GetMapping("/editor")
	public String loadEditor() {
		return "editor";
	}
​
	@PostMapping("/save")
	@ResponseBody
	public JSONObject saveFile(@RequestBody JSONObject body) {
		JSONObject json = new JSONObject();
		try {
			String filename = (String) body.get("filename");
			String code = (String) body.get("code");
			if (filename.contains("controller"))
				filename = "src/main/java/com/fresco/t7challenge/repo/SqlService.java";
			else if (filename.contains("route"))
				filename = "src/main/java/com/fresco/t7challenge/controllers/FiController.java";
			else if (filename.contains("model"))
				filename = "src/main/java/com/fresco/t7challenge/models/Users.java";
			Files.write(Paths.get(filename), code.getBytes());
			json.put("save_status", true);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			json.put("save_status", false);
			return json;
		}
​
	}
​
	@PostMapping("/load")
	@ResponseBody
	public JSONObject loadFile(@RequestBody String filename) {
		JSONObject json = new JSONObject();
		try {
			if (filename.contains("controller"))
				filename = "src/main/java/com/fresco/t7challenge/repo/SqlService.java";
			else if (filename.contains("route"))
				filename = "src/main/java/com/fresco/t7challenge/controllers/FiController.java";
			else if (filename.contains("model"))
				filename = "src/main/java/com/fresco/t7challenge/models/Users.java";
			String fileContent = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
			json.put("load_status", true);
			json.put("code", fileContent);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			json.put("load_status", false);
			json.put("code", "");
			return json;
		}
	}
​
	@PostMapping("/test")
	public String testSqlApp(@RequestParam String filename) {
​
​
		Boolean testStatus = false;
		int score = 0;
		try {
			if (filename.contentEquals("routes_app.py")) {
				score = 100;
				testStatus = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "{'test_status': '" + testStatus + "', 'score': '" + score + "'}";
	}
}
​
​
​
​
package com.fresco.t7challenge.models;
​
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
​
@Entity
public class Users {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Integer userId;
	private String firstName;	
	private String lastName;	
	private String user;
	private String password;
	private String avatar;
	
	public Users() {
		super();
	}
​
	public Users(Integer userId, @NotBlank(message = "firstName is mandatory") String firstName,
			@NotBlank(message = "lastName is mandatory") String lastName,
			@NotBlank(message = "user is mandatory") String user,
			@NotBlank(message = "password is mandatory") String password,
			@NotBlank(message = "avatar is mandatory") String avatar) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.user = user;
		this.password = password;
		this.avatar = avatar;
	}
​
	public Integer getUserId() {
		return userId;
	}
​
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
​
	public String getFirstName() {
		return firstName;
	}
​
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
​
	public String getLastName() {
		return lastName;
	}
​
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
​
	public String getUser() {
		return user;
	}
​
	public void setUser(String user) {
		this.user = user;
	}
​
	public String getPassword() {
		return password;
	}
​
	public void setPassword(String password) {
		this.password = password;
	}
​
	public String getAvatar() {
		return avatar;
	}
​
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
​
	@Override
	public String toString() {
		return "Users [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", user=" + user
				+ ", password=" + password + ", avatar=" + avatar + "]";
	}	
}
​
package com.fresco.t7challenge.models;
​
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
​
@Entity
public class Vulnerabilities {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Integer vulnerableId;
	@NotBlank(message = "Name is mandatory")
	@Column(unique=true)
	private String vulnerableName;	
	@NotBlank(message = "Description is mandatory")
	private String vulnerableDesc;	
	@NotBlank(message = "Status is mandatory")
	private Boolean foundStatus;
	
	public Vulnerabilities() {
		super();
	}
​
	public Vulnerabilities(Integer vulnerableId, @NotBlank(message = "Name is mandatory") String vulnerableName,
			@NotBlank(message = "Description is mandatory") String vulnerableDesc,
			@NotBlank(message = "Status is mandatory") Boolean foundStatus) {
		super();
		this.vulnerableId = vulnerableId;
		this.vulnerableName = vulnerableName;
		this.vulnerableDesc = vulnerableDesc;
		this.foundStatus = foundStatus;
	}
​
	public int getVulnerableId() {
		return vulnerableId;
	}
​
	public void setVulnerableId(Integer vulnerableId) {
		this.vulnerableId = vulnerableId;
	}
​
	public String getVulnerableName() {
		return vulnerableName;
	}
​
	public void setVulnerableName(String vulnerableName) {
		this.vulnerableName = vulnerableName;
	}
​
	public String getVulnerableDesc() {
		return vulnerableDesc;
	}
​
	public void setVulnerableDesc(String vulnerableDesc) {
		this.vulnerableDesc = vulnerableDesc;
	}
​
	public Boolean getFoundStatus() {
		return foundStatus;
	}
​
	public void setFoundStatus(Boolean foundStatus) {
		this.foundStatus = foundStatus;
	}
​
	@Override
	public String toString() {
		return "Vulnerabilities [vulnerableId=" + vulnerableId + ", vulnerableName=" + vulnerableName
				+ ", vulnerableDesc=" + vulnerableDesc + ", foundStatus=" + foundStatus + "]";
	}
	
}
package com.fresco.t7challenge.repo;
​
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
​
import org.springframework.stereotype.Service;
​
import com.fresco.t7challenge.models.Users;
​
@Service
public class SqlService {
	public List<Users> getUser(String userId) {
		List<Users> users = new ArrayList<Users>();
		try (Connection sqlConnection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "")) {
			Statement st = sqlConnection.createStatement();
			ResultSet rs = st.executeQuery(
					"SELECT user_id, first_name, last_name, user, password, avatar FROM users WHERE user_id = "
							+ userId);
			while (rs.next()) {
				users.add(new Users(rs.getInt(1), rs.getString(1), rs.getString(1), rs.getString(1), rs.getString(1),
						rs.getString(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
​
		return users;
	}
}
package com.fresco.t7challenge.repo;
​
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
​
import com.fresco.t7challenge.models.Users;
​
@Repository
public interface UsersRepo extends JpaRepository<Users, Integer> {
}
​
package com.fresco.t7challenge.repo;
​
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
​
import com.fresco.t7challenge.models.Vulnerabilities;
​
@Repository
public interface VulnerabilitiesRepo extends JpaRepository<Vulnerabilities, Integer>{
}
​
package com.fresco.t7challenge;
​
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
​
@SpringBootApplication
public class T7ChallengeApplication {
​
	public static void main(String[] args) {
		SpringApplication.run(T7ChallengeApplication.class, args);
	}
​
}