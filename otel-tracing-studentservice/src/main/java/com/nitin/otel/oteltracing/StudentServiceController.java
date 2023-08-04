package com.nitin.otel.oteltracing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import io.opentelemetry.api.trace.Span;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

 
@RestController
public class StudentServiceController {
 
  private static Map<String, List<Student>> schooDB = new HashMap<String, List<Student>>();
 
  static {
    schooDB = new HashMap<String, List<Student>>();
 
    List<Student> lst = new ArrayList<Student>();
    Student std = new Student("Sajal", "Class IV");
    lst.add(std);
    std = new Student("Lokesh", "Class V");
    lst.add(std);
 
    schooDB.put("abcschool", lst);
 
    lst = new ArrayList<Student>();
    std = new Student("Kajal", "Class III");
    lst.add(std);
    std = new Student("Madhumitha", "Class VI");
    lst.add(std);
 
    schooDB.put("xyzschool", lst);
 
  }
 
  @RequestMapping(value = "/getStudentDetailsForSchool/{schoolname}", method = RequestMethod.GET)
  public List<Student> getStudents(@PathVariable String schoolname) {
    Span span = Span.current();
    span.setAttribute("schoolname",schoolname);
    //System.out.println("Getting Student details for " + schoolname);
    LoggerFactory.getLogger(StudentServiceController.class).info("Getting Student details for" + schoolname);
 
    List<Student> studentList = schooDB.get(schoolname);
    if (studentList == null) {
      studentList = new ArrayList<Student>();
      Student std = new Student("Not Found", "N/A");
      studentList.add(std);
    }
    return studentList;
  }
  @RequestMapping(value = "/health", method = RequestMethod.GET)
  public String getHealth()   {
    return "School Service is Healthy";
  }
}