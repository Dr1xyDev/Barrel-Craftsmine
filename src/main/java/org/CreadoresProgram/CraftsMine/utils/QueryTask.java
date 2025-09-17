package org.CreadoresProgram.CraftsMine.utils;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.CreadoresProgram.CraftsMine.meJustinLib.bedrockserverquery.data.BedrockQuery;
import org.CreadoresProgram.CraftsMine.server.Server;
import lombok.Getter;
public class QueryTask implements ActionListener{
  @Getter
  private static BedrockQuery infoServ = BedrockQuery.create(Server.getInstance().getConfig().getBedrockAddress(), Server.getInstance().getConfig().getBedrockPort());

  public void actionPerformed(ActionEvent e){
    infoServ = BedrockQuery.create(Server.getInstance().getConfig().getBedrockAddress(), Server.getInstance().getConfig().getBedrockPort());
    Server.getInstance().getRakNetServ().setMOTD("", Server.getInstance().getConfig().getSubmotd());
  }
}
