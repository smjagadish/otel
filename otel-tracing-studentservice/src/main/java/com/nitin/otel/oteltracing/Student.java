package com.nitin.otel.oteltracing;

/**
 * Student entity representing a student with name and class information.
 */
public class Student 
{
  private String name;
  private String className;
   
  /**
   * Constructor to create a Student instance.
   * @param name the student's name
   * @param className the student's class
   */
  public Student(String name, String className) {
    super();
    this.name = name;
    this.className = className;
  }
 
  /**
   * Gets the student's name.
   * @return the student's name
   */
  public String getName() {
    return name;
  }
 
  /**
   * Sets the student's name.
   * @param name the student's name
   */
  public void setName(String name) {
    this.name = name;
  }
 
  /**
   * Gets the student's class.
   * @return the student's class
   */
  public String getClassName() {
    return className;
  }
 
  /**
   * Sets the student's class.
   * @param className the student's class
   */
  public void setClassName(String className) {
    this.className = className;
  }
}