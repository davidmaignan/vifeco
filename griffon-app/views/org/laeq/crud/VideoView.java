package org.laeq.crud;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.VifecoView;
import org.laeq.model.*;

import java.util.Collections;
import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class VideoView extends AbstractJavaFXGriffonView {
    private VideoController controller;
    private VideoModel model;

    @MVCMember private VifecoView parentView;

    @FXML private TableView<Video> videoTable;
    @FXML private TableView<CategoryCount> categoryTable;

    @FXML private Label titleValue;
    @FXML private Label durationValue;
    @FXML private Label totalValue;
    @FXML private Group categoryGroup;

    @FXML private TableColumn<Video, String> createdAt;
    @FXML private TableColumn<Video, String> name;
    @FXML private TableColumn<Video, String> path;
    @FXML private TableColumn<Video, String> duration;
    @FXML private TableColumn<Video, User>  user;
    @FXML private TableColumn<Video, Collection> collection;
    @FXML private TableColumn<Video, Number> total;
    @FXML private TableColumn<Video, Void> actions;

    @FXML private TableColumn<CategoryCount, Icon> icon;
    @FXML private TableColumn<CategoryCount, String> category;
    @FXML private TableColumn<CategoryCount, String> count;


    @MVCMember
    public void setController(@Nonnull VideoController controller) {
        this.controller = controller;
    }

    @MVCMember
    public void setModel(@Nonnull VideoModel model) {
        this.model = model;
    }

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        connectActions(node, controller);
        connectMessageSource(node);

        parentView.middle.getItems().clear();
        parentView.middle.getItems().add(node);

        model.name.bindBidirectional(titleValue.textProperty());
        model.duration.bindBidirectional(durationValue.textProperty());
        model.total.bindBidirectional(totalValue.textProperty());

        init();
    }

    private void init() {
        videoTable.setEditable(true);

        createdAt.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCreatedAtFormatted()));
        name.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().pathToName()));
        path.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPath()));
        duration.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDurationFormatted()));
        total.setCellValueFactory(cellData -> new ReadOnlyIntegerWrapper(cellData.getValue().getPoints().size()));
        actions.setCellFactory(addActions());

        videoTable.setItems(this.model.videoList);
        categoryTable.setItems(this.model.categoryCounts);

//        icon.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().category.getIcon2()));
        category.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().category.getName()));
        count.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().count.toString()));

    }

    public void initForm(){
        ObservableList<User> users = FXCollections.observableArrayList(model.getUserSet());
        user.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getUser()));
        user.setMinWidth(140);
        user.setCellFactory(ComboBoxTableCell.forTableColumn(users));
        user.setOnEditCommit(event -> controller.updateUser(event));

        ObservableList<Collection> collections = FXCollections.observableArrayList(model.getCollectionSet());
        System.out.println(collections);
        collection.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getCollection()));
        collection.setMinWidth(140);
        collection.setCellFactory(ComboBoxTableCell.forTableColumn(collections));
        collection.setOnEditCommit(event -> controller.updateCollection(event.getRowValue(), event.getNewValue()));
    }

    private Callback<TableColumn<Video, Void>, TableCell<Video, Void>> addActions() {
        return param -> {
            final  TableCell<Video, Void> cell = new TableCell<Video, Void>(){
                Button export = new Button(translate("btn.export"));
                Button edit = new Button(translate("btn.details"));
                Button delete = new Button(translate("btn.delete"));

                Group btnGroup = new Group();
                {
                    edit.setLayoutX(5);
                    export.setLayoutX(100);
                    delete.setLayoutX(200);

                    edit.getStyleClass().addAll("btn", "btn-info", "btn-sm");
                    export.getStyleClass().addAll("btn", "btn-warning", "btn-sm");
                    delete.getStyleClass().addAll("btn", "btn-danger", "btn-sm");

                    btnGroup.getChildren().addAll(edit, export, delete);

                    edit.setOnAction(event -> {
                        controller.select(videoTable.getItems().get(getIndex()));
                    });

                    export.setOnAction(event -> {
                        controller.export(videoTable.getItems().get(getIndex()));
                    });

                    delete.setOnAction(event -> {
                        controller.delete(videoTable.getItems().get(getIndex()));
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

    public void refresh() {
        videoTable.refresh();
    }

    private String translate(String key){
        return getApplication().getMessageSource().getMessage(key);
    }
}
