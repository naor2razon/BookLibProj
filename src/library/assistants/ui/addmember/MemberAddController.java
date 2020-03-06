
package library.assistants.ui.addmember;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import library.assistants.database.DataBaseHandler;


public class MemberAddController implements Initializable {

    DataBaseHandler handler;
    
    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField Id;
    @FXML
    private JFXTextField mobile;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handler = DataBaseHandler.getInstance();
    }    

    @FXML
    private void addMember(ActionEvent event) {
        String memberName = name.getText();
        String memberId = Id.getText();
        String memberMobile = mobile.getText();
        String memberEmail = email.getText();
        
        if(memberName.isEmpty() || memberId.isEmpty() || memberMobile.isEmpty() || memberEmail.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please enter in all filed");
            alert.showAndWait();
            return;
        }
        
        String st = "INSERT INTO MEMBER(id,name,mobile,email) VALUES("+
         "'" + memberId + "',"+
        "'" + memberName + "',"+
        "'" + memberMobile + "',"+
        "'" + memberEmail + "'"+        
        ")";
        System.out.println(st);
        
        if(handler.execAction(st)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("success");
            alert.showAndWait();
            clearAddMemberFieldsAfterAdd();
            return; 
        }else { //error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("FAILD");
            alert.showAndWait();
            clearAddMemberFieldsAfterAdd();
            return;
        }
    }

    ////fix 1 
    @FXML
    private void cancel(ActionEvent event) {
        Platform.exit();
    }
    
    public void clearAddMemberFieldsAfterAdd(){
        this.Id.setText(null);
        this.name.setText(null);
        this.mobile.setText(null);
        this.email.setText(null);
    }
}


//////////////////////////////////////need to fix///////////////////////////////////////////
//1-need to exit dialog and not all platform
