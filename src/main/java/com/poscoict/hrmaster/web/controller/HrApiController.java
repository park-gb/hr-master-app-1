package com.poscoict.hrmaster.web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.poscoict.hrmaster.domain.employee.Employee;
import com.poscoict.hrmaster.domain.stafflevel.StaffLevel;
import com.poscoict.hrmaster.service.HrAdminService;
import com.poscoict.hrmaster.service.HrBasicService;
import com.poscoict.hrmaster.service.HrFixedService;
import com.poscoict.hrmaster.web.dto.HrAdminDto;
import com.poscoict.hrmaster.web.dto.HrBasicDto;
import com.poscoict.hrmaster.web.dto.HrFileDto;
import com.poscoict.hrmaster.web.dto.HrFixedDto;
import com.poscoict.hrmaster.web.dto.HrStaffLevelDto;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@RestController
public class HrApiController {

	private final HrFixedService hrFixedService;
	private final HrBasicService hrBasicService;
	private final HrAdminService hrAdminService;
	
	// @지수
	// put method for employee
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@PutMapping("/hrfixed/{id}")
	public Long updateByIdForEmployee(@PathVariable Long id, @RequestBody HrFixedDto hrFixedDto) {

		System.out.println("controller: " + hrFixedDto.getKorName());
		return hrFixedService.updateByIdForEmployee(id, hrFixedDto);
	}

	// @윤욱
	// delete method for employee
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public Long delete(@PathVariable Long id) {
		hrFixedService.delete(id);
		return id;
	}

	// @경빈
	// 메서드 이름 변경: findById -> hrFixedById
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@GetMapping("/hrfixed/{id}")
	public HrFixedDto hrFixedfindById(@PathVariable Long id) {
		return hrFixedService.findById(id);			
	}

	// @경빈
	// 인사기본 조회 추가
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@GetMapping("/hrbasic/{id}")
	public HrBasicDto hrBasicfindById(@PathVariable Long id) {
		return hrBasicService.findById(id);
	}

	// @수현
	// put method for employee(basic)
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@PutMapping("/hrbasic/{id}")
	public Long updateByIdForBasicEmployee(@PathVariable Long id, @RequestBody HrBasicDto hrBasicDto) {
		return hrBasicService.updateByIdForEmployee(id, hrBasicDto);
	}

	// @수현
	// 회원 전체 리스트 조회
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/hradmin/list")
	public List<Employee> hrBasicfindAll() {
		return hrAdminService.findByAll();
	}

	// @지수
	// 어드민 사원디테일 조회
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/hradmin/{id}")
	public List<Employee> hrAdminfindDetail(@PathVariable Long id) {
		return hrAdminService.findbyIdForDetail(id);
	}

	// @지수
	// 어드민 사원디테일 추가
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/hradmin/")
	public Long save(@RequestBody HrAdminDto hrAdminDto) {
		return hrAdminService.saveByAdmin(hrAdminDto);
	}

	// @지수
	// 사진 업로드
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@PostMapping("/hradmin/image" )
	public void saveImageToServer(HrFileDto hrFileDto, MultipartFile img) {
		System.out.println("파일 이름 : " + img.getOriginalFilename());
		System.out.println("파일 타입 : " + img.getContentType());
		System.out.println("파일 크기 : " + img.getSize());
		
		hrAdminService.saveImageToServer(hrFileDto, img);
	}

	// @지수
	// 사진 가져오기
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@GetMapping("/hradmin/image/{id}")
	public ResponseEntity<byte[]> getImage(@PathVariable Long id){
		Employee employeeList = hrAdminService.getImageToWeb(id);
		String filePath = employeeList.getFilesId().getPath();
		String name = employeeList.getFilesId().getName();
		
		File file = new File("C:\\upload\\"+filePath+"\\"+name);
		ResponseEntity<byte[]> result = null;
		
		try {

			HttpHeaders header = new HttpHeaders();
			header.add("Content-type", Files.probeContentType(file.toPath()));
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	//@지수
	//staff_level 테이블 가져오기
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/hradmin/admin/stafflevel")
	public List<StaffLevel> hrAdminfindStaffLevel() {
		
		return hrAdminService.findByAllStafflevel();
	}

}
