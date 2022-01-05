package com.poscoict.hrmaster.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.poscoict.hrmaster.domain.employee.Employee;
import com.poscoict.hrmaster.domain.employee.EmployeeRepository;
import com.poscoict.hrmaster.web.dto.HrAdminDto;
import com.poscoict.hrmaster.web.dto.HrFixedDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // @Autowired 없이 생성자로 주입하는 방식
@Service

public class HrAdminService {
	private final EmployeeRepository employeeRepository;

	// @지수
	// 어드민 아이디로 사원 조회
	public List<Employee> findbyIdForDetail(Long id) {
		Employee entity = employeeRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("해당 정보가 없습니다. id=" + id));

		List<Employee> employeeList = new ArrayList<>();
		employeeList.add(entity);

		return employeeList;
	}

	// @수현
	// 사원 전체 리스트 조회
	public List<Employee> findByAll() {
		List<Employee> employeeList = employeeRepository.findAll();
		return employeeList;
	}

	// @지수
	// 어드민 사원 추가
	public Long saveByAdmin(HrAdminDto hrAdminDto) {
		return employeeRepository.save(hrAdminDto.toEntity()).getId();

	}

}
