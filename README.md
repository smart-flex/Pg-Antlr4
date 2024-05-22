# Pg-Antrl4
****
**Краткое описание на русском:** Pg-Antrl4 - Java инструмент для генерации оптимизированных хранимых процедур Postgres, реализованная с использованием библиотеки Antrl4.

Основной функционал Pg-Antrl4 - парсинг хранимой процедуры, в которой имеются вызовы подпроцедур, и генерации новой хранимой процедуры с включением тел вызывамых процедур, что в итоге ускоряет ее исполнение. Разница в производительности особенно заметна при вызовах подпроцедур в цикле. Далее приводим сравнение времени работы оригинальных хранимых процедур и их оптимизированных вариантов.

**Pg-Antrl4** is Java Tool for optimization Postgres stored procedures using Antrl4 lib. Its main aim is to improve performance of stored procedures execution. Having a stored procedure with subcalls of others stored procedures, Pg-Antlr4 modifies body of main procedure by replacing subcalls of stored procedures with their actual bodies. This replacement makes real difference if we have calls of subprocedures in a loop. After some researches, we got great results for these cases. Below we give some comparison of time increasing for regular stored procedures and optimized ones.


**Usage**


**Некоторые результаты**

Приведем примеры времени работы оригинальных хранимых процедур и оптимизированных процедур.
1) Имея 2 уровня вложенности вызовов хранимых процедур, на первом уровне вложенности имея вызов подпроцедуры в цикле, был замечен прирост времени выполнения объединенной процедуры начиная с цикла = 10000 (был прирост в 1.27 раз), на цикле = 300000 наблюдали прирост в 1.68 раз.
2) Передавая бо́льшее число параметров (в нашем примере 6) и имея бо́льший уровень вложенности (в нашем случае 3) при вызове хранимой процедуры в цикле, мы получали лучший результат. В результате тестирования получили прирост в 1.69 раз, начиная с 1000 итераций, а с 5000 итераций наблюдали прирост почти в 2 раза (1.92).

**Some results**

Let's give some examples of time comparison between regular stored procedures and optimized ones.
1) When we had 2 levels of nesting of stored procedures, and in the first level having call of procedure in a loop, we got 1.27 times increase for 10000 iterations; in a loop for 300000 iterations we got 1.68 times increase.
2) When we got more params and having more levels of nesting, we can generate stored procedure with greater performance. E.g. having 3 levels of nesting and sending more params (in our case, 6) we already got increase 1.69x from 1000 iterations and almost 2 times (1.92x) from 5000 iterations.
