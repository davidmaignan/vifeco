package org.laeq.crud;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import griffon.transform.Threading;
import org.laeq.DatabaseService;
import org.laeq.model.Category;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class CategoryController extends AbstractGriffonController {
    private CategoryModel model;

    @Inject private DatabaseService dbService;

    @MVCMember
    public void setModel(@Nonnull CategoryModel model) {
        this.model = model;
    }

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        try{
            model.categoryList.addAll(dbService.categoryDAO.findAll());
            getApplication().getEventRouter().publishEvent("status.info", Arrays.asList("db.success.fetch"));
        } catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.error.fetch"));
        }

        getApplication().getEventRouter().addEventListener(listeners());
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void save(){
        try{
            dbService.categoryDAO.create(model.getCategory());
            model.categoryList.clear();
            model.clear();
            model.categoryList.addAll(dbService.categoryDAO.findAll());
            getApplication().getEventRouter().publishEvent("status.success", Arrays.asList("db.success.save"));

        } catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.error.save"));
        }
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void clear(){
        model.clear();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void delete(Category category) {
        try{
            dbService.categoryDAO.delete(category);
            model.categoryList.remove(category);
            getApplication().getEventRouter().publishEvent("status.success", Arrays.asList("db.success.delete"));
        }  catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.error.delete"));
        }
    }

    private Map<String, RunnableWithArgs> listeners() {
        Map<String, RunnableWithArgs> list = new HashMap<>();

        return list;
    }
}