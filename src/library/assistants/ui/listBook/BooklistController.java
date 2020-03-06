/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistants.ui.listBook;

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
import javafx.scene.layout.AnchorPane;
import library.assistants.database.DataBaseHandler;
import library.assistants.ui.addbook.BookAddController;

/**
 * FXML Controller class
 *
 * @author Ohayon
 */
public class BooklistController implements Initializable {
    
    ObservableList<Book> list = FXCollections.observableArrayList();
    

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Book> tableView;
    @FXML
    private TableColumn<Book,String> TitleColume;
    @FXML
    private TableColumn<Book,String> BookIdColume;
    @FXML
    private TableColumn<Book,String> AuthorColume;
    @FXML
    private TableColumn<Book,String> PublisherColume;
    @FXML
    private TableColumn<Book,Boolean> availColume;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        initCol();
        loadData();
    }    

    private void initCol() {
        TitleColume.setCellValueFactory(new PropertyValueFactory<>("title"));
        BookIdColume.setCellValueFactory(new PropertyValueFactory<>("Id"));
        AuthorColume.setCellValueFactory(new PropertyValueFactory<>("Author"));
        PublisherColume.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        availColume.setCellValueFactory(new PropertyValueFactory<>("availability"));

    }

    private void loadData() {
        list.clear();
        DataBaseHandler handler = DataBaseHandler.getInstance();
        
        String query = "SELECT * FROM BOOK ";
        ResultSet resultSet = handler.execQuery(query);
        try {
            while(resultSet.next()){
                String checkTitle = resultSet.getString("title");
                String checkAuthor = resultSet.getString("author");
                String checkId = resultSet.getString("id");
                String checkPublisher = resultSet.getString("publisher");
                Boolean checkAvail = resultSet.getBoolean("isAvail");
                
                list.add(new Book(checkTitle, checkId, checkAuthor, checkPublisher, checkAvail));

                System.out.println(checkAvail);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BookAddController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        tableView.setItems(list);

    }
    
    public static class Book{
        private final SimpleStringProperty title;
        private final SimpleStringProperty id;
        private final SimpleStringProperty author;
        private final SimpleStringProperty publisher;
        private final SimpleBooleanProperty availabilty;
            Book(String title, String id,String author,String publisher,Boolean availabilty){
            this.title = new SimpleStringProperty(title);
            this.id = new SimpleStringProperty(id);
            this.author = new SimpleStringProperty(author);
            this.publisher = new SimpleStringProperty(publisher);
            this.availabilty = new SimpleBooleanProperty(availabilty);
        }

        /**
         * @return the title
         */
        public String getTitle() {
            return title.get();
        }

        /**
         * @return the id
         */
        public String getId() {
            return id.get();
        }

        /**
         * @return the author
         */
        public String getAuthor() {
            return author.get();
        }

        /**
         * @return the publisher
         */
        public String getPublisher() {
            return publisher.get();
        }

        /**
         * @return the availabilty
         */
        public Boolean getAvailabilty() {
            return availabilty.get();
        }
            
    }
    
}
