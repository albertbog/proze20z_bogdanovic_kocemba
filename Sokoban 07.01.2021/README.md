Nadrobiliśmy zaległości i zaprojektowaliśmy oraz zaimplementowaliśmy moduły odczytu i parsowania plików konfiguracyjnych i opracowaliśmy program odczytujący pliki konfiguracyjne (poziomy gry, bohaterzy, tekstury) i przechowujący je w odpowiednich strukturach danych.(Etap 2)

Następnie dodaliśmy interfejs graficzny, obsługę zdarzeń i animację (przykładowy poziom gry, odczytany z pliku konfiguracyjnego, z elementami ruchomymi i możliwością sterowania bohaterem). Niestety, ponieważ korzystaliśmy z IntelliJ IDEA, który budując projekt "rostrzyga problem migotania animacji", pomimo że zastosowaliśmy podwójne bufforowanie i wszystko działało i nadal działa sprawnie w wyżej wymienionym IDE, dzisiaj tworząc pliki kompilacyjne i uruchamiając grę z poziomu terminala ( pliki *.bat) zastaliśmy poważny problem migotania, który w ostatecznym terminie musimy poprawić.(3 etap)

Poza tym gra działa sprawnie, pozostają dodanie/poprawa serwera(etap 4), poprawa animacji i dodatek. Postanowiliśmy zrezygnować z magnesów i dodać "złego zgredka" jako przeszkodę dla gracza, który będzie się poruszał i przy styczności zabierał życia, co odpowiednio wpływa na punktację. Jeżeli gracz traci ostatnie życie, gra będzie się kończyć i nie załaduje kolejnego poziomu tylko wyświetli wynik. Życia i zgredki odpowiednio będą wczytywane z plików konfiguracyjnych.