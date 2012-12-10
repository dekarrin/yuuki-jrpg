package yuuki.ui;

import javax.swing.JPanel;

import yuuki.entity.Character;

@SuppressWarnings("serial")
public class BattleScreen extends JPanel {

	private Character[][] fighters;
	
	public void startBattle(Character[][] fighters) {
		this.fighters = fighters;
	}
	
	public void showStart() {
		
	}
	
}
