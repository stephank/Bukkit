package org.bukkit.plugin.java;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A ClassLoader for plugins, to allow shared classes across multiple plugins
 */
public class PluginClassLoader extends URLClassLoader {
    private final JavaPluginLoader loader;
    private final Map<String, Class<?>> classes = new HashMap<String, Class<?>>();

    public PluginClassLoader(final JavaPluginLoader loader, final URL[] urls, final ClassLoader parent) {
        super(urls, parent);

        this.loader = loader;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> result = classes.get(name);

        if (result == null) {
            result = loader.getClassByName(name);

            if (result == null) {
                result = super.findClass(name);

                if (result != null) {
                    loader.setClass(name, result);
                }
            }

            classes.put(name, result);
        }

        return result;
    }

    /**
     * Get a set of classes loaded by this loader
     *
     * Used by JavaPluginLoader to clean up the cache once the plugin that
     * uses this ClassLoader is disabled.
     *
     * @return A set of classes
     */
    public Set<String> getClasses() {
        return classes.keySet();
    }
}
