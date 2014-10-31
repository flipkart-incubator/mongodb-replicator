package flipkart.mongo.replicator.core.versions;

import com.google.common.collect.ImmutableList;
import flipkart.mongo.replicator.core.interfaces.VersionHandler;
import flipkart.mongo.replicator.core.model.MongoV;
import org.reflections.Reflections;

import java.util.Set;

/**
 * Created by pradeep on 30/10/14.
 */
public class VersionManager {

    protected final  ImmutableList<VersionHandler> versionHandlers;

    protected VersionManager() {
        Reflections reflections = new Reflections("flipkart.mongo.replicator.core.versions");
        Set<Class<? extends VersionHandler>> classes = reflections.getSubTypesOf(VersionHandler.class);
        ImmutableList.Builder<VersionHandler> builder = new ImmutableList.Builder<VersionHandler>();

        for ( Class<? extends VersionHandler> clazz : classes) {
            try {
                builder.add(clazz.newInstance());
            } catch (Exception e) { }
        }

        versionHandlers = builder.build();
    }

    private static Integer mutex = new Integer(0);
    private static VersionManager instance = null;

    public static VersionManager singleton() {
        if ( instance == null) {
            synchronized (mutex) {
                if ( instance == null) {
                    instance = new VersionManager();
                }
            }
        }

        return instance;
    }

    public VersionHandler getVersionHandler(MongoV version ) {
        for ( VersionHandler versionHandler : versionHandlers ) {
            if ( versionHandler.getFrom().isGte(version) && versionHandler.getTo().isLte(version) ) {
                return versionHandler;
            }
        }

        return null;
    }

}
