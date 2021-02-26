package org.laeq.template;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.VifecoView;
import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class MenuView extends AbstractJavaFXGriffonView {
    private MenuController controller;
    private MenuModel model;

    @MVCMember private VifecoView parentView;

    @FXML private Label clickLabel;

    @MVCMember
    public void setController(@Nonnull MenuController controller) {
        this.controller = controller;
    }

    @MVCMember
    public void setModel(@Nonnull MenuModel model) {
        this.model = model;
    }

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        connectActions(node, controller);
        connectMessageSource(node);
        parentView.menu.getChildren().add(node);
    }
}
