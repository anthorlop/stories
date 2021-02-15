package com.anthorlop.stories_test.application

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.multidex.MultiDexApplication
import androidx.viewpager2.widget.ViewPager2
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import es.anthorlop.stories.StoriesManager
import es.anthorlop.stories.interfaces.ImageLoaderInterface
import es.anthorlop.stories.interfaces.StoriesInterface
import kotlin.math.abs


class StoriesApplication : MultiDexApplication() {

    override fun toString(): String {
        return "StoriesApplication.kt"
    }

    override fun onCreate() {
        super.onCreate()

        initStoriesImpl()

    }

    private fun initStoriesImpl() {

        StoriesManager.getInstance().setImageInterface(object : ImageLoaderInterface {
            override fun loadImage(context: Context, url: String, imageView: ImageView) {

                if (url.isEmpty()) return

                val picasso = Picasso.get().load(url)

                picasso.into(imageView, object : Callback {
                    override fun onSuccess() {

                    }

                    override fun onError(e: Exception) {

                    }
                })
            }
        })

        StoriesManager.getInstance().setstoriesInterface(object : StoriesInterface {
            override fun onShowMoreClicked(activity: Activity, idStory: Int, nameStory: String, storyType: String,
                                           idScene: Int, link: String) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(link)
                activity.startActivity(intent)

                Log.d(toString(), " > > > onShowMoreClicked: story = $idStory, scene = $idScene")
            }

            override fun onAvatarClicked(position: Int, id: Int, name: String, storyType: String) {
                // enviar analitica
                Log.d(toString(), " > onAvatarClicked: position = $position, story = $id")
            }

            override fun onStoryDetailStarted(id: Int, name: String, type: String) {
                // enviar analitica
                Log.d(toString(), " > > onStoryDetailStarted:  story = $id")
            }

            override fun onSceneDetailStarted(id: Int, idStory: Int, nameStory: String, storyType: String) {
                // enviar analitica
                Log.d(toString(), " > > > onSceneDetailStarted:  scene = $id")
            }

            override fun onStoriesDetailClosed(fromUser: Boolean) {
                // enviar analitica
                Log.d(toString(), " > onStoriesDetailClosed: fromUser = $fromUser")
            }

            override fun getViewPagerTransformer(): ViewPager2.PageTransformer {
                return ZoomOutPageTransformer()
            }
        })
    }

    companion object {
        private const val MIN_SCALE = 0.85f
        private const val MIN_ALPHA = 0.5f
    }

    internal class ZoomOutPageTransformer : ViewPager2.PageTransformer {

        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }
                    position <= 1 -> { // [-1,1]
                        // Modify the default slide transition to shrink the page as well
                        val scaleFactor = MIN_SCALE.coerceAtLeast(1 - abs(position))
                        val verticalMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzontalMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzontalMargin - verticalMargin / 2
                        } else {
                            horzontalMargin + verticalMargin / 2
                        }

                        // Scale the page down (between MIN_SCALE and 1)
                        scaleX = scaleFactor
                        scaleY = scaleFactor

                        // Fade the page relative to its size.
                        alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }
                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }
    }
}