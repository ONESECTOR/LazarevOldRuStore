package com.owlylabs.platform.helpers.glide_svg_support

import android.content.Context
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.caverock.androidsvg.SVG
import java.io.InputStream

/** Module for the SVG sample app.  */
@GlideModule
class SvgModule : AppGlideModule() {
    override fun registerComponents(
        context: Context, glide: Glide, registry: Registry
    ) {
        registry
            .register(
                SVG::class.java,
                Drawable::class.java,
                SvgDrawableTranscoder(context)
            )
            .append(
                InputStream::class.java,
                SVG::class.java,
                SvgDecoder()
            )
    }

    // Disable manifest parsing to avoid adding similar modules twice.
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}