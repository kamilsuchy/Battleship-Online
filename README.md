#Battleship online

### Parametry uruchomieniowe
Aplikacja obsługuje następujące parametry:
* `-mode [server|client]` - wskazuje tryb działania (jako serwer: przyjmuje połączenie, jako klient: nawiązuje połączenie z serwerem).
* `-port N` - port, na którym aplikacja ma się komunikować.
* `-map map-file` - ścieżka do pliku zawierającego mapę z rozmieszczeniem statków.

### Protokół komunikacji
* Komunikacja odbywa się z użyciem protokołu TCP, z kodowaniem UTF-8.
* Klient i serwer wysyłają sobie na przemian _wiadomość_, która składa się z 2 części: _komendy_ i _współrzędnych_, oddzielonych znakiem `;`, i zakończonych znakiem końca linii `\n`.
    * Format wiadomości: `komenda;współrzędne\n`
    * Przykład wiadomości: `pudło;D6\n`
* Komendy i ich znaczenie:
    * _start_
        * komenda inicjująca rozgrywkę.
        * Wysyła ją klient tylko raz, na początku.
        * Przykład: `start;A1\n`
    * _pudło_
        * odpowiedź wysyłana, gdy pod współrzędnymi otrzymanymi od drugiej strony nie znajduje się żaden okręt.
        * Przykład: `pudło;A1\n`
    * _trafiony_
        * odpowiedź wysyłana, gdy pod współrzędnymi otrzymanymi od drugiej strony znajduje się okręt, i nie jest to jego ostatni dotychczas nietrafiony segment.
        * Przykład: `trafiony;A1\n`
    * _trafiony zatopiony_
        * odpowiedź wysyłana, gdy pod współrzędnymi otrzymanymi od drugiej strony znajduje się okręt, i trafiono ostatni jeszcze nietrafiony segment tego okrętu.
        * Przykład: `trafiony zatopiony;A1\n`
    * _ostatni zatopiony_
        * odpowiedź wysyłana, gdy pod współrzędnymi otrzymanymi od drugiej strony znajduje się okręt, i trafiono ostatni jeszcze nietrafiony segment okrętu całej floty w tej grze.
        * Jest to ostatnia komenda w grze. Strona wysyłająca ją przegrywa.
        * Przy tej komendzie nie podaje się współrzędnych strzału (już nie ma kto strzelać!).
        * Przykład: `ostatni zatopiony\n`
* Możliwe jest wielokrotne strzelanie w to samo miejsce. Aplikacja odpowiada wtedy zgodnie z aktualnym stanem planszy:
    * `pudło` w razie pudła,
    * `trafiony` gdy okręt już był trafiony w to miejsce, ale nie jest jeszcze zatopiony,
    * `trafiony zatopiony` gdy okręt jest już zatopiony.
* Obsługa błędów:
    * W razie otrzymania niezrozumiałej komendy lub po 1 sekundzie oczekiwania ponownie wysłana jest ostatnią wiadomość.
    * Po 3 nieudanej próbie wyświetlony zostaje komunikat `Błąd komunikacji`.

### Działanie aplikacji
* Po uruchomieniu (w dowolnym trybie), aplikacja wyświetla swoją mapę.
* W czasie działania aplikacja wyświetla wszystkie wysyłane i otrzymywane wiadomości.
* Po zakończeniu rozgrywki, aplikacja wyświetla:
    * `Wygrana\n` w razie wygranej lub `Przegrana\n` w razie przegranej,
    * W razie wygranej: pełną mapę przeciwnika,
    * W razie przegranej: mapę przeciwnika, z zastąpieniem nieznanych pól znakiem `?`. _Uwaga_: pola sąsiadujące z zatopionym okrętem należy uznać za odkryte (nie może się na nich znajdować inny okręt).
    * Pusty wiersz
    * Swoją mapę, z dodatkowymi oznaczeniami: `~` - pudła przeciwnika, `@` - celne strzały przeciwnika.

Przykład mapy przeciwnika z przegranej sesji:
```
..#..??.?.
#.????.#..
#....??...
..##....?.
?.....##..
??#??.....
..?......#
..##...#..
.##....#.#
.......#..
```

Przykład swojej mapy po grze (wygranej; nie wszystkie okręty zatopione):
```
~~@~~.~~~.
@..~.~.@.~
#.~#..~.~.
..##..~..~
..~.~.@@..
.#@~..~...
.~.~.~.~.@
~.##.~.#~~
.##~..~~~~
..~.~.~~~.
```