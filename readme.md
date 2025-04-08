# Paralelizare pe Bază de Thread-uri

## Tip de paralelizare: Threads

## Factor de paralelism:
Numărul de thread-uri este egal cu numărul de câmpuri utilizate (ex: 5 câmpuri, 5 thread-uri, unul pentru fiecare câmp)

## Frecvențe câmpuri:

```java
Map<String, Double> fieldFrequencies = Map.of(
                "city", 0.7,
                "date", 0.5,
                "temp", 0.6,
                "wind", 0.5,
                "direction", 0.8,
                "stationid",0.9,
                "rain",0.2
);
```

## Teste de performanță

### Număr subscrieri: 2.452.129

#### Timp de execuție (4 rulări) - **Single thread**:
- 19101ms
- 18490ms
- 17416ms
- 19293ms

#### Timp de execuție (4 rulări) - **Multi thread**:
- 10765ms
- 10847ms
- 10236ms
- 10599ms

### Număr subscrieri: 100.000

#### Timp de execuție (4 rulări) - **Single thread**:
- 783ms
- 819ms
- 854ms
- 828ms

#### Timp de execuție (4 rulări) - **Multi thread**:
- 765ms
- 731ms
- 812ms
- 774ms

## Specificații sistem

- **Sistem de operare**: Windows 11 10.0
- **Arhitectură**: amd64
- **Număr de nuclee**: 16