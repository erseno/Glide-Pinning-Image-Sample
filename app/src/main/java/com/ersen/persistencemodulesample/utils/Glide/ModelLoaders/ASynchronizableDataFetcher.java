package com.ersen.persistencemodulesample.utils.Glide.ModelLoaders;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
 
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
 
import java.io.InputStream;
 
 
/**
 * Created by François on 29/10/14.
 * {@link com.bumptech.glide.load.data.DataFetcher} implementation that allows to implement the
 * following flow :
 * <ol>
 * <li>check in RAM LRU</li>
 * <li>check in disk LRU</li>
 * <li>check in our synchro module, in case the image belongs to a synchronized media</li>
 * <li>fetch from our API throughout a network connection</li>
 * </ol>
 *
 * @see ASynchronizableImageLoader
 */
public abstract class ASynchronizableDataFetcher<Model> implements DataFetcher<InputStream> {
 
 
    @Nullable
    private final DataFetcher<InputStream> networkFetcher;
 
    @NonNull
    private final Model model;
 
    private final int width;
 
    private final int height;
 
    public ASynchronizableDataFetcher(@Nullable DataFetcher<InputStream> networkFetcher,
                                      @NonNull Model model,
                                      int width,
                                      int height) {
        this.networkFetcher = networkFetcher;
        this.model = model;
        this.width = width;
        this.height = height;
    }
 
    @Override
    public InputStream loadData(Priority priority) throws Exception {
        InputStream result = loadFromSynchro(model, width, height);
 
        if (result == null && networkFetcher != null) {
                // you can eventually also add another condition allowing to force the Fetcher to do not use the networkFetcher
                // -> that way you can provide an 'offline mode' in your app in order to let the user economize data.
                result = networkFetcher.loadData(priority);
        }
 
        return result;
    }
 
    @Nullable
    public abstract InputStream loadFromSynchro(@NonNull Model model, int width, int height);
 
 
    @Override
    public void cleanup() {
        if (networkFetcher != null) networkFetcher.cleanup();
    }
 
    @Override
    public void cancel() {
        if (networkFetcher != null) networkFetcher.cancel();
    }
 
}
