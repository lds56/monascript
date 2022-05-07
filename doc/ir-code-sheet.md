# Examples
## Simple Expression
`b? 1+2 : 3`
```
CONST_VAL   : [Nil, 1, 2, 3]
GLOBAL_VAR  : []
# LOCAL_VAR   : [b]
0 | LOADL   0 (b)
1 | BRF     6
2 | LOADC   1 (1)
3 | LOADC   2 (2)
4 | ADD
5 | RETVAL
6 | LOADC   3 (3)
7 | RETVAL
```
## Function Definition
```
fn f(x) {
    x+1
}
f(y)
```
```
__main__
CONST_VAL   :   [Nil, 'f', 'y']
GLOBAL_VAR  :   []
LOCAL_VAR   :   ['f', 'y']
# LOCAL_VAL   :   [_, __f__]
0 | MAKEF   1    # bb index
1 | STOREL  0 ('f')  
2 | LOADL   0 ('f')
3 | LOADL   1 ('y')
4 | CALLF   1
5 | RETVAL
__f__
CONST_VAL   :   [Nil, 1]
GLOBAL_VAR  :   []
LOCAL_VAR   :   ['x']
# LOCAL_VAL   :   [2]
0 | LOADL   0 (x)
1 | LOADC   1 (1)
2 | ADD
3 | RETVAL
```
## While Statement
```
let x = 0;
while x<y {
    x += 1;
}
return x;
```
```
__main__
CONST_VAL   :   [Nil, 0, 1]
GLOBAL_VAR  :   []
LOCAL_VAR   :   ['x', y']
0 | LOADC   1
1 | STOREL  0
2 | LOADL   0
3 | LOADL   1
4 | LT
5 | BRF     11
6 | LOADL   0
7 | LOADC   2
8 | INADD
9 | STOREL  0
10| JUMPL   2
11| LOADL   0
12| RETVAL
```
## For Statement
```
let ans = 0;
for x in l {
    if x % 2 == 1 {
        continue;
    }
    if x > 10 {
        break;
    }
    ans += x;
}
return ans;
```
```
__main__
CONST_VAL   :   [Nil, 0, 2, 1, 10]
GLOBAL_VAR  :   []
LOCAL_VAR   :   ['l', ans', 'x']
0 | LOADC   1
1 | STOREL  1 (ans)
2 | LOADL   0
3 | ITER            # iterator
4 | NEXT            # if has next, push next value & true, else only push false
5 | BRF     20      # for
6 | STOREL  2 (x)
7 | LOADL   2 (x)
8 | LOADC   2 (2)
9 | MOD
10| LOADC   3 (1)
11| BREQ    4       # continue
12| LOADL   2 (x)
13| LOADC   4 (10)
14| BRGT    20      # break
15| LOADL   1 (ans)
16| LOADL   2 (x)
17| INADD   
18| STOREL  1 (ans)
19| JUMPL   4
20| LOADL   1
21| RETVAL
```
## Func Call Statement II
```
let counter = 1;
let adder = x -> {
    counter += 1;
    return x + counter;
}
return adder(1) + adder(1);
```
```
__main__: bbi=1, bbf=null
CONST_VAL:  [Nil, 1]
LOCAL_VAR:  [counter, adder]
0 | LOADC   1
1 | STOREL  0
2 | MAKEF   0
3 | STOREL  1 
4 | LOADC   1 (1)
5 | LOADL   1 (adder)
6 | CALLF   1
7 | LOADL   1 (adder)
8 | LOADC   1 (1)
9 | CALLF   1
10| ADD
11| RETVAL
__f1__: bbi=0, bbf=0
CONST_VAL:  [Nil, 1]
LOCAL_VAR:  [x]
GLOBAL_VAR: [counter]
GLOBAL_POS: [1]
0 | LOADG   0 (counter)
1 | LOADC   1 (1)
2 | IADD
3 | STOREG  0 (counter)
4 | LOADL   0 (1)
5 | LOADG   0 (counter)
6 | ADD
7 | RETVAL
```
## Recursion
```
fn fib(n) {
    if n == 1 || n == 2 {
        return 1; 
    }
    return fib(n-1) + fib(n-2);
}
return fib(n);
```
## Closure
```
fn addgen(x) {
    fn adder(y) {
        return x+y;
    }
    return adder;
}
let adder1 = addgen(1);
let adder2 = addgen(2);
return adder1(1) + adder2(2)
```
```
__main__: bbi=0
CONST_VAL   :   [nil, 1, 2]
LOCAL_VAR   :   [addgen, adder1, adder2]
0 | MAKEF   1
1 | STOREL  0 (addgen)
2 | LOADL   0 (addgen)
3 | LOADC   1 (1)
4 | CALLF   1
5 | STOREL  1 (adder1)
6 | LOADL   0 (addgen)
7 | LOADC   2 (2)
8 | CALLF   1
9 | STOREL  2 (adder2)
10| LOADL   1 (adder1)
11| LOADC   1 (1)
12| CALLF   1
13| LOADL   2 (adder2)
14| LOADC   2 (2)
15| CALLF
16| ADD
17| RETVAL
__addgen__: bbi=1
CONST_VAL   :   [nil]
LOCAL_VAR   :   [x, adder]
0 | MAKEF   2
1 | STOREL  1 (adder)
2 | LOADL   1 (adder)
3 | RETVAL
__addgen_adder__: bbi=2
CONST_VAL   :   [nil]
LOCAL_VAR   :   [y]
0 | LOADG   0 (x)
1 | LOADL   0 (y)
2 | ADD
3 | RETVAL
```













