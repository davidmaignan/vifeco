package org.laeq.crud;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.VifecoView;
import org.laeq.model.*;
import org.laeq.model.icon.IconSVG;
import javax.annotation.Nonnull;


@ArtifactProviderFor(GriffonView.class)
public class CollectionView extends AbstractJavaFXGriffonView {
    private CollectionController controller;
    private CollectionModel model;

    @MVCMember private VifecoView parentView;

    @FXML private TextField nameField;
    @FXML private Group categoryContainer;
    @FXML private Button saveActionTarget;
    @FXML private Button clearActionTarget;

    @FXML private TableView<Collection> collectionTable;
    @FXML private TableColumn<Collection, String> id;
    @FXML private TableColumn<Collection, String> name;
    @FXML private TableColumn<Collection, String> categories;
    @FXML private TableColumn<Collection, Icon> isDefault;
    @FXML private TableColumn<Collection, Void> actions;

    @MVCMember
    public void setController(@Nonnull CollectionController controller) {
        this.controller = controller;
    }

    @MVCMember
    public void setModel(@Nonnull CollectionModel model) {
        this.model = model;
    }

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        connectActions(node, controller);
        connectMessageSource(node);

        parentView.middle.getItems().clear();
        parentView.middle.getItems().add(node);

        init();
    }

    private void init() {
        name.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        categories.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCategorieNames()));
        isDefault.setCellValueFactory(cellData -> cellData.getValue().getDefault() ? new SimpleObjectProperty<>(new Icon(IconSVG.tick, ColorDefinition.green)) : null);
        actions.setCellFactory(addActions());

        collectionTable.setItems(this.model.collections);

        model.name.bindBidirectional(nameField.textProperty());
    }

    public void initForm(){
        runInsideUIAsync(() -> {
            double x = 0;
            double y = 0;
            double index = 0;
            for (Category category : model.getCategories()) {
                CategoryCheckedBox checkedBox = new CategoryCheckedBox(category);
                checkedBox.setLayoutX(x);
                checkedBox.setLayoutY(y);
                x += checkedBox.getWidth() + 20;
                index++;

                if (index != 0 && index % 3 == 0) {
                    x = 0;
                    y += 45;
                }

                categoryContainer.getChildren().add(checkedBox);
                checkedBox.getBox().selectedProperty().bindBidirectional(model.categorySBP.get(category));
            }
        });
    }

    private Callback<TableColumn<Collection, Void>, TableCell<Collection, Void>> addActions() {
        return param -> {
            final  TableCell<Collection, Void> cell = new TableCell<Collection, Void>(){
                Button edit = new Button("edit");
                Button delete = new Button("delete");

                Group btnGroup = new Group();
                {
                    edit.setLayoutX(5);
                    delete.setLayoutX(105);

                    btnGroup.getChildren().addAll(edit, delete);
                    edit.getStyleClass().addAll("btn", "btn-sm", "btn-info");
                    edit.setOnMouseClicked(event -> {
                        model.setSelectedCollection(collectionTable.getItems().get(getIndex()));
                    });

                    delete.getStyleClass().addAll("btn", "btn-sm", "btn-danger");
                    delete.setOnMouseClicked(event -> {
                        controller.delete(collectionTable.getItems().get(getIndex()));
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
