package interface11.employeemanagement.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import interface11.employeemanagement.daos.AdminDao;
import interface11.employeemanagement.daos.EmployeeDao;
import interface11.employeemanagement.helper.JwtUtil;
import interface11.employeemanagement.models.Admin;
import interface11.employeemanagement.models.Employee;

//CustomUserDetailsService has to override loadByUsername function provided by spring security...

@Component
public class CustomUserDetailsService implements UserDetailsService{


    @Autowired
    public EmployeeDao employeeDao;

    @Autowired
    public Employee employee;

    @Autowired
    public JwtUtil jwtUiUtil;

    @Autowired
    public AdminDao adminDao;

    @Autowired
    public Admin admin;

    public Employee findByUsername(String email){
        return employeeDao.getEmployeeByemail(email);
    }

    public Admin findByAdminname(String email){
        return adminDao.getAdminByemail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String useremail) throws UsernameNotFoundException {
       //username means email 

        admin = findByAdminname(useremail);
        if(admin != null){

            return new User(admin.getEmail(), admin.getPassword(), new ArrayList<>()); 
        }
        else{
            throw new UsernameNotFoundException("User not found");
        }
    }

    public ResponseEntity<?> getUserByToken(String token){
        try {
            String email = jwtUiUtil.extractUsername(token);
            UserDetails uDetails = loadUserByUsername(email);  //username == email id
            return ResponseEntity.ok(uDetails); 
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }


}