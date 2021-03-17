# Introduction

[Open Street Map](https://wiki.openstreetmap.org/wiki/Main_Page) is a powerful
and useful open geographical data source that we can use in our simulation.

Open Street Map contains data about many features : roads, buildings, cycleways,
public transports, centers of interest and activities... A model could
potential use one or several of all those features.

SmartGov provides some utilities to pre-process and import OSM data directly in
our simulation.

# Preprocess data

## OSM file

This first thing that we need in as XML .osm file, that represents a region.

[Many options](https://wiki.openstreetmap.org/wiki/Downloading_data) exist to
download OSM data. However, [GEOFABRIK](http://download.geofabrik.de/) is an
useful website that allows you to download regularly updated .osm files by
regions, so that they are not too heavy to apply further processes.

For the purpose of this example, we will be working on the [Rhone-Alpes
region](http://download.geofabrik.de/europe/france/rhone-alpes.html).

When using GEOFABRIK, you need to download the ".osm.bz2" option to download
your data.

## Extract a zone

Because of the huge amount of data contained even at the region scale, the
first thing that we might want to do is to crop the downloaded file, at the
scale of a city for example.

In this context, [Osmosis](https://wiki.openstreetmap.org/wiki/Osmosis) is an
useful tool that will help us to crop the data and apply a first filter on OSM
items.

Start by checking the [Osmosis installation
section](https://wiki.openstreetmap.org/wiki/Osmosis/Installation), and install
it on your system.

From there, a single command line will extract the required data for us :

```
bzcat rhone-alpes-latest.osm.bz2 | osmosis --read-xml enableDateParsing=no file=- --bounding-box top=46.146 left=4.4 bottom=45.43 right=5.46 --tf accept-ways highway=* --used-node --write-xml lyon.osm
```
where :
- `rhone-alpes-latest.osm.bz2` is the file downloaded from GEOFABRIK
- `lyon.osm` is our output filtered data
- `top`, `left`, `bottom`, `right` represents the bounding box of the area we
	want to extract, in latitude / longitude
-  `--tf accept-ways highway=* --used-node` is a tag filter that allows us to
	clean our data before other processes : here, we only keep [OSM
	highways](https://wiki.openstreetmap.org/wiki/Highways) and nodes that
	belong to them. **Do not use this if you want to keep other features**, such
	as buildings. Other examples are available in the [Osmosis
	documentation](https://wiki.openstreetmap.org/wiki/Osmosis#Usage).

## Convert to JSON data

In order to improve interoperability and genericity among our simulation inputs
and outputs, our simulation doesn't directly take a .osm file as input, but
should instead "convert it" to JSON files (a *nodes* and a *ways* file), eventually after applying more
filtering.

### Nodes file 

The nodes file should have the following general form :
```json
[ {
	"id": "1",
	"tags": {
		"key": "value",
		...
		},
	"lat": 0.0,
	"lon": 0.0
	},
	...
]
```

### Ways file 

The ways file should have the following general form :
```json
[ {
  "id" : "1",
  "tags" : {
    "name" : "road name",
    "highway" : "highway type",
    "oneway" : "no/yes/-1/...",
	"other_tag": "other_value",
	...
  },
  "nodeRefs" : [ "1", "2", "3", ... ]
  },
  ...
]
```
- The `name` tag is optional but useful
- The `highway` tag should always be specified
- The `oneway` tag is optional : no value means that the road is not oneway.


Notice that those format are easily human readable (and editable), could be
easily imported in a web application for example, or being generated using
other methods.

### Conversion using the OsmParser

In order to convert our .osm file to those JSON files, a side project, called
the [SmartGovOsmParser](https://github.com/smartgov-liris/SmartGovOsmParser)
has been implemented.

