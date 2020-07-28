# RSPLLogger

RSPLLogger is Kotlin logging library that brings some extra functionality:
  - A built in file logger that formats the logs and writes them to a Text file.
  - The posibility to easily share the logs file to server.
  - Share your Log Text file via Email

  ### Usage

  Init the logger before using it (ideally at the app level):

  If you want to save logs to file:
  ```sh
  $ RsplLogger.startWithRSPLLogger(context)
  ```

  To log an event use any of these functions:
  ```sh
  - verboseLog()
  - debugLog()
  - infoLog()
  - warnLog()
  - errorLog()
  ```

  To Send/Upload the logs file on server:
      
```sh
$ sendLogToServer(context, "Your Server URL")
```

  To Delete the logs file from storage Use Extension function:
      
```sh
$ deleteLogsFile()
```
  To share the logs file over a Email Use Extension function:
```sh
$ shareLogsFile(emailAddress: String)
```


### Note
  - Please refer example for better Understanding.
  
