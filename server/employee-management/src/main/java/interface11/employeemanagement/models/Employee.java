package interface11.employeemanagement.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Entity
@Component
@Table(name = "Employees")
public class Employee {
    @Id
    @NonNull
    @Column(name = "email")
    private String email;

    @Column(name = "firstName")
    @NonNull
    private String firstName;

    @NonNull
    @Column(name = "lastName")
    private String lastName;

    public Employee() {

    }
    
    public Employee(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    @Override
    public String toString() {
        return "Employee [email=" + email + ", firstName=" + firstName + ", lastName=" + lastName  + "]";
    }

}
