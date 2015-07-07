# External Configuration Store

The majority of application runtime environments include configuration information that is held in files which are deployed with the application. This limits the configuration to a single instance of the application, whereas in some scenarios it would be useful to share configuration settings across multiple applications.

Managing changes to local configurations across multiple instances of the application may be challenging and may result in instances using different configuration settings while the update is being deployed.

The solution is to store the configuration information in external storage and provide an interface that can be used to quickly and efficiently read and update configuration settings.

## See also

* See Managed Domain in [JBoss operating modes](https://docs.jboss.org/author/display/AS71/Operating+modes).

