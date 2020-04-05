package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientHandler implements Runnable{

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Main server;
    private String nick;
    List<String> blackList;

    public List<String> getBlackList() {
        return blackList;
    }

    public String getNick() {
        return nick;
    }

    public boolean checkBlackList(String nick) {
        return blackList.contains(nick);
    }
    public void fillBlackList() {
        blackList = AuthService.getBlackListByNickName(nick);
    }
    public boolean addUserInBlackList(String nick) {
        String blockUserId =  AuthService.getUserIDbyNick(nick);
        String currentUserId =  AuthService.getUserIDbyNick(this.nick);
        boolean res = false;
        if (blockUserId != null) {
            blackList.add(nick);
            AuthService.setUserInBlackList(currentUserId,blockUserId);
            res = true;
        }
        return res;
    }


    public ClientHandler(Socket socket, Main server) {
        try {
            this.blackList = new ArrayList<>();
            this.socket = socket;
            this.server = server;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            //ExecutorService executorService = Executors.newFixedThreadPool(10);

            //executorService.execute(new Runnable() {
                //).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {

            while (true) {
                String str = in.readUTF();
                if (str.startsWith("/reg"))
                {
                    String[] tokens = str.split(" ");
                    if (AuthService.getLogin(tokens[1]) == null ) {
                        AuthService.setNewUsers(tokens[1],tokens[2],tokens[3]);
                        sendMsg("/regOk");
                    } else {
                        sendMsg("Логин уже используется");
                    }

                }
                if (str.startsWith("/auth")) {
                    String[] tokens = str.split(" ");
                    String newNick = AuthService.getNickByLoginAndPass(tokens[1], tokens[2]);
                    if (newNick != null) {
                        if (!server.isNickBusy(newNick)) {
                            sendMsg("/authok" + " " + newNick);
                            nick = newNick;
                            server.subscribe(ClientHandler.this);
                            break;
                        } else {
                            sendMsg("Учетная запись уже используется");
                        }
                    } else {
                        sendMsg("Неверный логин/пароль");
                    }
                }
                server.broadcastMsg(ClientHandler.this, str);
            }
            fillBlackList();
            while (true) {
                String str = in.readUTF();
                if (str.startsWith("/")) {
                    if (str.equals("/end")) {
                        out.writeUTF("/serverclosed");
                        System.out.println("Клиент отклюился");
                        server.LOGGER.info("Info: {}.", "Клиент отклюился");
                        break;
                    }
                    if (str.startsWith("/w ")) { // /w nick3 lsdfhldf sdkfjhsdf wkerhwr
                        String[] tokens = str.split(" ", 3);
                        //if(tokens.length > 3) {
                        //String m = str.substring(tokens[1].length() + 4);
                        server.sendPersonalMsg(ClientHandler.this, tokens[1], tokens[2]);
                    }
                    if (str.startsWith("/blacklist ")) {
                        String[] tokens = str.split(" ");
                        if (tokens[1].equals(nick)) {
                            sendMsg("Нельзя добавлять себя в чёрный список!");
                        } else if (addUserInBlackList(tokens[1]) ) {
                            sendMsg("Вы добавили пользователя " + tokens[1] + " в черный список");
                        } else {
                            sendMsg("Пользователь " + tokens[1] + " не найден");
                        }
                    }
                    if (str.startsWith("/changenick ")){
                        String[] tokens = str.split(" ");
                        if (!AuthService.nickIsBusy(tokens[1])){
                            if (AuthService.changeNick(AuthService.getUserIDbyNick(nick), tokens[1])) {

                                sendMsg("Ник усешно сменён!");
                                server.changeBlackListNewNick(nick,tokens[1]);

                                nick = tokens[1];
                                server.broadcastClientList();
                            } else {
                                sendMsg("Ошибка");
                            }
                        } else {
                            sendMsg("Ник занят");
                        }


                    }

                } else {
                    server.broadcastMsg(ClientHandler.this,nick + ": " + str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        server.unsubscribe(ClientHandler.this);
}


    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
