// Copyright (C) 2012, The SAVI Project.
package ca.savi.aor.storage;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import ca.savi.aor.messageexceptions.GenericException;

/**
 * @author Keith
 */
public class TransactionManagerListener extends Thread {
  public int index;
  public boolean running;
  public boolean initRequired;
  public boolean serverFailed;
  private Object lock;
  private List transactionList;
  private Socket sock;
  private int arraySize;

  public TransactionManagerListener() {
    index = 0;
    running = false;
    this.lock = new Object();
    initRequired = false;
    serverFailed = false;
    transactionList = new LinkedList<TransactionDescription>();
    System.out.println("Creating TML");
  }

  @Override
  public void run() {
    running = true;
    listen();
    running = false;
  }

  public void setSocket(Socket s) {
    sock = s;
  }

  private void listen() {
    Message m = new Message();
    long tid;
    long used, storage, bandwidth;
    TransactionDescription current, required;
    byte[] payload = new byte[2048];
    System.out.println("Listening on returnSock");
    while (true) {
      try {
        m = Message.receive(sock);
      } catch (GenericException e) {
        if (e.getMessage().equals("Socket Closed Remotely")) {
          System.out.println("Socket Closed Remotely");
          return;
        }
        try {
          Thread.sleep(100);
        } catch (InterruptedException ie) {
          System.out.println("Got Error: " + ie);
        }
        continue;
      }
      payload = m.getPayload();
      System.out.println(payload[0] + " " + payload[1] + " " + payload[2] + " "
          + payload[3]);
      tid =
          (payload[0] << 24) + (payload[1] << 16) + (payload[2] << 8)
              + payload[3];
      if (tid == 0) {
        System.out.println("got message with tid = 0");
        if (m.getMessageType() == MessageType.SERV_FAILED_RQT) {
          serverFailed = true;
          System.out.println("Got SERV_FAILED_RQT");
          Message r = new Message(MessageType.SERV_FAILED_RSP);
          try {
            r.send(sock);
          } catch (IOException e) {
            System.out.println("Transaction Manager failed as well");
            return;
          }
        } else if (m.getMessageType() == MessageType.NEED_INIT_RQT) {
          synchronized (lock) {
            serverFailed = false;
            initRequired = true;
          }
          System.out.println("Got NEED_INIT msg");
          Message r = new Message(MessageType.NEED_INIT_RSP);
          try {
            r.send(sock);
          } catch (IOException e) {
            System.out.println("Transaction Manager failed as well");
            return;
          }
        }
      } else {
        System.out.println("Looking for tid: " + tid);
        required = null;
        ListIterator itr = transactionList.listIterator();
        while (itr.hasNext()) {
          current = (TransactionDescription) itr.next();
          if (current.tid == tid) {
            required = current;
            break;
          }
        }
        if (required == null) {
          System.out.println("Could not find the tid");
          continue;
        }
        synchronized (lock) {
          System.out.println("Found the tid, setting serviced");
          required.setServiced(true);
          required.returnType = m.getMessageType();
          required.payload = payload;
        }
      }
    } // end while(true);
  }

  private TransactionDescription findTransaction(long tid) {
    TransactionDescription current;
    ListIterator itr = transactionList.listIterator();
    while (itr.hasNext()) {
      current = (TransactionDescription) itr.next();
      if (current.tid == tid)
        return (current);
    }
    return (null);
  }

  public boolean addTransaction(TransactionDescription t) {
    boolean returnVal;
    synchronized (lock) {
      returnVal = transactionList.add(t);
    }
    return (returnVal);
  }

  public boolean removeTransaction(TransactionDescription t) {
    boolean returnVal;
    synchronized (lock) {
      returnVal = transactionList.remove(t);
    }
    return (returnVal);
  }
}
