# Zefiro

Zefiro is an Alfresco addon, oriented to business productivity.

Zefiro is an interface to Alfresco, alternative to Alfresco Share. 

While Alfresco Share is focused on team collaboration, Zefiro is focused on document types and their relationships.
It provides a modern interface oriented on the document content and its metadata, rather then all the collaboration/social feature that Alfresco Share provides.

Zefiro recognizes document types and relationships defined in Alfresco and without need for further development or configurations is able to manage:
* search for specific metadata of a document type
* advanced management of the types of metadata: search by intervals for dates or amounts, pull-down menu for metadata with a predetermined number of values
* sort search results by column heading
* export search results to excel
* preview of the document during the publication to facilitate the insert of metadata
* navigation between related documents 

## Compatibility

Zefiro is compatible with **Alfresco Community Edition 5.1**.

## Installation

Zefiro is JEE application packaged as WAR.

Zefiro needs JAVA 7.

## Configuration

Zefiro configuration is in WEB-inf/classes/jbrickConfig.xml file.

Configuration parameters:
* alfresco/@baseTypeId: base document type managed by Zefiro
* alfresco/@host: Alfresco Installation URL
* alfresco/@rootFolderId: Alfresco folder managed by Zefiro

For example you can change jbrickConfig.xml file with this entry for loading in Zefiro all your Alfresco Document
```
<alfresco baseTypeId="cmis:document" host="{add your alfresco url}" rootFolderId="{add your noderef folder id}"/>
```

Best practice suggests to create a custom content model with a general document type (ie. my:myDoc) and some derived document type child that could be your specific documents (ie. my:myInvoice, my:myCustomer, my:myOrder etc...)
If you do that your jbrickConfig.xml should be modified like this
```
<alfresco baseTypeId="my:myDoc" host="{add your alfresco url}" rootFolderId="{add your noderef folder id}"/>
```

![Image](http://i.imgur.com/wjE3BDp.png)


##License

Zefiro is is licensed under the terms of GNU AFFERO GENERAL PUBLIC LICENSE Version 3.

##Contribution

To contribute to Zefiro development you need to:
* download [Zefiro Individual Contributor Assignment Agreement](https://github.com/MakeITBologna/zefiro/blob/master/Zefiro-Individual-Contributor-Assignment-Agreement.pdf) or [Zefiro Entity Contributor Assignment Agreement](https://github.com/MakeITBologna/zefiro/blob/master/Zefiro-Entity-Contributor-Assignment-Agreement.pdf)
* sign the Agreement
* email the signed agreement to dev@make-it.it







