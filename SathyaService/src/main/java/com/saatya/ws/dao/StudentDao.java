package com.saatya.ws.dao;

import java.util.List;
import java.util.Map;

import com.saatya.ws.model.Student;



public interface StudentDao extends SaatyaDao {
	
	public List<Student> findAll();

	public List<Student> findByName();

	public boolean insertStudent(String dsConfigName, Map<String, String> params);

	public boolean updateStudent(Student user);
	
	public boolean editStudentProfile(String dsConfigName, Map<String, String> params);

	public boolean deleteStudentr(String userId);
	
	public Student findStudentByStudentId(String userId);
	
	

}
