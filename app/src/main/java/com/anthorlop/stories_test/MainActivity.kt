package com.anthorlop.stories_test

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import es.anthorlop.stories.StoriesManager
import es.anthorlop.stories.StoriesManager.Companion.UE_STORIES_ACTIVITY_REQUEST_CODE
import es.anthorlop.stories.datatype.Avatar
import es.anthorlop.stories.datatype.Scene
import es.anthorlop.stories.datatype.Story

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (StoriesManager.getInstance().isInitialized()) {

            StoriesManager.getInstance().set("http://lombrinus.com", 5, 5)

            val stories: ArrayList<Story> = arrayListOf()
            val avatars: ArrayList<Avatar> = arrayListOf()
            val campaigns: ArrayList<Story>? = null

            for (i in 0..5) {

                val idAvatar = i + 1

                avatars.add(
                    Avatar(
                        idAvatar,
                        "# S$idAvatar",
                        false,
                        "Test",
                        "http://lombrinus.com/pruebas/images/android-os.png",
                            "http://lombrinus.com/pruebas/images/android-img-1.png"
                    )
                )

                val scenes: ArrayList<Scene> = arrayListOf()
                for (n in 0..2) {
                    val idScene = n + 1
                    scenes.add(
                        Scene(
                            n, idScene, "http://lombrinus.com/pruebas/images/android-img-$idScene.png",
                            "http://lombrinus.com", true
                        )
                    )
                }

                stories.add(
                    Story(
                        0, idAvatar, "Story $idAvatar", "Test",
                        "http://lombrinus.com/pruebas/images/android-os.png",
                        false, scenes
                    )
                )
            }

            findViewById<FrameLayout>(R.id.stories_bar_container).addView(
                StoriesManager.getInstance().getBarView(this, avatars, stories)
            // StoriesManager.getInstance().getBarViewModePreview(this, avatars, stories)
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UE_STORIES_ACTIVITY_REQUEST_CODE) {
            val container = findViewById<FrameLayout>(R.id.stories_bar_container)
            StoriesManager.Companion.getInstance().refreshBarView(container)
        }
    }
}