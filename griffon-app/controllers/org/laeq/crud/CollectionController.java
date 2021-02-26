package org.laeq.crud;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import griffon.transform.Threading;
import org.laeq.DatabaseService;
import org.laeq.model.Collection;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class CollectionController extends AbstractGriffonController {
    private CollectionModel model;
    private CollectionView view;

    @Inject
    private DatabaseService dbService;

    @MVCMember
    public void setModel(@Nonnull CollectionModel model) {
        this.model = model;
    }

    @MVCMember
    public void setView(@Nonnull CollectionView view) {
        this.view = view;
    }

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        try {
            model.collections.addAll(dbService.collectionDAO.findAll());
            model.addCategories(dbService.categoryDAO.findAll());
            getApplication().getEventRouter().publishEvent("status.info", Arrays.asList("db.success.fetch"));
        } catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.error.fetch"));
        }

        view.initForm();

        getApplication().getEventRouter().addEventListener(listeners());
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void save(){
        try{
            Collection collection = model.getCollection();
            dbService.collectionDAO.create(collection);
            model.clear();
            model.collections.clear();
            model.collections.addAll(dbService.collectionDAO.findAll());

            getApplication().getEventRouter().publishEvent("status.success", Arrays.asList("db.success.save"));
        }catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.error.save"));
        }
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void clear(){
        model.clear();
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void delete(Collection collection) {
        try {
            dbService.collectionDAO.delete(collection);
            model.collections.clear();
            model.collections.addAll(dbService.collectionDAO.findAll());
            getApplication().getEventRouter().publishEvent("status.success", Arrays.asList("db.success.delete"));
        } catch (Exception e) {
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.error.delete"));
        }
    }

    private Map<String, RunnableWithArgs> listeners() {
        Map<String, RunnableWithArgs> list = new HashMap<>();

        return list;
    }
}