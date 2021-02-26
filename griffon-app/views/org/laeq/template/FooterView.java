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
public class FooterView extends AbstractJavaFXGriffonView {
    private FooterController controller;
    private FooterModel model;

    @MVCMember private VifecoView parentView;

    @FXML private Label clickLabel;

    @MVCMember
    public void setController(@Nonnull FooterController controller) {
        this.controller = controller;
    }

    @MVCMember
    public void setModel(@Nonnull FooterModel model) {
        this.model = model;
    }

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.footer.getChildren().add(node);

        connectActions(node, controller);
        connectMessageSource(node);
    }
}
