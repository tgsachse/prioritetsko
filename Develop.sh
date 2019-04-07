#!/bin/sh
# Some useful development functions for this repository!
# Written by Tiger Sachse.

DOCS_DIR="Docs"
DIST_DIR="Dist"
BUILD_DIR="Build"
SOURCE_DIR="Source"
MANIFEST="manifest.txt"
PACKAGE_NAME="Prioritetsko"
DOCS_JAR_DIR="Documentation"
MAIN_CLASS="PrioritetskoTester"

# Build the program in a build folder.
build_program() {
    rm -rf "$BUILD_DIR"
    mkdir "$BUILD_DIR"
    javac "$SOURCE_DIR/$PACKAGE_NAME"/* -d "$BUILD_DIR" "$@"
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
    java "$PACKAGE_NAME.$MAIN_CLASS" "$@"
    cd .. 1>/dev/null 2>&1
}

# Package the program into a jar.
package_program() {
    build_program
    if [ $? -ne 0 ]; then
        exit 1
    fi
    mkdir -p "$DIST_DIR" "$BUILD_DIR/$DOCS_JAR_DIR" "$BUILD_DIR/$SOURCE_DIR"

    # Copy documentation and source files into the build directory, and create
    # a manifest.
    find "$DOCS_DIR"/* -type f \( -name "*.txt" -o -name "*.pdf" \) \
        -exec cp {} "$BUILD_DIR/$DOCS_JAR_DIR" \;
    cp -r "$SOURCE_DIR/$PACKAGE_NAME"/* "$BUILD_DIR/$SOURCE_DIR"
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
        build_program
        run_program "$@"
        ;;
    --pack|-p)
        package_program
        ;;
    --clean|-c)
        cleanup
        ;;
esac
