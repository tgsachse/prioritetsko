# Some useful development functions for this repository!
# Written by Tiger Sachse.

DOCS_DIR="Docs"
DIST_DIR="Dist"
BUILD_DIR="Build"
SOURCE_DIR="Source"
MANIFEST="manifest.txt"
PACKAGE_NAME="Prioritetsko"
DOCS_JAR_DIR="Documentation"
MAIN_CLASS="PrioritetskoDriver"

# Build the program in a build folder.
build_program() {
    rm -rf "$BUILD_DIR"
    mkdir "$BUILD_DIR"
    javac "$SOURCE_DIR/$PACKAGE_NAME"/* -d "$BUILD_DIR" "$@"
    if [ $? -ne 0 ]; then
        echo "Build process failed."
        rm -rf "$BUILD_DIR"

        return 1
    fi
}

# Run the compiled program.
run_program() {
    cd "$BUILD_DIR" &> /dev/null
    if [ $? -ne 0 ]; then
        echo "Please build the program first using --build."
        exit 1
    fi
    java "$PACKAGE_NAME.$MAIN_CLASS"
    cd .. &> /dev/null
}

# Package the program into a jar.
package_program() {
    build_program
    if [ $? ne 0 ]; then
        exit 1
    fi
    mkdir -p "$DIST_DIR" "$BUILD_DIR/$DOCS_JAR_DIR" "$BUILD_DIR/$SOURCE_DIR"

    # Copy documentation and source files into the build directory, and create
    # a manifest.
    find "$DOCS_DIR"/* -type f \( -name "*.txt" -o -name "*.pdf" \) \
        -exec cp {} "$BUILD_DIR/$DOCS_JAR_DIR" \;
    cp -r "$SOURCE_DIR/$PACKAGE_NAME"/* "$BUILD_DIR/$SOURCE_DIR"
    echo "Main-Class: $PACKAGE_NAME.$MAIN_CLASS" > "$MANIFEST"

    # Create a jar with the contents of the build directory.
    cd $BUILD_DIR
    jar cvfm "../$DIST_DIR/$PACKAGE_NAME.jar" "../$MANIFEST" *
    cd .. &> /dev/null

    cleanup
}

# Clean up any mess.
cleanup() {
    rm -rf "$BUILD_DIR"
    rm -f "$MANIFEST"
}

# Main entry point to the program.
case $1 in
    --build|-b)
        build_program "${@:2}"
        ;;
    --run|-r)
        run_program "${@:2}"
        ;;
    --bar)
        build_program
        run_program "${@:2}"
        ;;
    --pack|-p)
        package_program
        ;;
    --clean|-c)
        cleanup
        ;;
esac
