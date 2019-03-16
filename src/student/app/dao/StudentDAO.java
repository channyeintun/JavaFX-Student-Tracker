/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import student.app.database.Database;
import student.app.model.Student;

/**
 *
 * @author zappyzero
 */
public class StudentDAO {
    private static StudentDAO stdDAO;
    private StudentDAO(){};
    public static StudentDAO getInstance(){
        if(stdDAO==null){
            stdDAO=new StudentDAO();
            return stdDAO;
        }
        return stdDAO; 
    }
    public List<Student> getStudents() throws SQLException{
     Connection con=Database.getInstance().connect();
     String url="select * from students";
     List<Student> stdlist=new ArrayList<>();
     Statement stm=con.createStatement();
     ResultSet rs=stm.executeQuery(url);
     while(rs.next()){
         int id=rs.getInt("id");
         String name=rs.getString("name");
         String email=rs.getString("email");
         String gender=rs.getString("gender");
         Date dob=rs.getDate("dob");
         stdlist.add(new Student(id,name,email,gender,dob));
     }
     return stdlist;
    }
    public int saveStudent(Student student) throws SQLException{
        String name=student.getName();
        String email=student.getEmail();
        String gender=student.getGender();
        Date dob=student.getDob();
        Connection con=Database.getInstance().connect();
        PreparedStatement stm=con.prepareStatement("insert into students(name,email,gender,dob) values(?,?,?,?)");
        stm.setString(1, name);
        stm.setString(2, email);
        stm.setString(3,gender);
        stm.setDate(4,  dob);
       return stm.executeUpdate();
    }
    public int deleteStudentbyID(int id) throws SQLException{
        Connection con=Database.getInstance().connect();
        PreparedStatement stm=con.prepareStatement("delete from students where id=?");
        stm.setInt(1, id);
        return stm.executeUpdate();
    }
    public Student getStudentByID(int id) throws SQLException{
        Connection con=Database.getInstance().connect();
        PreparedStatement stm=con.prepareStatement("select * from students where id=?");
        stm.setInt(1, id);
        ResultSet rs=stm.executeQuery();
        Student std=null;
        if(rs.next()){
            std.setId(rs.getInt("id"));
            std.setName(rs.getString("name"));
            std.setEmail(rs.getString("email"));
            std.setGender(rs.getString("gender"));
            std.setDob(rs.getDate("dob"));
        }
        return std;
    }
    public int updateStudent(Student std) throws SQLException{
        Connection con=Database.getInstance().connect();
        PreparedStatement stm=con.prepareStatement("update students set name=?,email=?,gender=?,dob=? where id=?");
        stm.setString(1, std.getName());
        stm.setString(2, std.getEmail());
        stm.setString(3, std.getGender());
        stm.setDate(4, std.getDob());
        stm.setInt(5, std.getId());
        return  stm.executeUpdate();
    }
}
