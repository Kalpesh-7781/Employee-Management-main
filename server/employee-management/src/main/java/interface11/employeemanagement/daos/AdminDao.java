package interface11.employeemanagement.daos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import interface11.employeemanagement.models.Admin;


@Repository
@Component
public interface AdminDao extends CrudRepository<Admin, String> {
    
    public Admin getAdminByemail(String email);
}
