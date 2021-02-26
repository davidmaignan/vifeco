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

    @Inject
    private DatabaseService dbService;

    @MVCMember
    public void setModel(@Nonnull CollectionModel model) {
        this.model = model;
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