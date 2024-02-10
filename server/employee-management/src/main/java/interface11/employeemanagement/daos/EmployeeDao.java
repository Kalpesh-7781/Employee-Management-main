package interface11.employeemanagement.daos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import interface11.employeemanagement.models.Employee;


@Repository
@Component
public interface EmployeeDao extends CrudRepository<Employee, String> {
    
    public Employee getEmployeeByemail(String email);
}
