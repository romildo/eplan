package semantic;

import error.CompilerError;

import javaslang.collection.List;
import parse.Loc;
import types.Type;

public interface SemanticHelper {

    static CompilerError typeMismatch(Loc loc, Type found, Type... expected) {
        final StringBuilder builder = new StringBuilder();
        final int n = expected.length;
        if (n > 0) {
            builder.append(expected[0]);
            if (n > 1) {
                for (int i = 1; i < n - 2; i++)
                    builder.append(", ").append(expected[i]);
                builder.append(" or ").append(expected[n - 1]);
            }
        }
        return new CompilerError(loc, "type mismatch: found %s but expected %s", found, builder);
    }

    static CompilerError undefined(Loc loc, String category, String name) {
        return new CompilerError(loc, "undefined %s '%s'", category, name);
    }

    static CompilerError notAFunction(Loc loc, String name) {
        return new CompilerError(loc, "'%s' is not a function", name);
    }

    static CompilerError tooFewArguments(Loc loc, String name) {
        return new CompilerError(loc, "too few arguments in call to '%s'", name);
    }

    static CompilerError tooMuchArguments(Loc loc, String name) {
        return new CompilerError(loc, "too much arguments in call to '%s'", name);
    }

    static CompilerError breakOutWhile(Loc loc) {
        return new CompilerError(loc, "break ins't in loop.");
    }

    static CompilerError functionTypeMismatch(Loc loc, Type result, Type found) {
        return new CompilerError(loc, "function type mismatch: found %s in body but expected %s", found, result);
    }

    static CompilerError arrayMismatch(Loc loc, Type found) {
        return new CompilerError(loc, "type mismatch: found %s but expected array type", found);
    }

    static CompilerError recordMismatch(Loc loc, Type found) {
        return new CompilerError(loc, "type mismatch: found %s but expected record type", found);
    }

    static CompilerError duplicatedParameter(Loc loc, String parameter) {
        return new CompilerError(loc, "parameter %s is duplicated", parameter);
    }

    static CompilerError unknownRecordParameter(Loc loc, String parameter) {
        return new CompilerError(loc, "record parameter %s is unknown", parameter);
    }

    static CompilerError recordTypeMismatch(Loc loc, String name, Type found, Type... expected) {
        final StringBuilder builder = new StringBuilder();
        final int n = expected.length;
        if (n > 0) {
            builder.append(expected[0]);
            if (n > 1) {
                for (int i = 1; i < n - 2; i++)
                    builder.append(", ").append(expected[i]);
                builder.append(" or ").append(expected[n - 1]);
            }
        }
        return new CompilerError(loc, "type mismatch in record parameter %s: found %s but expected %S", name, found, builder);
    }

    static CompilerError missingParametersRecord(Loc loc, List<String> s) {
        return new CompilerError(loc, "missing parameters %s in record", s.toString());
    }
}
