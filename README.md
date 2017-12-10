# UCL MSR Data Processor

Mining Software Repositories (MSR) consortium, mining challenge data processing
application (...and breathe).

## How To Run

To run the application, navigate to the repository using the command line, and
execute the following command. Note: to run a script on OSX and linux you need
to start the command with "./".

```
gradlew run -Ddata="PATH" -Doutput="PATH" -DeventMax=1000 (Optional) -DthreadMax=4 (Optional)
```

Options:
- `data`: the path of an MSR zip archive.
- `output`: the path into which the applications analysis will be written.
- `eventMax`: the total number of MSR events to process.
- `threadMax`: the total number of threads used to process MSR events.

## How to Develop

To start developing the project, navigate to the repository using the command
line, and execute EITHER of the following commands, depending on your preferred
development environment.

```
gradlew eclipse
gradlew idea
```
