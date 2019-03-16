/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student.app.controllers;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import student.app.dao.StudentDAO;
import student.app.model.Student;

/**
 * FXML Controller class
 *
 * @author zappyzero
 */
public class EditController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private RadioButton maleRadio;
    @FXML
    private ToggleGroup genderToggle;
    @FXML
    private RadioButton femaleRadio;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button submitButton;   
    private Student student;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        maleRadio.setUserData("male");
        femaleRadio.setUserData("female");
    }    

    @FXML
    private void updateStudent(ActionEvent event) throws SQLException {
         String name=nameField.getText();
        String email=emailField.getText();
        String gender=genderToggle.getSelectedToggle().getUserData().toString();
        
        if(name.isEmpty() || email.isEmpty() || gender.isEmpty() || datePicker.getValue() == null){
           
              showAlert("Please fill all required fields",new Alert(Alert.AlertType.ERROR));
              return;
        }
         Date dob=Date.valueOf(datePicker.getValue());
         student.setName(name);
         student.setEmail(email);
         student.setGender(gender);
         student.setDob(dob);
        StudentDAO stdDAO=StudentDAO.getInstance();
        stdDAO.updateStudent(student);
        showAlert("Updated student successfully", new Alert(Alert.AlertType.INFORMATION));
        Stage stage=(Stage) submitButton.getScene().getWindow();
        stage.close();
    }
    public void setSelectedStudent(Student selectedStudent){
       student=selectedStudent;
      nameField.setText(student.getName());
      emailField.setText(student.getEmail());
      String gender=student.getGender();
      if(gender.equals("male")){
          maleRadio.setSelected(true);
      }else{
          femaleRadio.setSelected(true);
      }
      datePicker.setValue(student.getDob().toLocalDate());
        System.out.println(student);
    }
     private void showAlert(String message,Alert alert){
               alert.setContentText(message);
               alert.setHeaderText(null);
               alert.showAndWait();
    }
    
}
