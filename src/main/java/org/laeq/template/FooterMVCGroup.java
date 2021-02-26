package org.laeq.template;

import javax.inject.Named;
import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;
import javax.annotation.Nonnull;

@Named("footer")
public class FooterMVCGroup extends AbstractTypedMVCGroup<FooterModel, FooterView, FooterController> {
    public FooterMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}