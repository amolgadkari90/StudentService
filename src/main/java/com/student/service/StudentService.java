package com.student.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.course.entity.Course;
import com.student.dto.CourseClient;
import com.student.dto.CourseDTO;
import com.student.entity.Student;
import com.student.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {

	
    private final StudentRepository repository;
    //Inter service communication
    @Autowired
    private RestTemplate restTemplate;

    private final String COURSE_SERVICE_URL = "http://localhost:8081/courses";
    //

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student addStudent(Student student) {
        return repository.save(student);
    }

    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    public Student getStudentById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Student updateStudent(Long id, Student student) {
        return repository.findById(id).map(s -> {
            s.setName(student.getName());
            s.setEmail(student.getEmail());
            s.setAge(student.getAge());
            return repository.save(s);
        }).orElse(null);
    }

    public void deleteStudent(Long id) {
        repository.deleteById(id);
    }
    
   
    public Student getStudentWithCourses(Long id) {
        Student student = getStudentById(id);

        CourseDTO[] courses = restTemplate.getForObject(
                COURSE_SERVICE_URL,
                CourseDTO[].class
        );

        // Just return student with courses (wrap in response DTO)
        return student;
    }
    
    @Autowired
    private CourseClient courseClient;

    public  String getStudentCourseInfo(Long studentId) {
        // Imagine we fetched student details from DB
        CourseDTO course = courseClient.getCourseById(1L);
        return "Student is enrolled in: " + course.getCourseName();
    }
    
    
}
