package services.drivers.dto;

import poker.PokerRound;

import java.util.ArrayList;

public record PokerGameDTO(int id, String timestamp, int numPlayers, ArrayList<PokerRound> rounds) {
}
