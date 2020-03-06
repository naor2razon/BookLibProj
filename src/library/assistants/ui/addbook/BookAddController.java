/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistants.ui.addbook;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.assistants.database.DataBaseHandler;

/**
 * FXML Controller class
 *
 * @author Ohayon
 */
public class BookAddController implements Initializable {

    @FXML
    private JFXTextField title;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField author;
    @FXML
    private JFXTextField publisher;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private AnchorPane rootPane;

    DataBaseHandler dataBaseHandler;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dataBaseHandler = DataBaseHandler.getInstance();
        checkData();
    }    

    
    @FXML
    private void addBook(ActionEvent event) {
        String bookID = id.getText();
        String bookTitle = title.getText();
        String bookAuthor = author.getText();
        String bookPublisher = publisher.getText();
        
        if(bookID.isEmpty() || bookAuthor.isEmpty() || bookPublisher.isEmpty() || bookTitle.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please enter in all filed");
            alert.showAndWait();
            return;
        }
        //the order of the qu is importent!
        String quStatment ="INSERT INTO BOOK(id,title,author,publisher,isAvail) VALUES("+
        "'" + bookID + "',"+
        "'" + bookTitle + "',"+
        "'" + bookAuthor + "',"+
        "'" + bookPublisher + "',"+
        "'" + true + "'"+
        ")";        
        System.out.println(quStatment);
        
        if(dataBaseHandler.execAction(quStatment)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("success");
            alert.showAndWait();
            clearAddBookFieldsAfterAdd();
            return; 
        }else { //error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("FAILD");
            alert.showAndWait();
            clearAddBookFieldsAfterAdd();
            return;
        }
    }

    //TASK 2
    //fix the cancel button
    @FXML
    private void cancel(ActionEvent event) {
      /*  Stage stage = (Stage) rootPane.getScene().getWindow();
          stage.close();
       */
         // ((Stage)rootPane.getScene().getWindow()).close();
            Platform.exit();

    }

    private void checkData() {
        //or SELECT title FROM BOOK
        String query = "SELECT * FROM BOOK ";
        ResultSet resultSet = dataBaseHandler.execQuery(query);
        try {
            while(resultSet.next()){
                String checkTitle = resultSet.getString("title");
                System.out.println(checkTitle);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BookAddController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void clearAddBookFieldsAfterAdd(){
        this.id.setText(null);
        this.title.setText(null);
        this.publisher.setText(null);
        this.author.setText(null);
    }

}
//*********************************TASKS******************************//
/*
TASK 1: DELETE ALL FILEDES AFTER ADDING A BOOK
*/

//TASK 2: EXIT ONLY THE DIALOG AND NOT ALL PLATFORM