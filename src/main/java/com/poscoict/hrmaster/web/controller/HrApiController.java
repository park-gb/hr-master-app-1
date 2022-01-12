package com.poscoict.hrmaster.web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.poscoict.hrmaster.domain.department.Department;
import com.poscoict.hrmaster.domain.employee.Employee;
import com.poscoict.hrmaster.domain.jobcategory.JobCategory;
import com.poscoict.hrmaster.domain.stafflevel.StaffLevel;
import com.poscoict.hrmaster.domain.workplace.WorkPlace;
import com.poscoict.hrmaster.service.HrAdminService;
import com.poscoict.hrmaster.service.HrBasicService;
import com.poscoict.hrmaster.service.HrFixedService;
import com.poscoict.hrmaster.web.dto.HrAdminDto;
import com.poscoict.hrmaster.web.dto.HrBasicDto;
import com.poscoict.hrmaster.web.dto.HrFileDto;
import com.poscoict.hrmaster.web.dto.HrFixedDto;
import com.poscoict.hrmaster.web.dto.HrStaffLevelDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class HrApiController {

	private final HrFixedService hrFixedService;
	private final HrBasicService hrBasicService;
	private final HrAdminService hrAdminService;

	// @지수
	// put method for master
	@PutMapping("/hrfixed/admin/{id}")
	public Long updateByIdForAdmin(@PathVariable Long id, @RequestBody HrFixedDto hrFixedDto) {

		System.out.println("controller: " + hrFixedDto.getKorName());
		return hrFixedService.updateByIdForAdmin(id, hrFixedDto);
	}

	// @지수
	// put method for employee
	@PutMapping("/hrfixed/employee/{id}")
	public Long updateByIdForEmployee(@PathVariable Long id, @RequestBody HrFixedDto hrFixedDto) {

		System.out.println("controller: " + hrFixedDto.getKorName());
		return hrFixedService.updateByIdForEmployee(id, hrFixedDto);
	}

	// @윤욱
	// delete method for employee
	@DeleteMapping("/hrfixed/{id}")
	public Long delete(@PathVariable Long id) {
		hrFixedService.delete(id);
		return id;
	}

	// @경빈
	// 메서드 이름 변경: findById -> hrFixedById
	@GetMapping("/hrfixed/{id}")
	public HrFixedDto hrFixedfindById(@PathVariable Long id) {
		return hrFixedService.findById(id);
	}

	// @경빈
	// 인사기본 조회 추가
	@GetMapping("/hrbasic/{id}")
	public HrBasicDto hrBasicfindById(@PathVariable Long id) {
		return hrBasicService.findById(id);
	}

	// @수현
	// put method for employee(basic)
	@PutMapping("/hrbasic/employee/{id}")
	public Long updateByIdForBasicEmployee(@PathVariable Long id, @RequestBody HrBasicDto hrBasicDto) {
		return hrBasicService.updateByIdForEmployee(id, hrBasicDto);

	}

	// @수현
	// 회원 전체 리스트 조회
	@GetMapping("/hradmin/admin/list")
	public List<Employee> hrBasicfindAll() {
		return hrAdminService.findByAll();
	}

	// @지수
	// 어드민 사원디테일 조회
	@GetMapping("/hradmin/admin/list/{id}")
	public List<Employee> hrAdminfindDetail(@PathVariable Long id) {
		return hrAdminService.findbyIdForDetail(id);
	}

	// @지수
	// 어드민 사원디테일 추가
	@PostMapping("/hradmin/admin")
	public Long save(@RequestBody HrAdminDto hrAdminDto) {
		System.out.println("controller");
		return hrAdminService.saveByAdmin(hrAdminDto);
	}

	// @지수
	// 사진 업로드
	@CrossOrigin("*")
	@PostMapping("/hradmin/image/{id}")
	public void saveImageToServer(HrFileDto hrFileDto, MultipartFile img, @PathVariable Long id) {
		System.out.println("파일 이름 : " + img.getOriginalFilename());
		System.out.println("파일 타입 : " + img.getContentType());
		System.out.println("파일 크기 : " + img.getSize());

		hrAdminService.saveImageToServer(hrFileDto, img, id);
	}

	// @지수
	// 사진 로컬에 가져오기
	@GetMapping("/hradmin/regist/image/{id}")
	public Files getImageTolocal(@PathVariable Long id) {
			hrAdminService.findFileById(id);
		return null;
	}

	// @지수
	// 이미지 화면에 가져오기
	@CrossOrigin("*")
	@GetMapping("/hradmin/image/{id}")
	public ResponseEntity<byte[]> getImage(@PathVariable Long id) {

		HrFileDto fileList = hrAdminService.findFileById(id);

		String filePath = fileList.getPath();
		String name = fileList.getName();
		File file = new File("C:\\upload\\" + filePath + "\\" + name);

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

//	// @지수
//	// 이미지 화면에 가져오기
//	@CrossOrigin("*")
//	@GetMapping("/hradmin/image/{id}")
//	public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
//		HrFixedDto employeeList = hrFixedService.findById(id);
//		HrFileDto fileList = hrAdminService.findFileById(id);
//
//		if (employeeList.getId() == fileList.getUserId()) {
//
//			String filePath = fileList.getPath();
//			String name = fileList.getName();
//			File file = new File("C:\\upload\\" + filePath + "\\" + name);
//
//			ResponseEntity<byte[]> result = null;
//
//			try {
//
//				HttpHeaders header = new HttpHeaders();
//				header.add("Content-type", Files.probeContentType(file.toPath()));
//				result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			return result;
//
//		}
//		return null;
//		
//	}

	// @지수
	// staff_level 테이블 가져오기
	@GetMapping("/hradmin/admin/stafflevel")
	public List<StaffLevel> hrAdminfindStaffLevel() {

		return hrAdminService.findByAllStafflevel();
	}

	// @지수
	// department 테이블 가져오기
	@GetMapping("/hradmin/admin/department")
	public List<Department> hrAdminfindDepartment() {

		return hrAdminService.findByAllDepartment();
	}

	// @지수
	// workplace 테이블 가져오기
	@GetMapping("/hradmin/admin/workPlace")
	public List<WorkPlace> hrAdminfindWorkPlace() {

		return hrAdminService.findByAllWorkPlace();
	}

	// @지수
	// jobCategory 테이블 가져오기
	@GetMapping("/hradmin/admin/jobCategory")
	public List<JobCategory> hrAdminfindJobCategory() {

		return hrAdminService.findByAllJobCategory();
	}

	// @지수
	// role이 팀장인 id 가져오기
	@GetMapping("/hradmin/admin/boss")
	public List<Employee> hrAdminfindBoss() {

		return hrAdminService.findBoss();
	}

}