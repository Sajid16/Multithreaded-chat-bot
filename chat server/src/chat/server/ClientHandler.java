/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author sajid
 */
// ClientHandler class
class ClientHandler implements Runnable 
{
    Scanner scn = new Scanner(System.in);
    private String name;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket s;
    boolean isloggedin;
     
    // constructor
    public ClientHandler(Socket s, String name,
                            DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.s = s;
        this.isloggedin=false;
    }
 
    @Override
    public void run() {
        String received;
        while (true){
            try{
                received = dis.readUTF();
                System.out.println(received);
                if(received.equals("logout")){
                    this.isloggedin=false;
                    //this.s.close();
                    //break;
                }
                else if(received.equals("login")){
                    this.name = dis.readUTF();
                    this.isloggedin=true;
                    dos.writeUTF("You are logged in...");
                }
                
                else if(received.equals("list")){
                    for (ClientHandler mc : Server.ar) {
                        if (mc.name.equals(this.name)==false &&  mc.isloggedin==true){
                            this.dos.writeUTF(mc.name+"\n");
                        }
                    }        
                }
                else{
                    StringTokenizer st = new StringTokenizer(received, ":");
                    if(st.countTokens()==2){
                        String first = st.nextToken();
                        String second = st.nextToken();
                        
                        if(second.equals("all")){
                            for (ClientHandler mc : Server.ar) {
                                if (mc.name.equals(this.name)==false &&  mc.isloggedin==true){
                                    mc.dos.writeUTF(this.name+" : "+first);
                                    //break;
                                }
                            }
                        }
                        else{
                            for (ClientHandler mc : Server.ar) {
                                if (mc.name.equals(second) && mc.isloggedin==true){
                                    mc.dos.writeUTF(this.name+" : "+first);
                                    break;
                                }
                            }
                        }
                    }
                    else{
                        String first = st.nextToken();
                        while(st.hasMoreTokens()){
                            String second = st.nextToken();
                            for (ClientHandler mc : Server.ar) {
                                if (mc.name.equals(second) && mc.isloggedin==true){
                                    mc.dos.writeUTF(this.name+" : "+first);
                                    break;
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /*
        try
        {
            this.dis.close();
            this.dos.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        */
    }
}
