package org.CreadoresProgram.CraftsMine;
import org.CreadoresProgram.CraftsMine.server.Server;
import org.CreadoresProgram.CraftsMine.inventory.Item;
import org.CreadoresProgram.CraftsMine.nukkitLib.entity.Attribute;
public class CraftsMine{
  public static String DATA_PATH = System.getProperty("user.dir") + "/";
  public static void main(String[] args){
    Thread.currentThread().setName("CraftsMine-main");
    System.out.println("Starting CraftsMine Proxy Software Prototype");
    System.setProperty("java.compiler", "javac");
    System.getProperties().putIfAbsent("io.netty.allocator.type", "unpooled");
    System.out.println("CraftsMine is distributed under the Terms and Conditions of Creadores Program ©2024");
    System.out.println("Read the Terms and Conditions at: https://creadoresprogram.blogspot.com/p/craftsmine.html");
    Item.init();
    Attribute.init();
    new Server(DATA_PATH);
  }
}
