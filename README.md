# IFCtoB4R-DM_OpenAPI
Version 1.14

An Restful OpenAPI implementation that converts an IFC Step file into Linked Building Data RDF defined in the B4R-DM ontology.
The output is compatible with Building Topology Ontology defined in https://w3c-lbd-cg.github.io/bot/. 


OpenAPI interface of the IFC-B4R DM converter accepts an IFC STEP formatted file as an input.
HTTP POST protocol is used where two content-types for input data are accepted.
They are plain text-based application/ifc that was defined in BIM4Ren WP2 D2.2.,
and MULTIPART_FORM_DATA Multimedia Internet Message Extensions (MIME).

The proposed IFC-B4R DM converter can handle IFC files from all actively used IFC schemas,
from IFC2x3 TC1 till IFC4x3. The converter first uses the existing IFC-to-RDF converter
internally to transform the original IFC file to a temporary ifcOWL Abox graph
according to the ifcOWL ontology.

When the intermediate ifcOWL Abox graph is ready, the converter iteratively creates new LBD nodes
into a separate RDF graph starting from the ifcOWL nodes that are of RDF types referred at
the Building Topology Ontology ifcOWL Alignment specification . The converter follows
the controlled graph traversal using Java coded RDF path templates: it begins from
the IfcSite instance node and traverses the ifcOWL graph towards its IfcBuilding instance
nodes, while at the same time looking for the IFC properties of the IfcSite instance.
In a third phase, it queries all instances of IfcBuildingStorey of the previously
found IfcBuilding cases, together with the properties of these same IfcBuilding instances.
This process is continued until all IFC building elements, and their properties are found.

The converter also adds the SMLS unit types of measurement in property set values when
the unit is known in the IFC model. The converter infers these in three phases. In
the first phase, the ifcowl:IfcSIUnit statements are read from the ifcOWL model and
collected in an internal data map. In the next phase, the property sets are read, and
the nominalValue_IfcPropertySingleValue and rdf:type are saved. There are specialized
RDF paths for ifcowl:hasString, ifcowl:hasDouble, ifcowl:hasInteger, ifcowl:hasBoolean,
and ifcowl:hasLogical. All other data types are copied as they are in ifcOWL.
In the last phase, when the property is referred in an element, and a corresponding
LBD graph is created, the unit type is added using the smls:unit property and the Base
Units Ontology Version 2.0 - Qudt values unit:M for metre, unit:M2 for square metre,
unit:M3 for cubic metre, and unit:RAD for Radian.

Since IFC models tend to be huge and using the Apache Jena RDF engine is memory intensive
the converter saves memory in the following manner: IFC-to-RDF converter creates the
ifcOWL transformation line based creating RDF triples in a streaming way. When the phase
is finished, the output is given as a file for the ifcOWL – B4R-DM conversion phase.
The file is read in as Turtle FLAT file triple by triple and as the ifcOWL standard is defined
so that the property names also contain the inherited class of the element that is used to
filter geometry and IfcOwnerHistory components from the graph. Because the B4R-DM model
is an order of magnitude smaller than the ifcOWL Abox model, the reduced memory consumption
is notable. When the standard IFC-to-RDF translator is used, it is given its output in Turtle
Blocks format. The converter transforms that into Turtle flat in stream base to allow easy filtering.

In case there would be a need to downstream the modifications that are made into the B4R-DM model
back to ifcOWL or to access the data contained in the ifcOWL model from the B4R-DM graph, the
corresponding elements in the models are connected using owl:sameAS statements. The converter adds
them when both models are co-existing in the memory, and a new Linked Building Data RDF resource
is created from the ifcOWL counterpart.

Furthermore, the bounding boxes for the elements are given. For this, the ifcOpenShell geometry
engine is used. Of the x,y, and z 3D co-ordinate values minimum, and maximum values are calculated
for each element and they are attached to the associated IFC elements in the B4R-DM graph using
the geo:hasGeometry and geo:asWKT triples of GeoSPARQL 1.0 . The bounding box is represented as
the Well-Known Text format Multipoint.

## A test installation:
http://lbd.arch.rwth-aachen.de/IFCtoB4R_OpenAPI/apidocs


## Swagger.json description for the services

http://lbd.arch.rwth-aachen.de/IFCtoB4R_OpenAPI/apidocs/ui/swagger.json

## Changes
- IFCtoLBD was added. BOT+SMLS has a separate REST interface 
- BIMserver integration is functional. Some beta testing is still needed.

## How to cite
```
@software{jyrki_oraskari_2020_4046603,
  author       = {Jyrki Oraskari},
  title        = {{jyrkioraskari/IFCtoB4R-DM\_OpenAPI: Restful API for 
                   IFC - B4R-DM version 1.12}},
  month        = sep,
  year         = 2020,
  publisher    = {Zenodo},
  version      = {1.14},
  doi          = {10.5281/zenodo.4046603},
  url          = {https://doi.org/10.5281/zenodo.4046603}
}
```

## Acknowledgements
This work has been written in RWTH-Aachen. The research was funded by the EU through 
the H2020 project BIM4REN.

https://dc.rwth-aachen.de/de/forschung/bim4ren

