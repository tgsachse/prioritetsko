prioritetsko
----
This project provides an adaptive, concurrent priority queue with elimination and combining.

execution
----
This program is provided as a zip, and can be executed as shown below. The arguments in <arrow brackets> are required. The argument in [normal brackets] is optional.
::
    sh develop.sh --run <totalThreads> <totalPushes> <totalPops> [totalRuns]

development
----
This repository contains a script, ``develop.sh`` that automates many development functions you might wish to perform. Here is a full list of flags that the script accepts.

``-b, --build <build arguments...>``
  Build the software into the ``Build`` directory.
``-r, --run <runtime arguments...>``
  Run the software you just built.
``--bar <runtime arguments...>``
  Build the software and then automatically run it afterwards.
``-a, --analyze <runtime arguments...>``
  Build and run the software, then graph the results.
``-p, --pack``
  Package the software into a ``jar`` archive.
``-c, --clean``
  Clean up leftover files.

examples
----
Here are some development example commands to get you started.
::
  # Build the project.
  sh develop.sh --build
  
  # Run the project across 32 threads, with 1000 total pushes and 2000 total pops.
  sh develop.sh --run 32 1000 2000
  
  # Build the project and run it with the same parameters as above and 20 runs per thread.
  sh develop.sh --bar 32 1000 2000 20
  
  # Produce a results graph for 16 threads, with 200 total pushes and 100 total pops.
  sh develop.sh --analyze 16 200 100

  # Package the project.
  sh develop.sh --pack
