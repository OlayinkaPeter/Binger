# Binger

Binger is an app that allows its users to view popular **tv shows** and keep track of their favorite **tv shows**. 
The user will be able to view tv shows from [TMDB](https://www.themoviedb.org/) and select which ones are *favorites* to be kept track of.

## Features
Here is a list of features that the app would have.

- Show Airing Today: the user should be able to view a list of tv show airing a particular day
- Popular Tv Shows: the user should be able to view the most popular tv shows
- Tv Show's Details: the user should be able to view a detailed information about any particular tv show
- Favorite Tv Shows: the user should be able to view his selected favorite tv shows.
- Syncronizing updates: the app should synchronize with [TMDB](https://www.themoviedb.org/) server for updates on latest episodes & seasons


## Project Structure
Here is the project default project structure that you are to work with

* __Activities__
    * Main Activity - This is the entry point of the application, it shows a viewPager of tv shows (both: popular tv shows & tv shows airing today)
    * Details Activity - This activity is responsible for showing the details of the a particular tv show
    * Favorite Activity - This activity is responsible for showing the users favorite tv show, that is stored on the device

* __Adapters__
    * Tv Show Adapter - This is a RecyclerView Adapter responsible for rendering a list of tv shows
    * Tv Show PagerAdapter - This is a Pager Adapter responsible for the tabbed navigation of view pager (used for Popular tv Shows & Tv Shows Airing Today)

* __Fragments__
    * AiringToday Fragment - This is a fragment responsible for displaying list of tv shows airing in a particular day
    * PopularShows Fragment - This is a fragment responsible for displaying list of popular tv shows

* __Receivers__
    * BootReceiver - This is a broadcast receiver responsible for triggering favorite shows synchronization with [TMDB](https://www.themoviedb.org/) on device boot complete

* __Services__
    * Synchronize Service - This is an Intent Service responsible for performing favorite shows synchronization with [TMDB](https://www.themoviedb.org/) 

* __Data__
    * AppDatabase - This is a [RoomDatabase]() responsible for local storage of *favorite tv shows*.
    * ResponseModels - Data model objects, that model json responses from [TMDB](https://www.themoviedb.org/) server
    * FavoriteShow - Data model object for local storage of favorite tv shows

* __Utils__
    * Constants - File containing constants for app configuration like *api_key* and *base_url*
    * DisplayUtils - File containing helper methods for ui components

    
## Instructions
Clone the repository, build and run the application on an emulator or physical device. What you will see is a basic project structure containing the folder and files listed above. You will need to provide a solution to the *requirements* listed below. You will be using the free tvShow database [TMDB](https://www.themoviedb.org/), you can checkout their [api documentation](https://www.themoviedb.org/documentation/api) to see how to interact with thier rest endpoint. As it is just a simple structure containing the UI components and data models, you will have to setup the internals of the project like the network and persistence layer, you are to use the provided data models for this.

The project is well commented describing functionality of each code block, and a couple of *TODOs* for you to follow. You are allowed to use what ever architecture pattern you prefer (MVVM, MVC, MVP), to provide a solution to the requirements listed below.

All basic libraries needed to provide a solution have been added, but you can add any other external libraries you need. 
Here is a list of dependencies
- [Glide v4](https://bumptech.github.io/glide/)
- [Retrofit v2](https://square.github.io/retrofit/)
- [RxJava v2](https://github.com/ReactiveX/RxJava)
- [Dagger v2](https://github.com/google/dagger)
- [Room Presistence Library](https://developer.android.com/topic/libraries/architecture/room)

## Requirements (Compulsory)
- __Popular Shows__ - Display a list of popular tv shows using the following UI components; PopularShowFragment & TvShowAdapter
- __Display Today's Shows__ - Display a list of tv shows running on the current day using the following UI components: AiringTodayFragment & TvShowAdapter
- __Display Details for Tv Show__ - Display the details for a selected tv show from either *Today's or Popular* tv shows' list. Show Details when a tv show is clicked, using the DetailsActivity
- __Select Favorite Tv Show__ - Add a tv show to favorites list in local db, when the *heart icon* is clicked on a tv show. Using the following components: AppDatabase, FavoriteTvShowDao, FavoriteTvShow
- __View Favorite Tv Show__ - When the *Favorite* menu option from *MainActivity* is clicked, the list of selected fovrite tv shows should be displayed, using the following components: FavoriteActivity, TvShowAdapter, FavoriteTvShowDao (Note: user should also be able to view details when a tv show is clicked)
- __Synchronize Favorite Shows__ Using the list of shows that have been added to favorites, use the intent service to get latest information on the shows. This service should run on two occasions, first, it should be executed every day, a scheduler could be used to schedule the action. secondly, it should be executed on boot, i.e. when the device is turned on, using the BootReceiver.
- __Navigation Requirements__ - The navigation for the app should be setup like so: user should be able to go forward and back to calling activity as shown below.
    MainActivity
        <-> DetailsActivity 
        <-> FavoriteActivity <-> DetailActivity

## Addition Requirements (*Not compulsory, but is a plus)
- Implement Dependency Injection
- Write Unit Tests
- Write Behavioural UI tests
