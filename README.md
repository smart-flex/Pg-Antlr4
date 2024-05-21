# Pg-Antrl4
****
**Краткое описание на русском:** Pg-Antrl4 - Java инструмент для генерации оптимизированных хранимых процедур Postgres, реализованная с использованием библиотеки [Antrl4](https://github.com/antlr/antlr4), описаны правила для лексера и парсера для языка plpgsql.

Основной функционал Pg-Antrl4 - парсинг хранимой процедуры, в которой имеются вызовы подпроцедур, и ее модификация с целью оптимизации. На выходе мы получаем новую хранимую процедуру с исключенными вызовами подпроцедур, что ускоряет ее выполнение т.к. в результате ее вызова не требуется хранить стек вызовов. Особенно разница заметна при вызовах подпроцедур в цикле. Далее приводим сравнение времени работы оригинальных хранимых процедур и их оптимизированных вариантов.

**Pg-Antrl4** is Java Tool for optimization Postgres stored procedures using [Antrl4](https://github.com/antlr/antlr4) parser. It's aimed to improve performance of stored procedures execution. Having a stored procedure with subcalls of others stored procedures, Pg-Antlr4 modifies body of main procedure by replacing subcalls of stored procedures with their actual bodies. This replacement makes real difference if we have calls of subprocedures in a loop. After some researches, we got great results for these cases. Below we give some comparison of time increasing for regular stored procedures and optimized ones.

There's a common


**Usage**


**Некоторые результаты**

Приведем примеры времени работы оригинальных хранимых процедур и оптимизированных процедур.
1) Имея 2 уровня вложенности вызовов ХП, на первом уровне вложенности имея вызов подпроцедуры в цикле, был замечен прирост времени выполнения объединенной ХП, начиная с цикла = 10000 (был прирост в 1.27 раз), на цикле = 300000 наблюдали прирост в 1.68 раз.
2) Передавая бо́льшее число параметров и имея больший уровень вложенности (например, 3) при вызове ХП в цикле мы получаем лучший результат. В результате тестирования получили прирост = 1.69 начиная с 1000 итераций, а с 5000 наблюдали прирост почти в 2 раза (1.92).

**Some results**

Let's give some examples of time comparison between regular stored procedures and optimized ones.
1) When we had 2 levels of nesting, and in the first level having subcall of SP in a loop, we got 1.27 times increase in a loop from 1 to 10000 els; in a loop from 1 to 300000 we got 1.68 times increase.
 
2) When we got more params and having more levels of nesting, we can get more optimized stored procedure. E.g. having 3 levels of nesting and sending more params (in our case, 6) we already got increase 1.69x from 1000 iterations and almost 2 times(1.92) from 5000 iterations.
