# Glide-Pinning-Image-Sample
This is a sample to demonstrate a practical example of this https://github.com/Teovald/glide-wiki/blob/master/Caching-and-Cache-Invalidation.md#dialog-with-another-persistence-layer

# Read me first
As of 6 Jan 2016, this sample does not yet demonstrate the core topic of how to Dialog with another persistence layer using Glide as I am still in process of finding out myself :)
I made this sample as a starting point so others can look at it and possibly help me out with it!

# Scenario 
For your mobile app, you are required to store media content on the device to enable offline access to content. 
The content is some images of treats.
The backend provides you with a zip file that contains all the media content to be downloaded and it is your job to download this file, unzip it and store it on the device external storage.

# Aim
The aim of this sample is to show a practical example of the information stated in this link. https://github.com/Teovald/glide-wiki/blob/master/Caching-and-Cache-Invalidation.md#dialog-with-another-persistence-layer

# Code Explanation
The app displays a list of treats. Each treat has a name, image URL and a absolute file path (set later) to where this image is stored on disk.

There is a button at the bottom of the screen which when pressed starts an async task that downloads a zip file hosted by my server which contains all the images used in the app. 
Each treat object has its absolute file path set to where the treat image is stored on disk and finally saved to shared preferences. 

The objective of the Glide Model Loader will be simple. If the String that stores the absolute file path is null or empty, it will proceed to use the network. If not, an input stream from the file will be used.
