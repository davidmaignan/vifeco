package org.laeq.template;

import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import griffon.transform.Threading;
import org.laeq.DatabaseService;
import org.laeq.model.Collection;
import org.laeq.model.User;
import org.laeq.model.Video;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@ArtifactProviderFor(GriffonController.class)
public class MenuController extends AbstractGriffonController {
    private MenuModel model;
    private FileChooser fileChooser;

    @Inject private DatabaseService dbService;

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

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void open() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "Video Files",
                        "*.mp4", "*.wav", "*.mkv", "*.avi", "*.wmv", "*.mov")
        );

        Stage stage = (Stage) getApplication().getWindowManager().findWindow("mainWindow");

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            getApplication().getEventRouter().publishEvent("status.info", Arrays.asList("video.create.start"));

            this.createVideo(selectedFile);

        } else {
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.create.error"));
        }
    }

    private void createVideo(File selectedFile) {
        try {
            Video video = new Video();
            String path = selectedFile.getAbsolutePath();
            User defaultUser = dbService.userDAO.findDefault();
            Collection defaultCollection = dbService.collectionDAO.findDefault();
            video.setPath(path);
            video.setCollection(defaultCollection);
            video.setUser(defaultUser);
            video.setDuration(Duration.UNKNOWN);

            dbService.videoDAO.create(video);
            getApplication().getEventRouter().publishEvent("status.success", Arrays.asList("video.create.success"));
            getApplication().getEventRouter().publishEvent("video.created", Arrays.asList(video));

            runOutsideUIAsync(() -> {
                System.out.println("Duration calculation");
                try {
                    File file = new File(video.getPath());
                    Media media = new Media(file.getCanonicalFile().toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(media);

                    mediaPlayer.setOnError(() -> {
                        System.out.println(mediaPlayer.getError());
                        getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.create.error"));
                    });

                    mediaPlayer.setOnReady(()-> {
                        video.setDuration(mediaPlayer.getTotalDuration());
                        try {
                            dbService.videoDAO.create(video);
                            System.out.println("Duration success");


                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Duration error");
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.create.error"));
        }
    }
}