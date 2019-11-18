# Eindhoven city experience

school project executed during the smart-mobile minor (semester 7).

Eindhoven city experience is an app that allows you to experience the city center of Eindhoven in an interactive way.
You go on your own and choose a tour / walk that you want to do in the app based on the details of a tour such as the number of kilometers, number of locations and the possibility to have a drink in a cafe.
When the tour has started, the user is guided past the various locations through a map view where the locations are indicated. When the user arrives at a specific location, the relevant story becomes available and the user can view and listen to video clips, audio clips, photos and AR animations.

Modules
-------

Project contains the following modules:

* `app` : phone application presentation module(presenter, views, navigator), resources etc.
* `domain` : use cases that do a specific task.
* `data` : all data endpoints(local, remote).


Most important libraries
-------

* Dagger2
* rxjava2
* Glide
* Airbnb Lottie
* Room
* Firebase(analytics, crashlytics, maps sdk)
* Material

Structure
-------

* Model–view–presenter (MVP)
* repository pattern
* Clean architecture


Contentful
-------

Contentful is a content management system (CMS) where all tour data, story data, assets are stored.
This data is synchronized with the Eindhoven city expierence app and stores this data offline.
This allows you to easily add and edit new tours later.

Useful links
-------
* [portfolio](http://i369674.hera.fhict.nl/eindhoven_city_experience.html)
* [design](https://sketch.cloud/s/3EKDD)
* [scrum board](https://bramko.myjetbrains.com/youtrack/agiles/104-3/105-11)


