package org.laeq.crud;

import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import griffon.transform.Threading;
import org.laeq.DatabaseService;
import org.laeq.model.User;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class UserController extends AbstractGriffonController {
    private UserModel model;

    @Inject
    private DatabaseService dbService;

    @MVCMember
    public void setModel(@Nonnull UserModel model) {
        this.model = model;
    }

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        try{
            model.userList.addAll(dbService.userDAO.findAll());
            getApplication().getEventRouter().publishEvent("status.info", Arrays.asList("db.success.fetch"));
        } catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.error.fetch"));
        }
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void save(){
        try {
            User user = model.getUser();
            dbService.userDAO.create(user);
            model.userList.clear();
            model.userList.addAll(dbService.userDAO.findAll());
            model.clear();
            getApplication().getEventRouter().publishEvent("status.success", Arrays.asList("db.success.save"));
        } catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.error.save"));
        }
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void delete(User user) {
        try {
            dbService.userDAO.delete(user);
            model.userList.remove(user);
            getApplication().getEventRouter().publishEvent("status.success", Arrays.asList("db.success.delete"));
        }  catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.error.delete"));
        }
    }
}