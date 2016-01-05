package com.ersen.persistencemodulesample.utils.Glide.ModelLoaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
 
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
 
import java.io.InputStream;
 
 
 
/**
 * A base class for loading an image associated with a  {@link Model} that can be present
 * in our synchronisation module.<br>
 * Coupled with the corresponding {@link com.bumptech.glide.load.data.DataFetcher},
 * this class allows to implement the following flow :<br>
 * <ol>
 * <li>check in RAM LRU </li>
 * <li>check in disk LRU </li>
 * <li>check in our synchro module, in case the image belongs to a synchronized media </li>
 * <li>fetch from our API, throughout the network connection</li>
 * </ol>
 * You can optionally override {@link ASynchronizableImageLoader#getModelCacheSize()}
 * in order to specify how many urls you want to cache.
 *
 * @param <Model> The type of the model
 */
public abstract class ASynchronizableImageLoader<Model> implements StreamModelLoader<Model> {
 
    private final static int DEFAULT_MODEL_CACHE_SIZE = 100;
 
    private final ModelLoader<GlideUrl, InputStream> mBaseLoader;
 
    @Nullable
    private ModelCache<Model, GlideUrl> modelCache;
 
 
    public ASynchronizableImageLoader(Context context) {
        mBaseLoader = Glide.buildModelLoader(GlideUrl.class, InputStream.class, context);
        if (getModelCacheSize() > 0) {
            modelCache = new ModelCache<>(getModelCacheSize());
        }
    }
 
    @Override
    @Nullable
    public final DataFetcher<InputStream> getResourceFetcher(final @Nullable Model model,
                                                             int width,
                                                             int height) {
        if (model == null) {
            return null;
        }
        GlideUrl glideUrl = null;
        if (modelCache != null) {
            glideUrl = modelCache.get(model, width, height);
        }
        if (glideUrl == null) {
            String url = getUrl(model, width, height);
 
            if (TextUtils.isEmpty(url)) {
                return null;
            }
            glideUrl = new GlideUrl(url);
            if (modelCache != null) {
                modelCache.put(model, width, height, glideUrl);
            }
        }
 
        final DataFetcher<InputStream> networkFetcher =
                mBaseLoader.getResourceFetcher(glideUrl, width, height);
 
        return getFetcher(networkFetcher, model, width, height);
    }
 
    /**
     * override this method if you want to modify the size of the modelCache
     *
     * @return modelCache size, the cache will be {@code null} if size >= 0 <br>
     * By default, modelCache contains the last
     * {@link ASynchronizableImageLoader#DEFAULT_MODEL_CACHE_SIZE}
     * = {@value ASynchronizableImageLoader#DEFAULT_MODEL_CACHE_SIZE} requests.
     */
    protected int getModelCacheSize() {
        return DEFAULT_MODEL_CACHE_SIZE;
    }
 
 
    /**
     * Implement this method in order to provide the Fetcher you want to use with this loader.<br>
     * You can return {@code null}, in this case the Loader will only rely on Glide caches.
     *
     * @param defaultFetcher the default fetcher to use as a fallback
     * @param model          the model to load
     * @param width          the target's width
     * @param height         the target's height
     * @return the fetcher to use or {@code null}.
     */
    @Nullable
    public abstract ASynchronizableDataFetcher<Model> getFetcher(DataFetcher<InputStream> defaultFetcher,
                                                                 @NonNull Model model,
                                                                 int width,
                                                                 int height);
 
 
    /**
     * Get a valid url http:// or https:// for the given model and dimensions as a string.
     *
     * @param model  The model.
     * @param width  The width in pixels of the view/target the image will be loaded into.
     * @param height The height in pixels of the view/target the image will be loaded into.
     * @return The String url or <code>null</code>
     */
    @Nullable
    protected abstract String getUrl(@NonNull Model model,
                                     int width,
                                     int height);
}
