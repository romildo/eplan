#include <stdio.h>

extern void __eplan_print_double(const double x) {
   printf("%f\n", x);
}

extern void __eplan_print_int(const int32_t x){
    printf("%"PRId32"\n",x);
}