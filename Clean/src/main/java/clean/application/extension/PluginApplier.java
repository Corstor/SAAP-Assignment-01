package clean.application.extension;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class PluginApplier<X> {
    private final Map<String, Plugin<X>> pluginRegistry = new HashMap<>();

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
        Plugin<X> effectPlugin = (Plugin<X>) pluginClass.getDeclaredConstructor().newInstance();
		pluginRegistry.put(pluginID, effectPlugin);
	}

    public void applyEffect(String effectID, X value) throws IOException {
		Plugin<X> effectPlugin = pluginRegistry.get(effectID);
		effectPlugin.applyPlugin(value);
	}
}
