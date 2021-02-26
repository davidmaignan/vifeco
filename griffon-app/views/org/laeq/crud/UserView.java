package org.laeq.crud;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.VifecoView;
import org.laeq.model.User;

import java.util.Collections;
import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class UserView extends AbstractJavaFXGriffonView {
    private UserController controller;
    private UserModel model;

    @MVCMember private VifecoView parentView;

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> id;
    @FXML private TableColumn<User, String> firstName;
    @FXML private TableColumn<User, String> lastName;
    @FXML private TableColumn<User, String> email;
//    @FXML private TableColumn<User, Icon> isDefault;
    @FXML private TableColumn<User, Void> actions;

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private Button saveActionTarget;
    @FXML private Button clearActionTarget;

    @MVCMember
    public void setController(@Nonnull UserController controller) {
        this.controller = controller;
    }

    @MVCMember
    public void setModel(@Nonnull UserModel model) {
        this.model = model;
    }

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        connectActions(node, controller);
        connectMessageSource(node);

        parentView.middle.getChildren().add(node);

        init();
    }
    private void init(){
        id.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getId().toString()));
        firstName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        email.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
//        isDefault.setCellValueFactory(cellData -> cellData.getValue().getDefault() ? new SimpleObjectProperty<>(new Icon(IconSVG.tick, Color.green)) : null);
        actions.setCellFactory(addActions());

        model.firstName.bindBidirectional(firstNameField.textProperty());
        model.lastName.bindBidirectional(lastNameField.textProperty());
        model.email.bindBidirectional(emailField.textProperty());

        userTable.setItems(model.userList);
    }

    private Callback<TableColumn<User, Void>, TableCell<User, Void>> addActions() {
        return param -> {
            final  TableCell<User, Void> cell = new TableCell<User, Void>(){
                Button edit = new Button("edit");
                Button delete = new Button("delete");

                Group btnGroup = new Group();
                {
                    btnGroup.getChildren().addAll(edit, delete);
                    edit.setLayoutX(5);
                    edit.getStyleClass().addAll("btn", "btn-sm", "btn-info");
                    delete.setLayoutX(105);
                    delete.getStyleClass().addAll("btn", "btn-sm", "btn-danger");

                    edit.setOnAction(event -> {
                        model.setSelectedUser(userTable.getItems().get(getIndex()));
                    });

                    delete.setOnAction(event -> {
                        controller.delete(userTable.getItems().get(getIndex()));
                    });
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btnGroup);
                    }
                }
            };

            return cell;
        };
    }
}