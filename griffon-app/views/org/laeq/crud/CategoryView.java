package org.laeq.crud;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.util.Callback;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.VifecoView;
import org.laeq.model.Category;
import org.laeq.model.ColorDefinition;
import org.laeq.model.Icon;
import org.laeq.model.icon.IconSVG;
import java.util.Arrays;
import javax.annotation.Nonnull;


@ArtifactProviderFor(GriffonView.class)
public class CategoryView extends AbstractJavaFXGriffonView {
    private CategoryController controller;
    private CategoryModel model;

    @MVCMember private VifecoView parentView;

    @FXML private TextField nameField;
    @FXML private TextField shortCutField;
    @FXML private TextField pathField;
    @FXML private TextField colorPickerField;
    @FXML private Pane svgDisplayPane;


    @FXML private TableView<Category> categoryTable;
    @FXML private TableColumn<Category, String> id;
    @FXML private TableColumn<Category, Void> icon;
    @FXML private TableColumn<Category, String> name;
    @FXML private TableColumn<Category, String> shortcut;
    @FXML private TableColumn<Category, Void> actions;

    private SVGPath svgPath;

    @MVCMember
    public void setController(@Nonnull CategoryController controller) {
        this.controller = controller;
    }

    @MVCMember
    public void setModel(@Nonnull CategoryModel model) {
        this.model = model;
    }

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        connectActions(node, controller);
        connectMessageSource(node);

        parentView.middle.getItems().clear();
        parentView.middle.getItems().add(node);

        svgPath = new SVGPath();
        svgPath.setSmooth(true);
        svgPath.setFill(Paint.valueOf("#000000"));
        svgPath.setScaleX(4);
        svgPath.setScaleY(4);
        svgPath.setLayoutX(50);
        svgPath.setLayoutY(50);

        svgDisplayPane.getChildren().add(svgPath);

        init();
    }

    private void init(){
        //Table
        id.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getId().toString()));
        name.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        shortcut.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getShortcut()));
        icon.setCellFactory(iconAction());
        actions.setCellFactory(addActions());

        //Form
        model.name.bindBidirectional(nameField.textProperty());
        model.shortCut.bindBidirectional(shortCutField.textProperty());
        model.icon.bindBidirectional(pathField.textProperty());
        model.color.bindBidirectional(colorPickerField.textProperty());

        pathField.textProperty().addListener((observable, oldValue, newValue) -> {
            svgPath.setFill(Paint.valueOf(colorPickerField.textProperty().get()));
            svgPath.setContent(newValue);
        });

        colorPickerField.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                svgPath.setFill(Paint.valueOf(newValue));
            } catch (Exception e){
                getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("category.color.invalid"));
            }
        });

        categoryTable.setItems(this.model.categoryList);
    }

    private Callback<TableColumn<Category, Void>, TableCell<Category, Void>> addActions() {
        return param -> {
            final  TableCell<Category, Void> cell = new TableCell<Category, Void>(){
                Button edit = new Button("edit");
                Button delete = new Button("delete");

                Group btnGroup = new Group();
                {
                    edit.setLayoutX(5);
                    delete.setLayoutX(105);

                    btnGroup.getChildren().addAll(edit, delete);
                    edit.getStyleClass().addAll("btn", "btn-sm", "btn-info");

                    edit.setOnAction(event -> {
                        model.setSelectedCategory(categoryTable.getItems().get(getIndex()));
                        Category category = categoryTable.getItems().get(getIndex());
                        colorPickerField.setText(category.getColor());
                    });

                    delete.getStyleClass().addAll("btn", "btn-sm", "btn-danger");
                    delete.setOnAction(event -> {
                        controller.delete(categoryTable.getItems().get(getIndex()));
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
    private Callback<TableColumn<Category, Void>, TableCell<Category, Void>> iconAction() {
        return  param -> {
            final TableCell<Category, Void> cell = new TableCell<Category, Void>() {

                SVGPath icon = new SVGPath();

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    try{
                        Category category = categoryTable.getItems().get(getIndex());
                        icon.setContent(category.getIcon());
                        icon.setFill(Paint.valueOf(category.getColor()));

                        colorPickerField.setText(category.getColor());
                    } catch (Exception e){
//                        getLog().error(e.getMessage());
                    }

                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(icon);
                    }
                }
            };

            return cell;
        };
    }
}
