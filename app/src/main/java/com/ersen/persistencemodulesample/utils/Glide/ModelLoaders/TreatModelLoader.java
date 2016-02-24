package com.ersen.persistencemodulesample.utils.Glide.ModelLoaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.ersen.persistencemodulesample.models.Treat;

import java.io.InputStream;


/**
 * {@link com.bumptech.glide.load.model.ModelLoader} for {@link Treat}.
 * Get urls from {@link Treat#getImageUrl()}  and  creates a
 * {@link TreatDataFetcher} for each request.
 *
 * @author francois
 */
public class TreatModelLoader extends ASynchronizableImageLoader<Treat> {


    public TreatModelLoader(Context context) {
        super(context);
    }

    public static class Factory implements ModelLoaderFactory<Treat, InputStream> {

        @Override
        public ModelLoader<Treat, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new TreatModelLoader(context);
        }

        @Override
        public void teardown() {  }
    }

    @Nullable
    @Override
    protected String getUrl(@NonNull Treat model, int width, int height) {
        // this method is responsible for providing an url associated with the model.
        // this has several advantages :
        // -> when we call Glide.with(ctx).load(treat), we don't have to  worry about how the url will be
        // built
        // -> if we want to work with an API which handles buckets (so we can choose between multiple size for each resource,
        // it is very easy to do so from this method
        return model.getImageUrl();
    }


    @Override
    public ASynchronizableDataFetcher<Treat> getFetcher(DataFetcher<InputStream> defaultFetcher,
                                                        @NonNull Treat treat,
                                                        int width,
                                                        int height) {
        return new TreatDataFetcher(defaultFetcher, treat, width, height);
    }
}
