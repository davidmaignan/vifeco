import griffon.util.AbstractMapResourceBundle;

import javax.annotation.Nonnull;
import java.util.Map;

import static java.util.Arrays.asList;
import static griffon.util.CollectionUtils.map;

public class Config extends AbstractMapResourceBundle {
    @Override
    protected void initialize(@Nonnull Map<String, Object> entries) {
        map(entries)
            .e("application", map()
                .e("title", "vifeco")
                .e("startupGroups", asList("vifeco"))
                .e("autoShutdown", true)
            )
            .e("mvcGroups", map()
                .e("vifeco", map()
                    .e("model", "org.laeq.VifecoModel")
                    .e("view", "org.laeq.VifecoView")
                    .e("controller", "org.laeq.VifecoController")
                )
                .e("menu", map()
                    .e("model", "org.laeq.template.MenuModel")
                    .e("view", "org.laeq.template.MenuView")
                    .e("controller", "org.laeq.template.MenuController")
                )
                .e("footer", map()
                    .e("model", "org.laeq.template.FooterModel")
                    .e("view", "org.laeq.template.FooterView")
                    .e("controller", "org.laeq.template.FooterController")
                )
                .e("user", map()
                    .e("model", "org.laeq.crud.UserModel")
                    .e("view", "org.laeq.crud.UserView")
                    .e("controller", "org.laeq.crud.UserController")
                )
                .e("category", map()
                        .e("model", "org.laeq.crud.CategoryModel")
                        .e("view", "org.laeq.crud.CategoryView")
                        .e("controller", "org.laeq.crud.CategoryController")
                )
                .e("collection", map()
                        .e("model", "org.laeq.crud.CollectionModel")
                        .e("view", "org.laeq.crud.CollectionView")
                        .e("controller", "org.laeq.crud.CollectionController")
                )
                .e("video", map()
                        .e("model", "org.laeq.crud.VideoModel")
                        .e("view", "org.laeq.crud.VideoView")
                        .e("controller", "org.laeq.crud.VideoController")
                )
            );
    }
}