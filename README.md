# Custom Listview populated from url json objects
Assignment purpose is to create the custom listview and the data is extracted from the API URL. This assignemnt has three Modules:


## A. Extracting JSON Objects from URL :
MainActivity: Convert the URL data into String which will be further used to extract the JSONArray from the URL string .

JSON array is processed to extract all the JSON objects one by one . 

Putting each JSON object in the arraylist of hashmap objects. where each object has a String based key and String based value

The image is extracted from the image url using the third party Glide.

activity_template.cml: We have to design a separate xml UI , which will act as the UI template of each of the listview item.

CustomAdapter:To create the custom listview , we have to create a custom adapter inheriting the properties and behaviour of ArrayAdapter.
In the getView() method we will extract each item from the arraylist and set it on the views of template UI.



## B. Filtering the items in the custom list on the basis of type of news:
To perform search operation , this project has options menu where the search option is shown on the actionbar with the icon.
First we extract the type value from each of the json object and the one , whose value is matching with the searched value,
we will put it into a new arraylist.and we will notify the customadapter about this change.


## C. Check the network connectivity
Add the permission of internet and network access change to the manifect file.
We will use the connectivity manager.
whenever there will be any network change, the will be broad casted to the applications.
The broadcast receiver is registered with this mainactivity ,it will get the info of network whether the app is connected to cellular, wifi or no internet.
it will return the status of the network to the broadcast receiver which is whoen on the alert dialog box.

I was here. 
