package layered.business.extension;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import layered.business.user.UserFactoryImpl;

public class PluginApplier {
    private final Map<String, Plugin> pluginRegistry = new HashMap<>();

    public void loadNewEffect(File effectPluginFile, String effectID) {
		try {
			registerNewEffectPlugin(effectID, effectPluginFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

    private void registerNewEffectPlugin(String pluginID, File libFile) throws Exception  {     
        var loader = new PluginClassLoader(libFile.getAbsolutePath());
        String className = "layered.business.extension." + pluginID;
        Class<?> pluginClass = loader.loadClass(className);
        Plugin effectPlugin = (Plugin) pluginClass.getDeclaredConstructor().newInstance();
		pluginRegistry.put(pluginID, effectPlugin);
	}

    public void applyEffect(String effectID, String userId) throws IOException {
		Plugin effectPlugin = pluginRegistry.get(effectID);
		effectPlugin.applyPlugin(UserFactoryImpl.getInstance().getUserWithId(userId), UserFactoryImpl.getInstance());
	}
}
