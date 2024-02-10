package interface11.employeemanagement.services.admin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import interface11.employeemanagement.config.MySecurityConfig;
import interface11.employeemanagement.daos.AdminDao;
import interface11.employeemanagement.daos.EmployeeDao;
import interface11.employeemanagement.helper.JwtUtil;
import interface11.employeemanagement.helper.ResponseMessage;
import interface11.employeemanagement.models.Admin;
import interface11.employeemanagement.models.Employee;
import interface11.employeemanagement.services.CustomUserDetailsService;

@Component
public class AdminService {

    @Autowired
    public Admin admin;

    @Autowired
    public AdminDao adminDao;

    @Autowired
    public EmployeeDao employeeDao;

    @Autowired
    public ResponseMessage responseMessage;

    @Autowired
    public MySecurityConfig mySecurityConfig;

    @Autowired
    public AuthenticationManager authenticationManager;

    @Autowired
    public JwtUtil jwtUtil;

    @Autowired
    public CustomUserDetailsService customUserDetailsService;

    Log log = LogFactory.getLog(AdminService.class);

    public ResponseEntity<?> createAdmin(Admin admin) {
        try {
            if (admin.getPassword() == null) {
                // Handle the case where the password is null
                throw new IllegalArgumentException("Password cannot be null");
            }
            //Step 1: Admin should not exist either in citizen or admin DB.
            String email = admin.getEmail();
            if (employeeDao.getEmployeeByemail(email)!=null || adminDao.getAdminByemail(email)!=null) {
                responseMessage.setMessage("Email already exists.....");
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(responseMessage);
            }

            log.info("Admin:"+admin.toString());
            
            String password = admin.getPassword();
            admin.setPassword(mySecurityConfig.passwordEncoder().encode(admin.getPassword()));

            //Step 2: Save the citizen in db.
            adminDao.save(admin);

            //Step 3: Token generate krna for the citizen
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(admin.getEmail()); //username == email
            
            String token = jwtUtil.generateToken(userDetails);

            //Step 4: Return the token in message.
            responseMessage.setMessage(token);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            responseMessage.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
    }

    public ResponseEntity<?> loginAdmin(String email, String password) {
        try {
            Admin admin = adminDao.getAdminByemail(email);
            if (admin==null) {
                responseMessage.setMessage("Email id does not exist....");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
            }
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            if (bCryptPasswordEncoder.matches(password, admin.getPassword())) {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(admin.getEmail());
                String token = jwtUtil.generateToken(userDetails);
                responseMessage.setMessage(token);
                return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
            }

            responseMessage.setMessage("Bad credentials.");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(responseMessage);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            responseMessage.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
    }

    public ResponseEntity<?> addEmployee(String authorization, Employee employee) {
        try {
            String token = authorization.substring(7);
            String email = jwtUtil.extractUsername(token);

            if (adminDao.getAdminByemail(email)==null) {
                responseMessage.setMessage("You are not authorized to add an employee.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMessage);
            }

            if (employeeDao.getEmployeeByemail(employee.getEmail())!=null || adminDao.getAdminByemail(employee.getEmail())!=null) {
                responseMessage.setMessage("Email already exists.....");
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(responseMessage);
            }
            
            employeeDao.save(employee);

            responseMessage.setMessage("Employee saved successfully.....");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            responseMessage.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
    }

    public ResponseEntity<?> getEmployees(String authorization) {
        try {
            String token = authorization.substring(7);
            String email = jwtUtil.extractUsername(token);

            if (adminDao.getAdminByemail(email)==null) {
                responseMessage.setMessage("You are not authorized to add an employee.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMessage);
            }

        
            
            return ResponseEntity.status(HttpStatus.OK).body(employeeDao.findAll());
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            responseMessage.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
    }

    public ResponseEntity<?> getEmployeeByEmail(String authorization, String email) {
        try {
            String token = authorization.substring(7);
            String admin_email = jwtUtil.extractUsername(token);

            if (adminDao.getAdminByemail(admin_email)==null) {
                responseMessage.setMessage("You are not authorized to add an employee.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMessage);
            }
            
            if (employeeDao.getEmployeeByemail(email)==null) {
                responseMessage.setMessage("Employee Not found....");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
            }
            
            return ResponseEntity.status(HttpStatus.OK).body(employeeDao.findById(email));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            responseMessage.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
    }

    public ResponseEntity<?> updateEmployee(String authorization, String email, Employee employee) {
        try {
            String token = authorization.substring(7);
            String admin_email = jwtUtil.extractUsername(token);

            if (adminDao.getAdminByemail(admin_email)==null) {
                responseMessage.setMessage("You are not authorized to update an employee.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMessage);
            }
            
            if (employeeDao.getEmployeeByemail(email)==null) {
                responseMessage.setMessage("Employee Not found....");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
            }

            employee.setEmail(email);

            employeeDao.save(employee);

            responseMessage.setMessage("Employee Updated successfully.....");
            
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            responseMessage.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
    }

    public ResponseEntity<?> deleteEmployee(String authorization, String email) {
        try {
            String token = authorization.substring(7);
            String admin_email = jwtUtil.extractUsername(token);

            if (adminDao.getAdminByemail(admin_email)==null) {
                responseMessage.setMessage("You are not authorized to update an employee.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMessage);
            }
            
            if (employeeDao.getEmployeeByemail(email)==null) {
                responseMessage.setMessage("Employee Not found....");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
            }

            employeeDao.delete(employeeDao.getEmployeeByemail(email));

            responseMessage.setMessage("Employee Deleted successfully.....");
            
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            responseMessage.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
    }

}
