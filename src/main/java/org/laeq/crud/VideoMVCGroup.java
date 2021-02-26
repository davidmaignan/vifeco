package org.laeq.crud;

import javax.inject.Named;
import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;
import javax.annotation.Nonnull;

@Named("video")
public class VideoMVCGroup extends AbstractTypedMVCGroup<VideoModel, VideoView, VideoController> {
    public VideoMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}