# Galaxy Trucker

# Implemented Features
| FEATURE             |  |
|---------------------|--|
| Simplified Rules    | ✅ |
| Complete Rules      | ✅ |
| TUI                 | ✅ |
| GUI                 | ✅ |
| RMI                 | ✅ |
| Socket              | ✅ |
| Test Flight         | ✅ |
| Multigame           | ✅ |
| Resilience          | ❌ |
| Persistence         | ❌ |


## Run the server
```bash
java -jar <path to the .jar file> server
```

If RMI connections don't work add the following parameter
```bash
-Djava.rmi.server.hostname=<server ip>
```

On Windows if RMI connections doesn't still work: 

- Open Control Panel → System and Security → Windows Defender Firewall → Allow an app or feature through Windows Defender Firewall.

- Click Change settings.

- Ensure Java(TM) Platform SE binary is checked for both Private and Public networks.

## Run the client with RMI
```bash
java -jar <path to the .jar file> client rmi <server ip> 9696 <TUI/GUI>
```

## Run the client with Socket
```bash
java -jar <path to the .jar file> client tcp <server ip> 4200 <TUI/GUI>
```