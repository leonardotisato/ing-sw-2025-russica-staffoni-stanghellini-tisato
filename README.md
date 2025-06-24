# Galaxy Trucker

# Funzionalità implementate
| FUNZIONALITA'       |  |
|---------------------|--|
| Regole Semplificate | ✅ |
| Regole Complete     | ✅ |
| TUI                 | ✅ |
| GUI                 | ✅ |
| RMI                 | ✅ |
| Socket              | ✅ |
| Volo di Prova       | ✅ |
| Multigame           | ✅ |
| Resilienza          | ❌ |
| Persistenza         | ❌ |

## Run the server
```bash
java -jar <path to the .jar file> server
```

## Run the client with RMI
```bash
java -jar <path to the .jar file> client rmi <server ip> 9696 <TUI/GUI>
```

## Run the client with Socket
```bash
java -jar <path to the .jar file> client tcp <server ip> 4200 <TUI/GUI>
```