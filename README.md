FileServer
==========
Файловым сервером является папка на диске C://Disk
У клиента файлы будут сохранятся С://Disk2
Чтобы клиент узнал список файлов на сервере нужно откомментировать 69 строку в исходном файле "Server.java" и тогда мы увидел файлы, но не сможем их отправить.
Это строка "открывает" поток, тем самым не давая буферу(он делает отправку файла по байтово) работать нормально.
