# eplan
Educational programming language for compiler construction teaching

EPlan is used to teach compilation techniques to undergraduate
students at Universidade Federal de Ouro. The language specification
starts with a very minimal kernel and grows with new features as new
techniques are learnt.

Tags will be used to give access to each important stage of the
compiler implementation.

## Lexical analysis

The lexical analyser (also called scanner) is built with the help of
JFlex.

The lexical specification of the language is saved in a text file
(`lexer.jflex`) which is given to JFlex to generate the lexical
analyser. JFlex can be run with the command below given on a terminal:

``` sh
$ cd src
$ jflex lexer.jflex
```

## Running the compiler

The eplan compiler can be run with the following commands typed in the terminal:

```
$ eplan <file>.eplan
$ llc <file>.ll
$ clang src/main/c/bindings.c <file>.s
$ ./a.out
```

You will be able also to run in one line:
```
F=<file> && ./driver -ast -dot $F && dot -O -Tpng $F.dot && llc $F.ll && clang $F.s src/main/c/bindings.c -o $F.exe && feh -Zd $F*png
```
