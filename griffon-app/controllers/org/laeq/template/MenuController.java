package org.laeq.template;

import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import griffon.transform.Threading;
import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonController.class)
public class MenuController extends AbstractGriffonController {
    private MenuModel model;

    @MVCMember
    public void setModel(@Nonnull MenuModel model) {
        this.model = model;
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void video() {
        getApplication().getEventRouter().publishEvent("video.section");
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void user() {
        getApplication().getEventRouter().publishEvent("user.section");
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void collection() {
        getApplication().getEventRouter().publishEvent("collection.section");
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void category() {
        getApplication().getEventRouter().publishEvent("category.section");
    }
}