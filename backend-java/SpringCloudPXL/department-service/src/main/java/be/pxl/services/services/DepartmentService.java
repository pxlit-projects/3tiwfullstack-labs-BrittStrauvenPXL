package be.pxl.services.services;

import be.pxl.services.domain.Department;
import be.pxl.services.domain.dto.DepartmentRequest;
import be.pxl.services.domain.dto.DepartmentResponse;
import be.pxl.services.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    public List<DepartmentResponse> findAll() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream().map(department -> mapToDepartmentResponse(department)).toList();
    }

    public void addDepartment(@RequestBody DepartmentRequest request) {
        Department department = Department.builder()
                .organizationId(request.getOrganizationId())
                .employees(request.getEmployees())
                .name(request.getName())
                .position(request.getPosition())
                .build();
        departmentRepository.save(department);
    }

    public DepartmentResponse findById(Long id) {
        Department department = departmentRepository.findById(id).orElse(null);
        return mapToDepartmentResponse(department);
    }

    private DepartmentResponse mapToDepartmentResponse(Department department) {
        return DepartmentResponse.builder()
                .organizationId(department.getOrganizationId())
                .name(department.getName())
                .employees(department.getEmployees())
                .position(department.getPosition())
                .build();
    }
}
