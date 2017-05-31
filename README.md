# avoCADo
avoCADo is a 3D design and modeling program (CAD) which aims to be a collaborative tool with an intuitive user interface for both engineers and artists alike, with community plugins.

## Roadmap
Hello, I am Jacob Gustafson. I have a good understanding of principles of binary and ASCII 3D mesh formats, and OpenGL. I took over this project primarily to get the program running on newer computers and to encourage the community to help me make a production-ready feature set. However, I took over this project in 2017 after it was discontinued in 2012, so I will have to get familiar with the code before adding new features myself. Please be advised of the following roadmap (especially that the amount of detail in it directly reveals my level of familiarity with the project) before making requests or demands, however you are free to fork the project, add the features and fixes you want, then submit a pull request.

The discontinued 2012 sourceforge master is considered to be version 8.3.0 under the new versioning scheme (Semantic Versioning 2.0).

### Planned for version 8.3.1
* Binary release that runs on Java 1.8 (especially Windows 10 and various GNU/Linux distros)

### Planned for version 8.3.2
* Import all information from "Old info to import into GitHub" below, and determine better roadmap based on that information.

## Overview

avoCADo (often written avoCADo-CAD to distinguish it from the fruit) is an Open Source 3D CAD design/modeling program written under the GPL. It is an attempt to offer a real alternative to commercial mechanical CAD and 3D modeling software. Many Open Source CAD programs have been developed over the years, but all seem to fall short in some form or another. By starting from a solid foundation and allowing for rapid expansion of both functionality and collaboration via plugins, avoCADo aims to be an indispensable tool with an intuitive interface for both engineers and artists alike.

The key elements of avoCADo will be:
* Simple 3D object design environment
* Expandable plugin framework (tools,conversion,elements,materials,etc.)
* Collaboration and sharing with others
* Integrated part library support
* XML based open file formats
* Careful attention to usability
* Minimal restrictions to creative thinking

Like any good Open Source project, there will also be a very big emphasis on community. 

## Known Issues
* see also "Old info to import into GitHub" below
* rename project from avoCADo (MANY projects are called avocado)
* Contributors.txt should be eliminated or people at bottom of this file should be moved to it
* final binary release of original sourceforge master doesn't work on Windows 10 due to changes in Java (missing library when using paths that are specified in avoCADo.bat)

### Sites that should be updated
* https://www.openhub.net/p/avoCADo-CAD
* https://sourceforge.net/projects/avocado-cad/
* http://avocado-cad.sourceforge.net/
* https://sourceforge.net/p/avocado-cad/discussion/656395/

### Old info to import into GitHub
* https://sourceforge.net/p/avocado-cad/wiki/Home/
* https://sourceforge.net/p/avocado-cad/bugs/ (including CLOSED)
* https://sourceforge.net/p/avocado-cad/feature-requests/ (including CLOSED)
* https://sourceforge.net/p/avocado-cad/discussion/656395/
* https://sourceforge.net/p/avocado-cad/mailman/avocado-cad-announce/
* All pages of website http://avocado-cad.sourceforge.net/

## Changes specific to forking
(2017-05-31)
* added this file (README.md)
* corrected spelling of contributors in filename and contents of Contributors.txt
* renamed "GNU GPLv2.txt" to LICENSE.txt
* Changed to Semantic Versioning 2.0 ("Additional labels for pre-release and build metadata are available as extensions to the MAJOR.MINOR.PATCH format.") as per [http://semver.org/](semver.org)
  * MAJOR version when you make incompatible API changes,
  * MINOR version when you add functionality in a backwards-compatible manner, and
  * PATCH version when you make backwards-compatible bug fixes.



## Pre-fork Research

### Possible alternate names
* NaturalCAD
* PerseaCAD (from Persea americana, genus & species of avocado; not to be confused with the avocado-persea by cha0s on GitHub which is a resource editor and staging framework for avocado game development framework by cha0s)

### Authors of avoCADo original master on sourceforge
Usernames and full names below are from sourceforge.net.
Additional info such as email addresses are via Google and GitHub.
(all notified of fork if found some type of contact info)

#### (original admin on original sourceforge master)
Adam Kumpf
https://github.com/akumpf

#### cstroe
Cosmin Stroe
https://github.com/cstroe

#### danielemusiani
(no contact info)

#### przcvkdn
Andy Truong
https://github.com/a7truong

#### ndsherman
(no contact info)