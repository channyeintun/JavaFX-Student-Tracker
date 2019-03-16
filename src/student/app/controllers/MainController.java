/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student.app.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import student.app.dao.StudentDAO;
import student.app.model.Student;

/**
 *
 * @author zappyzero
 */
public class MainController implements Initializable {
    @FXML
    private Menu aboutItem;
    @FXML
    private MenuBar menubar;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private RadioButton maleRadio;
    @FXML
    private ToggleGroup Gender;
    @FXML
    private RadioButton femaleRadio;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button saveButton;
    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, Integer> idCol;
    @FXML
    private TableColumn<Student, String> nameCol;
    @FXML
    private TableColumn<Student, String> emailCol;
    @FXML
    private TableColumn<Student, String> genderCol;
    @FXML
    private TableColumn<Student, Date> dobCol;
    @FXML
    private MenuItem editItem;
    @FXML
    private MenuItem deleteItem;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        maleRadio.setUserData("male");
        femaleRadio.setUserData("female");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        dobCol.setCellValueFactory(new PropertyValueFactory<>("dob"));
        try {
            load_Table_Data();
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    


    @FXML
    private void showAboutWindow(ActionEvent event) throws IOException {
       Parent layout=FXMLLoader.load(getClass().getResource("/student/app/views/about.fxml"));
       Stage stage=new Stage();
       Scene scne=new Scene(layout);
       stage.setScene(scne);
       stage.initModality(Modality.WINDOW_MODAL);
       stage.initOwner(menubar.getScene().getWindow());
       stage.showAndWait();
    }

    @FXML
    private void saveStudents(ActionEvent event) throws SQLException {
        
        String name=nameField.getText();
        String email=emailField.getText();
        String gender=Gender.getSelectedToggle().getUserData().toString();
        
        if(name.isEmpty() || email.isEmpty() || gender.isEmpty() || datePicker.getValue() == null){
           
              showAlert("Please fill all required fields",new Alert(Alert.AlertType.ERROR));
              return;
        }
         Date dob=Date.valueOf(datePicker.getValue());
        StudentDAO stdDAO=StudentDAO.getInstance();
        Student student=new Student(name,email,gender,dob);
        stdDAO.saveStudent(student);
        load_Table_Data();
        clearFields();
        showAlert("Save student successfully",new Alert(Alert.AlertType.INFORMATION));
        System.out.println("success");
    }
    private void clearFields(){
        nameField.clear();
        emailField.clear();
    }
    private void load_Table_Data() throws SQLException{
        StudentDAO stdDAO=StudentDAO.getInstance();
         List<Student> stdlist=stdDAO.getStudents();
        studentTable.getItems().setAll(stdlist);
        nameField.requestFocus();
    }

    @FXML
    private void showEditWindow(ActionEvent event) throws IOException, SQLException {
        Student selectedStudent=studentTable.getSelectionModel().getSelectedItem();
        if(selectedStudent!=null){
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/student/app/views/edit.fxml"));
        Parent root = loader.load();
        EditController editController=loader.getController();
        editController.setSelectedStudent(selectedStudent);
        Scene scene=new Scene(root);
        Stage editStage=new Stage();
        editStage.setScene(scene);
        editStage.initModality(Modality.WINDOW_MODAL);
        editStage.initOwner(studentTable.getScene().getWindow());
        editStage.setTitle("Edit");
        editStage.showAndWait();
        load_Table_Data();
        } else{
            showAlert("Please select a student",new Alert(Alert.AlertType.ERROR));
    }
    }

    @FXML
    private void deleteStudent(ActionEvent event) throws SQLException {
        Student selectedStudent=studentTable.getSelectionModel().getSelectedItem();
        if(selectedStudent!=null){
            int id=selectedStudent.getId();
            StudentDAO stdDAO=StudentDAO.getInstance();
            stdDAO.deleteStudentbyID(id);
            load_Table_Data();
            showAlert("Deleted student successfully", new Alert(Alert.AlertType.INFORMATION));
        }else{
            showAlert("Please select a student", new Alert(Alert.AlertType.ERROR));
        }
    }
    private void showAlert(String message,Alert alert){
               alert.setContentText(message);
               alert.setHeaderText(null);
               alert.show();
    }
    
}
