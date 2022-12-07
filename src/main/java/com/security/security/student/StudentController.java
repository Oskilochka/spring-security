package com.security.security.student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {

    private static final List<Student> students = Arrays.asList(
            new Student(1, "Julia"),
            new Student(2, "Andrew"),
            new Student(3, "Valentin")
    );

    @GetMapping(path = "{studentId}")
    public Student getStudent(@PathVariable("studentId") Integer studentId) {
        return students.stream()
                .filter(student -> studentId.equals(student.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Student with id " + studentId + " doesn't exist"));
    }
}
