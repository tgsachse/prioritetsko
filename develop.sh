#!/bin/sh
# Some useful development functions for this repository!
# Written by Tiger Sachse.

LIB_DIR="lib"
DOCS_DIR="docs"
DIST_DIR="dist"
BUILD_DIR="build"
SOURCE_DIR="source"
MANIFEST="manifest.txt"
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
    ln -s "$PWD/$LIB_DIR/" "$PWD/$BUILD_DIR/"
}

# Run the compiled program.
run_program() {
    cd "$BUILD_DIR" 1>/dev/null 2>&1
    if [ $? -ne 0 ]; then
        printf "Please build the program first using --build.\n"
        exit 1
    fi
    java -javaagent:"$LIB_DIR/$DEUCE_JAR" "$PACKAGE_NAME.$MAIN_CLASS" "$@"
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
    build_and_run_program "$@" | tee /dev/tty | python3 "$GRAPHER_SCRIPT"
}

# Package the program into a jar.
package_program() {
    build_program
    if [ $? -ne 0 ]; then
        exit 1
    fi
    mkdir -p "$DIST_DIR" "$BUILD_DIR/$DOCS_DIR" "$BUILD_DIR/$SOURCE_DIR"

    # Copy documentation and source files into the build directory, and create
    # a manifest.
    find "$DOCS_DIR"/* -type f \
          \( -name "*.txt" -o -name "*.pdf" \) \
          -exec ln -s "$PWD"/{} "$PWD/$BUILD_DIR/$DOCS_DIR/" \;
    ln -s "$PWD/$SOURCE_DIR/$PACKAGE_NAME"/* "$PWD/$BUILD_DIR/$SOURCE_DIR/"
    printf "Main-Class: $PACKAGE_NAME.$MAIN_CLASS\n" > "$MANIFEST"

    # Create a jar with the contents of the build directory.
    cd "$BUILD_DIR"
    jar cvfm "../$DIST_DIR/$PACKAGE_NAME.jar" "../$MANIFEST" *
    cd .. 1>/dev/null 2>&1

    cleanup
}

# Clean up any mess.
cleanup() {
    rm -rf "$BUILD_DIR"
    rm -f "$MANIFEST"
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
