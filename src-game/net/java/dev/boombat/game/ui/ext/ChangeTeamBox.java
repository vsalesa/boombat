package net.java.dev.boombat.game.ui.ext;

import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.game.ui.core.Button;
import net.java.dev.boombat.game.ui.core.CheckBox;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;

public class ChangeTeamBox {
	private boolean visible;

	private int x;

	private int y;

	private CheckBox terroristCheckBox;

	private CheckBox counterTerroristCheckBox;

	private Button okButton;

	private Button cancelButton;

	private TankSlider slider;

	private Image bgImage;

	private Font font;

	public ChangeTeamBox(GameContainer container, int x, int y) {
		this.x = x;
		this.y = y;

		font = ResourceManager.getFont("dejavuSmallFont");

		bgImage = ResourceManager.getImage("changeTeamBoxImage");

		counterTerroristCheckBox = new CheckBox(container, ResourceManager
				.getImage("checkboxFalseImage"), ResourceManager
				.getImage("checkboxTrueImage"), x + 20, y + 100);
		terroristCheckBox = new CheckBox(container, ResourceManager
				.getImage("checkboxFalseImage"), ResourceManager
				.getImage("checkboxTrueImage"), x + 20, y + 150);

		terroristCheckBox.addListener(new ComponentListener() {
			
			public void componentActivated(AbstractComponent comp) {
				counterTerroristCheckBox.setSelected(!terroristCheckBox
						.isSelected());
				slider.setTerrorist(true);
			}
		});

		counterTerroristCheckBox.addListener(new ComponentListener() {
			
			public void componentActivated(AbstractComponent comp) {
				terroristCheckBox.setSelected(!counterTerroristCheckBox
						.isSelected());
				slider.setTerrorist(false);
			}
		});

		counterTerroristCheckBox.setSelected(true);

		slider = new TankSlider(container, x + 230, y + 80);

		okButton = new Button(container, ResourceManager
				.getImage("okNormalImage"), x + 140, y + 280);
		cancelButton = new Button(container, ResourceManager
				.getImage("cancelNormalImage"), x + 260, y + 280);

		okButton.setMouseOverImage(ResourceManager.getImage("okOverImage"));
		cancelButton.setMouseOverImage(ResourceManager
				.getImage("cancelOverImage"));
	}

	public void update(int delta) {
		if (visible) {
			updateChangeTeamBox(delta);
		}
	}

	public void render(GameContainer container, Graphics g) {
		if (visible) {
			drawChangeTeamBox(container, g);
		}
	}

	private void updateChangeTeamBox(int delta) {
		slider.update(delta);
	}

	private void drawChangeTeamBox(GameContainer container, Graphics g) {
		bgImage.draw(x, y);
		font.drawString(x + 60, y + 105, "Counter Terrorist");
		counterTerroristCheckBox.render(container, g);
		font.drawString(x + 60, y + 155, "Terrorist");
		terroristCheckBox.render(container, g);
		slider.render(container, g);
		okButton.render(container, g);
		cancelButton.render(container, g);
	}

	public void addOkListener(ComponentListener listener) {
		okButton.addListener(listener);
	}

	public void addCancelListener(ComponentListener listener) {
		cancelButton.addListener(listener);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public byte getSelectedTeam() {
		byte team = Player.TEAM_COUNTER_TERRORIST;
		if (terroristCheckBox.isSelected()) {
			team = Player.TEAM_TERRORIST;
		}

		return team;
	}

	public String getSelectedTank() {
		return slider.getSelectedTank();
	}

}
