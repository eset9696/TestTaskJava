//Test task sort data from *.txt files and write data to integers.txt, floats.txt, strings.txt
//[JINT] (SHIFT-58584)
//JDK 21.0.2 Build system Intelij
//Целые числа размерностью более, чем Long переходят в разряд вещественных, файлы читаются по одной строке по очереди. В соответсвии с заданием статистика
//собирается по мере сортировки строк, а среднее арифметическое пересчитывается каждый раз при добавлении новой строки в integers.txt, floats.txt
//использованы только стандартные библиотеки, исходные файлы должны лежать в папке с *.jar файлом. запускается командой в соответсвтии с заданием
// java -jar TestTaskJava.jar -a -f -o 'path' -p 'prefix' in1.txt in2.txt