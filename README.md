# LibreCSG
LibreCSG (fork of discontinued avoCADo) is a 3D design and modeling program (CAD) which aims to be a collaborative tool with an intuitive user interface for both engineers and artists alike, with community plugins. avoCADo is Open Source AND Free **Libre** Software since it is released under the [GNU GPL v2.1](https://github.com/avoCADo-3d/avoCADo/blob/master/LICENSE.txt).

## Overview

LibreCSG formerly avoCADo (often referenced as avoCADo-CAD to distinguish it from the fruit) is an Open Source 3D CAD design/modeling program released under the GPL. It is an attempt to offer a real alternative to commercial mechanical CAD and 3D modeling software. Many Open Source CAD programs have been developed over the years, but all seem to fall short in some form or another. By starting from a solid foundation and allowing for rapid expansion of both functionality and collaboration via plugins, avoCADo aims to be an indispensable tool with an intuitive interface for both engineers and artists alike.

The key elements of LibreCSG formerly avoCADo:
* Simple 3D object design environment
* Expandable plugin framework (tools, conversion, elements, materials, etc.)
* Collaboration and sharing with others
* Integrated part library support
* ASCII open file formats (such as XML)
* Careful attention to usability
* Minimal restrictions to creative thinking

Like any good Open Source project, there will also be a very big emphasis on community.

## Roadmap
I took over this project primarily to get the program running on newer computers and to encourage the community to help me make a production-ready feature set. However, I took over this project in 2017 after it was discontinued in 2012, so I will have to get familiar with the code before adding new features myself. Please be advised of the following roadmap (especially that the amount of detail in it directly reveals my level of familiarity with the project, which I didn't create) before submitting requests, bugs, or other issues. Until 0.8.5 is listed under releases, there is a development freeze since old issues aren't imported from SourceForge yet, so don't submit issues yet--until 0.8.5 is listed in releases tab at top of https://github.com/avoCADo-3d/avoCADo, you have to fix it yourself: find or become a Java developer, login to GitHub, fork the project, add the features and fixes you want, then submit a pull request. Rest assured that additional known issues will be copied from the old project issue tracker to this one. Thank you for your understanding.
    -poikilos

In task lists in this document, tasks are marked as complete by a date of completion in parenthesis before the task(s).

### 0.8.3
* The discontinued January 2012 sourceforge master is considered retrospectively as this version number under the new versioning scheme (Semantic Versioning).
* The latest version of Java that was released at this time was Java SE 6 Update 30 (2011-12-12) according to https://en.wikipedia.org/wiki/Java_version_history#Java_SE_6

### Planned for version 0.9.1
(2017-05-31)
* Import into GitHub, add README.md, clean up file naming and Contributors list
(ToDo)
* Binary release that runs on Java 1.8 (especially Windows 10 and various GNU/Linux distros)
* Bump version since plans to break compatibility with older Java versions

### Planned for version 0.9.2
* Import all information from "Old info to import into GitHub" below.
* Add priorities for this version to this list, based on that information.

## Changes
(2018-08-01)
* Rename to LibreCSG to avoid naming conflicts with many projects
* add maven project (for why, see http://maven.apache.org/background/
  philosophy-of-maven.html and https://jogamp.org/wiki/index.php/Maven).
  Formerly, there was no build process other than Eclipse project file
  which manually imports JOGL (jogamp) jars from a Windows path on "C:".
* Add Maven default .gitignore lines from github.com/github/gitignore
  to existing Java gitignore
* add to .gitignore: `.directory`  from Dolphin on KDE
* mv -f main/AvoCADo.java librecsg/src/main/java/org/poikilos/app/App.java
* rename class AvoCADo to App in App.java
* replace "package main" with "package org.poikilos.app"
* mv ui librecsg/src/main/java/org/poikilos/
* replace "package  backend" with "package org.poikilos.librecsg.backend"
* replace "package  ui" with "package org.poikilos.librecsg.ui"
* replace "import  ui" with "import org.poikilos.librecsg.ui"
* replace "import org.poikilos.librecsg.backend" with "import org.poikilos.librecsg.backend"
* add maven index lookup using Eclipse
  (see https://stackoverflow.com/questions/24252256/how-do-i-enable-index-downloads-in-eclipse-for-maven-dependency-search)
  * Windows, Preferences, Maven, then check:
    * Download repository index updates on startup
    * Download Artifact Sources (optional)
    * Download Artifact JavaDoc (optional)
    * restart Eclipse
* optionally update maven index from central manually (not tried)
  * Window, Show View, Other
    * Maven, Maven Repositories, Open
  * In the Maven Repositories view, open Global Repositories, right-click central, Rebuild Index
    * wait for index download to complete
* add swt Maven dependency via Eclipse (see <https://stackoverflow.com/questions/9164893/how-do-i-add-a-maven-dependency-in-eclipse>):
  * right-click the project, Maven, Add Dependency, search for swt in search box then click you must choose the "swt-linux-gtk" in "org.eclipse" -- this will automatically fill in groupId: "org.eclipse" and artifactId: "swt-linux-gtk", version something like 3.0.1 (excluding version may not work), OK
  * also add dependency javax.media (choose jai_core version for more popular version)
* replace "import  javax.media.opengl.glu.GLU" with "import com.jogamp.opengl.glu.GLU"
* replace "import  javax.media.opengl.GLDrawableFactory" with "import com.jogamp.opengl.GLDrawableFactory"
* replace "import  javax.media.opengl.GLContext" with "import com.jogamp.opengl.GLContext"
* replace "import  javax.media.opengl.GLCapabilities" with "import com.jogamp.opengl.GLCapabilities"
* replace "import  javax.media.opengl.GL" with "import com.jogamp.opengl.GL"
* replace "import  org.eclipse.swt.opengl.GLCanvas" with "import com.jogamp.opengl.swt.GLCanvas"
## not done yet:
* replace "org.eclipse.swt.opengl.GLData" ..
* replace "" with ""
(2017-05-31)
* see also "Changes specific to forking" below

### Changes specific to forking
(2017-05-31)
* Changed key elements "XML based" to "ASCII...(such as XML)" formats preferred
* people at bottom of README: move to Contributors.txt
* added this file (README.md)
* corrected spelling of contributors in filename and contents of Contributors.txt
* renamed "GNU GPLv2.txt" to LICENSE.txt
* Changed to Semantic Versioning 2.0 ("Additional labels for pre-release and build metadata are available as extensions to the MAJOR.MINOR.PATCH format.") as per [http://www.semver.org](http://www.semver.org)
  * MAJOR version when you make incompatible API changes,
  * MINOR version when you add functionality in a backwards-compatible manner, and
  * PATCH version when you make backwards-compatible bug fixes.

## Known Issues
* make compile for Java 8 (aka 1.8) in Eclipse (final binary release of original sourceforge master doesn't work on Windows 10 due to changes in Java--missing library when using paths that are specified in avoCADo.bat)
* rename project from avoCADo (MANY projects are called avocado)--see "Possible alternate names" below (enter as Issue or create forum)
* for more known issues, see also "Old info to import into GitHub" below

### Sites that should be updated
(to link to https://github.com/avoCADo-3d/avoCADo)
* https://www.openhub.net/p/avoCADo-CAD
* https://sourceforge.net/projects/avocado-cad/
* http://avocado-cad.sourceforge.net/
* https://sourceforge.net/p/avocado-cad/discussion/656395/
#### external sites
* http://jogamp.org/jogl/www/
* http://ossandcad.blogspot.com/2006/10/open-source-software-in-mechanical.html

### Old info to import into GitHub
* https://sourceforge.net/p/avocado-cad/wiki/Home/
* https://sourceforge.net/p/avocado-cad/bugs/ (including CLOSED)
* https://sourceforge.net/p/avocado-cad/feature-requests/ (including CLOSED)
* https://sourceforge.net/p/avocado-cad/discussion/656395/
* https://sourceforge.net/p/avocado-cad/mailman/avocado-cad-announce/
* All pages of website http://avocado-cad.sourceforge.net/

## Compiling
* Install System Dependencies
  * Fedora: `sudo dnf -y install eclipse-jdt java-1.8.0-openjdk-devel maven`
  #eclipse-jdt pulls in large sets of dependencies such as: ant, jetty, felix, glassfish, plexus, some parts of maven
* Install jar dependencies
(requires JOGL--specifically, jogl-1.1.1-rc8-windows-i586/lib/jogl.jar according to version 0.8.3's .classpath file which also specifies jogl-1.1.1-rc8-windows-i586/lib/gluegen-rt.jar)

### Getting 0.8.3 to compile in maven (transitioning to 0.9.1)
* creating maven project:
    #see also <https://maven.apache.org/guides/getting-started/index.html#How_do_I_make_my_first_Maven_project>
    mvn -B archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes \
        -DarchetypeArtifactId=maven-archetype-quickstart \
        -DgroupId=org.poikilos.librecsg \
        -DartifactId=librecsg
    # (output indicates that it maven applied -DarchetypeArtifactId=maven-archetype-quickstart by default)
* Fedora packages not tried:
  maven-jar-plugin maven-eclipse-plugin maven-dependency-plugin maven-dependency-tree

### Possible alternate names
(originally avoCADo)
* NaturalCAD
* PerseaCAD (from Persea americana, genus & species of avocado; not to be confused with the avocado-persea by cha0s on GitHub which is a resource editor and staging framework for avocado game development framework by cha0s)
* FernCAD
* CriolloCAD (criollo is undomesticated avocado)
* LibreCSG
* LibreSolid
