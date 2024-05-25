package org.example.input;

import org.example.exceptions.InvalidArguments;
import org.example.exceptions.PermissionException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseInput {
    private static CommandExecution commandExecution = CommandExecution.getInstance();

    public static void parseInput(String inputString) {
        if (inputString.isBlank()) {
            return;
        }

        List<String> inputList = new ArrayList<>();
        Matcher m = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'").matcher(inputString);
        while (m.find()) {
            if (m.group(1) != null) {
                // Add double-quoted string without the quotes
                inputList.add(m.group(1));
            } else if (m.group(2) != null) {
                // Add single-quoted string without the quotes
                inputList.add(m.group(2));
            } else {
                // Add unquoted word
                inputList.add(m.group());
            }
        }

        int nrOfArguments = inputList.size();

        boolean commandStatusOk = false;
        int nrPagina = 0;

        try {
            switch (inputList.get(0)) {
                case "register":
                    if (nrOfArguments != 3) {
                        throw new InvalidArguments("Invalid number of arguments!\nUsage: register <username> <password>");
                    }
                    commandStatusOk = commandExecution.userRegister(inputList.get(1), inputList.get(2));
                    break;
                case "login":
                    if (nrOfArguments != 3) {
                        throw new InvalidArguments("Invalid number of arguments!\nUsage: login <username> <password>");
                    }
                    commandStatusOk = commandExecution.userLogin(inputList.get(1), inputList.get(2));
                    break;
                case "logout":
                    if (nrOfArguments != 1) {
                        throw new InvalidArguments("Invalid number of arguments!\nUsage: logout");
                    }
                    commandStatusOk = commandExecution.userLogout();
                    break;
                case "promote":
                    if (nrOfArguments != 2) {
                        throw new InvalidArguments("Invalid number of arguments!\nUsage: promote <username>");
                    }
                    commandStatusOk = commandExecution.userPromote(inputList.get(1));
                    break;
                case "create":
                    switch(inputList.get(1)) {
                        case "song":
                            if (nrOfArguments != 5) {
                                throw new InvalidArguments("Invalid number of arguments!\nUsage: create song \"<title>\" \"<artist>\" <releaseYear>");
                            }
                            commandStatusOk = commandExecution.createSong(inputList.get(2), inputList.get(3), inputList.get(4));
                            break;
                        case "playlist":
                            if (nrOfArguments != 3) {
                                throw new InvalidArguments("Invalid number of arguments!\nUsage: create playlist \"<playlistName>\"");
                            }
                            commandStatusOk = commandExecution.createPlaylist(inputList.get(2));
                            break;
                        default:
                            System.out.println("Invalid command!");
                            break;
                    }
                    break;
                case "add":
                    if (nrOfArguments < 4) {
                        throw new InvalidArguments("Invalid number of arguments!\nUsage: add byId/byName <playlistId>/\"<playlistName>\" <songId1> [songId2] ...");
                    }
                    switch (inputList.get(1)) {
                        case "byId":
                            commandStatusOk = commandExecution.addSongToPlaylistById(inputList.get(2), inputList.subList(3, nrOfArguments));
                            break;
                        case "byName":
                            commandStatusOk = commandExecution.addSongToPlaylistByName(inputList.get(2), inputList.subList(3, nrOfArguments));
                            break;
                        default:
                            System.out.println("Invalid command!");
                            break;
                    }
                    break;
                case "list":
                    if (nrOfArguments < 2 || nrOfArguments > 3) {
                        throw new InvalidArguments("Invalid number of arguments!\nUsage: list playlists [page]");
                    }
                    if (inputList.get(1).equals("playlists")) {
                        if (nrOfArguments == 3) {
                            try {
                                nrPagina = Integer.parseInt(inputList.get(2)) - 1;
                            } catch (NumberFormatException e) {
                                throw new InvalidArguments("Invalid page number!");
                            }
                        }
                        commandStatusOk = commandExecution.getPlaylists(nrPagina);
                    } else {
                        System.out.println("Invalid command!");
                    }
                    break;
                case "search":
                    if (nrOfArguments < 3 || nrOfArguments > 4) {
                        throw new InvalidArguments("Invalid number of arguments!\nUsage: search name/author <value> [page]");
                    } else if (nrOfArguments == 4) {
                        try {
                            nrPagina = Integer.parseInt(inputList.get(3)) - 1;
                        } catch (NumberFormatException e) {
                            throw new InvalidArguments("Invalid page number!");
                        }
                    }
                    if (inputList.get(1).equalsIgnoreCase("name")) {
                        commandStatusOk = commandExecution.searchSongs("title", inputList.get(2), nrPagina);
                        break;
                    } else if (inputList.get(1).equalsIgnoreCase("author")) {
                        commandStatusOk = commandExecution.searchSongs("artist", inputList.get(2), nrPagina);
                        break;
                    } else {
                        System.out.println("Invalid search criteria!");
                    }
                    break;
                case "export":
                    if (nrOfArguments != 4) {
                        throw new InvalidArguments("Invalid number of arguments!\nUsage: export byId/byName <id>/<name> <format>");
                    }
                    if (!List.of("csv", "json", "txt").contains(inputList.get(3))) {
                        System.out.println("Invalid export format!");
                        break;
                    }
                    if (inputList.get(1).equalsIgnoreCase("byId")) {
                        commandStatusOk = commandExecution.exportPlaylistById(inputList.get(2), inputList.get(3));
                        break;
                    } else if (inputList.get(1).equalsIgnoreCase("byName")) {
                        commandStatusOk = commandExecution.exportPlaylistByName(inputList.get(2), inputList.get(3));
                        break;
                    } else {
                        System.out.println("Invalid export criteria!");
                    }
                    break;
                case "import":
                    if (nrOfArguments != 5) {
                        throw new InvalidArguments("Invalid number of arguments!\nUsage: import byId/byName <id>/<name> <format> <pathToFile>");
                    }
                    if (!List.of("csv", "json", "txt").contains(inputList.get(3))) {
                        System.out.println("Invalid import format!");
                        break;
                    }
                    if (inputList.get(1).equalsIgnoreCase("byId")) {
                        commandStatusOk = commandExecution.importPlaylistById(inputList.get(2), inputList.get(3), inputList.get(4));
                        break;
                    } else if (inputList.get(1).equalsIgnoreCase("byName")) {
                        commandStatusOk = commandExecution.importPlaylistByName(inputList.get(2), inputList.get(3), inputList.get(4));
                        break;
                    } else {
                        System.out.println("Invalid import criteria!");
                    }
                    break;
                case "audit":
                    if (nrOfArguments < 2 || nrOfArguments > 3) {
                        throw new InvalidArguments("Invalid number of arguments!\nUsage: audit <username> [page]");
                    } else if (nrOfArguments == 3) {
                        try {
                            nrPagina = Integer.parseInt(inputList.get(2)) - 1;
                        } catch (NumberFormatException e) {
                            throw new InvalidArguments("Invalid page number!");
                        }
                    }
                    commandStatusOk = commandExecution.getAuditForUser(inputList.get(1), nrPagina);
                    break;
                case "rerun":
                    if (nrOfArguments != 2) {
                        throw new InvalidArguments("Invalid number of arguments!\nUsage: rerun <auditId>");
                    }
                    try {
                        commandStatusOk = commandExecution.reReRunAudit(Integer.parseInt(inputList.get(1)));
                    } catch (NumberFormatException e) {
                        throw new InvalidArguments("Invalid audit ID!");
                    }
                    break;
                case "quit":
                    System.out.println("Quitting the application...");
                    commandStatusOk = true;
                    break;
                default:
                    System.out.println("Invalid command!");
            }
        } catch (PermissionException | InvalidArguments e) {
            System.out.println(e.getMessage());
        }

        commandExecution.insertIntoAudit(inputString,  commandStatusOk);
    }
}
