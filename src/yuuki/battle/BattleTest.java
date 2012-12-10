/**
 * Simple Test of Battle subsystem.
 */

package yuuki.battle;

import yuuki.entity.Character; // necessary to avoid java.lang.* import
import yuuki.entity.*;
import yuuki.action.*;
import yuuki.buff.*;

import java.io.*;
import java.util.ArrayList;

public class BattleTest {

	private BufferedReader input;
	
	private static boolean usePauses = false;
	
	public static void main(String[] args) {
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("pause")) {
				BattleTest.usePauses = true;
			}
		}
		BattleTest test = new BattleTest();
		test.run();
	}
	
	public BattleTest() {
		input = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public void run() {
		Character char1 = makeChar1();
		Character char2 = makeChar2();
		Character[][] teams = new Character[2][1];
		teams[0][0] = char1;
		teams[1][0] = char2;
		Battle b = new Battle(teams);
		runBattle(b);
	}
	
	private void pause() {
		if (BattleTest.usePauses) {
			try {
				input.readLine();
			} catch(IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	private void pause(String s) {
		System.out.println(s);
		pause();
	}
	
	private void print(String s) {
		System.out.print(s);
	}
	
	private void println(String s) {
		print(s);
		print("\n");
	}
	
	private void println() {
		println("");
	}
	
	private void showStatus(Character f) {
		println(f.getName() + " (" + f.getTeamId() + ":" + f.getFighterId() + ")");
		println("---------------");
		VariableStat hp = f.getHPStat();
		VariableStat mp = f.getMPStat();
		Stat str = f.getStrengthStat();
		Stat def = f.getDefenseStat();
		Stat agt = f.getAgilityStat();
		Stat acc = f.getAccuracyStat();
		Stat mag = f.getMagicStat();
		Stat luk = f.getLuckStat();
		println("HP: " + hp.getCurrent() + "/" + hp.getMax(f.getLevel()));
		println("MP: " + mp.getCurrent() + "/" + mp.getMax(f.getLevel()));
		println("---------------");
		println("STR: " + str.getEffective(f.getLevel()));
		println("DEF: " + def.getEffective(f.getLevel()));
		println("AGT: " + agt.getEffective(f.getLevel()));
		println("ACC: " + acc.getEffective(f.getLevel()));
		println("MAG: " + mag.getEffective(f.getLevel()));
		println("LUK: " + luk.getEffective(f.getLevel()));
		println("---------------");
		for (Buff b: f.getBuffs()) {
			println(b.getName() + "("+b.getTurns()+"): " + b.getEffect());
		}
		println();
	}
	
	private void runBattle(Battle battle) {
		pause("A battle started!");
		ArrayList<Character> t1 = battle.getFighters(0);
		ArrayList<Character> t2 = battle.getFighters(1);
		pause(t1.get(0).getName()+" v. "+t2.get(0).getName()+"!");
		showStatus(t1.get(0));
		showStatus(t2.get(0));
		while (battle.advance()) {
			Action a = battle.getLastAction();
			Character c = battle.getCurrentFighter();
			ArrayList<Buff> b = c.getBuffs();
			switch (battle.getLastState()) {
				case STARTING_TURN:
					pause(c.getName() + " is up next.");
					break;
					
				case GETTING_ACTION:
					pause(c.getName() + " tried to use " + a.getName()+"!");
					break;
					
				case APPLYING_ACTION:
					if (!a.wasSuccessful()) {
						println("And failed.");
					} else {
						println("And pulled it off!");
					}
					pause();
					showStatus(t1.get(0));
					showStatus(t2.get(0));
					pause();
					break;
					
				case APPLYING_BUFFS:
					if (b.size() > 0) {
						String bs = "";
						for (int i = 0; i < b.size(); i++) {
							bs += b.get(i).getName();
							if (i + 1 < b.size()) {
								bs += ", ";
							}
						}
						pause(c.getName() + " is feeling the effects of " + bs + ".");
					}
					break;
					
				case CHECKING_DEATH:
					println("Checked for deaths...");
					break;
					
				case ENDING_TURN:
					println("Checked for team deaths...");
					break;
					
				case CHECKING_VICTORY:
					println("Checked for victory...");
					break;
					
				case LOOTING:
					println("Did loot...");
					break;
				
				default:
					break;
			}
			if (battle.getState() == Battle.State.ENDING) {
				pause("Battle is over.");
				Character winner = battle.getFighters(0).get(0);
				pause(winner.getName() + " won.");
			}
		}
	}
	
	private Character makeChar1() {
		VariableStat hp, mp;
		Stat str, def, agi, acc, mag, luk;
		hp = new VariableStat("health", 0, 1);
		mp = new VariableStat("mana", 10, 3);
		str = new Stat("strength", 5, 1);
		def = new Stat("defense", 5, 1);
		agi = new Stat("agility", 5, 1);
		acc = new Stat("accuracy", 5, 1);
		mag = new Stat("magic", 5, 1);
		luk = new Stat("luck", 5, 1);
		Action[] moves = new Action[2];
		moves[0] = new BasicAttack(1.0);
		moves[1] = new BasicDefense(10);
		int lvl = 5;
		int xpb = 5;
		Character c = new NonPlayerCharacter("Jack", lvl, moves, hp, mp, str,
											def, agi, acc, mag, luk, xpb);
		return c;
	}
	
	private Character makeChar2() {
		VariableStat hp, mp;
		Stat str, def, agi, acc, mag, luk;
		hp = new VariableStat("health", 0, 1);
		mp = new VariableStat("mana", 10, 3);
		str = new Stat("strength", 5, 1);
		def = new Stat("defense", 5, 1);
		agi = new Stat("agility", 5, 1);
		acc = new Stat("accuracy", 5, 1);
		mag = new Stat("magic", 5, 1);
		luk = new Stat("luck", 5, 1);
		Action[] moves = new Action[2];
		moves[0] = new BasicAttack(1.0);
		moves[1] = new BasicDefense(3);
		int lvl = 5;
		int xpb = 5;
		Character c = new NonPlayerCharacter("Jill", lvl, moves, hp, mp, str,
											def, agi, acc, mag, luk, xpb);
		return c;
	}
}