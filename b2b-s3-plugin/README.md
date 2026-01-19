## AWS S3 plugin with Pickup, and Delivery capabilities

* Download PluggableTransport [here](https://github.com/Deviani/axway-b2bi-plugins/blob/master/b2b-s3-plugin/distributions/b2b-s3.zip)
* Extract the contents of the zip file to {B2BI_SHARED_HOME}/local_te/jars on each of the B2Bi cluster nodes.  All needed referenced libraries are included in the archive. Make sure you keep the sub-folder structure for the referenced libraries.

* This project is depending on a number of 3rd party libraries which have been included in the zip file
* Update the {B2BI_SHARED_HOME}/local_te/pluggable/pluggabletransports.xml configuration file with s3-pluggabletransports.xml
* NOTE: A constant setting with the name 'Exchange type' is used to determine whether the plugin is used for Pickup/Delivery.
* Restart Trading Engine on all the B2Bi nodes
* Source can be viewed [here](https://github.com/Deviani/axway-b2bi-plugins/tree/master/b2b-s3-plugin) 
* If you are building on your local developer environment, add a lib folder to the project and include interchange-server.jar and other relevant dependencies. 
* This connector has been developed for B2Bi 2.6.0 and higher


## Documentation

https://docs.google.com/document/d/1KrbAE8zcWEgZrpavlOQly0C5QJRZdZPUG1VemfIpZFU/edit?usp=sharing


## Sample screenshots

#### Sample Application Pickup Configuration
![Sample Application Pickup Configuration](distributions/screenshots/B2Bi_S3_ApplicationPickup.jpg)


#### Sample Application Delivery Configuration
![Sample Application Delivery Configuration](distributions/screenshots/B2Bi_S3_ApplicationDelivery.jpg)
