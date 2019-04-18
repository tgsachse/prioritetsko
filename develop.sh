#!/bin/sh
# Some useful development functions for this repository!
# Written by Tiger Sachse.

LIB_DIR="lib"
DOCS_DIR="docs"
BUILD_DIR="build"
SOURCE_DIR="source"
GRAPHER_SCRIPT="grapher.py"
PACKAGE_NAME="prioritetsko"
MAIN_CLASS="PrioritetskoTester"
DEUCE_JAR="deuceAgent-1.3.0.jar"

# Build the program in a build folder.
build_program() {
    rm -rf "$BUILD_DIR"
    mkdir "$BUILD_DIR"
    javac -cp "$LIB_DIR/$DEUCE_JAR" \
          "$SOURCE_DIR/$PACKAGE_NAME"/* \
          -d "$BUILD_DIR" "$@"
    if [ $? -ne 0 ]; then
        printf "Build process failed.\n"
        rm -rf "$BUILD_DIR"

        return 1
    fi
}

# Run the compiled program.
run_program() {
    cd "$BUILD_DIR" 1>/dev/null 2>&1
    if [ $? -ne 0 ]; then
        printf "Please build the program first using --build.\n"
        exit 1
    fi
    java  -javaagent:"../$LIB_DIR/$DEUCE_JAR" \
         "$PACKAGE_NAME.$MAIN_CLASS" "$@"
    cd .. 1>/dev/null 2>&1
}

# Build and run the program.
build_and_run_program() {
    build_program
    if [ $? -ne 0 ]; then
        exit 1
    fi
    run_program "$@"
}

# Build and run the program, then pipe results to a graphing script.
analyze_program() {
    build_program 1>/dev/null 2>&1
    run_program "$@" | tee /dev/tty | python3 "$GRAPHER_SCRIPT" "$2" "$3"
}

# Package the program.
package_program() {
    build_program
    if [ $? -ne 0 ]; then
        exit 1
    fi

    zip -r "$PACKAGE_NAME.zip" *

    cleanup
}

# Clean up any mess.
cleanup() {
    rm -rf "$BUILD_DIR"
}

# Main entry point to the program.
if [ $# -lt 1 ]; then
    printf "No argument provided. Check the README for arguments.\n"
    exit 1
fi
COMMAND="$1"
shift

case "$COMMAND" in
    --build|-b)
        build_program "$@"
        ;;
    --run|-r)
        run_program "$@"
        ;;
    --bar)
        build_and_run_program "$@"
        ;;
    --analyze|-a)
        analyze_program "$@"
        ;;
    --pack|-p)
        package_program
        ;;
    --clean|-c)
        cleanup
        ;;
esac
