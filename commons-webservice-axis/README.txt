L'exemple est tiré de http://axis.apache.org/axis2/java/core/docs/userguide-buildingservices.html#deployrun
Le wsdl a été copié à partir de http://axis.apache.org/axis2/java/core/docs/userguide-codelisting5.html
sous le nom de Axis2UserGuideService.wsdl

Les commandes
 -  mvn generate-sources -PgenerateSkeleton : génération du skeleton
 -  mvn axis2-aar:aar : génération de l'archive .aar
 
Les méthodes ont été implémentées dans la classe Axis2UserGuideServiceSkeleton