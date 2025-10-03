package com.student.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.student.dto.CourseDTO;
import com.student.dto.StudentResponse;
import com.student.entity.Student;
import com.student.service.StudentService;

import java.util.List;


@RestController
@RequestMapping("/students")
public class StudentController {
	@Autowired
	private StudentService studentService;
	@Autowired
    private RestTemplate restTemplate;

//    public StudentController(StudentService studentService) {
//        this.studentService = studentService;
//        this.restTemplate = new RestTemplate();
//    }

    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }



    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return studentService.updateStudent(id, student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
    
    @GetMapping("/{id}/with-courses")
    public ResponseEntity<StudentResponse> getStudentWithCourses(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);

        CourseDTO[] courses = restTemplate.getForObject(
                "http://localhost:8081/courses",
                CourseDTO[].class
        );

        StudentResponse response = StudentResponse.builder()
                .id(student.getId())
                .name(student.getName())
                .email(student.getEmail())
                .age(student.getAge())
                .courses(List.of(courses))
                .build();

        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<String> getStudentCourseInfo(@PathVariable Long id){
    	String courseName = studentService.getStudentCourseInfo(id);
    	return  ResponseEntity.ok(courseName);
    }
    

}

