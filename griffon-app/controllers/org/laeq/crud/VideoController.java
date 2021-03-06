package org.laeq.crud;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.scene.control.TableColumn;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import griffon.transform.Threading;
import org.laeq.DatabaseService;
import org.laeq.model.Collection;
import org.laeq.model.User;
import org.laeq.model.Video;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class VideoController extends AbstractGriffonController {
    private VideoModel model;
    private VideoView view;

    @Inject private DatabaseService dbService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        try{
            model.videoList.addAll(dbService.videoDAO.findAll());
            model.getUserSet().addAll(dbService.userDAO.findAll());
            model.getCollectionSet().addAll(dbService.collectionDAO.findAll());
            model.categorySet.addAll(dbService.categoryDAO.findAll());
            getApplication().getEventRouter().publishEvent("status.info", Arrays.asList("db.success.fetch"));

            //@todo add BiDirectionalBinding to remove this hack
            view.initForm();
        } catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.error.fetch"));
        }

        getApplication().getEventRouter().addEventListener(listeners());
    }

    @MVCMember
    public void setModel(@Nonnull VideoModel model) {
        this.model = model;
    }

    @MVCMember
    public void setView(@Nonnull VideoView view) {
        this.view = view;
    }

    public void clear(){
        runInsideUISync(() -> {
            model.clear();
        });
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void delete(Video video){
        try{
            dbService.videoDAO.delete(video);
            model.videoList.remove(video);
            getApplication().getEventRouter().publishEvent("status.success.parametrized", Arrays.asList("video.delete.success", video.pathToName()));
        }  catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.delete.error"));
        }
    }

    public void edit(){
        if(model.selectedVideo == null){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.edit.error"));
            model.selectedVideo = model.videoList.get(0);
        }

        createDisplay();
    }

    private void  createDisplay(){
//        getApplication().getMvcGroupManager().destroyMVCGroup("test");
//        Map<String, Object> args = new HashMap<>();
//        args.put("video",model.selectedVideo);
//        createMVCGroup("test", args);
    }

    public void export(Video video){
//        try {
//            String filename = exportService.export(video);
//
//            getApplication().getEventRouter().publishEvent("status.success.parametrized", Arrays.asList("video.export.success", filename));
//        } catch (IOException e) {
//            e.printStackTrace();
//            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.export.error"));
//        }
    }

    public void updateUser(TableColumn.CellEditEvent<Video, User> event) {
        try {
            Video video = event.getRowValue();
            video.setUser(event.getNewValue());
            dbService.videoDAO.create(video);
            getApplication().getEventRouter().publishEvent("status.success", Arrays.asList("video.user.updated.success"));
        } catch (Exception e) {
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.user.updated.error"));
        }

        runInsideUISync(() -> {
            view.refresh();
        });
    }

    public void updateCollection(Video video, Collection newValue) {
        try{
            clear();
            video.updateCollection(newValue);
            dbService.videoDAO.create(video);


            getApplication().getEventRouter().publishEvent("status.success", Arrays.asList("video.collection.updated.success"));
        } catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.collection.updated.error"));
        }

        runInsideUISync(() -> {
            view.refresh();
        });
    }

    public void select(Video video) {
        runInsideUISync(() -> {
            model.clear();
            model.setSelectedVideo(video);
        });
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("video.created", objects -> {
            Video video = (Video) objects[0];
            runInsideUISync(() -> {
                model.videoList.add(video);
            });
        });

        list.put("point.created", objects -> {
            runInsideUISync(() -> {
                view.refresh();
            });
        });

        return list;
    }
}
