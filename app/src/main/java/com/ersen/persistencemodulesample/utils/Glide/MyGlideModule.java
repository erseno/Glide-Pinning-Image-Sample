package com.ersen.persistencemodulesample.utils.Glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.GlideModule;
import com.ersen.persistencemodulesample.models.Treat;
import com.ersen.persistencemodulesample.utils.Glide.ModelLoaders.TreatModelLoader;

import java.io.InputStream;

public class MyGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // let's register an additional class into glide :
        // we tell glide that we want to load Treat objects, as InputStream resources and provide a
        // factory that will create the associated ModelLoader (which does the loading job)
        glide.register(Treat.class, InputStream.class, new TreatModelLoader.Factory());
    }
}
