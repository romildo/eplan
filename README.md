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
