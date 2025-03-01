def euler5():
    from math import gcd
    from functools import reduce
    return reduce(lambda a,b: a*b//gcd(a,b), range(1,21))

print(euler5())
