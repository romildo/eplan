#include <stdio.h>
#include "inttypes.h"

extern void __eplan_print_double(const double x) {
   printf("%f\n", x);
}

extern void __eplan_print_int(const int32_t x) {
   printf("%" PRIi32 "\n", x);
}
