## Building

    ./gradlew clean create

## Running on Linux

    ./scripts/plz-tools

    > Missing command name
    > usage: plz-tools <command>
    > where <command> may be one of the following:
    >    list-plz
    >    list-plz-with-distance

Listing all PLZ with a certain prefix:

    ./scripts/plz-tools list-plz --prefix 4022

    > 40221: 51.204, 6.746
    > 40223: 51.197, 6.777
    > 40225: 51.197, 6.794
    > 40227: 51.213, 6.804
    > 40229: 51.197, 6.844

Listing all PLZ with a certain prefix and their distance a specified PLZ:

    ./scripts/plz-tools list-plz-with-distance --plz 40225 --prefix 4022

    > 40221: 51.204, 6.746 3.45km
    > 40223: 51.197, 6.777 1.19km
    > 40225: 51.197, 6.794 0.00km
    > 40227: 51.213, 6.804 1.90km
    > 40229: 51.197, 6.844 3.50km

## Hacking

To work on the project in Eclipse, create the Eclipse project files using
Gradle:

    ./gradlew cleanEclipse eclipse

IntelliJ should also be able to open the project without further configuration.
