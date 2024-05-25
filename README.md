# PAO_audioLibrary

## Overview
Java based application for managing an audio library using the command line. It allows user to authentificate, create playlists, search for songs, export/import playlists.

## Commands


### User Commands

- **register `<username>` `<password>`**  
  Registers a new user with the given username and password.
  
- **login `<username>` `<password>`**  
Logs in an existing user with the given username and password.

- **logout**  
Logs out the current user.

- **promote `<username>`**  
Promotes the specified user to a higher privilege level. **[ADMIN ONLY]**


### Playlist and Song Management Commands

- **create song "`<title>`" "`<artist>`" `<releaseYear>`**  
Creates a new song with the given title, artist, and release year. **[ADMIN ONLY]**

- **create playlist "`<playlistName>`"**  
Creates a new playlist with the given name.

- **add byId `<playlistId>` `<songId1>` `[songId2]` ...**  
Adds songs to a playlist specified by the playlist ID.

- **add byName "`<playlistName>`" `<songId1>` `[songId2]` ...**  
Adds songs to a playlist specified by the playlist name.

- **list playlists `[page]`**  
Lists all playlists with pagination.

- **search name `"<value>"` `[page]`**  
Searches for songs by title with pagination.

- **search author `"<value>"` `[page]`**  
Searches for songs by artist name with pagination.


### Import/Export Commands

- **export byId `<id>` `<format>`**  
Exports a playlist specified by the playlist ID in the given format (csv, json, txt).

- **export byName `"<name>"` `<format>`**  
Exports a playlist specified by the playlist name in the given format (csv, json, txt).

- **import byId `<id>` `<format>` `"<pathToFile>"`**  
Imports a playlist from a file specified by the playlist ID and format (csv, json, txt).

- **import byName `"<name>"` `<format>` `"<pathToFile>"`**  
Imports a playlist from a file specified by the playlist name and format (csv, json, txt).

### Audit Commands

- **audit `<username>` `[page]`**  
Retrieves the audit log for a specified user with pagination.

- **rerun `<auditId>`**  
Reruns a command from the audit log specified by the audit ID.


### Application Command

- **quit**  
Quits the application.
