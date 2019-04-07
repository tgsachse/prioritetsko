prioritetsko
----
This project provides an adaptive, concurrent priority queue with elimination and combining.

development
----
This repository contains a script, ``Develop.sh`` that automates many development functions you might wish to perform. Here is a full list of flags that the script accepts.

``-b, --build <build arguments...>``
  Build the software into the ``Build`` directory.
``-r, --run <runtime arguments...>``
  Run the software you just built.
``--bar <runtime arguments...>``
  Build the software and then automatically run it afterwards.
``-p, --pack``
  Package the software into a ``jar`` archive.
``-c, --clean``
  Clean up leftover files.

examples
----
Here are some example commands to get you started.
::
  # Build the project.
  bash Develop.sh --build
  
  # Run the project with no arguments.
  bash Develop.sh --run
  
  # Run the project with two integer arguments.
  bash Develop.sh --run 42 9001
  
  # Build the project and run it with a floating point argument.
  bash Develop.sh --bar 3.1415926
  
  # Package the project.
  bash Develop.sh --pack
