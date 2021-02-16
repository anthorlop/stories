# Stories Lib

## 1. Add repository and dependency.
```
    repositories {
        maven { url 'https://github.com/anthorlop/mvn-android/raw/master/' }
    }
....
    repositories {
        maven { url 'https://github.com/anthorlop/mvn-android/raw/master/' }
    }
```


## 2. Interfaces implementation in your Application class (onCreate method).
```
   // Image Loader Interface Implementation
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
        
    // StoriesInterface -> Catch events and set custom ViewPagerTransformer
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
            return null
            // return ZoomOutPageTransformer()
        }
    })
```

## 3. Build your data.

You have to create an array of avatars and a stories one. (See MainActivity.kt)


## 4. Get View Bar and add it to your container.
```
    findViewById<FrameLayout>(R.id.stories_bar_container).addView(
                StoriesManager.getInstance().getBarView(this, avatars, stories)
```


## 5. Refresh View Bar if you want to update the viewed stories.
```
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UE_STORIES_ACTIVITY_REQUEST_CODE) {
            val container = findViewById<FrameLayout>(R.id.stories_bar_container)
            StoriesManager.Companion.getInstance().refreshBarView(container)
        }
    }
```

![](screen_storie_sample.gif)
