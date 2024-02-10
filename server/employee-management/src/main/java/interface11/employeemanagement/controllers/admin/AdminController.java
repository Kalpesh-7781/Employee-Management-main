package interface11.employeemanagement.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import interface11.employeemanagement.helper.ResponseMessage;
import interface11.employeemanagement.models.Admin;
import interface11.employeemanagement.models.Employee;
import interface11.employeemanagement.services.admin.AdminService;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
public class AdminController {

    @Autowired
    public ResponseMessage responseMessage;

    @Autowired
    public AdminService adminService;
    
    @PostMapping(value = "/admin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCitizen(@RequestBody Admin admin) {
        try {
            return adminService.createAdmin(admin);
        } catch (Exception e) {
            e.printStackTrace();
            responseMessage.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
    }

    @GetMapping(value = "/admin/{email}/{password}")
    public ResponseEntity<?> loginAdmin(@PathVariable("email") String email, @PathVariable("password") String password) {
        try {
            return adminService.loginAdmin(email, password);
        } catch (Exception e) {
            e.printStackTrace();
            responseMessage.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
    }


    @PostMapping("/employee")
    public ResponseEntity<?> addEmployee(@RequestHeader("Authorization") String authorization, @RequestBody Employee employee) {
        //TODO: process POST request
        try {
            return adminService.addEmployee(authorization, employee);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            responseMessage.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
    }

    @GetMapping("/employee")
    public ResponseEntity<?> getEmployees(@RequestHeader("Authorization") String authorization) {
        //TODO: process POST request
        try {
            return adminService.getEmployees(authorization);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            responseMessage.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
    }
    
    @GetMapping("/employee/{email}")
    public ResponseEntity<?> getEmployeeByEmail(@RequestHeader("Authorization") String authorization, @PathVariable("email") String email) {
        //TODO: process POST request
        try {
            return adminService.getEmployeeByEmail(authorization, email);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            responseMessage.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
    }
    

    @PutMapping("/employee/{email}")
    public ResponseEntity<?> putMethodName(@RequestHeader("Authorization") String authorization, @PathVariable("email") String email, @RequestBody Employee employee) {
        try {
            return adminService.updateEmployee(authorization, email, employee);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            responseMessage.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
    }


    @DeleteMapping("/employee/{email}")
    public ResponseEntity<?> deleteEmployee(@RequestHeader("Authorization") String authorization, @PathVariable("email") String email) {
        try {
            return adminService.deleteEmployee(authorization, email);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            responseMessage.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
    }
}
