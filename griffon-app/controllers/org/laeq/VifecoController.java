package org.laeq;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import griffon.transform.Threading;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class VifecoController extends AbstractGriffonController {
    private VifecoModel model;
    private String currentMVCId = null;

    @MVCMember
    public void setModel(@Nonnull VifecoModel model) {
        this.model = model;
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void click() {
        int count = Integer.parseInt(model.getClickCount());
        model.setClickCount(String.valueOf(count + 1));
    }

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listenerList());

    }

    private Map<String, RunnableWithArgs> listenerList(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("user.section", objects -> createGroup("user"));
        list.put("category.section", objects -> createGroup("category"));
        list.put("collection.section", objects -> createGroup("collection"));
        list.put("video.section", objects -> createGroup("video"));
//        list.put("statistic.section", objects -> createGroup("statistic_container"));
//        list.put("about.section", objects -> createGroup("about_container"));

        return list;
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void createGroup(String groupName){
        System.out.println("Create group " + groupName);
        try{
            if(currentMVCId != null){
                destroyMVCGroup(currentMVCId);
            }

            currentMVCId = groupName;
            createMVCGroup(groupName);
        } catch (Exception e){
            e.printStackTrace();
            getLog().info(String.format("CreateMVCGroup: %s - %s", groupName, e.getMessage()));
        }
    }
}