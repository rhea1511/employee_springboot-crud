package net.springboot.controller;
import net.springboot.exception.ResourceNotFoundException;
import net.springboot.model.Employee;
import net.springboot.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;

    //get employees
    @GetMapping("employees")
    public List<Employee> getAllEmployee(){
        return this.employeeRepository.findAll();
    }
    //get employee by id
    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById (@PathVariable(value="id")Long employeeId)
        throws ResourceNotFoundException{
        Employee employee=employeeRepository.findById(employeeId)
                .orElseThrow(()->new ResourceNotFoundException("Employee not found by this id: "+employeeId));
        return ResponseEntity.ok().body(employee);
    }

    public static boolean isValidName(String name)
    {
        if(!name.matches("[a-z A-Z]+\\S"))
            return false;
        else
            return true;
    }
    //save employee
    @PostMapping("employees")
    public Employee createEmployee(@RequestBody Employee employee) throws Exception {
        if(!isValidName(employee.getFirstName())|| !isValidName(employee.getLastName())) {
            throw new Exception();
        }

        return this.employeeRepository.save(employee);
    }
    //update employee
    @PutMapping("employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(value="id")Long employeeId,
                                                    @RequestBody Employee employeeDetails) throws ResourceNotFoundException {
        Employee employee=employeeRepository.findById(employeeId)
                .orElseThrow(()->new ResourceNotFoundException("Employee not found by this id: "+employeeId));
        employee.setEmail(employeeDetails.getEmail());
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        return ResponseEntity.ok(this.employeeRepository.save(employee));
    }
    //delete employee
    @DeleteMapping("employees/{id}")
    public Map<String,Boolean> deleteEmployee(@PathVariable(value="id")Long employeeId) throws ResourceNotFoundException {
        Employee employee=employeeRepository.findById(employeeId)
                .orElseThrow(()->new ResourceNotFoundException("Employee not found for this id: "+employeeId));
        this.employeeRepository.delete(employee);
        Map<String,Boolean> response =new HashMap<>();
        response.put("deleted",Boolean.TRUE);
        return response;
    }
}
