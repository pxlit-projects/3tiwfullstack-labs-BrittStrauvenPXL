package be.pxl.services.services;

import be.pxl.services.client.NotificationClient;
import be.pxl.services.domain.Employee;
import be.pxl.services.domain.dto.EmployeeRequest;
import be.pxl.services.domain.dto.EmployeeResponse;
import be.pxl.services.model.request.NotificationRequest;
import be.pxl.services.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService{

    private final EmployeeRepository employeeRepository;

    private final NotificationClient notificationClient;
    @Override
    public List<EmployeeResponse> getAllEmployees() {
        List<Employee> employees =  employeeRepository.findAll();
        return employees.stream().map(employee -> mapToEmployeeResponse(employee)).toList();
    }

    private EmployeeResponse mapToEmployeeResponse(Employee employee) {
        return EmployeeResponse.builder()
                .age(employee.getAge())
                .name(employee.getName())
                .position(employee.getPosition())
                .build();
    }

    @Override
    public void addEmployee(EmployeeRequest employeeRequest) {
        Employee employee = Employee.builder()
                .age(employeeRequest.getAge())
                .name(employeeRequest.getName())
                .position(employeeRequest.getPosition())
                .build();
        employeeRepository.save(employee);
        notificationClient.sendNotification(NotificationRequest.builder()
                        .message("Employee added")
                        .to("admin")
                        .from("employee-service")
                        .build());

    }

    public EmployeeResponse findById(Long id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        assert employee != null;
        return mapToEmployeeResponse(employee);
    }
}
