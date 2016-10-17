# EPLan

EPLan is an educational programming language for compiler construction teaching.

EPlan is used to teach compilation techniques to undergraduate
students at Universidade Federal de Ouro. The language specification
starts with a very minimal kernel and grows with new features as new
techniques are learnt.

Tags will be used to give access to each important stage of the
compiler implementation.

## Development tools needed

- [GIT](https://git-scm.com/)
- [Java development kit](http://www.oracle.com/technetwork/java/javase/downloads/) (version 1.8 or greater)
- C compiler
- [Maven](https://maven.apache.org/)
- Text editor for software development (Suggested: [Atom](https://atom.io/), [Notepad++](https://notepad-plus-plus.org/), gedit, [Emacs](https://www.gnu.org/software/emacs/))
- IDE for Java development (optional) (suggested: [IntelliJ IDEA](https://www.jetbrains.com/idea/))

## Initial setup to work with EPlan in BCC328

In order to develop the activities of the BCC328 (Compiler Construction) course you should:

- Have a [github](https://github.com/) account. If you do not have one, visit the github site and [sign up](https://github.com/join).
- [Log in](https://github.com/login) the github.
- Visit the [EPLan](https://github.com/romildo/eplan) project page.
- Fork the EPlan project.
- In your computer clone your fork of the eplan project.
```
$ cd <working directory>
$ git clone https://github.com/romildo/eplan.git
$ cd eplan
```
- Set the remote repository for your clone.
```
$ git remote add upstream https://github.com/romildo/eplan
$ git remote -v
```

## When testing a version of the eplan compiler

- Change your working directory to the folder containing your clone.
```
$ cd <working directory>
```
- Select the master branch of the clone of your forked project.
```
$ git branch
$ git checkout master
```
- Pull the latest changes from the remote repository.
```
$ git pull upstream master
```
- Select the appropriate branch for the activity.
```
$ git checkout -b <activity>
```
- Develop the activity.

## To submit an activity

- Select the master branch of the clone of your forked project.
```
$ cd <working directory>
$ git checkout master
```
- Pull the latest changes from the remote repository.
```
$ git pull upstream master
```
- Create a new branch where you will develop the activity.
```
$ git checkout -b <activity>
```
- Develop the activity.
- Push your changes to your forked project.
```
git push origin <activity>
```
- Make a pull request (PR) from your forked project at github.

## Some useful commands

### To remove the generated files 

```
$ mvn clean
```

### To compile the project

```
$ mvn compile

```

### To make `jar` files of the project

```
$ mvn package
```

### To run the eplan compiler

Run the eplan compiler:

```
$ java -jar target/uber-eplan-0.1-SNAPSHOT.jar [options] [file]
```

Or you may use the provided shell script `driver`:

```
$ ./driver <file>
```

The generated LLVM intermediate representation code should be compiled with the `llc`:

```
$ llc <file>.ll
```

The generated assembly code and the runtime library should be compiled and linked with a C compiler. If using CLang:

```
$ clang -o <file>.exe src/main/c/bindings.c <file>.s
```

Or if using GCC:

```
$ gcc -o <file>.exe src/main/c/bindings.c <file>.s
```

Finally run the binary obtained for the eplan source code:

```
$ ./<file>.exe
```

To get an image of the syntact tree of your program:

```
$ dot -O -Tpng <file>.dot <file>.dot
$ eog <file>.dot.png
```
