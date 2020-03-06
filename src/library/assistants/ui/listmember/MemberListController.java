/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistants.ui.listmember;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import library.assistants.database.DataBaseHandler;
import library.assistants.ui.addbook.BookAddController;
import library.assistants.ui.listBook.BooklistController;

/**
 * FXML Controller class
 *
 * @author Ohayon
 */
public class MemberListController implements Initializable {
    
        ObservableList<MemberListController.Member> list = FXCollections.observableArrayList();


    @FXML
    private TableView<Member> tableView;
    @FXML
    private TableColumn<Member, String> MemberNameCol;
    @FXML
    private TableColumn<Member, String> MemberIdColume;
    @FXML
    private TableColumn<Member,String> MobileColume;
    @FXML
    private TableColumn<Member,String> EmailColume;

    
    public static class Member{
        private final SimpleStringProperty name;
        private final SimpleStringProperty id;
        private final SimpleStringProperty mobile;
        private final SimpleStringProperty email;
            Member(String name, String id,String mobile,String email){
            this.name = new SimpleStringProperty(name);
            this.id = new SimpleStringProperty(id);
            this.mobile = new SimpleStringProperty(mobile);
            this.email = new SimpleStringProperty(email);
            
        }

        public String getName() {
            return name.get();
        }

        public String getId() {
            return id.get();
        }

        public String getMobile() {
            return mobile.get();
        }

        public String getEmail() {
            return email.get();
        }            
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        initCol();
        loadData();
    }   
    
      private void initCol() {
        MemberNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        MemberIdColume.setCellValueFactory(new PropertyValueFactory<>("Id"));
        MobileColume.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        EmailColume.setCellValueFactory(new PropertyValueFactory<>("email"));
    }
      
       private void loadData() {
        list.clear();
        DataBaseHandler handler = DataBaseHandler.getInstance();
        
        String query = "SELECT * FROM MEMBER ";
        ResultSet resultSet = handler.execQuery(query);
        try {
            while(resultSet.next()){
                String checkName = resultSet.getString("name");
                String checkId = resultSet.getString("id");
                String checkMobile = resultSet.getString("mobile");
                String checkEmail = resultSet.getString("email");
                
                list.add(new MemberListController.Member(checkName, checkId, checkMobile, checkEmail));

            }
        } catch (SQLException ex) {
            Logger.getLogger(BookAddController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        tableView.setItems(list);

    }
    
}
