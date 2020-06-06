package battleGame;
import java.awt.Color; 
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;
import java.awt.Rectangle;

@SuppressWarnings("serial")
public class sandbox extends JPanel implements ActionListener, KeyListener {

	int buildspacing = 0;
	int knockbackRNG = 0;
	int landspacing = 0;
	int blockoffset = 0;

	boolean fire = false;
	boolean allowmovement = true;
	boolean gravity = false;

	bullet[] b = new bullet[100];
	target[] t = new target[100];
	buildings[] towers = new buildings[50];
	NPC[] player = new NPC[50];

	human user = new human();
	clouds cloud = new clouds();
	armor iron = new armor();
	Timer time = new Timer(5, this);
	controls gui = new controls();
	battery power = new battery();

	public sandbox() {

		for (int i = 0; i < player.length; i++) {
			NPC player1 = new NPC((int) (Math.random() * 10000) + 500, 1);
			player[i] = player1;
		}

		for (int i = 0; i < b.length; i++) {
			bullet b1 = new bullet(iron.armorPosX + 19, iron.armorPosY - 5);
			b[i] = b1;
		}

		for (int i = 0; i < t.length; i++) {
			target t1 = new target((int) (Math.random() * 4000) + 2900, (int) (Math.random() * 800));
			t[i] = t1;
		}

		for (int i = 0; i < towers.length; i++) {
			buildings building = new buildings(buildspacing);
			buildspacing += 600;
			towers[i] = building;
		}

		time.start();
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		gui.naturaldrawings(g);

		for (int i = 0; i < towers.length; i++) {
			towers[i].draw(g);
			ImageIcon i5 = new ImageIcon("C:\\Users\\yash0\\Pictures\\buildingIMG.png");
			i5.paintIcon(this, g, towers[i].movingsurrounding1, towers[i].bY);
		}

		for (int i = 0; i < player.length; i++) {
			if (player[i].alive) {
				player[i].drawNPC(g);
				ImageIcon i6 = new ImageIcon("C:\\Users\\yash0\\Pictures\\imageface.png");
				i6.paintIcon(this, g, player[i].npcX, player[i].npcY + 10);
			} else {

				ImageIcon i8 = new ImageIcon("C:\\Users\\yash0\\Pictures\\skull.png");
				i8.paintIcon(this, g, player[i].npcX, player[i].npcY + 10);

			}
		}
		cloud.draw(g);

		if (!iron.track) {

			user.draw(g);
			iron.normal = true;
			iron.fire = false;

			gui.draw(g);

			ImageIcon i2 = new ImageIcon("C:\\Users\\yash0\\Pictures\\imageface.png");
			i2.paintIcon(this, g, user.personX, user.personY + 10);

			if (power.powerlength <= 0) {
				g.setColor(Color.RED);
				g.setFont(new Font("default", Font.BOLD, 25));
				g.drawString("!", iron.armorPosX, iron.armorPosY);
			}
		}

		// bullet
		g.setColor(Color.BLACK);
		for (int i = 0; i < b.length; i++) {
			b[i].fire();
			b[i].draw(g);
		}

		if (iron.normal) {
			ImageIcon i = new ImageIcon("C:\\Users\\yash0\\Pictures\\ironmanNOFire.png");
			i.paintIcon(this, g, iron.armorPosX, iron.armorPosY);
		}

		if (iron.fire) {
			ImageIcon i3 = new ImageIcon("C:\\Users\\yash0\\Pictures\\ironmanSuitJavaCanvasIMG.png");
			i3.paintIcon(this, g, iron.armorPosX, iron.armorPosY);
		}

		if (iron.blast) {
			ImageIcon i4 = new ImageIcon("C:\\Users\\yash0\\Pictures\\ironmanBlast.png");
			i4.paintIcon(this, g, iron.armorPosX, iron.armorPosY);

		}

		if (iron.confirmgroundfire) {
			ImageIcon i4 = new ImageIcon("C:\\Users\\yash0\\Pictures\\ironmanBlastOnGround.png");
			i4.paintIcon(this, g, iron.armorPosX, iron.armorPosY);

		}

		if (iron.track) {

			gui.drawArmor(g);

			if (power.powerlength <= 0) {

				g.setColor(Color.RED);
				g.setFont(new Font("default", Font.BOLD, 25));
				g.drawString("!          !          !", 1660, 45);
				user.nobattery = true;

				g.setColor(Color.RED);
				g.setFont(new Font("default", Font.BOLD, 25));
				g.drawString("!", iron.armorPosX, iron.armorPosY);

			}
			power.draw(g);

		}

		// target
		g.setColor(Color.red);

		for (int i = 0; i < t.length; i++) {
			g.fillRect(t[i].targetx, t[i].targety, 30, 30);
		}

	}

	public void targetmove() {

		for (int i = 0; i < t.length; i++) {
			t[i].move();
		}

	}

	public void contain() {
		for (int i = 0; i < b.length; i++) {
			if (!b[i].bulletFire) {
				b[i].bulletX = iron.armorPosX + 19;
				b[i].bulletY = iron.armorPosY - 5;
			}
		}
	}

	public void trackSystem() {

		if (iron.track) {
			iron.armorPosX = user.personX - 12;
			iron.armorPosY = user.personY + 8;
		}
	}

	public void batterydecrease() {
		if (user.insideSuit && iron.armorPosY <= 869) {
			power.isflyingforbattery = true;
		} else {
			power.isflyingforbattery = false;
		}
	}

	public void knockbackRNG() {

		knockbackRNG++;
		for (int i = 0; i < player.length; i++) {
			if (knockbackRNG % 10 == 0) {
				player[i].knockback = false;

			}
		}
	}

	public void deadnpc() {
		for (int i = 0; i < player.length; i++) {
			if (!player[i].alive) {
				if (player[i].npcY <= 915) {
					player[i].speedY = 2;
					player[i].speedaddition = 0;
					player[i].speed = 0;
				} else {
					player[i].speedY = 0;

				}
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		user.move();
		for (int i = 0; i < player.length; i++) {
			player[i].npcBehavior();
		}
		knockbackRNG();
		deadnpc();
		for (int i = 0; i < towers.length; i++) {
			towers[i].move();
		}
		for (int i = 0; i < player.length; i++) {
			player[i].move();
		}
		iron.move();
		batterydecrease();
		targetmove();
		contain();
		user.jump();
		iron.tracking();
		trackSystem();
		power.batteryfunction();
		cloud.move();
		Collision();
		for (int i = 0; i < t.length; i++) {
			t[i].destroy();
		}
		for (int i = 0; i < t.length; i++) {
			b[i].destroy();
		}
		user.shutdown();
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int i = e.getKeyCode();

		if (i == KeyEvent.VK_P) {

			for (int j = 0; j < t.length; j++)
				t[j].move = true;
		}

		if (i == KeyEvent.VK_R) {
			if (iron.blast || (iron.confirmgroundfire && iron.fireonground)) {
				if (gui.ammo > 0) {
					gui.ammo--;
					fire = true;
					b[gui.ammo].bulletFire = true;
				}
			}
		}

		if (i == KeyEvent.VK_F) {

			if (iron.track) {

				iron.blast = true;
				iron.normal = false;
				iron.fire = false;
				iron.activatefire = false;
			}
			if (iron.track && iron.armorPosY >= 870) {

				iron.blast = false;
				iron.normal = false;
				iron.fire = false;
				iron.activatefire = false;
				iron.fireonground = true;
				iron.confirmgroundfire = true;

			}

		}

		if (i == KeyEvent.VK_G) {

			iron.blast = false;
			iron.fire = true;
			iron.normal = false;
			iron.confirmgroundfire = false;
			iron.fireonground = false;
		}

		if (i == KeyEvent.VK_V) {

			if (iron.armorPosX != user.personX - 12) {
				if (iron.armorPosX >= user.personX) {
					iron.armorspeed = -10;
				}

				if (iron.armorPosX <= user.personX) {
					iron.armorspeed = 10;
				}
			}
			power.track = true;
			user.insideSuit = true;
		}

		if (i == KeyEvent.VK_C) {
			if (user.personY >= 870) {
				iron.track = false;
				power.track = false;
				iron.armorPosX = -5;
			}
			user.insideSuit = false;
		}
		if (i == KeyEvent.VK_W) {
			if (iron.track == true && iron.confirmgroundfire == false && iron.fireonground == false) {
				user.speedY = -5;

				if (user.personY >= 870) {
					power.isflyingforbattery = false;
				}
			}

			if (!iron.track) {
				user.jump = true;

			}
		}

		if (i == KeyEvent.VK_S && iron.track == true && !(iron.armorPosY >= 870)) {

			user.speedY = 5;

		}

		if (i == KeyEvent.VK_A) {
			// ground
			if (iron.track == true && iron.armorPosY >= 870) {
				for (int h = 0; h < player.length; h++) {
					player[h].speed = 6;
				}
				for (int j = 0; j < towers.length; j++) {
					towers[j].speed = 6;
				}
			}
			if (!iron.track) {
				for (int k = 0; k < player.length; k++) {
					player[k].speed = 2;
				}
				for (int j = 0; j < towers.length; j++) {
					towers[j].speed = 2;
				}
				iron.armorspeed = 2;
			}
			// air
			if (iron.track == true && iron.armorPosY < 870) {
				for (int h = 0; h < player.length; h++) {
					player[h].speed = 12;
				}
				for (int j = 0; j < towers.length; j++) {
					towers[j].speed = 12;
				}

			} else {
				for (int k = 0; k < player.length; k++) {
					player[k].speed = 4;
				}
				for (int j = 0; j < towers.length; j++) {
					towers[j].speed = 4;
				}
				iron.armorspeed = 4;
			}

		}

		if (i == KeyEvent.VK_D) {
			// ground
			if (iron.track == true && iron.armorPosY >= 870) {
				for (int j = 0; j < player.length; j++) {
					player[j].speed = -6;
				}
				for (int j = 0; j < towers.length; j++) {
					towers[j].speed = -6;
				}
			}
			if (!iron.track) {
				for (int k = 0; k < player.length; k++) {
					player[k].speed = -2;
				}
				for (int j = 0; j < towers.length; j++) {
					towers[j].speed = -2;
				}
				iron.armorspeed = -2;
			}
			// air
			if (iron.track == true && iron.armorPosY < 870) {
				for (int j = 0; j < player.length; j++) {
					player[j].speed = -12;
				}
				for (int j = 0; j < towers.length; j++) {
					towers[j].speed = -12;
				}
			} else {
				for (int k = 0; k < player.length; k++) {
					player[k].speed = -4;
				}
				for (int j = 0; j < towers.length; j++) {
					towers[j].speed = -4;
				}
				iron.armorspeed = -4;
			}

		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {

		if (iron.ignore == false) {
			user.speedY = 0;
		}

		iron.armorspeed = 0;
		for (int j = 0; j < towers.length; j++) {
			towers[j].speed = 0;
		}
		for (int i = 0; i < player.length; i++) {
			player[i].speed = 0;
		}
	}

	public void Collision() {

		Rectangle suit = iron.bounds();
		Rectangle human = user.bounds();

		for (int i = 0; i < b.length; i++) {
			Rectangle BRec = b[i].bounds();

			for (int k = 0; k < player.length; k++) {
				Rectangle npc = player[k].bounds();

				for (int j = 0; j < t.length; j++) {
					Rectangle TRec = t[j].bounds();

					if (BRec.intersects(TRec) && b[i].bulletFire) { // the (&& b[i].bulletFire) is to make sure that the
																	// interaction only occurs when bullet is fired
						t[j].letDestroy = true;
						b[i].letdestroy = true;
						gui.hitcount++;
					}

					if (BRec.intersects(npc) && b[i].bulletFire) {

						player[k].knockback = true;
						b[i].bulletFire = false;
						b[i].letdestroy = true;
						if (player[k].healthcount > 0) {
							player[k].healthcount -= 10;
						}
					}
				}
			}
		}

		if (suit.intersects(human)) {
			iron.track = true;
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();

		Container contentpane = frame.getContentPane();
		sandbox sPanel = new sandbox();

		Dimension preferredSize = new Dimension();
		preferredSize.setSize(600, 600);

		frame.setSize(preferredSize);
		contentpane.add(sPanel);
		frame.setVisible(true);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}