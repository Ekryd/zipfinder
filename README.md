# ZipFinder #
This is a standalone tool that searches zip- or jarfiles for specific classes. The tool accepts a directory which it scans recursively for jarfiles and a string that it tries to match with the classes in the jarfiles.

The main use for this tool is to answer questions like: **"Which jarfile in the Spring framework contains the class URLContextResolver?"**

## Usage ##
Download and compile with Maven

### Command line ###
Example:
```
zipfinder.cmd c:\eclipse3_5 URLResourceLoader
```

### With Swing GUI ###
With no arguments:
```
zipfinder.cmd
```

With directory specification:
```
zipfinder.cmd c:\eclipse3_5
```

![https://raw.githubusercontent.com/GreenThingSalad/zipfinder/master/src/test/resources/swing.jpg](https://raw.githubusercontent.com/GreenThingSalad/zipfinder/master/src/test/resources/swing.jpg)

## Scripts ##
I would be glad if somebody could send me a startscript that works i Unix.
