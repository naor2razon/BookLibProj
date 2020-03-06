/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistants.ui.main;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistants.database.DataBaseHandler;

/**
 * FXML Controller class
 *
 * @author Ohayon
 */
public class MainController implements Initializable {

    @FXML
    private HBox book_info;
    @FXML
    private HBox member_info;
    @FXML
    private TextField bookIdInput;
    @FXML
    private Text bookName;
    @FXML
    private Text bookAuthor;
    @FXML
    private Text bookStatus;
    
    DataBaseHandler dataBaseHandler;
    @FXML
    private TextField memberIdInput;
    @FXML
    private Text memberName;
    @FXML
    private Text memberMobile;
    @FXML
    private JFXTextField bookID;
    @FXML
    private ListView<String> issueDataList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         JFXDepthManager.setDepth(book_info, 1);
         JFXDepthManager.setDepth(member_info, 1);
         dataBaseHandler = DataBaseHandler.getInstance(); 
    }    

    @FXML
    private void loadAddMember(ActionEvent event) {
        loadWindow("/library/assistants/ui/addmember/member_add.fxml", "Add New Member");
    }

    @FXML
    private void loadAddBook(ActionEvent event) {
        loadWindow("/library/assistants/ui/addbook/FXMLDocument.fxml", "Add New Book");
    }

    @FXML
    private void loadMemberTable(ActionEvent event) {
        loadWindow("/library/assistants/ui/listmember/member_list.fxml", "Member List");

    }

    @FXML
    private void loadBookTable(ActionEvent event) {
        loadWindow("/library/assistants/ui/listBook/Book_list.fxml", "Book List");

    }
    
    void loadWindow(String loc,String title){
        
        try {
        
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void loadBook(ActionEvent event) {
        String id = bookIdInput.getText();
        String qu = "SELECT * FROM BOOK WHERE id = '" + id + "'";
        ResultSet rs = dataBaseHandler.execQuery(qu);
        boolean flage = false;
        try {
            while(rs.next()){
                String bName = rs.getString("title");
                String bAuthor = rs.getString("author");
                Boolean bStatus = rs.getBoolean("isAvail");
                String Status = (bStatus)?"Available":"Not Available";
                
                bookName.setText(bName);
                bookAuthor.setText(bAuthor);
                bookStatus.setText(Status);
                
                flage = true;
            }
            if(!flage){
                bookName.setText("There Is No Such Book Available");
                clearBookCache();
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void loadMemberInfo(ActionEvent event) {
        String id = memberIdInput.getText();
        String qu = "SELECT * FROM MEMBER WHERE id = '" + id + "'";
        ResultSet rs = dataBaseHandler.execQuery(qu);
        boolean flage = false;
        try {
            while(rs.next()){
                String mName = rs.getString("name");
                String mMobile = rs.getString("mobile");
                
                memberName.setText(mName);
                memberMobile.setText(mMobile);
                
                flage = true;
            }
            if(!flage){
                memberName.setText("There Is No Such Member Available");
                clearMemberCache();
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
      
    }
       void clearBookCache(){
            bookAuthor.setText("");
            bookStatus.setText("");
        }
       void clearMemberCache(){
            memberMobile.setText("");
       }

    @FXML
    private void loadIssueOperation(ActionEvent event) {
        String memberID = memberIdInput.getText();
        String bookID = bookIdInput.getText();
        
        Alert confAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confAlert.setTitle("Confirm New Opperation");
        confAlert.setHeaderText(null);
//TASK1 
        confAlert.setContentText("Are you sure about issue the book "+bookName.getText()+
                "\n to "+memberName.getText()+"?");
        
        Optional<ButtonType> response = confAlert.showAndWait();
        if(response.get()==ButtonType.OK){
            String insertStr = "INSERT INTO ISSUE(memberID,bookID) VALUES("
                    + "'" + memberID + "',"
                    + "'" + bookID + "')";
            String updateStr = "UPDATE BOOK SET isAvail = false WHERE id = '"+bookID+"'";
            System.out.println(insertStr+" and "+updateStr);
            
            if(dataBaseHandler.execAction(insertStr) && dataBaseHandler.execAction(updateStr)){
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Complete");
                    successAlert.showAndWait();
                }else{
                    Alert failedAlert = new Alert(Alert.AlertType.ERROR);
                    failedAlert.setTitle("Failed");
                    failedAlert.setHeaderText(null);
                    failedAlert.setContentText("Issue Operation Failed");
                    failedAlert.showAndWait();
                }
        }else{
            Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
            cancelAlert.setTitle("Canceled");
            cancelAlert.setHeaderText(null);
            cancelAlert.setContentText("Issue Operation Canceled");
            cancelAlert.showAndWait();
        }
    }

    @FXML
    private void loadBookInfo2(ActionEvent event) {
        
        ObservableList<String> issueData=FXCollections.observableArrayList();        
        String id=bookID.getText();
        String qu= "SELECT * FROM ISSUE WHERE bookID = '" + id + "'";
        ResultSet rs = dataBaseHandler.execQuery(qu);

        try {
                     System.out.println(rs.getString("id"));                                   

            while(rs.next()){
                                    System.out.println("step4");                                   
                String mBookID =id;
                String mMemberID = rs.getString("memberID");
                Timestamp mIssueTime = rs.getTimestamp("issueTime");
                int mRenewCount = rs.getInt("renew_Count");
                
                issueData.add("Issue Data and Time:"+mIssueTime.toGMTString());
                issueData.add("Renew Count:"+mRenewCount);

                issueData.add("Book Information:-");
                qu="SELECT * FROM BOOK WHERE ID = '" + mBookID +"'";
                ResultSet rl = dataBaseHandler.execQuery(qu);
                while(rl.next()){
                    issueData.add("Book Name:"+rl.getString("id"));
                    issueData.add("Book Name:"+rl.getString("title"));
                    issueData.add("Book Author:"+rl.getString("author"));
                    issueData.add("Book Publisher:"+rl.getString("publisher"));

                }
                qu="SELECT * FROM MEMBER WHERE ID = '" + mMemberID +"'";
                rl = dataBaseHandler.execQuery(qu);
                issueData.add("Member Information:-");
                while(rl.next()){
                    issueData.add("Name: "+rl.getString("name"));
                    issueData.add("Mobile: "+rl.getString("mobile"));
                    issueData.add("Email: "+rl.getString("email"));

                }
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
                System.out.println("step 3");
        issueDataList.getItems().setAll(issueData);
        
    }
}



///**********************************TASKS**************************************
//TASK 1: RETURN A STRING OF THE CONTENT OF THE NAME AND NOT BOOK NAME AND MEMBER NAME LIKE NOW
