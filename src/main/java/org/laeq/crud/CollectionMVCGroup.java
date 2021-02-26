package org.laeq.crud;

import javax.inject.Named;
import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;
import javax.annotation.Nonnull;

@Named("collection")
public class CollectionMVCGroup extends AbstractTypedMVCGroup<CollectionModel, CollectionView, CollectionController> {
    public CollectionMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}