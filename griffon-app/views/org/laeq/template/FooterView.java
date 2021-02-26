package org.laeq.template;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.VifecoView;
import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class FooterView extends AbstractJavaFXGriffonView {
    private FooterController controller;
    private FooterModel model;

    @MVCMember private VifecoView parentView;

    @FXML private Label messageLabel;
    @FXML private AnchorPane messageBox;

    @MVCMember
    public void setModel(@Nonnull FooterModel model) {
        this.model = model;
    }

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.footer.getChildren().add(node);

        model.message.bindBidirectional(messageLabel.textProperty());
        model.styles.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                messageBox.getStyleClass().clear();
                messageBox.getStyleClass().addAll(c.getList());
            }
        });

        connectActions(node, controller);
        connectMessageSource(node);
    }
}
