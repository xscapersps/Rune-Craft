package com.elvarg.game.model.dialogue;

import java.util.HashMap;
import java.util.Map;

import com.elvarg.game.definition.NpcDefinition;
import com.elvarg.game.entity.impl.player.Player;

/**
 * Manages the loading and start of dialogues.
 * 
 * @author relex lawl
 */

public class DialogueManager {

	/**
	 * A {@link Map} containing all of our {@link Dialogue}s.
	 */
	public static Map<Integer, Dialogue> dialogues = new HashMap<Integer, Dialogue>();

	/**
	 * A value representing the interface id
	 * for a dialogue.
	 */
	public static final int CHATBOX_INTERFACE_ID = 50;

	/**
	 * Starts a dialogue gotten from the dialogues map.
	 * @param player	The player to dialogue with.
	 * @param id		The id of the dialogue to retrieve from dialogues map.
	 */
	public static void start(Player player, int id) {
		Dialogue dialogue = dialogues.get(id);
		start(player, dialogue);
	}

	/**
	 * Starts a dialogue.
	 * @param player	The player to dialogue with.	
	 * @param dialogue	The dialogue to show the player.
	 */
	public static void start(Player player, Dialogue dialogue) {

		//If player isn't currently in a dialogue and they are busy,
		//simply send interface removal.
		if(player.getDialogue() == null) {
			if(player.busy()) {
				player.getPacketSender().sendInterfaceRemoval();
			}
		}

		//Update our dialogue state
		player.setDialogue(dialogue);

		//If dialogue is null, send interface removal.
		//Otherwise, show the dialogue!
		if (dialogue == null || dialogue.id() < 0) {
			player.getPacketSender().sendInterfaceRemoval();
		} else {
			showDialogue(player, dialogue);
			dialogue.specialAction();
		}
	}

	/**
	 * Handles the clicking of 'click here to continue', option1, option2 and so on.
	 * @param player	The player who will continue the dialogue.
	 */
	public static void next(Player player) {

		//Make sure we are currently in a dialogue..
		if (player.getDialogue() == null) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}

		//Fetch next dialogue..
		Dialogue next = player.getDialogue().nextDialogue();
		if (next == null)
			next = dialogues.get(player.getDialogue().nextDialogueId());

		//Make sure the next dialogue is valid..
		if (next == null || next.id() < 0) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}

		//Start the next dialogue.
		start(player, next);
	}

	/**
	 * Configures the dialogue's type and shows the dialogue interface
	 * and sets its child id's.
	 * @param player		The player to show dialogue for.
	 * @param dialogue		The dialogue to show.
	 */
	private static void showDialogue(Player player, Dialogue dialogue) {
		String[] lines = dialogue.dialogue();
		switch (dialogue.type()) {
		case NPC_STATEMENT:
			int startDialogueChildId = NPC_DIALOGUE_ID[lines.length - 1];
			int headChildId = startDialogueChildId - 2;
			player.getPacketSender().sendNpcHeadOnInterface(dialogue.npcId(), headChildId);
			player.getPacketSender().sendInterfaceAnimation(headChildId, dialogue.animation().getAnimation());
			player.getPacketSender().sendString(startDialogueChildId - 1, NpcDefinition.forId(dialogue.npcId()) != null ? NpcDefinition.forId(dialogue.npcId()).getName().replaceAll("_", " ") : "");
			for (int i = 0; i < lines.length; i++) {
				player.getPacketSender().sendString(startDialogueChildId + i, lines[i]);
			}
			player.getPacketSender().sendChatboxInterface(startDialogueChildId - 3);
			break;
		case PLAYER_STATEMENT:
			startDialogueChildId = PLAYER_DIALOGUE_ID[lines.length - 1];
			headChildId = startDialogueChildId - 2;
			player.getPacketSender().sendPlayerHeadOnInterface(headChildId);
			player.getPacketSender().sendInterfaceAnimation(headChildId, dialogue.animation().getAnimation());
			player.getPacketSender().sendString(startDialogueChildId - 1, player.getUsername());
			for (int i = 0; i < lines.length; i++) {
				player.getPacketSender().sendString(startDialogueChildId + i, lines[i]);
			}
			player.getPacketSender().sendChatboxInterface(startDialogueChildId - 3);
			break;
		case ITEM_STATEMENT:
			startDialogueChildId = NPC_DIALOGUE_ID[lines.length - 1];
			headChildId = startDialogueChildId - 2;
			player.getPacketSender().sendInterfaceModel(headChildId, Integer.valueOf(dialogue.item()[0]), Integer.valueOf(dialogue.item()[1]));
			player.getPacketSender().sendString(startDialogueChildId - 1, dialogue.item()[2]);
			for (int i = 0; i < lines.length; i++) {
				player.getPacketSender().sendString(startDialogueChildId + i, lines[i]);
			}
			player.getPacketSender().sendChatboxInterface(startDialogueChildId - 3);
			break;
		case STATEMENT:
			int chatboxInterface = STATEMENT_DIALOGUE_ID[lines.length - 1];
			for (int i = 0; i < lines.length; i++) {
				player.getPacketSender().sendString((chatboxInterface + 1) + i, lines[i]);
			}
			player.getPacketSender().sendChatboxInterface(chatboxInterface);
			break;
		case OPTION:
			int firstChildId = OPTION_DIALOGUE_ID[lines.length - 1];
			player.getPacketSender().sendString(firstChildId - 1, "Choose an option");
			for (int i = 0; i < lines.length; i++) {
				player.getPacketSender().sendString(firstChildId + i, lines[i]);
			}
			player.getPacketSender().sendChatboxInterface(firstChildId - 2);
			break;
		}
	}

	public static void sendStatement(Player p, String statement) {
		p.getPacketSender().sendString(357, statement);
		p.getPacketSender().sendChatboxInterface(356);
	}

	/**
	 * Gets an empty id for a dialogue.
	 * @return	An empty index from the map or the map's size itself.
	 */
	public static int getDefaultId() {
		int id = dialogues.size();
		for (int i = 0; i < dialogues.size(); i++) {
			if (dialogues.get(i) == null) {
				id = i;
				break;
			}
		}
		return id;
	}

	/**
	 * Retrieves the dialogues map.
	 * @return	dialogues.
	 */
	public static Map<Integer, Dialogue> getDialogues() {
		return dialogues;
	}

	/**
	 * This array contains the child id where the dialogue
	 * statement starts for npc and item dialogues.
	 */
	private static final int[] NPC_DIALOGUE_ID = {
			4885,
			4890,
			4896,
			4903
	};

	/**
	 * This array contains the child id where the dialogue
	 * statement starts for player dialogues.
	 */
	private static final int[] PLAYER_DIALOGUE_ID = {
			971,
			976,
			982,
			989
	};

	/**
	 * This array contains the child id where the dialogue
	 * statement starts for option dialogues.
	 */
	private static final int[] OPTION_DIALOGUE_ID = {
			13760,
			2461,
			2471,
			2482,
			2494,
	};

	/**
	 * This array contains the chatbox interfaces
	 * for statements.
	 */
	private static final int[] STATEMENT_DIALOGUE_ID = {
			356,
			359,
			363,
			368,
			374,
	};
}
