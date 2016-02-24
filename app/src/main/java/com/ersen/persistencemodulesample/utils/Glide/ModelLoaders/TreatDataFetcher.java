package com.ersen.persistencemodulesample.utils.Glide.ModelLoaders;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.data.DataFetcher;
import com.ersen.persistencemodulesample.models.Treat;

import java.io.InputStream;

import static com.ersen.persistencemodulesample.utils.FileAndStorageUtils.getInputStream;

/**
 * @author francois
 */
public class TreatDataFetcher extends ASynchronizableDataFetcher<Treat> {

    private final String id;


    public TreatDataFetcher(DataFetcher<InputStream> defaultFetcher,
                            @NonNull Treat treat,
                            int width,
                            int height) {
        super(defaultFetcher, treat, width, height);
        id = treat.getImageUrl();
    }


    @Nullable
    @Override
    public InputStream loadFromSynchro(@NonNull Treat treat, int width, int height) {
        // the only responsability of loadFromSynchro is to make a bridge with our synchronization
        // module.
        // if the resource is available there, send it back as an InputStream
        // Otherwise, just return null and the networkFetcher will try to get the resource
        return getInputStream(treat);
    }

    @Override
    public String getId() {
        return id;
    }

}



