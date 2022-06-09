# Flixster


<img src="https://github.com/msadriddinov1994gmailcom/Flixster/blob/master/Images/Cover.jpg" width=3000>


## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview

### Description

Flixster is a movie browsing app to see a list of trending, currently playing and upcoming movies.

### App Evaluation

- **Category:** Browsing/Entertainment
- **Mobile:** This app would be primarily developed for mobile but would perhaps be just as viable on a computer, such as AMC or other similar apps. Functionality wouldn’t be limited to mobile devices, however mobile version could potentially have more features.
- **Story:**  displays a list of trending, currently playing and upcoming movies. User can change different screens to see trending, upcoming and currently playing movies at theatre.
- **Market:** all-age group
- **Habit:** This app could be used as often or unoften as the user wanted depending on movie watching habits, and what exactly they’re looking for.
- **Scope:** First the app sends a request to MOVIE Database API and handle json response. Then app outlines list of movies. According to user's input, the user can see a list of trending, currently playing and upcoming movies.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories** 

- [x] User can view a list of movies (title, poster image, and overview) currently playing in theaters from the Movie Database API.
   - [x] Making an API request.
   - [x] Parsing JSON.
   - [x] Implementating RecyclerView.
   - [x] Using the AsyncHttpClient and the Glide libraries to handle newtwork requests and displaying movie poster images.
- [x] Expose details of movie (ratings using RatingBar, popularity, and synopsis) in a separate activity.
   - [x]  Assignment Intro - Activities & Intents.
   - [x]  Details Screen Implementation.
- [x] Allow video posts to be played in full-screen using the YouTubePlayerView.
   - [x] YoutubePlayerView.
   - [x] See the videos API for video information.
   - [x] If your Android device or emulator is running on API 30+, you will likely see an error with the Youtube initialization.
   
**Optional Nice-to-have Stories**

* Views should be responsive for both landscape/portrait mode.
   * In portrait mode, the poster image, title, and movie overview is shown.
   * In landscape mode, the rotated alternate layout should use the backdrop image instead and show the title and movie overview to the right of it.
* Display a nice default placeholder graphic for each image during loading.
* Improve the user interface through styling and coloring.
* For popular movies (i.e. a movie voted for more than 5 stars), the full backdrop image is displayed. 
* Otherwise, a poster image, the movie title, and overview is listed. 
* Use Heterogenous RecyclerViews and use different ViewHolder layout files for popular movies and less popular ones.

### 2. Screen Archetypes

* SplashActivity screen - A very brief into is displayed.
* Now_playing screen - User can see a list of movies which are currently being played at theatre.
* Trending screen - User can see a list of movies which are trending at theatre.
* Upcoming screen  - User can see a list of movies which are upcoming at theatre.

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Now_playing screen
* Trending screen
* Upcoming screen

**Flow Navigation** (Screen to Screen)

* Forced SplashActivity -> displays a very brief intoduction with app logo.
* Now_playing screen - sends a request to server to send back a list of movies which are currently being played at theatre.
* Trending screen - sends a request to server to send back a list of movies which are trending at theatre.
* Upcoming screen  - sends a request to server to send back a list of movies which are upcoming at theatre.

## Wireframes
<img src="https://github.com/msadriddinov1994gmailcom/Flixster/blob/master/Images/Diagram.png" width=600>

### [BONUS] Digital Wireframes & Mockups
<img src="https://github.com/msadriddinov1994gmailcom/Flixster/blob/master/Images/Screen%201%20-%20Splash.jpg" width=300>   <img src="https://github.com/msadriddinov1994gmailcom/Flixster/blob/master/Images/Screen%202%20-%20Main%20Activity.jpg" width=300>   <img src="https://github.com/msadriddinov1994gmailcom/Flixster/blob/master/Images/Screen%203%20-%20Details.jpg" width=300>


### [BONUS] Interactive Prototype
<img src="https://github.com/msadriddinov1994gmailcom/Flixster/blob/master/Images/Flixster%20Prototype.gif" width=300>

## Schema 

### Models
#### GET


   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | movieId       | Integer  | unique id for each movie (default field) |
   | voteAverage   | Double   | rating of item rated by watchers |
   | posterPath    | String   | path for movie poster images |
   | title         | String   | movie's title |
   | overview      | String   | overview of movie |

### Networking
#### List of network requests by screen
   - Now_playing Screen
      - (Read/GET) Query all movies which are currently played at theatre.
         ```kotlin
         val client = AsyncHttpClient()
         client.get(NOW_PLAYING, object : JsonHttpResponseHandler(){
            override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure.")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                try {
                    Log.i(TAG, "onSuccess. $statusCode")
                    val movieJsonArray = json.jsonObject.getJSONArray("results")
                    movies.addAll(Movie.fromJsonArray(movieJsonArray))
                    movieAdapter.notifyDataSetChanged()
                }
                catch (e: JSONException) { Log.e(TAG, "Encountered exception $e.") }
            }

        })
         ```
      - (Read/GET) Query all movies which are trending at theatre.
      - (Read/GET) Query all movies which are upcoming at theatre.

